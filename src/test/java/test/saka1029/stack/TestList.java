package test.saka1029.stack;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import saka1029.stack.Int;
import saka1029.stack.List;

public class TestList {
    
    @Test
    public void testEquals() {
        assertEquals(List.NIL, List.NIL);
        assertEquals(List.of(Int.of(0)), List.of(Int.of(0)));
    }

}
