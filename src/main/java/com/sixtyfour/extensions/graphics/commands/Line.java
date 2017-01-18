package com.sixtyfour.extensions.graphics.commands;

import com.sixtyfour.extensions.graphics.GraphicsDevice;
import com.sixtyfour.parser.Atom;
import com.sixtyfour.system.BasicProgramCounter;
import com.sixtyfour.system.Machine;
import com.sixtyfour.util.VarUtils;

/**
 * The LINE command
 * 
 * @author EgonOlsen
 * 
 */
public class Line extends AbstractGraphicsCommand {

	public Line() {
		super("LINE");
	}

	@Override
	public String parse(String linePart, int lineCnt, int lineNumber, int linePos, boolean lastPos, Machine machine) {
		return super.parse(linePart, lineCnt, lineNumber, linePos, lastPos, machine, 4, 0);
	}

	@Override
	public BasicProgramCounter execute(Machine machine) {
		Atom xs = pars.get(0);
		checkType(xs);
		Atom ys = pars.get(1);
		checkType(ys);
		Atom xe = pars.get(2);
		checkType(xe);
		Atom ye = pars.get(3);
		checkType(ye);
		GraphicsDevice window = GraphicsDevice.getDevice(machine);
		if (window != null) {
			window.line(VarUtils.getInt(xs.eval(machine)), VarUtils.getInt(ys.eval(machine)), VarUtils.getInt(xe.eval(machine)), VarUtils.getInt(ye.eval(machine)));
		}
		return null;
	}
}