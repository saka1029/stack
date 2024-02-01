package saka1029.stack;

import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * インターフェースにした場合、hashCode(), equals(), toString()を デフォルトメソッドとして実装することができない。
 */
public abstract class List implements Instruction {

    public abstract Iterator iterator();

    public Iterable<Instruction> iterable() {
        return () -> new java.util.Iterator<>() {
            final Iterator it = iterator();
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
        public Iterator iterator() {
            return () -> null;
        }

        @Override
        public String toString() {
            return "()";
        }
    };

    public static List of(Instruction... instructions) {
        int size = instructions.length;
        List result = NIL;
        for (int i = size - 1; i >= 0; --i)
            result = Cons.of(instructions[i], result);
        return result;
    }

    public static List of(java.util.List<Instruction> list) {
        List result = NIL;
        ListIterator<Instruction> it = list.listIterator(list.size());
        while (it.hasPrevious())
            result = Cons.of(it.previous(), result);
        return result;
    }

    @Override
    public void execute(Context context) {
        context.instruction(iterator());
    }

    @Override
    public int hashCode() {
        int hash = 17;
        Iterator it = iterator();
        for (Instruction i = it.next(); i != null; i = it.next())
            hash = hash * 31 + i.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof List list))
            return false;
        Iterator leftIterator = iterator(), rightIterator = list.iterator();
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
        Iterator it = iterator();
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
