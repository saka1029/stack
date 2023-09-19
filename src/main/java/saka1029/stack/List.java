package saka1029.stack;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public interface List extends Value, Iterable<Element> {

    public static final List NIL = new List() {
        @Override
        public String toString() {
            return "()";
        }
    };

    public static List of(Element... elements) {
        List result = NIL;
        for (int i = elements.length - 1; i >= 0; --i)
            result = Pair.of(elements[i], result);
        return result;
    }

    public static List of(java.util.List<Element> elements, Element tail) {
        ListIterator<Element> i = elements.listIterator(elements.size());
        if (!i.hasPrevious())
            if (tail == NIL)
                return NIL;
            else
                throw new IllegalArgumentException("Empty elements");
        Pair result = Pair.of(i.previous(), tail);
        while (i.hasPrevious())
            result = Pair.of(i.previous(), result);
        return result;
    }

    @Override
    default Iterator<Element> iterator() {
        return new Iterator<>() {

            Element element = List.this;

            @Override
            public boolean hasNext() {
                return element instanceof Pair;
            }

            @Override
            public Element next() {
                if (!(element instanceof Pair p))
                    throw new NoSuchElementException();
                element = p.tail;
                return p.head;
            }

        };
    }
    
    public static class ListInstruction implements Element {
        public final List list;

        ListInstruction(List list) {
            this.list = list;
        }

        @Override
        public void execute(Context context) {
            list.executeAll(context);
        }
        
        @Override
        public String toString() {
            return list.toString();
        }
        
    }
    
    default Element instruction() {
        return new ListInstruction(this);
    }

    default void executeAll(Context context) {
        for (Element i : this)
            i.execute(context);
    }
}
