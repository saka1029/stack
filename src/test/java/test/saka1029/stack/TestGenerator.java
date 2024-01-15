package test.saka1029.stack;

import static org.junit.Assert.assertEquals;
import static saka1029.stack.Stack.context;
import static saka1029.stack.Stack.eval;

import org.junit.Test;

import saka1029.stack.Context;
import saka1029.stack.Generator;
import saka1029.stack.Symbol;
import saka1029.stack.Terminal;

public class TestGenerator {

    /**
     * generator:
     * ARGUMENT LIST generator -> LIST
     */
    @Test
    public void test() {
        Context c = context();
        c.variable(Symbol.of("generator"), x -> x.push(Generator.of(x, c.pop())));
        c.variable(Symbol.of("generator1"), x -> x.push(Generator.of(x, c.pop(), c.pop())));
        c.variable(Symbol.of("generator2"), x -> x.push(Generator.of(x, c.pop(), c.pop(), c.pop())));
        c.variable(Symbol.of("yield"), Terminal.YIELD);
        assertEquals(eval(c, "'(1 2 3)"),
            eval(c, "'() '(1 2 + yield 2 yield 1 yield) generator 'rcons for"));
        assertEquals(eval(c, "'(4 5 6)"),
            eval(c, "'() 3 '(dup 1 2 + + yield dup 2 + yield dup 1 + yield) generator1 'rcons for"));
        assertEquals(eval(c, "'(8 9 10)"),
            eval(c, "'() 3 4 '(+ dup 2 1 + + yield dup 2 + yield dup 1 + yield) generator2 'rcons for"));
    }
}
