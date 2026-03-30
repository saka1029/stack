package test.saka1029.stack;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import saka1029.stack.Context;
import saka1029.stack.Int;
import saka1029.stack.Stack;

public class TestStore {

    @Test
    public void testStore() {
        Context c = Stack.context();
        assertEquals(Int.of(3), Stack.eval(c, "1 2 + @v v"));
        assertEquals(Int.of(4), Stack.eval(c, "v 1 + @v v"));
    }
}
