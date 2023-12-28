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
        assertEquals(Int.of(1), Stack.eval(context, "true '1 '2 if"));
        assertEquals(Int.of(3), Stack.eval(context, "1 2 true '+ '- if"));
        assertEquals(Int.of(-1), Stack.eval(context, "1 2 false '+ '- if"));
        assertEquals(Int.of(3), Stack.eval(context, "true '(1 2 +) '(1 2 -) if"));
        assertEquals(Int.of(-1), Stack.eval(context, "false '(1 2 +) '(1 2 -) if"));
    }
    
    @Test
    public void testFor() {
        Context context = Stack.context();
        assertEquals(Int.of(6), Stack.eval(context, "0 '(1 2 3) '+ for"));
    }
    
    @Test
    public void testRane() {
        Context context = Stack.context();
        assertEquals(Int.of(6), Stack.eval(context, "0 1 3 range '+ for"));
    }

}
