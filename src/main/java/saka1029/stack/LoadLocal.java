package saka1029.stack;

public class LoadLocal implements Instruction {

    public final Symbol symbol;
    public final int offset;

    public LoadLocal(Symbol symbol, int offset) {
        this.symbol = symbol;
        this.offset = offset;
    }

    @Override
    public void execute(Context context) {
        context.loadLocal(offset);
    }

    @Override
    public String toString() {
        return "%s@%d".formatted(symbol, offset);
    }

}
