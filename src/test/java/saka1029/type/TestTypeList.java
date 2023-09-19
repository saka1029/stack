package saka1029.type;

import static org.junit.Assert.assertEquals;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class TestTypeList {

    static final PrintStream OUT = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    static final Type A = PrimeType.of("A");
    static final Type B = PrimeType.of("B");
    static final Type C = PrimeType.of("C");

    @Test
    public void testLeft() {
        TypeList list = TypeList.of(A, B, C);
        assertEquals(TypeList.of(), list.left(0));
        assertEquals(TypeList.of(A), list.left(1));
        assertEquals(TypeList.of(A, B), list.left(2));
        assertEquals(TypeList.of(A, B, C), list.left(3));
    }

    @Test
    public void testRight() {
        TypeList list = TypeList.of(A, B, C);
        assertEquals(TypeList.of(), list.right(0));
        assertEquals(TypeList.of(C), list.right(1));
        assertEquals(TypeList.of(B, C), list.right(2));
        assertEquals(TypeList.of(A, B, C), list.right(3));
        assertEquals(TypeList.of(C), list.right(3).right(2).right(1));
    }
    
    @Test
    public void testAppend() {
        assertEquals(TypeList.of(A, B, C), TypeList.of(A).append(TypeList.of(B, C)));
        assertEquals(TypeList.of(A, B, C), TypeList.of().append(TypeList.of(A, B, C)));
        TypeList list = TypeList.of(A, B, C);
        assertEquals(TypeList.of(A, B, C), list.left(1).append(list.right(2)));
        assertEquals(TypeList.of(A, B, C), list.left(2).append(list.right(1)));
        assertEquals(TypeList.of(A, B, C), list.left(0).append(list.right(3)));
    }

}
