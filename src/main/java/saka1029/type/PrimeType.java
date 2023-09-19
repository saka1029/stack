package saka1029.type;

public class PrimeType implements Type {

    final String name;
    
    PrimeType(String name) {
        this.name = name;
    }
    
    public static PrimeType of(String name) {
        return new PrimeType(name);
    }
    
    @Override
    public String toString() {
        return name;
    }
}
