package saka1029.stack;

public class Value implements Evaluable {

	@Override
	public void eval(Context context) {
		context.push(this);
	}
	
	public void evlis(Context context) {
		eval(context);
	}

}
