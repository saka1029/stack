package saka1029.stack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import saka1029.Common;

public class Stack {

	static final Logger logger = Common.logger(Stack.class);

	private Stack() {
	}

	public static Context context() {
		Map<Symbol, Instruction> vars = new HashMap<>();
		standard(vars);
		return Context.of(vars);
	}

	public static List read(String source) {
		return Parser.of(source).read();
	}

	public static void run(Context context, String source) {
		context.run(read(source));
	}

	public static Instruction eval(Context context, String source) {
		return context.eval(read(source));
	}

	static void put(Map<Symbol, Instruction> variables, String name, Instruction instruction) {
		variables.put(Symbol.of(name), instruction);
	}

	static boolean b(Instruction instruction) {
		return ((Bool) instruction).value;
	}

	static Bool b(boolean value) {
		return Bool.of(value);
	}

	static int i(Instruction instruction) {
		return ((Int) instruction).value;
	}

	static Int i(int value) {
		return Int.of(value);
	}

	static Comparable comp(Instruction instruction) {
		return (Comparable) instruction;
	}

	static List l(Instruction instruction) {
		return ((List) instruction);
	}

	static Cons c(Instruction instruction) {
		return ((Cons) instruction);
	}

	static Symbol s(Instruction instruction) {
		return ((Symbol) instruction);
	}
	
	static Symbol symbol(String name) {
	    return Symbol.of(name);
	}

	static Quote quote(Instruction instruction) {
	    return Quote.of(instruction);
	}

	static List consList(Instruction... instructions) {
	    return Cons.list(instructions);
	}

	static List consList(java.util.List<Instruction> list) {
	    return Cons.list(list);
	}

