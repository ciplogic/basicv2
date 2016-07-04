/*
 * 
 */
package sixtyfour.elements.commands;

import sixtyfour.plugins.DeviceProvider;
import sixtyfour.system.Machine;
import sixtyfour.system.ProgramCounter;
import sixtyfour.util.VarUtils;

/**
 * The Class Verify.
 */
public class Verify extends FileOperation {

	/**
	 * Instantiates a new verify.
	 */
	public Verify() {
		super("VERIFY");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * sixtyfour.elements.commands.AbstractCommand#execute(sixtyfour.system.
	 * Machine)
	 */
	@Override
	public ProgramCounter execute(Machine machine) {
		DeviceProvider device = machine.getDeviceProvider();

		try {
			switch (pars.size()) {
			case 0:
				device.verify("", 1, 0);
				break;
			case 1:
				device.verify((String) pars.get(0).eval(machine), 1, 0);
				break;
			case 2:
				device.verify((String) pars.get(0).eval(machine), VarUtils.getInt(pars.get(1).eval(machine)), 0);
				break;
			case 3:
				device.verify((String) pars.get(0).eval(machine), VarUtils.getInt(pars.get(1).eval(machine)), VarUtils.getInt(pars.get(2).eval(machine)));
				break;
			default:
				throw new RuntimeException("Syntax error: " + this);
			}
		} catch (ClassCastException e) {
			throw new RuntimeException("Syntax error: " + this);
		}

		return null;
	}

}