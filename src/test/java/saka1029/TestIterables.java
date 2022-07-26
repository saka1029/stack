package saka1029;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static saka1029.Iterables.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.Test;

public class TestIterables {
	
	@Test
	public void testIsEmpty() {
		assertTrue(isEmpty(range(0, 0)));
		assertFalse(isEmpty(range(0, 1)));
	}

	@Test
	public void testIterator() {
		Iterator<Integer> it = iterator(new Object() { int i = 0; }, c -> c.i < 3, c -> c.i++);
		assertTrue(it.hasNext());
		assertEquals(0, (int)it.next());
		assertTrue(it.hasNext());
		assertEquals(1, (int)it.next());
		assertTrue(it.hasNext());
		assertEquals(2, (int)it.next());
		assertFalse(it.hasNext());
	}

	@Test
	public void testIterable() {
		Iterable<Integer> iterable = iterable(Stream.of(1, 2));
		Iterator<Integer> iterator = iterable.iterator();
		assertEquals(1, (int)iterator.next());
		assertEquals(2, (int)iterator.next());
		assertFalse(iterator.hasNext());
		Iterator<Integer> it = iterable(new Object() { int i = 0;}, c -> c.i < 3, c -> c.i++).iterator();
		assertTrue(it.hasNext());
		assertEquals(0, (int)it.next());
		assertEquals(1, (int)it.next());
		assertEquals(2, (int)it.next());
		assertFalse(it.hasNext());
	}
	
	@Test(expected = IllegalStateException.class)
	public void testIterableError() {
		Iterable<Integer> iterable = iterable(Stream.of(1, 2));
        iterable.iterator();
        iterable.iterator();
	}
	
	@Test
	public void testRangeIntInt() {
		assertEquals(List.of(2, 3, 4), arrayList(range(2, 5)));
		assertEquals(List.of(), arrayList(range(2, 2)));
	}

	@Test
	public void testRangeIntIntInt() {
		assertEquals(List.of(2, 3, 4), arrayList(range(2, 5, 1)));
		assertEquals(List.of(2, 4), arrayList(range(2, 5, 2)));
		assertEquals(List.of(5, 3), arrayList(range(5, 2, -2)));
		assertEquals(List.of(), arrayList(range(5, 9, -2)));
	}

	@Test
	public void testIterableInts() {
		assertEquals(List.of(2, 3, 4), arrayList(list(2, 3, 4)));
	}
	
	@Test
	public void testCodePoints() {
		assertEquals(List.of(97, 98, 99), arrayList(codePoints("abc")));
		assertEquals(List.of(97, 171581, 99), arrayList(codePoints("a𩸽c")));
		Iterable<Integer> it = codePoints("abc");
		assertEquals(List.of(97, 98, 99), arrayList(it));
		assertEquals(List.of(97, 98, 99), arrayList(it));
		assertEquals(List.of("a", "𩸽", "c"), arrayList(map(Character::toString, codePoints("a𩸽c"))));
		assertEquals("a𩸽c", cpstring(codePoints("a𩸽c")));
	}

	@Test
	public void testMap() {
		assertEquals(List.of(20, 30, 40), arrayList(map(n -> n * 10, list(2, 3, 4))));
//		assertEquals(List.of(12, 23, 34), arrayList(map((i, n) -> ++i * 10 + n, list(2, 3, 4))));
//		assertEquals(List.of("0:zero", "1:one"),
//            arrayList(map((i, n) -> i + ":" + n, List.of("zero", "one"))));
		assertEquals(List.of("0:zero", "1:one"),
            arrayList(map((l, r) -> l + ":" + r, range(0, 5), List.of("zero", "one"))));
	}

	@Test
	public void testFilter() {
		assertEquals(List.of(0, 2, 4), arrayList(filter(n -> n % 2 == 0, range(0, 5))));
//		assertEquals(List.of(0, 4), arrayList(filter((i, n) -> i == n, list(0, 9, 8, 7, 4))));
		assertEquals(List.of(10, 30), arrayList(map(n -> 10 * n, filter(n -> n % 2 == 1, range(0, 5)))));
	}
	
