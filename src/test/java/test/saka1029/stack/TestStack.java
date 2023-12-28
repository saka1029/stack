package test.saka1029.stack;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import saka1029.stack.Context;
import saka1029.stack.Int;
import saka1029.stack.Stack;

public class TestStack {

    @Test
    public void testPlus() {
        Context context = Stack.context();
        assertEquals(Int.of(3), Stack.eval(context, "1 2 + "));
        assertEquals(Int.of(3), Stack.eval(context, "(1 2) + "));
        assertEquals(Int.of(3), Stack.eval(context, "1 2 (+) "));
    }
}
