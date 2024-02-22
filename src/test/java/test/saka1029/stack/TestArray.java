package test.saka1029.stack;

import static org.junit.Assert.assertEquals;
import static saka1029.stack.Stack.eval;

import org.junit.Test;

import saka1029.stack.Context;
import saka1029.stack.Stack;

public class TestArray {

    @Test
    public void testAtPut() {
        Context c = Stack.context();
//        assertEquals(eval(c, "'(() () () () ())"), eval(c, "'() 5 array"));
        assertEquals(eval(c, "'(false false false false false)"), eval(c, "false 5 array"));
    }

}