	@Test
	public void testFlatMap() {
		List<int[]> input = List.of(
			new int[] {1,1,1,1,1},
		    new int[] {0,1,0,1,1},
		    new int[] {0,0,0,0,0});
		assertArrayEquals(new int[] {1,1,1,1,1,0,1,0,1,1,0,0,0,0,0},
			array(flatMap(Iterables::list, input)));
		assertArrayEquals(new int[] {0, 1, 2},
			array(flatMap(e -> list(e),
				List.of(new int[] {0}, new int[] {}, new int[] {1, 2}))));
		assertArrayEquals(new int[] {0, 1, 2},
			array(flatMap(e -> list(e),
				List.of(new int[] {}, new int[] {0, 1, 2}, new int[] {}))));
	}
	
	@Test
	public void testAcumulate() {
		assertEquals(List.of(0, 1, 3, 6), arrayList(acumulate(0, Integer::sum, range(0, 4))));
		assertEquals(List.of(1, 2, 6, 24), arrayList(acumulate(1, (a, b) -> a * b, range(1, 5))));
	}
	
	@Test
	public void testReduce() {
		assertEquals(45, (int)reduce(0, Integer::sum, rangeClosed(1, 9)));
		assertEquals(45, (int)reduce(Integer::sum, rangeClosed(1, 9)));
		assertEquals(120, (int)reduce((a, b) -> a * b, rangeClosed(1, 5)));
		assertEquals(1234, (int)reduce((a, b) -> a * 10 + b, rangeClosed(1, 4)));
	}

	@Test
	public void testSort() {
		assertEquals(List.of(0, 1, 2, 3, 4), sort(list(2, 0, 1, 4, 3)));
		assertEquals(List.of(4, 3, 2, 1, 0), sort(reverse(), list(2, 0, 1, 4, 3)));
		assertEquals(List.of(0, 1, 2, 3, 4), sort((a, b) -> Integer.compare(a, b), list(2, 0, 1, 4, 3)));
		assertEquals(List.of(4, 3, 2, 1, 0), sort((a, b) -> Integer.compare(b, a), list(2, 0, 1, 4, 3)));
	}

	@Test
	public void testReverse() {
		assertEquals(List.of(3, 4, 1, 0, 2), reverse(list(2, 0, 1, 4, 3)));
	}
	
	@Test
	public void testAllMatch() {
		assertTrue(allMatch(i -> i >= 0, range(0, 3)));
		assertFalse(allMatch(i -> i <= 1, range(0, 3)));
	}
	
	@Test
	public void testAnyMatch() {
		assertTrue(anyMatch(i -> i == 0, range(0, 3)));
		assertFalse(anyMatch(i -> i < 0, range(0, 3)));
	}
	
	@Test
	public void testCount() {
		assertEquals(3, count(range(0, 3)));
		assertEquals(0, count(range(0, 0)));
	}
	
	@Test
	public void testSum() {
		assertEquals(3, sum(range(0, 3)));
		assertEquals(0, sum(range(0, 0)));
		assertEquals(3, sum(map(Integer::parseInt, List.of("1", "2"))));
	}
	
	@Test
	public void testMin() {
		assertEquals(0, (int)min(range(0, 3)));
		assertEquals("A", min(List.of("A", "B", "C")));
	}
	
	@Test(expected = IllegalStateException.class)
	public void testMinException() {
		assertEquals(0, (int)min(range(0, 0)));
	}
	
	@Test
	public void testMax() {
		assertEquals(2, (int)max(range(0, 3)));
		assertEquals("C", max(List.of("A", "B", "C")));
	}
	
	@Test(expected = IllegalStateException.class)
	public void testMaxException() {
		assertEquals(0, (int)max(range(0, 0)));
	}
	
	@Test
	public void testSkip() {
		assertEquals(List.of(3, 4), arrayList(skip(3, range(0, 5))));
		assertEquals(List.of(), arrayList(skip(3, range(0, 3))));
	}
	
	@Test
	public void testLimit() {
		assertEquals(List.of(0, 1, 2), arrayList(limit(3, range(0, 5))));
		assertEquals(List.of(0, 1, 2, 3, 4), arrayList(limit(9, range(0, 5))));
		assertEquals(List.of(), arrayList(limit(0, range(0, 5))));
	}
	
	@Test
	public void testArray() {
		assertArrayEquals(new int[] {1, 2, 3}, array(list(1, 2, 3)));
		assertArrayEquals(new Integer[] {1, 2, 3}, array(Integer[]::new, list(1, 2, 3)));
	}
	
