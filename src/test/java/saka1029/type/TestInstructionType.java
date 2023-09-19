package saka1029.type;

import static org.junit.Assert.assertEquals;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class TestInstructionType {

    static final PrintStream OUT = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    static TypeList list(Type... list) {
        return TypeList.of(list);
    }

    static InstructionType ins(TypeList in, TypeList out) {
        return InstructionType.of(in, out);
    }

    static VariableType var(String name) {
        return VariableType.of(name);
    }

    static PrimeType prim(String name) {
        return PrimeType.of(name);
    }

    static final VariableType A = var("A"), B = var("B"), C = var("C"), D = var("D"), E = var("E"), F = var("F");
    static final InstructionType DUP = ins(list(A), list(A, A));
    static final InstructionType DROP = ins(list(B), list());
    static final InstructionType SWAP = ins(list(C, D), list(D, C));
    static final InstructionType OVER = ins(list(E, F), list(E, F, E));

    static final PrimeType INT = prim("i");
    static final InstructionType PLUS = ins(list(INT, INT), list(INT));
    static final InstructionType MINUS = ins(list(INT, INT), list(INT));

    @Test
    public void test() {
        OUT.println("これはテストです。");
        OUT.println(DUP);
        OUT.println(DROP);
        OUT.println(SWAP);
        OUT.println(OVER);
        OUT.println(PLUS);
    }

    @Test
    public void testCompine() {
        assertEquals(ins(list(INT, INT, INT), list(INT)), PLUS.combine(PLUS));
        InstructionType INT_DUP = ins(list(INT), list(INT, INT));
        assertEquals(ins(list(INT), list(INT)), INT_DUP.combine(PLUS));
        // dup : T -> T T
        // +   : Int Int -> Int
        // dup + : (T -> T T) ◦ (Int Int -> Int) = Int -> Int
        assertEquals(ins(list(INT), list(INT)), DUP.combine(PLUS));
    }

}
