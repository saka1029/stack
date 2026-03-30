package saka1029.stack;

public class LoadLocalDummy implements Instruction {

    public final Symbol symbol;
    public final int offset;

    public LoadLocalDummy(Symbol symbol, int offset) {
        this.symbol = symbol;
        this.offset = offset;
    }

    @Override
    public void execute(Context context) {
        // do nothing
    }

    @Override
    public String toString() {
        return "%s@%d".formatted(symbol, offset);
    }

}
