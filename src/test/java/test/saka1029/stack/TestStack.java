package test.saka1029.stack;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static saka1029.stack.Stack.*;

import java.util.Arrays;
import java.util.logging.Logger;

import org.junit.Test;

import saka1029.Common;
import saka1029.stack.Bool;
import saka1029.stack.Cons;
import saka1029.stack.Context;
import saka1029.stack.Int;
import saka1029.stack.Range;
import saka1029.stack.Symbol;
import saka1029.stack.Terminal;

public class TestStack {

	static final Logger logger = Common.logger(TestStack.class);

	// @Test
	// public void testNil() {
	// 	Context c = context();
	// 	run(c, "()");
	// 	assertEquals(0, c.sp);
	// 	assertEquals(eval(c, "nil"), eval(c, "nil"));
	// 	assertEquals(eval(c, "nil"), eval(c, "'()"));
	// }

	@Test
	public void testPlus() {
		Context c = context();
		assertEquals(Int.of(3), eval(c, "1 2 + "));
		assertEquals(Int.of(3), eval(c, "(1 2) + "));
		assertEquals(Int.of(3), eval(c, "1 2 (+) "));
	}

	@Test
	public void testDivide() {
		Context c = context();
		assertEquals(Int.of(2), eval(c, "4 2 / "));
		assertEquals(Int.of(4), eval(c, "(8 2) / "));
		assertEquals(Int.of(3), eval(c, "7 2 (/) "));
	}

	@Test
	public void testModulo() {
		Context c = context();
		assertEquals(Int.of(0), eval(c, "4 2 % "));
		assertEquals(Int.of(3), eval(c, "(8 5) % "));
		assertEquals(Int.of(1), eval(c, "7 2 (%) "));
	}

	@Test
	public void testEQ() {
		Context c = context();
		assertEquals(Bool.FALSE, eval(c, "4 2 == "));
		assertEquals(Bool.TRUE, eval(c, "2 2 == "));
		assertEquals(Bool.FALSE, eval(c, "2 4 == "));
	}

	@Test
	public void testNE() {
		Context c = context();
		assertEquals(Bool.TRUE, eval(c, "4 2 != "));
		assertEquals(Bool.FALSE, eval(c, "2 2 != "));
		assertEquals(Bool.TRUE, eval(c, "2 4 != "));
	}

	@Test
	public void testLT() {
		Context c = context();
		assertEquals(Bool.FALSE, eval(c, "4 2 < "));
		assertEquals(Bool.FALSE, eval(c, "2 2 < "));
		assertEquals(Bool.TRUE, eval(c, "2 4 < "));
	}

	@Test
	public void testLE() {
		Context c = context();
		assertEquals(Bool.FALSE, eval(c, "4 2 <= "));
		assertEquals(Bool.TRUE, eval(c, "2 2 <= "));
		assertEquals(Bool.TRUE, eval(c, "2 4 <= "));
	}

	@Test
	public void testGT() {
		Context c = context();
		assertEquals(Bool.TRUE, eval(c, "4 2 > "));
		assertEquals(Bool.FALSE, eval(c, "2 2 > "));
		assertEquals(Bool.FALSE, eval(c, "2 4 > "));
	}

	@Test
	public void testGE() {
		Context c = context();
		assertEquals(Bool.TRUE, eval(c, "4 2 >= "));
		assertEquals(Bool.TRUE, eval(c, "2 2 >= "));
		assertEquals(Bool.FALSE, eval(c, "2 4 >= "));
	}

	@Test
	public void testCar() {
		Context c = context();
		assertEquals(Int.of(1), eval(c, "'(1 2) car"));
	}

	@Test
	public void testCdr() {
		Context c = context();
		assertEquals(Cons.list(Int.of(2)), eval(c, "'(1 2) cdr"));
	}

	@Test
	public void testSize() {
		Context c = context();
		assertEquals(Int.of(2), eval(c, "'(1 2) size"));
		assertEquals(Int.of(2), eval(c, "0 2 array size"));
	}

	@Test
	public void testUncons() {
		Context c = context();
		run(c, "'(1 2) uncons");
		assertEquals(Cons.list(Int.of(2)), c.pop());
		assertEquals(Int.of(1), c.pop());
	}

	@Test
	public void testCons() {
		Context c = context();
		assertEquals(Cons.list(Int.of(1)), eval(c, "1 '() cons"));
	}

