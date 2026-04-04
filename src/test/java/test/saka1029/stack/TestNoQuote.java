package test.saka1029.stack;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

public class TestNoQuote {

    interface Instruction {
        void execute(Context c);
    }

    static class Context {
        final Map<Symbol, Instruction> instructions = new HashMap<>();
        final Instruction[] stack = new Instruction[100];
        int sp = 0;

        void push(Instruction value) {
            stack[sp++] = value;
        }

        Instruction pop() {
            return stack[--sp];
        }

        void instruction(Symbol symbol, Instruction i) {
            instructions.put(symbol, i);
        }

        Instruction instruction(Symbol symbol) {
            return instructions.get(symbol);
        }

        static int i(Instruction instruction) {
            return ((Int)instruction).value;
        }

        static Int i(int value) {
            return Int.of(value);
        }

        public Context() {
            instructions.put(Symbol.of("+"), c -> c.push(i(i(c.pop()) + i(c.pop()))));
        }

        @Override
        public String toString() {
            return "Context[sp=%d stack=%s]".formatted(
                sp, IntStream.range(0, sp)
                    .mapToObj(i -> stack[i].toString())
                    .collect(Collectors.joining(" ", "[", "]")));
        }
    }

    interface Value extends Instruction {
        @Override
        default void execute(Context c) {
            c.push(this);
        }
    }

    public static abstract class List implements Instruction, Iterable<Instruction> {
        public static final List NIL = new List() {
            @Override
            public Iterator<Instruction> iterator() {
                return new Iterator<>() {
                    public boolean hasNext() { return false; }
                    public Instruction next() { throw new NoSuchElementException(); }
                };
            }
        };

        @Override
        public void execute(Context c) {
            for (Instruction instruction : this)
                instruction.execute(c);
        }

        @Override
        public int hashCode() {
            int hash = 17;
            for (Iterator<Instruction> i = iterator(); i.hasNext();)
                hash = hash * 31 + i.next().hashCode();
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof List list))
                return false;
            Iterator<Instruction> li = iterator(), ri = list.iterator();
            while (li.hasNext() && ri.hasNext())
                if (!li.next().equals(ri.next()))
                    return false;
            return !li.hasNext() && !ri.hasNext();
        }

    }

    public static class Cons extends List {
        public final Instruction car;
        public final List cdr;

        Cons(Instruction car, List cdr) {
            this.car = car;
            this.cdr = cdr;
        }

        public static List of(Instruction... elements) {
            List result = NIL;
            for (int i = 0, size = elements.length; i < size; ++i)
                result = new Cons(elements[i], result);
            return result;
        }

        @Override
        public Iterator<Instruction> iterator() {
            return new Iterator<>() {
                List list = Cons.this;
                @Override
                public boolean hasNext() {
                    return list != NIL;
                }

                @Override
                public Instruction next() {
                    if (list instanceof Cons cons) {
                        Instruction result = cons.car;
                        list = cons.cdr;
                        return result;
                    } else
                        throw new NoSuchElementException();
                }
            };
        }
    }

    public static class Symbol implements Value {
        public final String name;
        static final Map<String, Symbol> all = new HashMap<>();

        Symbol(String name) {
            this.name = name;
        }

        public static Symbol of(String name) {
            return all.computeIfAbsent(name, k -> new Symbol(name));
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static class Int implements Value {
        public final int value;

        Int(int value) {
            this.value = value;
        }

        public static Int of(int value) {
            return new Int(value);
        }

        @Override
        public int hashCode() {
            return value;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Int i && i.value == value;
        }

        @Override
        public String toString() {
            return "" + value;
        }
    }

    @Test
    public void testLoad() {
        Context c = new Context();
        c.push(Int.of(3));
        assertEquals(1, c.sp);
        assertEquals(Int.of(3), c.pop());
        c.push(Cons.of(Int.of(1), Int.of(2)));
        assertEquals(1, c.sp);
        assertEquals(Cons.of(Int.of(1), Int.of(2)), c.pop());
    }

    @Test
    public void testExecute() {
        Context c = new Context();
        Int.of(3).execute(c);
        assertEquals(1, c.sp);
        assertEquals(Int.of(3), c.pop());
        Cons.of(Int.of(1), Int.of(2)).execute(c);
        assertEquals(2, c.sp);
        assertEquals(Int.of(1), c.pop());
        assertEquals(Int.of(2), c.pop());
    }

    @Test
    public void testPlus() {
        Context c = new Context();
        Int.of(3).execute(c);
        Int.of(4).execute(c);
        c.instruction(Symbol.of("+")).execute(c);
        assertEquals(1, c.sp);
        assertEquals(Int.of(7), c.pop());
    }
}
