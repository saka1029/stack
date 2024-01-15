package test.saka1029.stack;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import saka1029.stack.Context;
import saka1029.stack.Generator;
import saka1029.stack.Int;
import saka1029.stack.List;
import saka1029.stack.Stack;
import saka1029.stack.Symbol;
import saka1029.stack.Terminal;

public class TestGenerator {

    @Test
    public void test() {
        Context c = Stack.context();
        c.variable(Symbol.of("generator"), x -> x.push(Generator.of(x, c.pop())));
//        c.variable(Symbol.of("yield"), x -> x.push(Terminal.YIELD));
        c.variable(Symbol.of("yield"), Terminal.YIELD);
        assertEquals(List.of(Int.of(1), Int.of(2), Int.of(3)),
            Stack.eval(c, "'() '(1 2 + yield 2 yield 1 yield) generator 'rcons for"));
    }

}
