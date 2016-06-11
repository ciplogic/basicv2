package sixtyfour.elements.functions;

import sixtyfour.Machine;
import sixtyfour.elements.Type;

public class Sin extends AbstractFunction {

	public Sin() {
		super("SIN");
	}

	@Override
	public Type getType() {
		return term.getType();
	}

	@Override
	public Object eval(Machine memory) {
		if (!getType().equals(Type.STRING)) {
			return Float.valueOf((float) Math.sin(((Number) term.eval(memory)).floatValue()));
		}
		throw new RuntimeException("Type mismatch error: " + getType());
	}

}
