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
        assertEquals(eval(c, "'(3 2 1)"), eval(c, "'(1 2 + yield 2 yield 1 yield) generator"));
        assertEquals(eval(c, "'(6 5 4)"), eval(c, "3 '(dup 1 2 + + yield dup 2 + yield dup 1 + yield) generator1"));
        assertEquals(eval(c, "'(10 9 8)"), eval(c, "'3 4 '(+ dup 2 1 + + yield dup 2 + yield dup 1 + yield) generator2"));
        assertEquals(eval(c, "'(1 2)"), eval(c, "'(1 3 range 'yield for) generator"));
        assertEquals(eval(c, "'(1 2 6 24)"), eval(c, "'(1 1 (4 1 +) range '(* dup yield) for) generator"));
    }

    @Test
    public void testConcurrent() {
        Context c = context();
        assertEquals(eval(c, "'(1 2)"),
            eval(c, """
                '()
                    '('(gen 2) print 2 yield '(gen 1) print 1 yield) generator
                '(dup '() cons 'accept rcons print rcons) for
                """));
    }

    @Test
    public void testFactorials() {
        Context c = context();
        run(c, "'('(1 + 1 1 rot range '(* dup yield) for) generator1) @ factorials");
        assertEquals(eval(c, "'(1 2 6 24 120)"), eval(c, "5 factorials"));
    }

    @Test
    public void testFibonaccis() {
        Context c = context();
        run(c, """
            '(                      {proc fibonaccis(n -> )}
                '( : n -> ,             {generator1 lambda(n -> )}
                    a 0,                    {local a = 0}
                    b 1 :                   {local b = 0}
                    0 n range               {for % in range(0, n) do}
                    '( drop a yield             {yield a}
                        b a b + @b @a           {a, b = b, a + b}
                    ) for                   {end for}
                ) generator1            {end generator1}    
            ) @ fibonaccis          {end proc}
            """);
        assertEquals(eval(c, "'(0 1 1 2 3 5 8 13 21 34)"), eval(c, "10 fibonaccis"));
    }
}
