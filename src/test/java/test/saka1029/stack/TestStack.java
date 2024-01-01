package test.saka1029.stack;

import static org.junit.Assert.assertEquals;
import static saka1029.stack.Stack.*;

import org.junit.Test;

import saka1029.stack.Context;
import saka1029.stack.Int;
import saka1029.stack.List;

public class TestStack {

    @Test
    public void testPlus() {
        Context c = context();
        assertEquals(Int.of(3), eval(c, "1 2 + "));
        assertEquals(Int.of(3), eval(c, "(1 2) + "));
        assertEquals(Int.of(3), eval(c, "1 2 (+) "));
    }

    @Test
    public void testCar() {
        Context c = context();
        assertEquals(Int.of(1), eval(c, "'(1 2) car"));
    }

    @Test
    public void testCdr() {
        Context c = context();
        assertEquals(List.of(Int.of(2)), eval(c, "'(1 2) cdr"));
    }

    @Test
    public void testUncons() {
        Context c = context();
        run(c, "'(1 2) uncons");
        assertEquals(List.of(Int.of(2)), c.pop());
        assertEquals(Int.of(1), c.pop());
    }

    @Test
    public void testCons() {
        Context c = context();
        assertEquals(List.of(Int.of(1)), eval(c, "1 '() cons"));
    }

    @Test
    public void testRcons() {
        Context c = context();
        assertEquals(List.of(Int.of(1)), eval(c, "'() 1 rcons"));
    }

    @Test
    public void testIf() {
        Context c = context();
        assertEquals(Int.of(1), eval(c, "true 1 2 if"));
        assertEquals(Int.of(2), eval(c, "false 1 2 if"));
        assertEquals(Int.of(1), eval(c, "true '1 '2 if"));
        assertEquals(Int.of(3), eval(c, "1 2 true '+ '- if"));
        assertEquals(Int.of(-1), eval(c, "1 2 false '+ '- if"));
        assertEquals(Int.of(3), eval(c, "true '(1 2 +) '(1 2 -) if"));
        assertEquals(Int.of(-1), eval(c, "false '(1 2 +) '(1 2 -) if"));
    }
    
    @Test
    public void testFor() {
        Context c = context();
        assertEquals(Int.of(6), eval(c, "0 '(1 2 3) '+ for"));
    }
    
    @Test
    public void testRange() {
        Context c = context();
        assertEquals(Int.of(6), eval(c, "0 1 3 range1 '+ for"));
    }
    
    @Test
    public void testDefine() {
        Context c = context();
        run(c, "'+ 'plus define");
        assertEquals(Int.of(3), eval(c, "1 2 plus"));
        run(c, "3 'three define");
        assertEquals(Int.of(3), eval(c, "three"));
    }
    
    @Test
    public void testFactorialRecuesive() {
        Context c = context();
        run(c, "'(dup 0 <= '(drop 1) '(dup 1 - factorial *) if) 'factorial define");
        assertEquals(Int.of(1), eval(c, "0 factorial"));
        assertEquals(Int.of(1), eval(c, "1 factorial"));
        assertEquals(Int.of(2), eval(c, "2 factorial"));
        assertEquals(Int.of(6), eval(c, "3 factorial"));
        assertEquals(Int.of(24), eval(c, "4 factorial"));
    }
    
    @Test
    public void testFactorialFor() {
        Context c = context();
        run(c, "'(1 swap 1 swap 1 range '* for) 'factorial define");
        assertEquals(Int.of(1), eval(c, "0 factorial"));
        assertEquals(Int.of(1), eval(c, "1 factorial"));
        assertEquals(Int.of(2), eval(c, "2 factorial"));
        assertEquals(Int.of(6), eval(c, "3 factorial"));
        assertEquals(Int.of(24), eval(c, "4 factorial"));
    }
    
    @Test
    public void testFibonacciRecuesive() {
        Context c = context();
        run(c, "'(dup 1 <= '() '(dup 1 - fibonacci swap 2 - fibonacci +) if) 'fibonacci define");
        assertEquals(Int.of(0), eval(c, "0 fibonacci"));
        assertEquals(Int.of(1), eval(c, "1 fibonacci"));
        assertEquals(Int.of(1), eval(c, "2 fibonacci"));
        assertEquals(Int.of(2), eval(c, "3 fibonacci"));
        assertEquals(Int.of(3), eval(c, "4 fibonacci"));
        assertEquals(Int.of(5), eval(c, "5 fibonacci"));
        assertEquals(Int.of(8), eval(c, "6 fibonacci"));
    }
    
    @Test
    public void testFibonacciFor() {
        Context c = context();
        run(c, "'(0 swap 1 swap 1 swap 1 range '(drop dup rrot +) for drop) 'fibonacci define");
        assertEquals(Int.of(0), eval(c, "0 fibonacci"));
        assertEquals(Int.of(1), eval(c, "1 fibonacci"));
        assertEquals(Int.of(1), eval(c, "2 fibonacci"));
        assertEquals(Int.of(2), eval(c, "3 fibonacci"));
        assertEquals(Int.of(3), eval(c, "4 fibonacci"));
        assertEquals(Int.of(5), eval(c, "5 fibonacci"));
        assertEquals(Int.of(8), eval(c, "6 fibonacci"));
    }
    
