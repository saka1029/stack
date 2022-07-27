package saka1029;

import static org.junit.Assert.*;
import static saka1029.Stack.*;

import org.junit.Test;

public class TestStackCons {


    @Test
    public void testToString() {
        assertEquals("[]", NIL.toString());
        assertEquals("[1]", cons(i(1), NIL).toString());
        assertEquals("[0 1]", cons(i(0), cons(i(1), NIL)).toString());
        assertEquals("[0 1]", list(i(0), i(1)).toString());
    }

    @Test
    public void testEquals() {
        assertEquals(NIL, list());
        assertEquals(list(i(0)), list(i(0)));
        assertEquals(list(i(0), i(1)), list(i(0), i(1)));
    }
    
    @Test
    public void testBuilder() {
        assertEquals(list(), List.builder().build());
        assertEquals(list(i(1)), List.builder().add(i(1)).build());
        assertEquals(list(i(0), i(1)), List.builder().add(i(0)).add(i(1)).build());
    }
    
    @Test
    public void testFilter() {
        assertEquals(list(i(1)),
            toList(Iterables.filter(i -> ((Int)i).value % 2 == 1,list(i(0), i(1), i(2)))));
    }
    
}
