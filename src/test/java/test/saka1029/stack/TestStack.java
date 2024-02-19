package test.saka1029.stack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static saka1029.stack.Stack.context;
import static saka1029.stack.Stack.eval;
import static saka1029.stack.Stack.run;

import java.util.logging.Logger;

import org.junit.Test;

import saka1029.Common;
import saka1029.stack.Cons;
import saka1029.stack.Context;
import saka1029.stack.Int;
import saka1029.stack.Stack;

public class TestStack {

	static final Logger logger = Common.logger(TestStack.class);

	// GitKrakenテスト

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
		assertEquals(Cons.list(Int.of(2)), eval(c, "'(1 2) cdr"));
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
	public void testFor() {
		Context c = context();
		assertEquals(Int.of(6), eval(c, "0 '(1 2 3) '+ for"));
	}

	@Test
	public void testWhile() {
		Context c = context();
		assertEquals(eval(c, "6"), eval(c, "3 0 '(dup1 0 >) '(dup1 1 - rrot +) while ret1"));
		run(c,"'(1 '(dup1 0 >) '(dup1 1 - rrot *) while ret1) 'factorial define");
		assertEquals(eval(c, "1"), eval(c, "0 factorial"));
		assertEquals(eval(c, "1"), eval(c, "1 factorial"));
		assertEquals(eval(c, "2"), eval(c, "2 factorial"));
		assertEquals(eval(c, "6"), eval(c, "3 factorial"));
		assertEquals(eval(c, "24"), eval(c, "4 factorial"));
		assertEquals(eval(c, "120"), eval(c, "5 factorial"));
		assertEquals(eval(c, "720"), eval(c, "6 factorial"));
	}

	@Test
	public void testRange() {
		Context c = context();
		assertEquals(Int.of(6), eval(c, "0 1 3 1 range '+ for"));
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
		assertEquals(eval(c, "'(3 2 1)"), eval(c, "1 3 1 range reverse"));
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
		run(c, "'(swap dup null? '() '(uncons swap dup2 execute swap dup2 map cons) if ret1) 'map define");
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
		run(c, "'('() rrot '(rcons) cons for reverse) 'map define");
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
		run(c, "'(swap dup null? '() '(uncons dup2 filter swap dup dup3 execute 'rcons 'drop if) if ret1) 'filter define");
		assertEquals(eval(c, "'(0 2)"), eval(c, "'(0 1 2 3) '(2 % 0 ==) filter"));
		assertEquals(eval(c, "'(1 3)"), eval(c, "'(0 1 2 3) '(2 % 0 !=) filter"));
	}

	@Test
	public void testFilterRecursiveCarFirst() {
		Context c = context();
		run(c, "'(swap dup null? '() '(uncons swap dup dup3 execute rot dup3 filter swap 'cons 'ret1 if) if ret1) 'filter define");
		assertEquals(eval(c, "'(0 2)"), eval(c, "'(0 1 2 3) '(2 % 0 ==) filter"));
		assertEquals(eval(c, "'(1 3)"), eval(c, "'(0 1 2 3) '(2 % 0 !=) filter"));
	}

	@Test
	public void testFilterBuiltin() {
		Context c = context();
		assertEquals(eval(c, "'(0 2)"), eval(c, "'(0 1 2 3) '(2 % 0 ==) filter"));
		assertEquals(eval(c, "'(1 3)"), eval(c, "'(0 1 2 3) '(2 % 0 !=) filter"));
		run(c, "'('(!=) cons filter) 'remove define");
		assertEquals(eval(c, "'(0 1 3 1 0)"), eval(c, "'(0 1 2 3 2 1 0) 2 remove"));
	}

