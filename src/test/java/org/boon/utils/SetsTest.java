package org.boon.utils;


import org.junit.Test;


import java.util.Collection;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;

import static org.boon.utils.Sets.*;
import static org.boon.utils.Sets.safeSet;
import static org.boon.utils.Sets.safeSortedSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SetsTest {


    @Test
    public void simple() {
        Set<String> set =
                set("apple", "oranges", "pears", "grapes", "kiwi");

        assertEquals(5, len(set)) ;
        assertTrue(in("apple", set));

    }


    @Test
    public void sorted() {
        NavigableSet<String> set =
                sortedSet("apple", "kiwi", "oranges", "pears", "pineapple");

        assertEquals(
                5,
                len(set)
        );

        assertTrue(
                in("apple", set)
        );

        assertEquals(

                "oranges", idx(set, "ora")

        );

        assertEquals(

                "oranges", idx(set, "o")

        );

        assertEquals(

                "pears",
                idx(set, "p")

        );

        assertEquals(

                "pineapple",
                idx(set, "pi")

        );

        assertEquals(

                "pineapple",
                after(set, "pi")

        );

        assertEquals(

                "pears",
                before(set, "pi")

        );


        assertEquals(

                sortedSet("apple", "kiwi"),
                slc(set, "ap", "o")

        );

        assertEquals(

                sortedSet("apple", "kiwi"),
                slc(set, "o")

        );

        assertEquals(

                sortedSet("oranges", "pears", "pineapple"),
                slcEnd(set, "o")
        );

    }



    @Test public void copyTest() {
        Set<String> set = set("apple", "pear", "orange");

        Set<String> set2;

        set2 = set( copy(  set ) );
        assertEquals(
                set, set2
        );

        set2 = set( copy( sortedSet( set ) ) );
        assertEquals(
                set, set2
        );


        set2 = set( copy( safeSet( set ) ) );
        assertEquals(
                set, set2
        );



        set2 = set( copy( safeSortedSet( set ) ) );
        assertEquals(
                set, set2
        );


    }

    @Test public void creation() {

        Set<String> set = set("apple", "pear", "orange");

        Set<String> set2 = set(enumeration(set));
        assertEquals(
                set, set2
        );

        set2 = sortedSet(enumeration(set));
        assertEquals(
                set, set2
        );


        set2 = safeSet(enumeration(set));
        assertEquals(
                set, set2
        );

        set2 = safeSortedSet(enumeration(set));
        assertEquals(
                set, set2
        );

        Set<String> set3 = set((Iterable)set2);
        assertEquals(
                set2, set3
        );

        set3 = sortedSet((Iterable)set2);
        assertEquals(
                set2, set3
        );


        set3 = safeSortedSet((Iterable)set2);
        assertEquals(
                set2, set3
        );


        set3 = safeSet((Iterable)set2);
        assertEquals(
                set2, set3
        );


        Set<String> set4 = set((Collection)set3);
        assertEquals(
                set3, set4
        );


        set4 = safeSet((Collection)set3);
        assertEquals(
                set3, set4
        );


        set4 = safeSortedSet((Collection)set3);
        assertEquals(
                set3, set4
        );

        set4 = sortedSet((Collection)set3);
        assertEquals(
                set3, set4
        );

        Set<String> set5 = set(set4.iterator());
        assertEquals(
                set4, set5
        );


        set5 = sortedSet(set4.iterator());
        assertEquals(
                set4, set5
        );


        set5 = safeSortedSet(set4.iterator());
        assertEquals(
                set4, set5
        );


        set5 = safeSet(set4.iterator());
        assertEquals(
                set4, set5
        );

    }

    @Test
    public void creationalEquals() {

        assertTrue (

        sortedSet("apple", "pear", "orange") .equals(
                set("apple", "pear", "orange") ) &&
        safeSet("apple", "pear", "orange").equals(
                safeSortedSet("apple", "pear", "orange") ) &&
        sortedSet("apple", "pear", "orange") .equals(
                safeSortedSet("apple", "pear", "orange") )

        );

    }

    @Test
    public void enumerationTest() {
        Set<String> set = set("apple", "grape", "pears");
        Set<String> set2 = set(enumeration(set));
        assertEquals(
                set, set2
        );
    }

    Class<String> string = String.class;


    private void simpleOperations(Set<String> set) {

        add(set, "apple");

        assertTrue(

                len(set) == 1

        );



        assertTrue(

               set instanceof SortedSet ? idx(set, "a").equals("apple") : true

        );


        Set<String> set2 = copy(set);
        assertTrue(

                set2 instanceof SortedSet ? idx(set2, "a").equals("apple") : true

        );

        assertTrue(

                len(set2) == 1

        );




    }


    @Test
    public void simpleOperationsSortedSet() {

        simpleOperations(  sortedSet(string)  );
    }

    @Test
    public void simpleOperationsSet() {

        simpleOperations(  set(string)  );

    }

    @Test
    public void simpleOperationsSafeSet() {

        simpleOperations(  safeSet(string)  );

    }

    @Test
    public void simpleOperationsSafeSortedSet() {

        simpleOperations(  safeSortedSet(string)  );

    }

}