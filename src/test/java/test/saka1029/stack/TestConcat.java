package test.saka1029.stack;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestConcat {

    static class Func {
        public final int i, o;
        Func(int i, int o) {
            this.i = i;
            this.o = o;
        }
        @Override
        public boolean equals(Object obj) {
            return obj instanceof Func f && f.i == i && f.o == o;
        }
        @Override
        public String toString() {
            return i + ":" + o;
        }
    }

    static Func f(int i, int o) {
        return new Func(i, o);
    }

    static Func concat2(Func a, Func b) {
        int i = a.i, o = b.o;
        if (a.o < b.i)
            i = a.i + b.i - a.o;
        else if (a.o > b.i)
            o = b.o + a.o - b.i;
        return f(i, o);
    }
    static Func concat(Func a, Func... b) {
        Func r = a;
        for (Func f : b)
            r = concat2(r, f);
        return r;
    }

    @Test
    public void testConcat() {
        assertEquals(f(0, 1), concat(f(0, 1)));
        assertEquals(f(0, 1), concat(f(0, 1), f(0, 1), f(2, 1)));
        assertEquals(f(1, 1), concat(f(0, 1), f(2, 1)));
        assertEquals(f(0, 1), concat(f(0, 2), f(2, 1)));
        assertEquals(f(0, 2), concat(f(0, 1), f(0, 1)));
        assertEquals(f(2, 1), concat(f(1, 1), f(2, 1)));
    }
}