	@Test
	public void testRcons() {
		Context c = context();
		assertEquals(Cons.list(Int.of(1)), eval(c, "'() 1 rcons"));
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
	public void testWhen() {
		Context c = context();
		assertEquals(Int.of(2), eval(c, "1 true '(1 +) when"));
		assertEquals(Int.of(1), eval(c, "1 false '(1 +) when"));
	}

	@Test
	public void testUnless() {
		Context c = context();
		assertEquals(Int.of(1), eval(c, "1 true '(1 +) unless"));
		assertEquals(Int.of(2), eval(c, "1 false '(1 +) unless"));
	}

	@Test
	public void testFor() {
		Context c = context();
		assertEquals(Int.of(6), eval(c, "0 '(1 2 3) '+ for"));
	}

	@Test
	public void testWhile() {
		Context c = context();
		assertEquals(eval(c, "6"), eval(c, "3 0 '(dup1 0 >) '(dup1 1 - rrot +) while ret1"));
		run(c,"'(1 '(dup1 0 >) '(dup1 1 - rrot *) while ret1) @factorial");
		assertEquals(eval(c, "1"), eval(c, "0 factorial"));
		assertEquals(eval(c, "1"), eval(c, "1 factorial"));
		assertEquals(eval(c, "2"), eval(c, "2 factorial"));
		assertEquals(eval(c, "6"), eval(c, "3 factorial"));
		assertEquals(eval(c, "24"), eval(c, "4 factorial"));
		assertEquals(eval(c, "120"), eval(c, "5 factorial"));
		assertEquals(eval(c, "720"), eval(c, "6 factorial"));
	}

	/**
	 * breakをワードとして追加してみる。
	 */
	@Test
	public void testBreak() {
		Context c = context();
		c.variable("break", x -> x.push(Terminal.BREAK)); 
		// forで要素が3を超えたらbreakする。
		// ただしブロック'(drop break)がbreakするだけなので'(drop)と変わらない。
		// 要素4,5に対してもforの処理は続行する。
		// 以下はstackトレースの結果。
		// Context(sp=2, bp=0 stack=[(), 1])
		// Context(sp=2, bp=0 stack=[(1), 2])
		// Context(sp=2, bp=0 stack=[(2 1), 3])
		// Context(sp=2, bp=0 stack=[(3 2 1), 4])		// 4, 5についてはrconsはしないが、forの処理は継続する。
		// Context(sp=2, bp=0 stack=[(3 2 1), 5])
		// 結果としてどのブロックからbreakするのかの指定がなければ使えない、ということになる。
		assertEquals(eval(c, "'(3 2 1)"), eval(c, "'() 1 5 1 range '(dup 3 > '(drop break) 'rcons if) for"));
		// 以下はbreakせずに最後まで処理した結果、例外を投げる。
		// assertEquals(eval(c, "'(3 2 1)"), eval(c, "'() 1 5 1 range '(stack dup 3 > 'break 'rcons if) for"));
		// java.lang.RuntimeException: Illegal stack size Context(sp=3, bp=0 stack=[(3 2 1), 4, 5])
	}

	@Test
	public void testRange() {
		Context c = context();
		assertEquals(Range.of(1, 3, 1), eval(c, "1 3 1 range"));
		assertEquals(Int.of(6), eval(c, "0 1 3 1 range '+ for"));
	}

	@Test
	public void testDefine() {
		Context c = context();
		run(c, "'+ @plus");
		assertEquals(Int.of(3), eval(c, "1 2 plus"));
		run(c, "3 @three");
		assertEquals(Int.of(3), eval(c, "three"));
	}

	@Test
	public void testFactorialRecuesive() {
		Context c = context();
		run(c, "'(dup 0 <= '(drop 1) '(dup 1 - factorial *) if) @factorial");
		assertEquals(Int.of(1), eval(c, "0 factorial"));
		assertEquals(Int.of(1), eval(c, "1 factorial"));
		assertEquals(Int.of(2), eval(c, "2 factorial"));
		assertEquals(Int.of(6), eval(c, "3 factorial"));
		assertEquals(Int.of(24), eval(c, "4 factorial"));
		assertEquals(Int.of(120), eval(c, "5 factorial"));
	}

	@Test
	public void testFactorialFor() {
		Context c = context();
		run(c, "'(1 swap 1 swap 1 range '* for) @factorial");
		assertEquals(Int.of(1), eval(c, "0 factorial"));
		assertEquals(Int.of(1), eval(c, "1 factorial"));
		assertEquals(Int.of(2), eval(c, "2 factorial"));
		assertEquals(Int.of(6), eval(c, "3 factorial"));
		assertEquals(Int.of(24), eval(c, "4 factorial"));
	}

	@Test
	public void testFibonacciRecuesive() {
		Context c = context();
		run(c, "'(dup 1 <= '() '(dup 1 - fibonacci swap 2 - fibonacci +) if) @fibonacci");
		assertEquals(Int.of(0), eval(c, "0 fibonacci"));
		assertEquals(Int.of(1), eval(c, "1 fibonacci"));
		assertEquals(Int.of(1), eval(c, "2 fibonacci"));
		assertEquals(Int.of(2), eval(c, "3 fibonacci"));
		assertEquals(Int.of(3), eval(c, "4 fibonacci"));
		assertEquals(Int.of(5), eval(c, "5 fibonacci"));
		assertEquals(Int.of(8), eval(c, "6 fibonacci"));
		assertEquals(Int.of(13), eval(c, "7 fibonacci"));
		assertEquals(Int.of(21), eval(c, "8 fibonacci"));
	}

	@Test
	public void testFibonacciFor() {
		Context c = context();
		run(c, "'(0 swap 1 swap 1 swap 1 range '(drop dup rrot +) for drop) @fibonacci");
		assertEquals(Int.of(0), eval(c, "0 fibonacci"));
		assertEquals(Int.of(1), eval(c, "1 fibonacci"));
		assertEquals(Int.of(1), eval(c, "2 fibonacci"));
		assertEquals(Int.of(2), eval(c, "3 fibonacci"));
		assertEquals(Int.of(3), eval(c, "4 fibonacci"));
		assertEquals(Int.of(5), eval(c, "5 fibonacci"));
		assertEquals(Int.of(8), eval(c, "6 fibonacci"));
		assertEquals(Int.of(13), eval(c, "7 fibonacci"));
		assertEquals(Int.of(21), eval(c, "8 fibonacci"));
	}

	@Test
	public void testAppendBuiltin() {
		Context c = context();
		assertEquals(eval(c, "'(3 4)"), eval(c, "'() '(3 4) append"));
		assertEquals(eval(c, "'(2 3 4)"), eval(c, "'(2) '(3 4) append"));
		assertEquals(eval(c, "'(1 2 3 4)"), eval(c, "'(1 2) '(3 4) append"));
		assertEquals(eval(c, "'(1 2 3 4)"), eval(c, "'(1 2) '(3 4) append"));
		assertEquals(eval(c, "'(1 2 (3) 4)"), eval(c, "'(1 2) '((3) 4) append"));
		assertEquals(eval(c, "'(1 2 3 4)"), eval(c, "'(1 2) to-array '(3 4) to-array append"));
		assertEquals(eval(c, "'(1 2 3 3 3)"), eval(c, "'(1 2) 3 3 array append"));
	}

	@Test
	public void testAppendRecursive() {
		Context c = context();
		run(c, "'(swap dup null 'drop '(uncons rot append cons) if) @append");
		assertEquals(eval(c, "'(3 4)"), eval(c, "'() '(3 4) append"));
		assertEquals(eval(c, "'(2 3 4)"), eval(c, "'(2) '(3 4) append"));
		assertEquals(eval(c, "'(1 2 3 4)"), eval(c, "'(1 2) '(3 4) append"));
	}

	// @Test
	// public void testAppendTailRecursive() {
	// 	Context c = context();
	// 	run(c, "'(swap dup null? 'drop '(uncons rot append cons) if) 'append define");
	// 	assertEquals(eval(c, "'(3 4)"), eval(c, "'() '(3 4) append"));
	// 	assertEquals(eval(c, "'(2 3 4)"), eval(c, "'(2) '(3 4) append"));
	// 	assertEquals(eval(c, "'(1 2 3 4)"), eval(c, "'(1 2) '(3 4) append"));
	// }

	@Test
	public void testReverseBuiltIn() {
		Context c = context();
		assertEquals(eval(c, "'()"), eval(c, "'() reverse"));
		assertEquals(eval(c, "'(1)"), eval(c, "'(1) reverse"));
		assertEquals(eval(c, "'(2 1)"), eval(c, "'(1 2) reverse"));
		assertEquals(eval(c, "'(3 2 1)"), eval(c, "'(1 2 3) reverse"));
		assertEquals(eval(c, "'(3 2 1)"), eval(c, "1 3 1 range reverse"));
	}

	@Test
	public void testReverseRecursive() {
		Context c = context();
		run(c, "'(swap dup null 'drop '(uncons rot append cons) if) @append");
		run(c, "'(dup null '() '(uncons reverse swap '() cons append) if) @reverse");
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
		run(c, "'('() swap '(swap cons) for) @reverse");
		assertEquals(eval(c, "'()"), eval(c, "'() reverse"));
		assertEquals(eval(c, "'(1)"), eval(c, "'(1) reverse"));
		assertEquals(eval(c, "'(2 1)"), eval(c, "'(1 2) reverse"));
		assertEquals(eval(c, "'(3 2 1)"), eval(c, "'(1 2 3) reverse"));
		// forはnext()だけを使うのでreverseできる。
		assertEquals(eval(c, "'(3 2 1)"), eval(c, "1 3 1 range reverse"));
	}

	@Test
	public void testReverseForRcons() {
		Context c = context();
		run(c, "'('() swap 'rcons for) @reverse");
		assertEquals(eval(c, "'()"), eval(c, "'() reverse"));
		assertEquals(eval(c, "'(1)"), eval(c, "'(1) reverse"));
		assertEquals(eval(c, "'(2 1)"), eval(c, "'(1 2) reverse"));
		assertEquals(eval(c, "'(3 2 1)"), eval(c, "'(1 2 3) reverse"));
		// forはnext()だけを使うのでreverseできる。
		assertEquals(eval(c, "'(3 2 1)"), eval(c, "1 3 1 range reverse"));
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
		run(c, "'(swap dup null '() '(uncons swap dup2 execute swap dup2 map cons) if ret1) @map");
		assertEquals(eval(c, "'()"), eval(c, "'() '(1 +) map"));
		assertEquals(eval(c, "'(1)"), eval(c, "'(0) '(1 +) map"));
		assertEquals(eval(c, "'(1 2 3)"), eval(c, "'(0 1 2) '(1 +) map"));
		assertEquals(eval(c, "'(1 2 3 4 5)"), eval(c, "'(0 1 2 3 4) '(1 +) map"));
	}

	/**
	 * ラムダ式をマクロ的に置換することでmapを実現している。 F -> (F rcons)
	 */
	@Test
	public void testMapByReverse() {
		Context c = context();
		run(c, "'('() rrot '(rcons) cons for reverse) @map");
		assertEquals(eval(c, "'()"), eval(c, "'() '(1 +) map"));
		assertEquals(eval(c, "'(1)"), eval(c, "'(0) '(1 +) map"));
		assertEquals(eval(c, "'(1 2 3)"), eval(c, "'(0 1 2) '(1 +) map"));
		assertEquals(eval(c, "'(1 2 3 4 5)"), eval(c, "'(0 1 2 3 4) '(1 +) map"));
	}

	@Test
	public void testMapBuiltin() {
		Context c = context();
		assertEquals(eval(c, "'()"), eval(c, "'() '(1 +) map"));
		assertEquals(eval(c, "'(1)"), eval(c, "'(0) '(1 +) map"));
		assertEquals(eval(c, "'(1 2 3)"), eval(c, "'(0 1 2) '(1 +) map"));
		assertEquals(eval(c, "'(1 2 3 4 5)"), eval(c, "'(0 1 2 3 4) '(1 +) map"));
	}

//	@Test
//	public void testMap2Builtin() {
//		Context c = context();
//		assertEquals(eval(c, "'()"), eval(c, "'() '(1 +) map2"));
//		assertEquals(eval(c, "'(1)"), eval(c, "'(0) '(1 +) map2"));
//		assertEquals(eval(c, "'(1 2 3)"), eval(c, "'(0 1 2) '(1 +) map2"));
//		assertEquals(eval(c, "'(1 2 3 4 5)"), eval(c, "'(0 1 2 3 4) '(1 +) map2"));
//	}

	@Test
	public void testFilterRecursiveCdrFirst() {
		Context c = context();
		run(c, "'(swap dup null '() '(uncons dup2 filter swap dup dup3 execute 'rcons 'drop if) if ret1) @filter");
		assertEquals(eval(c, "'(0 2)"), eval(c, "'(0 1 2 3) '(2 % 0 ==) filter"));
		assertEquals(eval(c, "'(1 3)"), eval(c, "'(0 1 2 3) '(2 % 0 !=) filter"));
	}

	@Test
	public void testFilterRecursiveCarFirst() {
		Context c = context();
		run(c, "'(swap dup null '() '(uncons swap dup dup3 execute rot dup3 filter swap 'cons 'ret1 if) if ret1) @filter");
		assertEquals(eval(c, "'(0 2)"), eval(c, "'(0 1 2 3) '(2 % 0 ==) filter"));
		assertEquals(eval(c, "'(1 3)"), eval(c, "'(0 1 2 3) '(2 % 0 !=) filter"));
	}

	@Test
	public void testFilterBuiltin() {
		Context c = context();
		assertEquals(eval(c, "'(0 2)"), eval(c, "'(0 1 2 3) '(2 % 0 ==) filter"));
		assertEquals(eval(c, "'(1 3)"), eval(c, "'(0 1 2 3) '(2 % 0 !=) filter"));
		run(c, "'('(!=) cons filter) @remove");
		assertEquals(eval(c, "'(0 1 3 1 0)"), eval(c, "'(0 1 2 3 2 1 0) 2 remove"));
	}

	/**
	 * 通常の関数呼び出し: 引数 関数
	 * 
	 * 無名再帰の関数呼び出し: 引数 '関数 rexecute
	 * 
	 * 関数は自分自身をdupしてからexecuteすることで、自分自身を引数として受け取ることができる。
	 * 引数 関数 : rexecute
	 * 引数 関数 :
	 * dup 引数 関数 関数 : execute (関数は第2引数として[引数 関数]を受け取る)
	 */
	@Test
	public void testAnonymousRecursion() {
		Context c = context();
		assertEquals(eval(c, "1"), eval(c, "0 '(swap dup 0 <= '(drop 1) '(dup 1 - dup2 dup execute *) if ret1) dup execute"));
		assertEquals(eval(c, "1"), eval(c, "1 '(swap dup 0 <= '(drop 1) '(dup 1 - dup2 dup execute *) if ret1) dup execute"));
		assertEquals(eval(c, "2"), eval(c, "2 '(swap dup 0 <= '(drop 1) '(dup 1 - dup2 dup execute *) if ret1) dup execute"));
		run(c, "'(dup execute) @rexecute"); 
		assertEquals(eval(c, "6"), eval(c, "3 '(swap dup 0 <= '(drop 1) '(dup 1 - dup2 rexecute *) if ret1) rexecute"));
		assertEquals(eval(c, "24"), eval(c, "4 '(swap dup 0 <= '(drop 1) '(dup 1 - dup2 rexecute *) if ret1) dup execute"));
		// 無名関数に名前を付けた場合(先頭がクォート2個である点に注意する)
		run(c, "''(swap dup 0 <= '(drop 1) '(dup 1 - dup2 rexecute *) if ret1) @fact");
		assertEquals(eval(c, "24"), eval(c, "4 fact rexecute"));
		assertEquals(eval(c, "120"), eval(c, "5 fact rexecute"));
	}

	/**
	 * 無名再帰の関数呼び出し: 関数の先頭でswapしない方式。 2個以上の引数がある場合を考えるとswapしない方式の方がよい。
	 * 無名再帰関数は以下の引数を受け取る。 [引数1 引数2 ... 引数n 関数(自分自身)]
	 * 
	 * ifのelse部の動作: 3 F : dup1 dup (引数を2個pushする) 3 F 3 3 : 1 - (引数から1を引く) 3 F 3 2 :
	 * dup2 dup (関数自身を2個pushする) 3 F 3 2 F F : execute (再帰呼び出しする) 3 F 3 2! : *
	 * (その結果と引数を乗算する) 3 F (3 * 2!)
	 * 
	 * 関数最後の動作: 3 F (3 * 2!) : ret2 (引数2個をdropする)
	 * 
	 */
	@Test
	public void testAnonymousRecursionArgsFunction() {
		Context c = context();
		assertEquals(eval(c, "1"), eval(c, "0 '(dup1 0 <= '1 '(dup1 dup 1 - dup2 dup execute *) if ret2) dup execute"));
		assertEquals(eval(c, "1"), eval(c, "1 '(dup1 0 <= '1 '(dup1 dup 1 - dup2 dup execute *) if ret2) dup execute"));
		assertEquals(eval(c, "2"), eval(c, "2 '(dup1 0 <= '1 '(dup1 dup 1 - dup2 dup execute *) if ret2) dup execute"));
		assertEquals(eval(c, "6"), eval(c, "3 '(dup1 0 <= '1 '(dup1 dup 1 - dup2 dup execute *) if ret2) dup execute"));
		assertEquals(eval(c, "24"),
				eval(c, "4 '(dup1 0 <= '1 '(dup1 dup 1 - dup2 dup execute *) if ret2) dup execute"));
		run(c, "'('() rrot '(rcons) cons for reverse) @map");
		assertEquals(eval(c, "'(1 1 2 6 24)"), eval(c,
				"0 4 1 range" + " '('(dup1 0 <= '1 '(dup1 dup 1 - dup2 dup execute *) if ret2) dup execute) map"));
	}

	/**
	 * <pre>
	 * scheme:
	 * (define (permutations func ls)
	 *   (define (perm ls a)
	 *     (if (null? ls)
	 *         (func (reverse a))
	 *         (for-each
	 *           (lambda (n)
	 *             (perm (remove n ls) (cons n a)))
	 *           ls)))
	 *   (perm ls '()))
	 * </pre>
	 * 
	 * 結果をprintで収集する。
	 */
	@Test
	public void testPermutations() {
//        logger.info(Common.methodName());
		Context c = context();
		StringBuilder sb = new StringBuilder();
		c.output(sb::append);
		run(c, "'('(!=) cons filter) @remove");
		assertEquals(eval(c, "'(0 1 3)"), eval(c, "'(0 1 2 3) 2 remove"));
		run(c, "'(dup1 null" + " '(dup reverse print)"
				+ " '(dup1 '(dup2 dup1 remove swap dup2 cons perm) for) if drop2) @perm");
		run(c, "'('() perm) @permutations");
		sb.setLength(0);
		run(c, "'() permutations");
		assertEquals("()", sb.toString());
		sb.setLength(0);
		run(c, "'(1) permutations");
		assertEquals("(1)", sb.toString());
		sb.setLength(0);
		run(c, "'(1 2) permutations");
		assertEquals("(1 2)(2 1)", sb.toString());
		sb.setLength(0);
		run(c, "'(1 2 3) permutations");
		assertEquals("(1 2 3)(1 3 2)(2 1 3)(2 3 1)(3 1 2)(3 2 1)", sb.toString());
	}

	/**
	 * Generator(コルーチン)を使った再帰呼び出し permの結果を順次printする代わりに、すべての結果をリストにして返す。
	 * testPermutations()からの変更点：
	 * 
	 * <pre>
	 * (1) permの定義中のprintをyieldに変更する。
	 * (2) permutationsで「'() perm」を「'() 'perm generator2」に変更する。
	 *     (permは2引数なのでgenerator2を使う)
	 * </pre>
	 */
	@Test

	public void testPermutationsByGenerator() {
//        logger.info(Common.methodName());
		Context c = context();
		run(c, "'('(!=) cons filter) @remove");
		run(c, "'(dup1 null" + " '(dup reverse yield)"
				+ " '(dup1 '(dup2 dup1 remove swap dup2 cons perm) for) if drop2) @perm");
		run(c, "'('() 'perm generator2) @permutations");
		assertEquals(eval(c, "'(())"), eval(c, "'() permutations"));
		assertEquals(eval(c, "'((1))"), eval(c, "'(1) permutations"));
		assertEquals(eval(c, "'((1 2) (2 1))"), eval(c, "'(1 2) permutations"));
		assertEquals(eval(c, "'((1 2 3) (1 3 2) (2 1 3) (2 3 1) (3 1 2) (3 2 1))"), eval(c, "'(1 2 3) permutations"));
	}

	@Test
	public void testClassCastException() {
		Context c = context();
		try {
			run(c, "2 true +");
			fail();
		} catch (RuntimeException ex) {
			assertEquals("Cast error: Bool to Int", ex.getMessage());
		}
	}
	
	/**
	 * C言語
	 * <pre>
	 * // Square root of integer
     * unsigned int int_sqrt(unsigned int s) {
     *     // Zero yields zero
     *     // One yields one
     * 	   if (s <= 1) 
     * 		   return s;
     * 
     *     // Initial estimate (must be too high)
     * 	   unsigned int x0 = s / 2;
     * 
     * 	   // Update
     * 	   unsigned int x1 = (x0 + s / x0) / 2;
     * 
     * 	   while (x1 < x0)	// Bound check {
     * 		   x0 = x1;
     * 	      	x1 = (x0 + s / x0) / 2;
     * 	   }		
     * 	   return x0;
     * }
	 * <pre>
	 */
	static int isqrt(int s) {
		if (s <= 1)
			return s;
		int x0 = s, x1 = s / 2;
		while (x0 > x1) {
			x0 = x1;
			x1 = (x1 + s / x1) / 2;
		}
		return x0;
	}
	
	@Test
	public void testISqrtByJava() {
		int MAX = 10000;
		for (int i = 0; i <= MAX; ++i)
			assertEquals((int)Math.sqrt(i), isqrt(i));
	}

	/**
	 * ニュートン法により整数の平方根を求める。
	 * s x0 x1 : swap drop
	 * s x1 : dup1 dup1 /
	 * s x1 (s/x1) : dup1 +
	 * s x1 (s/x1+x1) : 2 /
	 * s x1 ((s/x1+x1)/2) :
	 * 漢字は入力できますか？
	 * エスケープキーはCtrl+[で代替できますか？
	 * できます。
	 */
	@Test
	public void testISqrtByNewton() {
		Context c = context();
		run(c, "'(dup 1 <="
		    + "   '()"
		    + "   '(dup dup 2 /"
		    + "     '(dup1 dup1 >) '(swap drop dup1 dup1 / dup1 + 2 /) while"
		    + "     drop ret1)"
		    + "   if) @int-sqrt");
		assertEquals(eval(c, "1"), eval(c, "1 int-sqrt"));
		assertEquals(eval(c, "1"), eval(c, "2 int-sqrt"));
		assertEquals(eval(c, "1"), eval(c, "3 int-sqrt"));
		assertEquals(eval(c, "2"), eval(c, "4 int-sqrt"));
		assertEquals(eval(c, "2"), eval(c, "5 int-sqrt"));
		assertEquals(eval(c, "2"), eval(c, "6 int-sqrt"));
		assertEquals(eval(c, "2"), eval(c, "7 int-sqrt"));
		assertEquals(eval(c, "2"), eval(c, "8 int-sqrt"));
		assertEquals(eval(c, "3"), eval(c, "9 int-sqrt"));
		c.variable(Symbol.of("expected-int-sqrt"),
			x -> x.push(Int.of((int)Math.sqrt(((Int)c.pop()).value))));
		assertEquals(eval(c, "1 200 1 range 'expected-int-sqrt map"),
			eval(c, "1 200 1 range 'int-sqrt map"));
	}

	/**
	 * sieveはListとInt(n)を引数として受け取り、Listをフィルターしたものを返す。
	 * フィルターの条件は以下のいずれかである。
	 * (1) nに等しい
	 * (2) nで割り切れない
	 * LIST Int sieve
	 * sieveは第2引数のIntをラムダ式にconsしてfilterを呼び出す。
	 * つまり第2引数はマクロ的に処理される。
	 * フィルター式はfilterにおいて、fork()したスタックで評価されるため、
	 * 外部の引数を参照することができない。
	 * LIST 2 sieveにおけるfilterの呼び出しは以下のように行われる。
	 * List (2 dup1 dup1 == true '(dup1 dup1 % 0 !=) if ret2) filter
	 * ここでフィルター式の先頭「2」はsieveの第2引数である。
	 * 
	 * primesはInt(n)を引数として受け取り、n以下の素数のリストを返す。
	 * 2からnまでのRangeを２つ作り、sieveをforで繰り返し実行する。
	 * n primes
	 * n : 2
	 * n 2 : dup1
	 * n 2 n : 1
	 * n 2 n 1 : range
	 * n Range(2,n,1) : 2
	 * n Range(2,n,1) 2 : rot
	 * n Range(2,n,1) 2 n : isqrt
	 * Range(2,n,1) 2 isqrt(n) : 1
	 * n Range(2,n,1) 2 isqrt(n) 1 : range
	 * n Range(2,n,1) Range(2,isqrt(n),1) : sieve for
	 * 
	 */
	@Test
	public void testPrimes() {
		Context c = context();
		run(c, "'('(dup1 dup1 == '(true ret2) '(% 0 !=) if) cons filter) @sieve");
		assertEquals(eval(c, "'(2)"), eval(c, "2 2 1 range 2 sieve"));
		assertEquals(eval(c, "'(2 3 5 7)"), eval(c, "2 7 1 range 2 sieve"));
		assertEquals(eval(c, "'(2 3 4 5 7 8 10 11 13 14 16 17 19 20)"),
			eval(c, "2 20 1 range 3 sieve"));
		assertEquals(eval(c, "'(2 3 5 7 11 13 17 19)"),
			eval(c, "2 20 1 range 2 20 1 range 'sieve for"));
		run(c, "'(dup 1 <="
		    + "   '()"
		    + "   '(dup dup 2 /"
		    + "     '(dup1 dup1 >) '(swap drop dup1 dup1 / dup1 + 2 /) while"
		    + "     drop ret1)"
		    + "   if) @isqrt");
		run(c, "'(2 dup1 1 range 2 rot isqrt 1 range 'sieve for) @primes");
		assertEquals(eval(c, "'(2 3 5 7 11 13 17 19)"), eval(c, "20 primes"));
		assertEquals(eval(c,
			"'(2 3 5 7 11 13 17 19 23 29 31 37 41 43 47 53 59 61 67 71 73 79 83 89 97)"),
			eval(c, "100 primes"));
	}

	/**
	 * ピボット リスト partition -> 分割リスト1 分割リスト2
	 * リストの内、ピボット以下の要素を分割リスト１へ、
	 * それ以外を分割リスト２へ追加して多値(２つの値)で返す。
	 * ただし結果のリストは逆順となる。
	 * 3 '(1 2 3 4 5 6) partition --> '(3 2 1) '(6 5 4)
	 */
	@Test
	public void testPartition() {
		Context c = context();
		run(c, "'() '() '(1 2 3 4 5 6) '(dup 3 <= '(rot cons swap) 'rcons if) for");
		assertEquals(eval(c, "'(6 5 4)"), c.pop());
		assertEquals(eval(c, "'(3 2 1)"), c.pop());
		run(c, "'('() '() rot '(dup dup4 <= '(rot cons swap) 'rcons if) for rot drop) @partition");
		run(c, "3 '(1 2 3 4 5 6) partition");
		assertEquals(eval(c, "'(6 5 4)"), c.pop());
		assertEquals(eval(c, "'(3 2 1)"), c.pop());
		assertEquals(0, c.sp);
	}

	static void swap(int[] arr, int i, int j) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

	static void quickSort(int[] arr, int low, int high) {
		if (low >= high)
			return;
		int pivot = arr[(low + high) / 2];
		int i = low, j = high;
		while (true) {
			while (arr[i] < pivot)
				++i;
			while (arr[j] > pivot)
				--j;
			if (i >= j)
				break;
			swap(arr, i, j);
		}
		quickSort(arr, low, j);
		quickSort(arr, j + 1, high);
	}

	@Test
	public void testQuickSortJava() {
		int[] arr = {4, 3, 5, 1, 2, 6};
		int[] expected = Arrays.copyOf(arr, arr.length);
		Arrays.sort(expected);
		int[] actual = Arrays.copyOf(arr, arr.length);
		quickSort(actual, 0, actual.length - 1);
		assertArrayEquals(expected, actual);
	}

	void defineSwapElements(Context c) {
		run(c, """
			'(: i j array -> ,				{proc swap-elements(i, j, array) -> ()}
				temp i array at :				{local temp = array[i]}
				j array at i array put			{array[i] = array[j]}
				temp j array put				{array[j] = temp}
			) @swap-elements				{end proc}
			""");
	}

	@Test
	public void testQuckSort() {
		Context c = context();
		defineSwapElements(c);
		assertEquals(eval(c, "'(1 2 5 4 3)"), eval(c, "'(1 2 3 4 5) to-array 2 4 dup2 swap-elements"));
		run(c, """
			'(: low high array -> ,						{proc(row, high array) -> ()}
				pivot 0,									{local pivot = 0}
				i low,										{local i = low}
				j high :									{local j = high}
				low high <									{when low < high do}
				'(  low high + 2 / array at @pivot				{pivot = array[(low + high) / 2]}
					'(i j <)									{while i < j do}
					'(  '(i array at pivot <)						{while array[i] < pivot do}
							'(i 1 + @i)									{i = i + 1}
						while										{end while}
						'(j array at pivot >)						{while array[j] > pivoit do}
							'(j 1 - @j)									{j = j - 1}
						while										{end while}
						i j <										{when i < j do}
							'(i j array swap-elements)					{swap-elements(i, j, array)}
						when										{end when}
					) while										{end while}
					low j array quick-sort-range				{quick-sort-range(low, j, array)}
					j 1 + high array quick-sort-range			{quick-sort-range(j + 1, high, array)}
				) when										{end when}
			) @quick-sort-range							{end proc}
			""");
		run(c, "'(0 swap dup size 1 - swap quick-sort-range) @quick-sort");
		assertEquals(eval(c, "'()"), eval(c, "'() to-array dup quick-sort"));
		assertEquals(eval(c, "'(1)"), eval(c, "'(1) to-array dup quick-sort"));
		assertEquals(eval(c, "'(1 2)"), eval(c, "'(2 1) to-array dup quick-sort"));
		assertEquals(eval(c, "'(1 2 3)"), eval(c, "'(1 2 3) to-array dup quick-sort"));
		assertEquals(eval(c, "'(1 2 3 4 5 6)"), eval(c, "'(4 3 5 1 2 6) to-array dup quick-sort"));
	}

	/**
	 * [Python]
	 * def partition(arr, low, high):
     *     pivot = arr[high]
     *     i = low - 1
     * 
     *     for j in range(low, high):
     *         if arr[j] <= pivot:
     *             i += 1
     *             arr[i], arr[j] = arr[j], arr[i]
     *     arr[i + 1], arr[high] = arr[high], arr[i + 1]
     *     return i + 1
     * 
     * def quick_sort(arr, low, high):
     *     if low < high:
     *         pi = partition(arr, low, high)
     * 
     *         quick_sort(arr, low, pi - 1)
     *         quick_sort(arr, pi + 1, high)
     * 
     * # 使用例
     * arr = [64, 34, 25, 12, 22, 11, 90]
     * quick_sort(arr, 0, len(arr) - 1)
     * print("Sorted array is:", arr)
	 */
	@Test
	public void testQuickSortPatition() {
		Context c = context();
		defineSwapElements(c);
		run(c, """
			'(: low high array -> pos,			  		{proc partition(low, high, array) -> pos}
				pivot high array at,					{local pivot = array[hight]}
				i low 1 -,								{local i = low - 1}
				j low :									{local j = low}
				'(j high <)								{while j < hight do}
				'(  j array at pivot <=						{when array[j] <= pivot do}
					'(  i 1 + @i								{i = i + 1}
						i j array swap-elements					{swap-elements(i, j, array)}
					) when									{end when}
					j 1 + @j								{j = j + 1}
				) while									{end while}
				i 1 + high array swap-elements			{swap-elements(i + 1, hight, array)}
				i 1	+								{return i + 1}
			) @partition 							{end proc}
			""");
		run(c, "'(1 5 2 4 6 3) to-array 0 dup1 size 1 - dup2 partition");
		assertEquals(eval(c, "2"), c.pop());
		assertEquals(eval(c, "'(1 2 3 4 6 5)"), c.pop());
		run(c, """
			'(: low high array -> ,					{proc quick-sort-range(low, high, array) -> }
				pi 0 :									{local pi = 0}
				low high <								{when low < high do}
				'(  low high array partition @pi			{pi = partition(low, high, array)}
					low pi 1 - array quick-sort-range		{quick-sort-range(low, pi - 1, array)}
					pi 1 + high array quick-sort-range		{quick-sort-range(pi + 1, high, array)}
				) when									{end when}
			) @quick-sort-range						{end proc}
			""");
		run(c, "'(0 swap dup size 1 - swap quick-sort-range) @quick-sort");
		assertEquals(eval(c, "'()"), eval(c, "'() to-array dup quick-sort"));
		assertEquals(eval(c, "'(1)"), eval(c, "'(1) to-array dup quick-sort"));
		assertEquals(eval(c, "'(1 2)"), eval(c, "'(1 2) to-array dup quick-sort"));
		assertEquals(eval(c, "'(1 2)"), eval(c, "'(2 1) to-array dup quick-sort"));
		assertEquals(eval(c, "'(1 2 3)"), eval(c, "'(1 2 3) to-array dup quick-sort"));
		assertEquals(eval(c, "'(1 2 3)"), eval(c, "'(1 3 2) to-array dup quick-sort"));
		assertEquals(eval(c, "'(1 2 3)"), eval(c, "'(3 1 2) to-array dup quick-sort"));
		assertEquals(eval(c, "'(1 2 3)"), eval(c, "'(2 1 3) to-array dup quick-sort"));
		assertEquals(eval(c, "'(1 2 3 4 5 6)"), eval(c, "'(1 5 2 4 6 3) to-array dup quick-sort"));
	}

	/**
	 *     public static int[] bubbleSort(int[] array) {
     *        for (int i = 0; i < array.length; i++)
     *            for (int j = 0; j < array.length - 1 - i; j++)
     *                if (array[j + 1] < array[j]) {
     *                    int temp = array[j + 1];
     *                    array[j + 1] = array[j];
     *                    array[j] = temp;
     *                }
     *        return array;
     *    }
     *
	 */
	@Test
	public void testBubbleSort() {
		Context c = context();
		defineSwapElements(c);
		run(c, """
			'( : array -> ,								{proc bubble-sort(array -> )}
				i 0, j 0 :									{local i = 0, j = 0}
				0 array size 1 - 1 range					{for % in range(0, array.size - 1, 1) do}
				'(  @i											{i = %}
					0 array size 2 - i - 1 range				{for % in range(0, array.size - 2 - i, 1)}
					'(  @j											{j = %}
						j 1 + array at j array at <					{when array[j + 1] < array[j] do}
							'(j j 1 + array swap-elements)				{swap-elements(j, j + 1, array)}
						when										{end when}
					) for										{end for}
				) for										{end for}
			) @bubble-sort								{end proc}
				
			""");
		assertEquals(eval(c, "'()"), eval(c, "'() to-array dup bubble-sort"));
		assertEquals(eval(c, "'(1)"), eval(c, "'(1) to-array dup bubble-sort"));
		assertEquals(eval(c, "'(1 2)"), eval(c, "'(1 2) to-array dup bubble-sort"));
		assertEquals(eval(c, "'(1 2)"), eval(c, "'(2 1) to-array dup bubble-sort"));
		assertEquals(eval(c, "'(1 2 3)"), eval(c, "'(1 2 3) to-array dup bubble-sort"));
		assertEquals(eval(c, "'(1 2 3)"), eval(c, "'(1 3 2) to-array dup bubble-sort"));
		assertEquals(eval(c, "'(1 2 3)"), eval(c, "'(3 1 2) to-array dup bubble-sort"));
		assertEquals(eval(c, "'(1 2 3)"), eval(c, "'(2 1 3) to-array dup bubble-sort"));
		assertEquals(eval(c, "'(1 2 3 4 5 6)"), eval(c, "'(1 5 2 4 6 3) to-array dup bubble-sort"));

	}

	/**
	 * void 
     * insertion_sort(int data[], size_t n) {
     *     for (size_t i = 1; i < n; i++) {
     *         if (data[i - 1] > data[i]) {
     *             size_t j = i;
     *             int tmp = data[i];
     *             do {
     *                 data[j] = data[j - 1];
     *                 j--;
     *             } while (j > 0 && data[j - 1] > tmp);
     *             data[j] = tmp;
     *         }
     *     }
     * }
	 */
	@Test
	public void testInsertionSort() {
		Context c = context();
		run(c, """
			'( : array -> ,									{proc insertion-sort(array -> )}
				i 0, j 0, tmp 0 :								{local i = 0, j = 0, tmp = 0}
				1 array size 1 - 1 range						{for % in range(1, array.size - 1, 1) do}
				'(  @i												{i = %}
					i 1 - array at i array at >						{when array[i -1] > array[i] then}
					'(  i @j											{j = i}
						i array at @tmp									{tmp = array[i]}
						'(j 0 > '(j 1 - array at tmp >) &&)				{while j > 0 && array[j - 1] > tmp do}
						'(  j 1 - array at j array put						{array[j] = array[j - 1]}
							j 1 - @j										{j = j - 1}
						) while											{end while}
						tmp j array put									{array[j] = tmp}
					) when											{end when}
				) for											{end for}
			) @ insertion-sort
			""");
		assertEquals(eval(c, "'()"), eval(c, "'() to-array dup insertion-sort"));
		assertEquals(eval(c, "'(1)"), eval(c, "'(1) to-array dup insertion-sort"));
		assertEquals(eval(c, "'(1 2)"), eval(c, "'(1 2) to-array dup insertion-sort"));
		assertEquals(eval(c, "'(1 2)"), eval(c, "'(2 1) to-array dup insertion-sort"));
		assertEquals(eval(c, "'(1 2 3)"), eval(c, "'(1 2 3) to-array dup insertion-sort"));
		assertEquals(eval(c, "'(1 2 3)"), eval(c, "'(1 3 2) to-array dup insertion-sort"));
		assertEquals(eval(c, "'(1 2 3)"), eval(c, "'(3 1 2) to-array dup insertion-sort"));
		assertEquals(eval(c, "'(1 2 3)"), eval(c, "'(2 1 3) to-array dup insertion-sort"));
		assertEquals(eval(c, "'(1 2 3 4 5 6)"), eval(c, "'(1 5 2 4 6 3) to-array dup insertion-sort"));
	}
}
