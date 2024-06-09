package test.saka1029.stack;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestSpec {

    interface Spec {
        int i();
        int o();
    }

    record Func(int i, int o) implements Spec {
        @Override
        public boolean equals(Object obj) {
            return obj instanceof Func f && f.i == i && f.o == o;
        }

        @Override
        public String toString() {
            return i + ":" + o;
        }
    }

    static Func spec(int i, int o) {
        return new Func(i, o);
    }

    static Spec concat2(Spec a, Spec b) {
        int i = a.i(), o = b.o();
        if (a.o() < b.i())
            i = a.i() + b.i() - a.o();
        else if (a.o() > b.i())
            o = b.o() + a.o() - b.i();
        return spec(i, o);
    }

    static Spec concat(Spec a, Spec... b) {
        Spec r = a;
        for (Spec s : b)
            r = concat2(r, s);
        return r;
    }

    @Test
    public void testConcat() {
        assertEquals(spec(0, 1), concat(spec(0, 1)));
        assertEquals(spec(0, 1), concat(spec(0, 1), spec(0, 1), spec(2, 1)));
        assertEquals(spec(1, 1), concat(spec(0, 1), spec(2, 1)));
        assertEquals(spec(0, 1), concat(spec(0, 2), spec(2, 1)));
        assertEquals(spec(0, 2), concat(spec(0, 1), spec(0, 1)));
        assertEquals(spec(2, 1), concat(spec(1, 1), spec(2, 1)));
    }
}
