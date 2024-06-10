package test.saka1029.stack;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestSpec {

    static class Context {

    }

    interface Instruction {
        void execute(Context c);
    }

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

    static Spec SPEC_VALUE = new Func(0, 1);
    static Spec SPEC_VALUE2 = new Func(0, 2);
    static Spec SPEC_UNARY = new Func(1, 1);
    static Spec SPEC_BINARY = new Func(2, 1);

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
        assertEquals(SPEC_VALUE, concat(SPEC_VALUE));
        assertEquals(SPEC_VALUE, concat(SPEC_VALUE, SPEC_VALUE, SPEC_BINARY));
        assertEquals(SPEC_UNARY, concat(SPEC_VALUE, SPEC_BINARY));
        assertEquals(SPEC_VALUE, concat(SPEC_VALUE2, SPEC_BINARY));
        assertEquals(SPEC_VALUE2, concat(SPEC_VALUE, SPEC_VALUE));
        assertEquals(SPEC_BINARY, concat(SPEC_UNARY, SPEC_BINARY));
    }
}
