package org.boon.utils;


import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Lists {


    public static <V> List<V> list(Class<V> clazz) {
        return new ArrayList<V>();
    }

    public static <V> List<V> list(Iterable<V> iterable) {
        List<V> list = new ArrayList<V>();
        for (V o : iterable) {
            list.add(o);
        }
        return list;
    }

    public static <V> List<V> list(Collection<V> collection) {
        List<V> list = new ArrayList<V>(collection);
        return list;
    }

    public static <V> List<V> list(Enumeration<V> enumeration) {
        List<V> list = new ArrayList<V>();
        while (enumeration.hasMoreElements()) {
            list.add(enumeration.nextElement());
        }
        return list;
    }


    public static <V> Enumeration<V> enumeration(final List<V> list) {
        final Iterator<V> iter = list.iterator();
        return new Enumeration<V>() {
            @Override
            public boolean hasMoreElements() {
                return iter.hasNext();
            }

            @Override
            public V nextElement() {
                return iter.next();
            }
        };

    }



    public static <V> List<V> list(Iterator<V> iterator) {
        List<V> list = new ArrayList<V>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

    public static <V> List<V> list(final V... array) {
        if (array==null) {
            return new ArrayList<>();
        }
        List<V> list = new ArrayList<V>(array.length);
        for (V o : array) {
            list.add(o);
        }
        return list;
    }

    public static <V> List<V> safeList(final V... array) {
        List<V> list = new CopyOnWriteArrayList<V>(array);
        return list;
    }

    public static <V> List<V> linkedList(final V... array) {
        if (array==null) {
            return new ArrayList<>();
        }
        List<V> list = new LinkedList<V>();
        for (V o : array) {
            list.add(o);
        }
        return list;
    }


    public static <V> List<V> safeList(Collection<V> collection) {
        List<V> list = new CopyOnWriteArrayList<V>(collection);
        return list;
    }

    public static <V> List<V> linkedList(Collection<V> collection) {
        List<V> list = new LinkedList<V>(collection);
        return list;
    }

    /** Universal methods */
    public static int len(List<?> list) {
        return list.size();
    }

    public static <V> boolean in(V value, List<?> list) {
            return list.contains(value);
    }

    public static <V> void add(List<V> list, V value) {
        list.add(value);
    }

    public static <T> T idx(List<T> list, final int index) {
        int i = calculateIndex(list, index);

        return list.get(i);

    }

    public static <V> void idx(List<V> list, int index, V v) {
        int i = calculateIndex(list, index);
        list.set(i, v);
    }

    public static <V> List<V> slc(List<V> list, int startIndex, int endIndex) {
        int start = calculateIndex(list, startIndex);
        int end = calculateIndex(list, endIndex);
        if (endIndex < 0) {
            return list.subList(start, end+1);
        } else {
            return list.subList(start, end);
        }
    }

    public static <V> List<V> slc(List<V> list, int startIndex) {
        return slc(list, startIndex, list.size());
    }


    public static <V> List<V> slcEnd(List<V> list, int endIndex) {
        return slc(list, 0, endIndex);
    }


    public static <V> List<V> copy(List<V> list) {
        if (list instanceof RandomAccess) {
            return new ArrayList<>(list);
        }   else if (list instanceof LinkedList) {
            return new LinkedList<>(list);
        }  else if (list instanceof CopyOnWriteArrayList) {
            return new CopyOnWriteArrayList<>(list);
        } else {
            return new ArrayList<>(list);
        }
    }

    public static <V> List<V> copy(CopyOnWriteArrayList<V> list) {
        Objects.requireNonNull(list, "list cannot be null");
        return new CopyOnWriteArrayList<>(list);
    }

    public static <V> List<V> copy(ArrayList<V> list) {
        Objects.requireNonNull(list, "list cannot be null");
        return new ArrayList<>(list);
    }

    public static <V> List<V> copy(LinkedList<V> list) {
        Objects.requireNonNull(list, "list cannot be null");
        return new LinkedList<>(list);
    }


    public static <V> void insert(List<V> list, int index, V v) {
        int i = calculateIndex(list, index);
        list.add(i, v);
    }


    /* End universal methods. */
    private static <T> int calculateIndex(List<T> list, int originalIndex) {
        final int length = list.size();

        Objects.requireNonNull(list, "list cannot be null");


        int index = originalIndex;

        /* Adjust for reading from the right as in
        -1 reads the 4th element if the length is 5
         */
        if (index < 0) {
            index = length + index;
        }

        /* Bounds check
            if it is still less than 0, then they
            have an negative index that is greater than length
         */
        if (index < 0) {
            throw new ArrayIndexOutOfBoundsException(
                    String.format("Out Of Bounds: length was %s index is %s and list is %s",
                            length, originalIndex, list
                    ));
        }
        if (index > length) {

            throw new ArrayIndexOutOfBoundsException(
                    String.format("Out Of Bounds: length was %s index is %s and list is %s",
                            length, originalIndex, list
                    ));
        }
        return index;
    }


}
