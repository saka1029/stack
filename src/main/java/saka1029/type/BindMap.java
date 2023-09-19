package saka1029.type;

import java.util.LinkedHashMap;
import java.util.Map;

public class BindMap {
	
	final Map<VariableType, Type> map = new LinkedHashMap<>();
	
	public static BindMap of() {
	    return new BindMap();
	}

	public Type get(VariableType key) {
		return map.get(key);
	}

	public boolean bind(VariableType key, Type value) {
		Type old = map.get(key);
		if (old != null && !old.equals(value))
			return false;
		map.put(key, value);
		return true;
	}

	@Override
	public String toString() {
		return map.toString();
	}
}
