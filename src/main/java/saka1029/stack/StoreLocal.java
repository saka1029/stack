package saka1029.stack;

public class StoreLocal extends Store {

    final int offset;

    StoreLocal(Symbol symbol, int offset) {
        super(symbol);
        this.offset = offset;
    }

    @Override
    public void execute(Context c) {
        c.stack[c.bp + offset] = c.pop();
    }

    // @Override
    // public String toString() {
    //     return "@%s@%d".formatted(symbol, offset);
    // }
}