	@Test
	public void testString() {
		assertEquals("[a, b, c]", string("[", ", ", "]", List.of("a", "b", "c")));
		assertEquals("[0, 1, 2]", string("[", ", ", "]", list(0, 1, 2)));
		assertEquals("\0\1\2", cpstring(list(0, 1, 2)));
		assertEquals("012", string("", "", "", list(0, 1, 2)));
	}
	
	@Test
	public void testCollection() {
		Collection<Integer> c = collection(HashSet::new, list(1, 2, 2, 3));
		assertEquals(HashSet.class, c.getClass());
		assertEquals(Set.of(1, 2, 3), c);
	}
	
	@Test
	public void testArrayList() {
		assertEquals(List.of(2, 3, 4), arrayList(list(2, 3, 4)));
		assertEquals(ArrayList.class, arrayList(list(2, 3, 4)).getClass());
		assertEquals(List.of(2, 3, 4), arrayList(List.of(2, 3, 4)));
		assertEquals(List.of(2, 3, 4), arrayList(() -> Stream.of(2, 3, 4).iterator()));
	}

	@Test
	public void testLinkedList() {
		assertEquals(List.of(2, 3, 4), linkedList(list(2, 3, 4)));
		assertEquals(LinkedList.class, linkedList(list(2, 3, 4)).getClass());
		assertEquals(List.of(2, 3, 4), linkedList(List.of(2, 3, 4)));
	}

    record N(int i, String s) {}

	@Test
	public void testHashMap() {
		assertEquals(Map.of(0, "zero", 1, "one"),
			hashMap(N::i, N::s, List.of(new N(0, "zero"), new N(1, "one"))));
	}

	@Test
	public void testTreeMap() {
		assertEquals(Map.of(0, "zero", 1, "one"),
			treeMap(N::i, N::s, List.of(new N(0, "zero"), new N(1, "one"))));
	}

	@Test
	public void testHashSet() {
		assertEquals(List.of(0, 1, 2), arrayList(hashSet(list(2, 0, 1, 1, 0))));
	}

	@Test
	public void testTreeSet() {
		assertEquals(List.of(0, 1, 2), arrayList(treeSet(list(2, 0, 1, 1, 0))));
		assertEquals(List.of(2, 1, 0), arrayList(treeSet(desc(i -> i), list(2, 0, 1, 1, 0))));
	}
	
	@Test
	public void testAnd() {
		record R(String s, int i) {}
		R a0 = new R("a", 0);
		R a1 = new R("a", 1);
		R b0 = new R("b", 0);
		R b1 = new R("b", 1);
		List<R> list = List.of(a0, a1, b0, b1);
		assertEquals(List.of(a0, a1, b0, b1), arrayList(sort(and(asc(R::s), asc(R::i)), list)));
		assertEquals(List.of(a1, a0, b1, b0), arrayList(sort(and(asc(R::s), desc(R::i)), list)));
		assertEquals(List.of(b0, b1, a0, a1), arrayList(sort(and(desc(R::s), asc(R::i)), list)));
		assertEquals(List.of(b1, b0, a1, a0), arrayList(sort(and(desc(R::s), desc(R::i)), list)));
		assertEquals(List.of(b1, b0, a1, a0), arrayList(sort(and(reverse(asc(R::s)), reverse(asc(R::i))), list)));
	}
	
	@Test
	public void testAsc() {
		assertEquals(List.of(0, 1, 2, 3), sort(asc(i -> i), range(0, 4)));
		assertEquals(List.of(0, 1, 2, 3), sort(asc(), range(0, 4)));
	}
	
	@Test
	public void testDesc() {
		assertEquals(List.of(3, 2, 1, 0), sort(desc(i -> i), range(0, 4)));
		assertEquals(List.of(3, 2, 1, 0), sort(desc(), range(0, 4)));
	}
	
	@Test
	public void testStatistics() {
		Statistics s = statistics(range(0, 10));
		assertEquals(10, s.count);
		assertEquals(45, s.sum, 0.1);
		assertEquals(285, s.squareSum, 0.1);
	}
	
	@Test
	public void testConcat() {
		assertArrayEquals(new int[] {0, 1, 2, 3}, array(concat(rangeClosed(0, 1), rangeClosed(2, 3))));
	}
}
