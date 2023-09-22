package test.saka1029.stack;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.junit.Test;

import saka1029.Common;
import saka1029.stack.Bool;
import saka1029.stack.Context;
import saka1029.stack.Element;
import saka1029.stack.Int;
import saka1029.stack.List;
import saka1029.stack.List.ListInstruction;
import saka1029.stack.Word;

public class TestStack {
    
    static final Logger logger = Common.logger(TestStack.class);

    @Test
    public void testFact() {
        Context c = Context.of();
        c.run("/fact (dup 0 <= (drop 1) (dup 1 - fact *) if) define");
        assertEquals(c.eval("1"), c.eval("0 fact"));
        assertEquals(c.eval("1"), c.eval("1 fact"));
        assertEquals(c.eval("6"), c.eval("3 fact"));
        assertEquals(c.eval("120"), c.eval("5 fact"));
    }

    /**
     * 3 fact
     * <pre>
     * 3 : 1
     * 3 1 : swap
     * 1 3 : 1
     * 1 3 1 : swap
     * 1 1 3 : 1
     * 1 1 3 1 : (*) for
     * 6
     * </pre>
     */
    @Test
    public void testFactByFor() {
        Context c = Context.of();
        c.run("/fact (1 swap 1 swap 1 (*) for) define");
        assertEquals(c.eval("1"), c.eval("0 fact"));
        assertEquals(c.eval("1"), c.eval("1 fact"));
        assertEquals(c.eval("6"), c.eval("3 fact"));
        assertEquals(c.eval("120"), c.eval("5 fact"));
    }
    
    @Test
    public void testReverseByForEach() {
        Context c = Context.of();
        c.run("/reverse (() swap (swap pair) foreach) define");
        assertEquals(c.eval("()"), c.eval("() reverse"));
        assertEquals(c.eval("(1)"), c.eval("(1) reverse"));
        assertEquals(c.eval("(3 2 1)"), c.eval("(1 2 3) reverse"));
    }
    
    /**
     * <pre>
     * (1 2) (3 4) append
     * -> (1 2) (3 4) : swap
     * -> (3 4) (1 2) : dup () ==
     * -> (3 4) (1 2) false (drop) (unpair rot append pair) : if
     * -> (3 4) (1 2) unpair
     * -> (3 4) 1 (2) rot
     * -> 1 (2) (3 4) append
     * -> 1 (2 3 4) pair
     * -> (1 2 3 4)
     * </pre>
     */
    @Test
    public void testAppend() {
        Context c = Context.of();
        c.run("/append (swap dup () == (drop) (unpair rot append pair) if) define");
        assertEquals(c.eval("(1 2 3 4)"), c.eval("() (1 2 3 4) append"));
        assertEquals(c.eval("(1 2 3 4)"), c.eval("(1) (2 3 4) append"));
        assertEquals(c.eval("(1 2 3 4)"), c.eval("(1 2) (3 4) append"));
        assertEquals(c.eval("(1 2 3 4)"), c.eval("(1 2 3) (4) append"));
        assertEquals(c.eval("(1 2 3 4)"), c.eval("(1 2 3 4) () append"));
    }

    /**
     * <pre>
     * (1 2 3) reverse
     * -> (1 2 3) : dup () ==
     * -> (1 2 3) false : () (unpair reverse swap () pair append) if
     * -> (1 2 3) : unpair
     * -> 1 (2 3) : reverse
     * -> 1 (3 2) : swap
     * -> (3 2) 1 : () pair
     * -> (3 2) (1) : append
     * -> (3 2 1)
     * </pre>
     */
    @Test
    public void testReverseRecursion() {
        Context c = Context.of();
        c.run("/append (swap dup () == (drop) (unpair rot append pair) if) define");
        c.run("/reverse (dup () == () (unpair reverse swap () pair append) if) define");
        assertEquals(c.eval("()"), c.eval("() reverse"));
        assertEquals(c.eval("(1)"), c.eval("(1) reverse"));
        assertEquals(c.eval("(3 2 1)"), c.eval("(1 2 3) reverse"));
    }
    
    /**
     * <pre>
     * (2 3 4) (dup *) : map
     * 
     * (2 3 4) (dup *) : swap
     * (dup *) (2 3 4) : unpair
     * (dup *) 2 (3 4) : swap
     * (dup *) (3 4) 2 : @2
     * (dup *) (3 4) 2 (dup *) : execute
     * (dup *) (3 4) 4 : swap
     * (dup *) 4 (3 4) : @2
     * (dup *) 4 (3 4) (dup *) : map
     * (dup *) 4 (9 16) : pair
     * (dup *) (4 9 16) : swap
     * (4 9 16) (dup *) : drop
     * (4 9 16)
     * </pre>
     */
    @Test
    public void testMap() {
        Context c = Context.of();
        c.run("/map (swap dup () == () (unpair swap @2 execute swap @2 map pair) if swap drop) define");
        assertEquals(c.eval("()"), c.eval("() (dup *) map"));
        assertEquals(c.eval("(4)"), c.eval("(2) (dup *) map"));
        assertEquals(c.eval("(4 9 16)"), c.eval("(2 3 4) (dup *) map"));
        assertEquals(c.eval("(4 9 16)"), c.eval("(1 2 3) (1 +) map (dup *) map"));
    }
    