    @Test
    public void testAppendRecursive() {
        Context c = context();
        run(c, "'(swap dup null? 'drop '(uncons rot append cons) if) 'append define");
        assertEquals(eval(c, "'(3 4)"), eval(c, "'() '(3 4) append"));
        assertEquals(eval(c, "'(2 3 4)"), eval(c, "'(2) '(3 4) append"));
        assertEquals(eval(c, "'(1 2 3 4)"), eval(c, "'(1 2) '(3 4) append"));
    }
    
    @Test
    public void testAppendTailRecursive() {
        Context c = context();
        run(c, "'(swap dup null? 'drop '(uncons rot append cons) if) 'append define");
        assertEquals(eval(c, "'(3 4)"), eval(c, "'() '(3 4) append"));
        assertEquals(eval(c, "'(2 3 4)"), eval(c, "'(2) '(3 4) append"));
        assertEquals(eval(c, "'(1 2 3 4)"), eval(c, "'(1 2) '(3 4) append"));
    }
    
    @Test
    public void testReverseRecursive() {
        Context c = context();
        run(c, "'(swap dup null? 'drop '(uncons rot append cons) if) 'append define");
        run(c, "'(dup null? '() '(uncons reverse swap '() cons append) if) 'reverse define");
        assertEquals(eval(c, "'()"), eval(c, "'() reverse"));
        assertEquals(eval(c, "'(1)"), eval(c, "'(1) reverse"));
        assertEquals(eval(c, "'(2 1)"), eval(c, "'(1 2) reverse"));
        assertEquals(eval(c, "'(3 2 1)"), eval(c, "'(1 2 3) reverse"));
        // rangeはConsではないのでunconsはできない。
//        assertEquals(eval(c, "'(3 2 1)"), eval(c, "1 3 range1 reverse"));
    }
    
    @Test
    public void testReverseFor() {
        Context c = context();
        run(c, "'('() swap '(swap cons) for) 'reverse define");
        assertEquals(eval(c, "'()"), eval(c, "'() reverse"));
        assertEquals(eval(c, "'(1)"), eval(c, "'(1) reverse"));
        assertEquals(eval(c, "'(2 1)"), eval(c, "'(1 2) reverse"));
        assertEquals(eval(c, "'(3 2 1)"), eval(c, "'(1 2 3) reverse"));
        // forはnext()だけを使うのでreverseできる。
        assertEquals(eval(c, "'(3 2 1)"), eval(c, "1 3 range1 reverse"));
    }
    
    @Test
    public void testReverseForRcons() {
        Context c = context();
        run(c, "'('() swap 'rcons for) 'reverse define");
        assertEquals(eval(c, "'()"), eval(c, "'() reverse"));
        assertEquals(eval(c, "'(1)"), eval(c, "'(1) reverse"));
        assertEquals(eval(c, "'(2 1)"), eval(c, "'(1 2) reverse"));
        assertEquals(eval(c, "'(3 2 1)"), eval(c, "'(1 2 3) reverse"));
        // forはnext()だけを使うのでreverseできる。
        assertEquals(eval(c, "'(3 2 1)"), eval(c, "1 3 range1 reverse"));
    }
    
    @Test
    public void testExecute() {
        Context c = context();
        assertEquals(Int.of(3), eval(c, "'(1 2 +) execute"));
        assertEquals(Int.of(3), eval(c, "3 execute"));
    }
    
    @Test
    public void testMapCarFirst() {
        Context c = context();
        run(c, "'(swap dup null? '() '(uncons swap dup2 execute swap dup2 map cons) if swap drop) 'map define");
        assertEquals(eval(c, "'()"), eval(c, "'() '(1 +) map"));
        assertEquals(eval(c, "'(1)"), eval(c, "'(0) '(1 +) map"));
        assertEquals(eval(c, "'(1 2 3)"), eval(c, "'(0 1 2) '(1 +) map"));
        assertEquals(eval(c, "'(1 2 3 4 5)"), eval(c, "'(0 1 2 3 4) '(1 +) map"));
    }
    
    @Test
    public void testFilterRecursiveCdrFirst() {
        Context c = context();
        run(c, "'(swap dup null? '() '(uncons dup2 filter swap dup dup3 execute 'rcons 'drop if) if swap drop) 'filter define");
        assertEquals(eval(c, "'(0 2)"), eval(c, "'(0 1 2 3) '(2 % 0 ==) filter"));
        assertEquals(eval(c, "'(1 3)"), eval(c, "'(0 1 2 3) '(2 % 0 !=) filter"));
    }
    
    @Test
    public void testFilterRecursiveCarFirst() {
        Context c = context();
        run(c, "'(swap dup null? '() '(uncons swap dup dup3 execute rot dup3 filter swap 'cons '(swap drop) if) if swap drop) 'filter define");
        assertEquals(eval(c, "'(0 2)"), eval(c, "'(0 1 2 3) '(2 % 0 ==) filter"));
        assertEquals(eval(c, "'(1 3)"), eval(c, "'(0 1 2 3) '(2 % 0 !=) filter"));
    }
}
