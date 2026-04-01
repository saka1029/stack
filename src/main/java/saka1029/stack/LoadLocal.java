package saka1029.stack;

public class LoadLocal extends Load {

    public final int offset;

    public LoadLocal(Symbol symbol, int offset) {
        super(symbol);
        this.offset = offset;
    }

    @Override
    public void execute(Context context) {
        context.loadLocal(offset);
    }

    // @Override
    // public String toString() {
    //     return "%s@%d".formatted(symbol, offset);
    // }

}
