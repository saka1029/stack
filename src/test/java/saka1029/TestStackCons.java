package saka1029;

import static org.junit.Assert.*;
import static saka1029.Stack.*;

import org.junit.Test;

public class TestStackCons {

    @Test
    public void testToString() {
        assertEquals("[]", List.NIL.toString());
        assertEquals("[1]", Cons.of(Int.of(1), List.NIL).toString());
        assertEquals("[0 1]", Cons.of(Int.of(0), Cons.of(Int.of(1), List.NIL)).toString());
        assertEquals("[0 1]", List.of(Int.of(0), Int.of(1)).toString());
    }
    
    @Test
    public void testBuilder() {
        assertEquals("[]", List.builder().build().toString());
        assertEquals("[1]", List.builder().add(Int.of(1)).build().toString());
        assertEquals("[0 1]", List.builder().add(Int.of(0)).add(Int.of(1)).build().toString());
        
    }

}
