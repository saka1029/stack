package saka1029.type;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TypeList implements Iterable<Type> {

    public final int start, end;
    final Type[] list;
    
    TypeList(int start, int end, Type... list) {
        this.start = start;
        this.end = end;
        this.list = list;
    }
    
    public boolean matches(TypeList right, BindMap map) {
        if (size() != right.size())
            return false;
        for (int i = start, j = right.start; i < end; ++i, ++j)
            if (!Type.matches(list[i], right.list[j], map))
                return false;
        return true;
    }

    public TypeList left(int size) {
        return new TypeList(start, start + size, list);
    }

    public TypeList right(int size) {
        return new TypeList(end - size, end, list);
    }
    
    public TypeList append(TypeList right) {
        int size = size(), rsize = right.size(), dsize = size + rsize;
        Type[] array = new Type[dsize];
        System.arraycopy(list, start, array, 0, size);
        System.arraycopy(right.list, right.start, array, size, rsize);
        return new TypeList(0, dsize, array);
    }
    
    public TypeList resolve(BindMap map) {
        int size = end - start;
        Type[] array = new Type[size];
        for (int i = start, j = 0; i < end; ++i, ++j) {
            if (list[i] instanceof VariableType v) {
                Type value = map.get(v);
                array[j] = value != null ? value : v;
            } else
                array[j] = list[i];
        }
        return new TypeList(0, size, array);
    }

    public static TypeList of(Type... list) {
        return new TypeList(0, list.length, list.clone());
    }
    
    public int size() {
        return end - start;
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj instanceof TypeList l && Arrays.equals(list, start, end, l.list, l.start, l.end);
    }

    @Override
    public Iterator<Type> iterator() {
        return new Iterator<>() {
            int i = start;

            @Override
            public boolean hasNext() {
                return i < end;
            }

            @Override
            public Type next() {
                return list[i++];
            }
        };
    }
    
    @Override
    public String toString() {
        return IntStream.range(start, end)
            .mapToObj(i -> list[i].toString())
            .collect(Collectors.joining(", ", "(", ")"));
    }
}
