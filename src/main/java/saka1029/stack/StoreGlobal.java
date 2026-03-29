package saka1029.stack;

public class StoreGlobal extends Store {

    public StoreGlobal(Symbol symbol) {
        super(symbol);
    }
    
    @Override
    public void execute(Context c) {
        c.variable(symbol, c.pop());
    }
}
