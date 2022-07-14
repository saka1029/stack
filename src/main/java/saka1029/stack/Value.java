package saka1029.stack;

public abstract class Value implements Evaluable {

	@Override
	public void eval(Context context) {
		context.push(this);
	}
	
	public void evlis(Context context) {
		eval(context);
	}
	
	protected UnsupportedOperationException error(String operator, Value right) {
		return new UnsupportedOperationException("%s %s %s".formatted(this, operator, right));
	}

	public Value plus(Value right) { throw new UnsupportedOperationException(); }
}
