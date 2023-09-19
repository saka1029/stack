package saka1029.type;

import java.util.logging.Logger;

import saka1029.Common;

public class InstructionType {
	
	static final Logger logger = Common.logger(InstructionType.class);

    final TypeList in, out;

    InstructionType(TypeList in, TypeList out) {
        this.in = in;
        this.out = out;
    }
    
    public static InstructionType of(TypeList in, TypeList out) {
        return new InstructionType(in, out);
    }
    
    public InstructionType combine(InstructionType right) {
        int commonSize = Math.min(out.size(), right.in.size());
        if (commonSize == 0)
            return InstructionType.of(in, right.out);
        BindMap map = BindMap.of();
        if (!out.right(commonSize).matches(right.in.right(commonSize), map))
            return null;
        TypeList i = right.in.size() > commonSize ? right.in.left(right.in.size() - commonSize).append(in) : in;
        TypeList o = out.size() > commonSize ? out.left(out.size() - commonSize).append(right.out) : right.out;
        InstructionType result = InstructionType.of(i.resolve(map), o.resolve(map));
        logger.info("%s ◦ %s => %s : %s".formatted(this, right, result, map));
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
    	return obj instanceof InstructionType r && r.in.equals(in) && r.out.equals(out);
    }
    
    @Override
    public String toString() {
        return "%s -> %s".formatted(in, out);
    }
}
