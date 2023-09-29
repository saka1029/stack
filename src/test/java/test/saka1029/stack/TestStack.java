package test.saka1029.stack;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.logging.Logger;

import org.junit.Test;

import saka1029.Common;
import saka1029.stack.Bool;
import saka1029.stack.Context;
import saka1029.stack.Element;
import saka1029.stack.Int;
import saka1029.stack.List;
import saka1029.stack.List.ListInstruction;
import saka1029.stack.Ordered;
import saka1029.stack.Pair;
import saka1029.stack.Word;

public class TestStack {
    
    static final Logger logger = Common.logger(TestStack.class);

    @Test
    public void testFactByRecursion() {
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
    public void testAppendByRecursion() {
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
    public void testReverseByRecursion() {
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
    public void testMapByRecursion() {
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
    public void testFilterByRecursion() {
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
       assertEquals(c.eval("21"), c.eval("8 fib")); 
       assertEquals(6, c.waterMark);
    }
    
    @Test
    public void testFibonacciByRecursion() {
       Context c = Context.of();
       c.run("/fib (dup 0 == () (dup 1 == () (dup 2 - fib swap 1 - fib +) if) if) define");
       assertEquals(c.eval("0"), c.eval("0 fib")); 
       assertEquals(c.eval("1"), c.eval("1 fib")); 
       assertEquals(c.eval("1"), c.eval("2 fib")); 
       assertEquals(c.eval("2"), c.eval("3 fib")); 
       assertEquals(c.eval("3"), c.eval("4 fib")); 
       assertEquals(c.eval("5"), c.eval("5 fib")); 
       assertEquals(c.eval("8"), c.eval("6 fib")); 
       assertEquals(c.eval("13"), c.eval("7 fib")); 
       assertEquals(c.eval("21"), c.eval("8 fib")); 
       assertEquals(11, c.waterMark);
    }
    
    static int tarai(int x, int y, int z) {
        return x <= y ? y : tarai(tarai(x - 1, y, z), tarai(y - 1, z, x), tarai(z - 1, x, y));
    }
    
    @Test
    public void testTaraiJava() {
        assertEquals(10, tarai(10, 4, 0));
    }
    
    /**
     * x y z tarai
     * x y z : @2
     * x y z x : 1
     * x y z x 1 : -
     * x y z x-1 : @2
     * x y z x-1 y : @2
     * x y z x-1 y z : tarai
     * x y z tara(x-1,y,z) : @2
     * x y z tara(x-1,y,z) y : 1
     * x y z tara(x-1,y,z) y 1 : -
     * x y z tara(x-1,y,z) y-1 : @2
     * x y z tara(x-1,y,z) y-1 z : @5
     * x y z tara(x-1,y,z) y-1 z x : tarai
     * x y z tara(x-1,y,z) tarai(y-1,z,x) : @2
     * x y z tara(x-1,y,z) tarai(y-1,z,x) z : 1
     * x y z tara(x-1,y,z) tarai(y-1,z,x) z 1 : -
     * x y z tara(x-1,y,z) tarai(y-1,z,x) z-1 : @5
     * x y z tara(x-1,y,z) tarai(y-1,z,x) z-1 x : @5
     * x y z tara(x-1,y,z) tarai(y-1,z,x) z-1 x y : tarai
     * x y z tara(x-1,y,z) tarai(y-1,z,x) tarai(z-1,x,y) : tarai
     * x y z tarai(tara(x-1,y,z) tarai(y-1,z,x) tarai(z-1,x,y)) : swap
     * x y tarai(tara(x-1,y,z) tarai(y-1,z,x) tarai(z-1,x,y)) z : drop
     * x y tarai(tara(x-1,y,z) tarai(y-1,z,x) tarai(z-1,x,y)) : swap
     * x tarai(tara(x-1,y,z) tarai(y-1,z,x) tarai(z-1,x,y)) y : drop
     * x tarai(tara(x-1,y,z) tarai(y-1,z,x) tarai(z-1,x,y)) : swap
     * tarai(tara(x-1,y,z) tarai(y-1,z,x) tarai(z-1,x,y)) x : drop
     * tarai(tara(x-1,y,z) tarai(y-1,z,x) tarai(z-1,x,y))
     */
    @Test
    public void testTarai() {
        Context c = Context.of();
        c.run("/tarai (@2 @2 <="
            + " ()"
            + " (@2 1 - @2 @2 tarai @2 1 - @2 @5 tarai @2 1 - @5 @5 tarai tarai swap drop swap)"
            + " if drop swap drop)"
            + " define");
        assertEquals(Int.of(6), c.eval("6 4 0 tarai"));
    }
    
    static List filter(List list, Predicate<Ordered> comparator) {
        if (!(list instanceof Pair p))
            return List.NIL;
        List rest = filter((List)p.tail, comparator);
        return comparator.test((Ordered)p.head) ? Pair.of(p.head, rest) : rest;
    }
    
    static List append(List a, List b) {
        return a instanceof Pair p ? Pair.of(p.head, append((List)p.tail, b)) : b;
    }

    static List qsort(List list) {
        if (!(list instanceof Pair p))
            return List.NIL;
        Ordered pivot = (Ordered)p.head;
        Predicate<Ordered> le = e -> e.compareTo(pivot) <= 0;
        Predicate<Ordered> gt = le.negate();
        List rest = (List)p.tail;
        return append(qsort(filter(rest, le)), Pair.of(pivot, qsort(filter(rest, gt))));
    }
    
    @Test
    public void testQSortJava() {
        Context c = Context.of();
        assertEquals(c.eval("(0 1 2 3)"), append((List)c.eval("(0 1)"), (List)c.eval("(2 3)")));
        assertEquals(c.eval("(0 1 2 3)"), append((List)c.eval("(0 1 2)"), (List)c.eval("(3)")));
        assertEquals(c.eval("(0 1 2 3)"), qsort((List)c.eval("(2 1 3 0)")));
        assertEquals(c.eval("(0 0 0 1 1 1 2 2 2 3 3 3)"), qsort((List)c.eval("(2 2 0 1 0 1 2 3 3 1 3 0)")));
    }
    
    /**
     * (2 4 1) 3 smaller
     * (2 1)
     */
    @Test
    public void testSmaller() {
        Context c = Context.of();
        c.run("/filter (swap dup () == () (unpair @2 filter swap dup @3 execute (swap pair) (drop) if) if swap drop) define");
        c.run("/smaller ((<=) pair filter) define");
        assertEquals(c.eval("(2 1)"), c.eval("(2 4 1) 3 smaller"));
        assertEquals(c.eval("(3 2 1)"), c.eval("(3 2 4 1) 3 smaller"));
    }
    
    /**
     * (2 4 1) 3 larger
     * (4)
     */
    @Test
    public void testLarger() {
        Context c = Context.of();
        c.run("/filter (swap dup () == () (unpair @2 filter swap dup @3 execute (swap pair) (drop) if) if swap drop) define");
        c.run("/larger ((>) pair filter) define");
        assertEquals(c.eval("(4)"), c.eval("(2 4 1) 3 larger"));
        assertEquals(c.eval("(4)"), c.eval("(3 2 4 1) 3 larger"));
    }
    
    /**
     * (3 2 4 1) qsort
     * (3 2 4 1) : unpair
     * 3 (2 4 1) : dup
     * 3 (2 4 1) (2 4 1) : @2
     * 3 (2 4 1) (2 4 1) 3 : (<=)
     * 3 (2 4 1) (2 4 1) 3 (<=) : pair
     * 3 (2 4 1) (2 4 1) (3 <=) : filter
     * 3 (2 4 1) (2 1) : qsort
     * 3 (2 4 1) (1 2) : @1
     * 3 (2 4 1) (1 2) (2 4 1) : @3
     * 3 (2 4 1) (1 2) (2 4 1) 3 : (>)
     * 3 (2 4 1) (1 2) (2 4 1) 3 : pair
     * 3 (2 4 1) (1 2) (2 4 1) (3 >) : filter
     * 3 (2 4 1) (1 2) (4) : qsort
     * 3 (2 4 1) (1 2) (4) : @3
     * 3 (2 4 1) (1 2) (4) 3 : swap
     * 3 (2 4 1) (1 2) 3 (4) : pair
     * 3 (2 4 1) (1 2) (3 4) : append
     * 3 (2 4 1) (1 2 3 4) : swap drop swap drop
     * (1 2 3 4)
     * 
     * ２回目のfilterを最適化
     * (3 2 4 1) qsort
     * (3 2 4 1) : unpair
     * 3 (2 4 1) : dup
     * 3 (2 4 1) (2 4 1) : @2
     * 3 (2 4 1) (2 4 1) 3 : (<=)
     * 3 (2 4 1) (2 4 1) 3 (<=) : pair
     * 3 (2 4 1) (2 4 1) (3 <=) : filter
     * 3 (2 4 1) (2 1) : qsort
     * 3 (2 4 1) (1 2) : swap
     * 3 (1 2) (2 4 1) : @2
     * 3 (1 2) (2 4 1) 3 : (>)
     * 3 (1 2) (2 4 1) 3 (>) : pair
     * 3 (1 2) (2 4 1) (3 >) : filter
     * 3 (1 2) (4) : qsort
     * 3 (1 2) (4) : @2
     * 3 (1 2) (4) 3 : swap
     * 3 (1 2) 3 (4) : pair
     * 3 (1 2) (3 4) : append
     * 3 (1 2 3 4) : swap drop
     * (1 2 3 4)
     * 
     * filterに渡すクロージャーはマクロ的に作成している。
     */
    @Test
    public void testQSort() {
        Context c = Context.of(); //.trace(logger::info);
        c.run("/filter (swap dup () == () (unpair @2 filter swap dup @3 execute (swap pair) (drop) if) if swap drop) define");
//        c.run("/smaller ((<=) pair filter) define");
//        c.run("/larger ((>) pair filter) define");
        c.run("/append (swap dup () == (drop) (unpair rot append pair) if) define");
//        c.run("/qsort (dup () == () (unpair dup @2 smaller qsort @1 @3 larger qsort @3 swap pair append swap drop swap drop) if) define");
//        c.run("/qsort (dup () == () (unpair dup @2 (<=) pair filter qsort @1 @3 (>) pair filter qsort @3 swap pair append swap drop swap drop) if) define");
        c.run("/qsort (dup () == () (unpair dup @2 (<=) pair filter qsort swap @2 (>) pair filter qsort @2 swap pair append swap drop) if) define");
        assertEquals(c.eval("()"), c.eval("() qsort"));
        assertEquals(c.eval("(1 2 3 4)"), c.eval("(3 2 4 1) qsort"));
        assertEquals(c.eval("(1 2 3 4 5 6 7 8 9)"), c.eval("(6 3 9 5 2 4 7 8 1) qsort"));
    }
    
    @Test
    public void testSet() {
        Context c = Context.of().trace(logger::info);
        c.run("/one 1 set");
        assertEquals(c.eval("1"), c.eval("one"));
        c.run("/inc (1 +) set");
        assertEquals(c.eval("(1 +)"), c.eval("inc"));
        assertEquals(c.eval("4"), c.eval("3 inc execute"));
    }
}
