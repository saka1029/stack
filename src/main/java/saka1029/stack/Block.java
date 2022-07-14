package saka1029.stack;

import java.util.List;

public class Block extends Value {

	final Evaluable[] elements;
	
	Block(Evaluable... elements) {
		this.elements = elements.clone();
	}
	
	Block(List<Evaluable> list) {
		this.elements = list.toArray(Evaluable[]::new);
	}
	
	public static Block of(Evaluable... elements) {
		return new Block(elements);
	}
	
	public static Block of(List<Evaluable> list) {
		return new Block(list);
	}
	
	@Override
	public void evlis(Context context) {
		for (Evaluable e : elements)
			e.eval(context);
	}
}
