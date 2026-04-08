package test.saka1029.stack;

import static org.junit.Assert.assertEquals;
import static saka1029.stack.Stack.context;
import static saka1029.stack.Stack.eval;
import static saka1029.stack.Stack.run;

import org.junit.Test;

import saka1029.stack.Context;

public class TestGenerator {

    /**
     * generator:
     * ARGUMENT LIST generator -> LIST
     */
    @Test
    public void testGenerator() {
        Context c = context();
        // c.variable(Symbol.of("generator"), x -> x.push(Generator.of(x, c.pop())));
        // c.variable(Symbol.of("generator1"), x -> x.push(Generator.of(x, c.pop(), c.pop())));
        // c.variable(Symbol.of("generator2"), x -> x.push(Generator.of(x, c.pop(), c.pop(), c.pop())));
        // c.variable(Symbol.of("yield"), Terminal.YIELD);
        assertEquals(eval(c, "'(3 2 1)"), eval(c, "'(1 2 + yield 2 yield 1 yield) generator"));
        assertEquals(eval(c, "'(6 5 4)"), eval(c, "3 '(dup 1 2 + + yield dup 2 + yield dup 1 + yield) generator1"));
        assertEquals(eval(c, "'(10 9 8)"), eval(c, "'3 4 '(+ dup 2 1 + + yield dup 2 + yield dup 1 + yield) generator2"));
        assertEquals(eval(c, "'(1 2)"), eval(c, "'(1 3 range 'yield for) generator"));
        assertEquals(eval(c, "'(1 2 6 24)"), eval(c, "'(1 1 (4 1 +) range '(* dup yield) for) generator"));
    }

    @Test
    public void testFactorials() {
        Context c = context();
        run(c, "'('(1 + 1 1 rot stack range '(* dup yield) for) cons generator) @ factorials");
        assertEquals(eval(c, "'(1 2 6 24 120)"), eval(c, "5 factorials"));
    }
}