    /**
     * <pre>
     * (0 1 2 3) (odd) : filter
     * (0 1 2 3) (odd) : swap
     * (odd) (0 1 2 3) : unpair
     * (odd) 0 (1 2 3) : @2
     * (odd) 0 (1 2 3) (odd) : filter
     * (odd) 0 (1 3) : swap
     * (odd) (1 3) 0 : dup
     * (odd) (1 3) 0 0 : @3
     * (odd) (1 3) 0 0 (odd) : execute
     * (odd) (1 3) 0 false : (swap pair) (drop) if
     * (odd) (1 3) : swap
     * (1 3) (odd) : drop
     * (1 3)
     * </pre>
     */
    @Test
    public void testFilter() {
        Context c = Context.of();
        c.run("/even (2 % 0 ==) define");
        c.run("/odd (even not) define");
        c.run("/filter (swap dup () == () (unpair @2 filter swap dup @3 execute (swap pair) (drop) if) if swap drop) define");
        assertEquals(c.eval("()"), c.eval("() (odd) filter"));
        assertEquals(c.eval("()"), c.eval("(0) (odd) filter"));
        assertEquals(c.eval("(3)"), c.eval("(3) (odd) filter"));
        assertEquals(c.eval("(3)"), c.eval("(0 3) (odd) filter"));
        assertEquals(c.eval("(3)"), c.eval("(3 0) (odd) filter"));
        assertEquals(c.eval("(1)"), c.eval("(0 1 2) (odd) filter"));
        assertEquals(c.eval("(0 2)"), c.eval("(0 1 2 3) (even) filter"));
        assertEquals(c.eval("(1 3)"), c.eval("(0 1 2 3) (odd) filter"));
    }

    static Element expand(Element element, Context context) {
        List list;
        if (element instanceof List.ListInstruction x)
            list = x.list;
        else
            return element;
        java.util.List<Element> result = new ArrayList<>();
        for (Element e : list) {
            if (e instanceof Word w) {
                Element inst = context.instruction(w.name);
                if (inst != null)
                    e = inst;
            }
            result.add(expand(e, context));
        }
        return List.of(result, List.NIL).instruction();
    }

    /**
     * TODO: リスト内のブロックが展開されない。
     */
    @Test
    public void testExpand() {
        Context c = Context.of();
        c.run("/even (2 % 0 ==) define");
        assertEquals("(2 % 0 ==)", c.instruction("even").toString());
        Element even = expand(c.instruction("even"), c);
        logger.info(even.toString());
        assertEquals(List.of(Int.TWO, c.instruction("%"), Int.ZERO, c.instruction("==")), ((ListInstruction)even).list);
        Int.ZERO.execute(c);
        even.execute(c);
        assertEquals(1, c.size());
        assertEquals(Bool.TRUE, c.pop());
        Int.ONE.execute(c);
        even.execute(c);
        assertEquals(1, c.size());
        assertEquals(Bool.FALSE, c.pop());
        c.run("/fact (dup 0 <= (drop 1) (dup 1 - fact *) if) define");
        Element fact = expand(c.instruction("fact"), c);
        logger.info(fact.toString());
    }
    
    @Test
    public void testFor() {
       Context c = Context.of();
       assertEquals(Int.of(6), c.eval("0 1 3 1 (+) for"));
       assertEquals(c.eval("(1 2 3)"), c.eval("() 3 1 -1 (swap pair) for"));
       assertEquals(c.eval("()"), c.eval("() 1 3 -1 (swap pair) for"));
       c.run("/! (1 swap 1 swap 1 (*) for) define");
       assertEquals(c.eval("1"), c.eval("0 !")); 
       assertEquals(c.eval("1"), c.eval("1 !")); 
       assertEquals(c.eval("6"), c.eval("3 !")); 
       assertEquals(c.eval("120"), c.eval("5 !")); 
    }
    
    /**
     * <pre>
     * 3 iota
     * 
     * 3 : ()
     * 3 () : swap
     * () 3 : 1 -1 (swap pair)
     * () 3 1 -1 (swap pair) : for
     * 
     * () 3 : (swap pair)
     * (3) 2 : (swap pair)
     * (2 3) 1 : (swap pair)
     * (1 2 3)
     * </pre>
     */
    @Test
    public void testIota() {
       Context c = Context.of();
       c.run("/iota (() swap 1 -1 (swap pair) for) define");
       assertEquals(c.eval("()"), c.eval("0 iota")); 
       assertEquals(c.eval("(1)"), c.eval("1 iota")); 
       assertEquals(c.eval("(1 2 3)"), c.eval("3 iota")); 
       c.run("/! (iota 1 swap (*) foreach) define");
       assertEquals(c.eval("1"), c.eval("0 !")); 
       assertEquals(c.eval("1"), c.eval("1 !")); 
       assertEquals(c.eval("6"), c.eval("3 !")); 
       assertEquals(c.eval("120"), c.eval("5 !")); 
    }
    
    /**
     * n fib
     * -> 0 1 1 n 1 for drop
     */
    @Test
    public void testFibonacciByFor() {
       Context c = Context.of();
       c.run("/fib (0 swap 1 swap 1 swap 1 (drop @1 @1 + rot drop) for drop) define");
       assertEquals(c.eval("0"), c.eval("0 fib")); 
       assertEquals(c.eval("1"), c.eval("1 fib")); 
       assertEquals(c.eval("1"), c.eval("2 fib")); 
       assertEquals(c.eval("2"), c.eval("3 fib")); 
       assertEquals(c.eval("3"), c.eval("4 fib")); 
       assertEquals(c.eval("5"), c.eval("5 fib")); 
       assertEquals(c.eval("8"), c.eval("6 fib")); 
       assertEquals(c.eval("13"), c.eval("7 fib")); 
    }
}
