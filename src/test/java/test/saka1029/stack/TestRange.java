package test.saka1029.stack;

import static org.junit.Assert.assertEquals;
import static saka1029.stack.Stack.context;
import static saka1029.stack.Stack.eval;

import org.junit.Test;

import saka1029.stack.Context;

public class TestRange {

    @Test
    public void testStep1() {
        Context c = context();
        assertEquals(eval(c, "'(1 2 3)"), eval(c, "1 3 1 range"));
    }

    @Test
    public void testStep3() {
        Context c = context();
        assertEquals(eval(c, "'(0 3 6)"), eval(c, "0 6 3 range"));
    }

    @Test
    public void testStepMinus3() {
        Context c = context();
        assertEquals(eval(c, "'(6 3 0)"), eval(c, "6 0 -3 range"));
    }

}
