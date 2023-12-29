package test.saka1029.stack;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import saka1029.stack.Context;
import saka1029.stack.Int;
import saka1029.stack.List;
import saka1029.stack.Stack;

public class TestStack {

    @Test
    public void testPlus() {
        Context context = Stack.context();
        assertEquals(Int.of(3), Stack.eval(context, "1 2 + "));
        assertEquals(Int.of(3), Stack.eval(context, "(1 2) + "));
        assertEquals(Int.of(3), Stack.eval(context, "1 2 (+) "));
    }

    @Test
    public void testCar() {
        Context c = Stack.context();
        assertEquals(Int.of(1), Stack.eval(c, "'(1 2) car"));
    }

    @Test
    public void testCdr() {
        Context c = Stack.context();
        assertEquals(List.of(Int.of(2)), Stack.eval(c, "'(1 2) cdr"));
    }

    @Test
    public void testUncons() {
        Context c = Stack.context();
        Stack.run(c, "'(1 2) uncons");
        assertEquals(List.of(Int.of(2)), c.pop());
        assertEquals(Int.of(1), c.pop());
    }

    @Test
    public void testCons() {
        Context c = Stack.context();
        assertEquals(List.of(Int.of(1)), Stack.eval(c, "1 '() cons"));
    }

    @Test
    public void testRcons() {
        Context c = Stack.context();
        assertEquals(List.of(Int.of(1)), Stack.eval(c, "'() 1 rcons"));
    }

    @Test
    public void testIf() {
        Context context = Stack.context();
        assertEquals(Int.of(1), Stack.eval(context, "true 1 2 if"));
        assertEquals(Int.of(2), Stack.eval(context, "false 1 2 if"));
        assertEquals(Int.of(1), Stack.eval(context, "true '1 '2 if"));
        assertEquals(Int.of(3), Stack.eval(context, "1 2 true '+ '- if"));
        assertEquals(Int.of(-1), Stack.eval(context, "1 2 false '+ '- if"));
        assertEquals(Int.of(3), Stack.eval(context, "true '(1 2 +) '(1 2 -) if"));
        assertEquals(Int.of(-1), Stack.eval(context, "false '(1 2 +) '(1 2 -) if"));
    }
    
    @Test
    public void testFor() {
        Context context = Stack.context();
        assertEquals(Int.of(6), Stack.eval(context, "0 '(1 2 3) '+ for"));
    }
    
    @Test
    public void testRange() {
        Context context = Stack.context();
        assertEquals(Int.of(6), Stack.eval(context, "0 1 3 range1 '+ for"));
    }
    
    @Test
    public void testDefine() {
        Context context = Stack.context();
        Stack.run(context, "'+ 'plus define");
        assertEquals(Int.of(3), Stack.eval(context, "1 2 plus"));
        Stack.run(context, "3 'three define");
        assertEquals(Int.of(3), Stack.eval(context, "three"));
    }
    
    @Test
    public void testFactorialRecuesive() {
        Context context = Stack.context();
        Stack.run(context, "'(@0 0 <= '(drop 1) '(@0 1 - factorial *) if) 'factorial define");
        assertEquals(Int.of(1), Stack.eval(context, "0 factorial"));
        assertEquals(Int.of(1), Stack.eval(context, "1 factorial"));
        assertEquals(Int.of(2), Stack.eval(context, "2 factorial"));
        assertEquals(Int.of(6), Stack.eval(context, "3 factorial"));
        assertEquals(Int.of(24), Stack.eval(context, "4 factorial"));
    }
    
    @Test
    public void testFactorialFor() {
        Context context = Stack.context();
        Stack.run(context, "'(1 swap 1 swap 1 range '* for) 'factorial define");
        assertEquals(Int.of(1), Stack.eval(context, "0 factorial"));
        assertEquals(Int.of(1), Stack.eval(context, "1 factorial"));
        assertEquals(Int.of(2), Stack.eval(context, "2 factorial"));
        assertEquals(Int.of(6), Stack.eval(context, "3 factorial"));
        assertEquals(Int.of(24), Stack.eval(context, "4 factorial"));
    }
    
    @Test
    public void testFibonacciRecuesive() {
        Context context = Stack.context();
        Stack.run(context, "'(@0 1 <= '() '(@0 1 - fibonacci swap 2 - fibonacci +) if) 'fibonacci define");
        assertEquals(Int.of(0), Stack.eval(context, "0 fibonacci"));
        assertEquals(Int.of(1), Stack.eval(context, "1 fibonacci"));
        assertEquals(Int.of(1), Stack.eval(context, "2 fibonacci"));
        assertEquals(Int.of(2), Stack.eval(context, "3 fibonacci"));
        assertEquals(Int.of(3), Stack.eval(context, "4 fibonacci"));
        assertEquals(Int.of(5), Stack.eval(context, "5 fibonacci"));
        assertEquals(Int.of(8), Stack.eval(context, "6 fibonacci"));
    }

}
