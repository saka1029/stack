package test.saka1029.stack;

import static org.junit.Assert.*;

import java.util.Objects;

import org.junit.Test;

import saka1029.stack.Context;
import saka1029.stack.Value;

public class TestContext {

    static class Str implements Value {
        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Str s && s.value.equals(value);
        }

        final String value;

        Str(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    static final Str A = new Str("A");
    static final Str B = new Str("B");
    static final Str C = new Str("C");
    static final Str D = new Str("D");
    
    @Test
    public void testRot() {
        Context c = Context.of();
        c.push(A);
        c.push(B);
        c.push(C);
        c.execute("rot");
        assertEquals(A, c.pop());
        assertEquals(C, c.pop());
        assertEquals(B, c.pop());
    }

    @Test
    public void testDupOverDropSwap() {
        Context context = Context.of();
        context.push(A);
        assertEquals(1, context.size());
        context.execute("dup");
        assertEquals(2, context.size());
        assertEquals(A, context.pop());
        assertEquals(1, context.size());
        context.push(B);
        assertEquals(2, context.size());
        context.execute("over");
        assertEquals(3, context.size());
        assertEquals(A, context.peek());
        assertEquals(3, context.size());
        context.execute("drop");
        assertEquals(2, context.size());
        assertEquals(B, context.peek(0));
        assertEquals(A, context.peek(1));
        context.execute("swap");
        assertEquals(2, context.size());
        assertEquals(A, context.peek(0));
        assertEquals(B, context.peek(1));
    }
    
    @Test
    public void testDup() {
        Context context = Context.of();
        context.execute(A);
        context.execute(B);
        context.execute(C);
        context.execute(D);
        context.execute("dup");
        assertEquals(D, context.pop());
        context.execute("@1");
        assertEquals(C, context.pop());
        context.execute("@2");
        assertEquals(B, context.pop());
        context.execute("@3");
        assertEquals(A, context.pop());
    }

}
