package saka1029.stack;

public interface Value extends Element {

    @Override
    default void execute(Context context) {
        context.push(this);
    }
}
