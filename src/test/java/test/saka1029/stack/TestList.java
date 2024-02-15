package test.saka1029.stack;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import saka1029.stack.Bool;
import saka1029.stack.Cons;
import saka1029.stack.Int;
import saka1029.stack.List;

public class TestList {
    
    @Test
    public void testEquals() {
        assertEquals(List.NIL, List.NIL);
        assertEquals(Cons.of(Int.of(0)), Cons.of(Int.of(0)));
        assertEquals(Cons.of(Int.of(0), Bool.of(true)), Cons.of(Int.of(0), Bool.of(true)));
    }

}