	static void standard(Map<Symbol, Instruction> vars) {
		put(vars, "dup", Context::dup);
		put(vars, "dup0", Context::dup);
		put(vars, "dup1", c -> c.dup(1));
		put(vars, "dup2", c -> c.dup(2));
		put(vars, "dup3", c -> c.dup(3));
		put(vars, "drop", Context::drop);
		put(vars, "drop2", c -> c.drop(2));
		put(vars, "drop3", c -> c.drop(3));
		put(vars, "swap", Context::swap);
		put(vars, "rot", Context::rot);
		put(vars, "rrot", Context::rrot);
		put(vars, "ret1", c -> c.ret(1));
		put(vars, "ret2", c -> c.ret(2));
		put(vars, "ret3", c -> c.ret(3));
		put(vars, "true", Bool.TRUE);
		put(vars, "false", Bool.FALSE);
		put(vars, "==", c -> c.push(b(c.pop().equals(c.pop()))));
		put(vars, "!=", c -> c.push(b(!c.pop().equals(c.pop()))));
		put(vars, "<", c -> c.push(b(comp(c.pop()).compareTo(c.pop()) > 0)));
		put(vars, "<=", c -> c.push(b(comp(c.pop()).compareTo(c.pop()) >= 0)));
		put(vars, ">", c -> c.push(b(comp(c.pop()).compareTo(c.pop()) < 0)));
		put(vars, ">=", c -> c.push(b(comp(c.pop()).compareTo(c.pop()) <= 0)));
		put(vars, "+", c -> c.push(i(i(c.pop()) + i(c.pop()))));
		put(vars, "-", c -> c.push(i(-i(c.pop()) + i(c.pop()))));
		put(vars, "*", c -> c.push(i(i(c.pop()) * i(c.pop()))));
		put(vars, "and", c -> c.push(b(b(c.pop()) & b(c.pop()))));
		put(vars, "or", c -> c.push(b(b(c.pop()) | b(c.pop()))));
		put(vars, "not", c -> c.push(b(!b(c.pop()))));
		put(vars, "/", c -> {
			int r = i(c.pop());
			c.push(i(i(c.pop()) / r));
		});
		put(vars, "%", c -> {
			int r = i(c.pop());
			c.push(i(i(c.pop()) % r));
		});
		put(vars, "null?", c -> c.push(b(c.pop().equals(List.NIL))));
		put(vars, "car", c -> c.push(c(c.pop()).car));
		put(vars, "cdr", c -> c.push(c(c.pop()).cdr));
		put(vars, "uncons", c -> {
			Cons cons = c(c.pop());
			c.push(cons.car);
			c.push(cons.cdr);
		});
		put(vars, "cons", c -> {
			List cdr = l(c.pop());
			Instruction car = c.pop();
			c.push(Cons.of(car, cdr));
		});
		put(vars, "rcons", c -> c.push(Cons.of(c.pop(), l(c.pop()))));
		put(vars, "reverse", c -> {
			List list = l(c.pop());
			List result = List.NIL;
			for (Instruction i : list)
				result = Cons.of(i, result);
			c.push(result);
		});
		put(vars, "print", c -> c.print(c.pop()));
		put(vars, "println", c -> c.println(c.pop()));
		put(vars, "stack", c -> System.out.println(c));
		put(vars, "execute", c -> c.execute(c.pop()));
		put(vars, "if", c -> {
			Instruction orElse = c.pop(), then = c.pop();
			if (b(c.pop()))
				c.execute(then);
			else
				c.execute(orElse);
		});
		put(vars, "for", c -> {
			Instruction closure = c.pop();
			Sequence it = l(c.pop()).sequence();
			c.instruction(() -> {
				Instruction i = it.next();
				return i == null ? null : consList(i, closure);
			});
		});
		/*
		 * COND BODY while はCONDをexecuteして結果がtrueである間、BODYを実行する。
		 * 実装はリスト (COND 'BODY 'END if) を繰り返し返すSequenceをinstructionsにプッシュする。
		 * ENDはSequence内の終了フラグをセットするInstructionである。
		 * 終了フラグがtrueの場合、Sequenceはリストではなくnullを返す。
		 */
		put(vars, "while", c -> {
		    Instruction body = c.pop(), cond = c.pop();
		    c.instruction(new Sequence() {
		        boolean done = false;
		        List w = consList(cond, quote(body), quote(x -> done = true), symbol("if"));
                @Override
                public Instruction next() {
                    return done ? null : w;
                }
		    });
		});
		/*
		 * 名前に反してConsによるListを返す。
		 */
		put(vars, "list", c -> {
			List list = l(c.pop());
			java.util.List<Instruction> a = new ArrayList<>();
			for (Instruction i : list)
				a.add(i);
			c.push(consList(a));
		});
//		/*
//		 * このmapの実装はクロージャーを別コンテキストで評価する点に注意する。
//		 * クロージャー内ではmapのコンテキストにアクセスできない。
//		 * クロージャー実行時は常にスタックが空である。
//		 * このmapはConsではないListのサブクラスを返す点に注意する。
//		 */
//		put(vars, "map", c -> {
//			Instruction closure = c.pop();
//			List list = l(c.pop());
//			Context child = c.fork();    // クロージャー評価用コンテキスト
//			c.push(new List() {
//				@Override
//				public Sequence sequence() {
//					Sequence it = list.sequence();
//					return () -> {
//						Instruction i = it.next();
//						return i == null ? null : child.eval(Cons.list(i, closure));
//					};
//				}
//			});
//		});
		/*
		 * このmapの実装は結果をConsで返す。
		 * closure(mapper)の評価は現在のContext内で行う。
		 */
		put(vars, "map", c -> {
			Instruction closure = c.pop();
			List list = l(c.pop());
			c.instruction(new Sequence() {
			    Sequence s = list.sequence();
                java.util.List<Instruction> result = new ArrayList<>();
                @Override
                public Instruction next() {
                    Instruction i = s.next();
                    if (i == null) {
                        c.push(Cons.list(result));
                        return null;
                    }
                    return Cons.list(i, closure, x -> result.add(c.pop()));
                }
			});
		});
		/*
		 * このfilterの実装はクロージャーを別コンテキストで評価する点に注意する。
		 * クロージャー内ではfilterのコンテキストにアクセスできない。
		 * クロージャー実行時は常にスタックが空である。
		 * このfilterはConsではないListのサブクラスを返す点に注意する。
		 */
		put(vars, "filter", c -> {
			Instruction closure = c.pop();
			List list = l(c.pop());
			Context child = c.fork();    // クロージャー評価用コンテキスト
			c.push(new List() {
				@Override
				public Sequence sequence() {
					Sequence it = list.sequence();
					return new Sequence() {
						Instruction cur;

						{
							advance();
						}

						void advance() {
							for (cur = it.next(); cur != null && !b(child.eval(Cons.list(cur, closure))); cur = it.next())
								/* do nothing */;
						}

						@Override
						public Instruction next() {
							Instruction result = cur;
							advance();
							return result;
						}
					};
				}
			});
		});
		put(vars, "range", c -> {
			int step = i(c.pop()), end = i(c.pop()), start = i(c.pop());
			c.push(Range.of(start, end, step));
		});
		put(vars, "define", c -> c.variable(s(c.pop()), c.pop()));
		put(vars, "yield", Terminal.YIELD);
		put(vars, "generator", c -> c.push(Generator.of(c, c.pop())));
		put(vars, "generator1", c -> c.push(Generator.of(c, c.pop(), c.pop())));
		put(vars, "generator2", c -> c.push(Generator.of(c, c.pop(), c.pop(), c.pop())));
	}
}
