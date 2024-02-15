package saka1029.stack;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * インターフェースにした場合、hashCode(), equals(), toString()を デフォルトメソッドとして実装することができない。
 */
public abstract class List implements Instruction, Iterable<Instruction> {

    public abstract Sequence sequence();

    @Override
    public Iterator<Instruction> iterator() {
        return new Iterator<Instruction>() {
            final Sequence it = sequence();
            Instruction next = it.next();
            
            @Override
            public boolean hasNext() {
                return next != null;
            }
            
            @Override
            public Instruction next() {
                if (next == null)
                    throw new NoSuchElementException();
                Instruction result = next;
                next = it.next();
                return result;
            }
        };
    }

    public static final List NIL = new List() {

        @Override
        public Sequence sequence() {
            return () -> null;
        }

        @Override
        public String toString() {
            return "()";
        }
    };
    
//    public static List of(Iterable<Instruction> iterable) {
//        return new List() {
//            @Override
//            public Sequence sequence() {
//                Iterator<Instruction> iterator = iterable.iterator();
//                return () -> iterator.hasNext() ? iterator.next() : null;
//            }
//        };
//    }

    @Override
    public void execute(Context context) {
        context.instruction(sequence());
    }

    @Override
    public int hashCode() {
        int hash = 17;
        Sequence it = sequence();
        for (Instruction i = it.next(); i != null; i = it.next())
            hash = hash * 31 + i.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof List list))
            return false;
        Sequence leftIterator = sequence(), rightIterator = list.sequence();
        Instruction l = leftIterator.next(), r = rightIterator.next();
        for (; l != null && r != null; l = leftIterator.next(), r = rightIterator.next())
            if (!l.equals(r))
                return false;
        if (l == null && r == null)
            return true;
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        Sequence it = sequence();
        Instruction e = it.next();
        if (e != null) {
            sb.append(e);
            e = it.next();
        }
        while (e != null) {
            sb.append(" ").append(e);
            e = it.next();
        }
        return sb.append(")").toString();
    }
}
