package saka1029.stack;

public interface Value extends Instruction{

    @Override
    default void execute(Context context) {
        context.push(this);
    }
}
