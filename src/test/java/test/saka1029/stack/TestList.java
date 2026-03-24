package test.saka1029.stack;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import saka1029.stack.Array;
import saka1029.stack.Bool;
import saka1029.stack.Cons;
import saka1029.stack.Int;
import saka1029.stack.List;

public class TestList {
    
    @Test
    public void testEquals() {
        assertEquals(List.NIL, List.NIL);
        assertEquals(Cons.list(Int.of(0)), Cons.list(Int.of(0)));
        assertEquals(Cons.list(Int.of(0), Bool.of(true)), Cons.list(Int.of(0), Bool.of(true)));
    }

    @Test
    public void testAppend() {
        assertEquals(Cons.list(Int.of(1), Int.of(2), Int.of(3)),
            List.append(
                Cons.list(Int.of(1), Int.of(2)),
                List.NIL,
                Cons.list(Int.of(3))));
        assertEquals(Cons.list(Int.of(1), Int.of(2), Int.of(3)),
            List.append(
                Array.of(Int.of(1), Int.of(2)),
                Array.of(),
                Cons.list(Int.of(3))));
    }

}
