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

    @Test
    public void testIf() {
        Context context = Stack.context();
        assertEquals(Int.of(1), Stack.eval(context, "true 1 2 if"));
        assertEquals(Int.of(2), Stack.eval(context, "false 1 2 if"));

    }

}
