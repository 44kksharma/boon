package org.boon.primitive;

import org.boon.Exceptions;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;

import static org.boon.Exceptions.die;

public class ReaderCharacterSource implements CharacterSource {


    private static final int MAX_TOKEN_SIZE=5;

    private final Reader reader;
    private int readAheadSize;
    private int ch = -2;

    private boolean foundEscape;


    private char[] readBuf;

    private int index;

    private int length;


    boolean more = true;
    private boolean done = false;

    public ReaderCharacterSource( final Reader reader, final int readAheadSize ) {
        this.reader = reader;
        this.readBuf =  new char[readAheadSize + MAX_TOKEN_SIZE];
        this.readAheadSize = readAheadSize;
    }

    public ReaderCharacterSource( final Reader reader ) {
        this.reader = reader;
        this.readAheadSize = 10_000;
        this.readBuf =  new char[ readAheadSize + MAX_TOKEN_SIZE ];
    }

    public ReaderCharacterSource( final String string ) {
        this(new StringReader ( string ));
    }


    private void readForToken() {
        try {
            length += reader.read ( readBuf, readBuf.length-MAX_TOKEN_SIZE, MAX_TOKEN_SIZE );
        } catch ( IOException e ) {
            Exceptions.handle ( e );
        }
    }

    private void ensureBuffer() {

        try {
            if (index >= length && !done) {
                readNextBuffer ();
            } else if (done && index >=length) {
                more = false;
            }else {
                more = true;
            }
        } catch ( Exception ex ) {
            String str = CharScanner.errorDetails ( "ensureBuffer issue", readBuf, index, ch );
            Exceptions.handle (  str, ex );
        }
    }

    private void readNextBuffer() throws IOException {
        length = reader.read ( readBuf, 0, readAheadSize );

        index = 0;
        if (length == -1) {
             ch = -1;
             length = 0;
             more = false;
             done = true;
        } else {
             more = true;
        }
    }

    @Override
    public final int nextChar() {
        ensureBuffer();
        return ch = readBuf[index++];
    }

    @Override
    public  final int currentChar() {
        ensureBuffer();
        return readBuf[index];
    }

    @Override
    public  final boolean hasChar() {
        ensureBuffer();
        return more;
    }

    @Override
    public  final boolean consumeIfMatch( char[] match ) {
        try {

            char [] _chars = readBuf;
            int i=0;
            int idx = index;
            boolean ok = true;

            if ( idx + match.length > length ) {
                readForToken ();
            }

            for (; i < match.length; i++, idx++) {
                    ok &=  ( match[i] == _chars[idx] );
                    if (!ok) break;
            }

            if ( ok ) {
                index = idx;
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            String str = CharScanner.errorDetails ( "consumeIfMatch issue", readBuf, index, ch );
            return Exceptions.handle ( boolean.class, str, ex );
        }

    }

    @Override
    public final  int location() {
        return index;
    }

    public final int safeNextChar() {
        try {
            ensureBuffer();
            return index + 1 < readBuf.length ? readBuf[index++] : -1;
        } catch (Exception ex) {
            String str = CharScanner.errorDetails ( "safeNextChar issue", readBuf, index, ch );
            return Exceptions.handle ( int.class, str, ex );
        }
    }


    private final char[] EMPTY_CHARS = new char[0];


    @Override
    public char[] findNextChar( int match, int esc ) {
        try{
            ensureBuffer();

            int idx = index;
            char[] _chars = readBuf;

            int ch = this.ch;
            if ( ch == '"' ) {

            } else if ( idx < length -1 ) {
                ch = _chars[idx];

                if (ch == '"') {
                    idx++;
                }
            }


            if ( idx < length ) {
                ch = _chars[idx];
            }

            if (ch == '"') {
                index = idx;
                index++;
                return EMPTY_CHARS;
            }
            int start = idx;


            foundEscape=false;

            boolean foundEnd = false;
            char [] results ;


            for (; idx < length; idx++) {
                    ch  = _chars[idx];
                    if ( ch == match || ch == esc ) {
                        if ( ch == match ) {
                            foundEnd = true;

                            break;
                        } else if ( ch == esc ) {
                            foundEscape=true;
                            /** if we are dealing with an escape then see if the escaped char is a match
                             *  if so, skip it.
                             */
                            if ( idx + 1 < length) {
                                idx++;
                            }
                        }
                    }
                }


                if (idx == 0 ) {
                     results = EMPTY_CHARS;
                }   else {
                    results =  Arrays.copyOfRange ( _chars, start, idx );
                }
                index = idx;


                if (foundEnd) {
                    index++;
                    if (index < length) {
                        ch = _chars[index ];
                        this.ch = ch;
                    }
                    return results;
                } else {

                    if (index >= length && !done) {
                        ensureBuffer();
                        char results2[] = findNextChar(match, esc);
                        return Chr.add(results, results2);
                    } else {
                        return die (char[].class, "Unable to find close char " + (char)match + " " + new String(results));
                    }
                }
        } catch (Exception ex ) {
            String str = CharScanner.errorDetails ( "findNextChar issue", readBuf, index, ch );
            return Exceptions.handle ( char[].class, str, ex );
        }


    }

    @Override
    public boolean hadEscape() {
        return foundEscape;
    }


    @Override
    public void skipWhiteSpace() {
        try {
            index = CharScanner.skipWhiteSpace( readBuf, index, length );
            if (index >= length && more) {

                ensureBuffer();

                skipWhiteSpace();
            }
        } catch ( Exception ex ) {
            String str = CharScanner.errorDetails ( "skipWhiteSpace issue", readBuf, index, ch );
             Exceptions.handle (  str, ex );
        }
    }







    public char[] readNumber(  ) {
        try {
            ensureBuffer();

            char [] results =  CharScanner.readNumber( readBuf, index, length);
            index += results.length;

            if (index >= length && more) {
                ensureBuffer();
                if (length!=0) {
                    char results2[] = readNumber();
                    return Chr.add(results, results2);
                } else  {
                    return results;
                }
            } else {
                return results;
            }
        } catch (Exception ex) {
            String str = CharScanner.errorDetails ( "readNumber issue", readBuf, index, ch );
            return Exceptions.handle ( char[].class, str, ex );
        }

    }

    @Override
    public String errorDetails( String message ) {

        return CharScanner.errorDetails ( message, readBuf, index, ch );
    }

}