	/**
	 * 通常の関数呼び出し: 引数 関数
	 * 
	 * 無名再帰の関数呼び出し: 引数 '関数 rexecute
	 * 
	 * 関数は自分自身をdupしてからexecuteすることで、自分自身を引数として受け取ることができる。 引数 関数 : rexecute 引数 関数 :
	 * dup 引数 関数 関数 : execute (関数は第2引数として[引数 関数]を受け取る)
	 */
	@Test
	public void testAnonymousRecursion() {
		Context c = context();
		assertEquals(eval(c, "1"),
				eval(c, "0 '(swap dup 0 <= '(drop 1) '(dup 1 - dup2 dup execute *) if ret1) dup execute"));
		assertEquals(eval(c, "1"),
				eval(c, "1 '(swap dup 0 <= '(drop 1) '(dup 1 - dup2 dup execute *) if ret1) dup execute"));
		assertEquals(eval(c, "2"),
				eval(c, "2 '(swap dup 0 <= '(drop 1) '(dup 1 - dup2 dup execute *) if ret1) dup execute"));
		run(c, "'(dup execute) 'rexecute define");
		assertEquals(eval(c, "6"), eval(c, "3 '(swap dup 0 <= '(drop 1) '(dup 1 - dup2 rexecute *) if ret1) rexecute"));
		assertEquals(eval(c, "24"),
				eval(c, "4 '(swap dup 0 <= '(drop 1) '(dup 1 - dup2 rexecute *) if ret1) dup execute"));
		// 無名関数に名前を付けた場合(先頭がクォート2個である点に注意する)
		run(c, "''(swap dup 0 <= '(drop 1) '(dup 1 - dup2 rexecute *) if ret1) 'fact define");
		assertEquals(eval(c, "24"), eval(c, "4 fact rexecute"));
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
		run(c, "'('() rrot '(rcons) cons for reverse) 'map define");
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
		Context c = Stack.context();
		StringBuilder sb = new StringBuilder();
		c.output(sb::append);
		run(c, "'('(!=) cons filter) 'remove define");
		assertEquals(eval(c, "'(0 1 3)"), eval(c, "'(0 1 2 3) 2 remove"));
		run(c, "'(dup1 null?" + " '(dup reverse print)"
				+ " '(dup1 '(dup2 dup1 remove swap dup2 cons perm) for) if drop2) 'perm define");
		run(c, "'('() perm) 'permutations define");
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
		Context c = Stack.context();
		run(c, "'('(!=) cons filter) 'remove define");
		run(c, "'(dup1 null?" + " '(dup reverse yield)"
				+ " '(dup1 '(dup2 dup1 remove swap dup2 cons perm) for) if drop2) 'perm define");
		run(c, "'('() 'perm generator2) 'permutations define");
		assertEquals(eval(c, "'(())"), eval(c, "'() permutations"));
		assertEquals(eval(c, "'((1))"), eval(c, "'(1) permutations"));
		assertEquals(eval(c, "'((1 2) (2 1))"), eval(c, "'(1 2) permutations"));
		assertEquals(eval(c, "'((1 2 3) (1 3 2) (2 1 3) (2 3 1) (3 1 2) (3 2 1))"), eval(c, "'(1 2 3) permutations"));
	}

	@Test
	public void testClassCastException() {
		Context c = Stack.context();
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
			x1 = (x0 + s / x0) / 2;
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
	 */
	@Test
	public void testISqrtByNewton() {
		Context c = Stack.context();
		run(c, "'(dup 1 <= '() '(dup dup 2 / )) 'isqrt define");
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
	 * Range(2,n,1) 2 n : 1
	 * n Range(2,n,1) 2 n 1 : range
	 * n Range(2,n,1) Range(2,n,1) : sieve for
	 * 
	 */
	@Test
	public void testPrimes() {
		Context c = Stack.context();
//		run(c, "'('(dup1 dup1 == true '(dup1 dup1 % 0 !=) if ret2) cons filter) 'sieve define");
		run(c, "'('(dup1 dup1 == '(true ret2) '(% 0 !=) if) cons filter) 'sieve define");
		assertEquals(eval(c, "'(2)"), eval(c, "2 2 1 range 2 sieve"));
		assertEquals(eval(c, "'(2 3 5 7)"), eval(c, "2 7 1 range 2 sieve"));
		assertEquals(eval(c, "'(2 3 4 5 7 8 10 11 13 14 16 17 19 20)"), eval(c, "2 20 1 range 3 sieve"));
		assertEquals(eval(c, "'(2 3 5 7 11 13 17 19)"), eval(c, "2 20 1 range 2 20 1 range 'sieve for"));
		run(c, "'(2 dup1 1 range 2 rot 1 range 'sieve for) 'primes define");
		assertEquals(eval(c, "'(2 3 5 7 11 13 17 19)"), eval(c, "20 primes"));
		assertEquals(eval(c, "'(2 3 5 7 11 13 17 19 23 29 31 37 41 43 47 53 59 61 67 71 73 79 83 89 97)"), eval(c, "100 primes"));
	}
}
