package saka1029.type;

public interface Type {
    
    public static boolean matches(Type left, Type right, BindMap bind) {
        if (left instanceof VariableType v)
            return bind.bind(v, right);
        else if (right instanceof VariableType)
            return matches(right, left, bind);
        else
            return left.equals(right);
    }
}
