package com.sixtyfour.system;

public class Cpu {
	private int acc;
	private int x;
	private int y;
	private int status = 0;
	private int stackPointer = 0xff;
	private boolean exitOnBreak = true;

	public int getAcc() {
		return acc;
	}

	public void setAcc(int acc) {
		this.acc = acc;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStackPointer() {
		return stackPointer;
	}

	public void setStackPointer(int stackPointer) {
		this.stackPointer = stackPointer;
	}

	public void execute(Machine machine, Program prg) {
		execute(machine, prg, null);
	}

	public void execute(Machine machine, Program prg, CommandListener commandListener) {
		machine.loadProgram(prg);
		int[] ram = machine.getRam();
		int pc = prg.getAddress();
		boolean brk = false;
		push(machine, 0);
		push(machine, 0);

		do {
			int cmd = ram[pc++];
			int xb = x & 0xff;
			int yb = y & 0xff;
			int accb = acc & 0xff;
			int spb = stackPointer & 0xff;
			int ac = status & 1;
			int index = 0;
			int tmp = 0;
			int lo = 0;
			int hi = 0;

			boolean decimal = (status & 0b00001000) > 0;

			switch (cmd) {

			case 0x60:
				// RTS
				int addr = getWord(pop(machine), pop(machine));
				if (addr == 0) {
					brk = true;
				} else {
					pc = ++addr;
				}
				break;
			case 0x00:
				// BRK
				pc++;
				push(machine, getHigh(pc));
				push(machine, getLow(pc));
				push(machine, status);
				pc = getWord(ram[0xFFFE], ram[0xFFFF]);
				brk = exitOnBreak;
				break;
			case 0xA9:
				// LDA #$nn
				acc = getWord(ram[pc++], ram[pc++]);
				setFlags(acc, true, true);
				break;
			case 0xAD:
				// LDA $hhll
				acc = ram[getWord(ram[pc++], ram[pc++])];
				setFlags(acc, true, true);
				break;
			case 0xBD:
				// LDA $hhll,X
				acc = ram[getWord(ram[pc++], ram[pc++]) + xb];
				setFlags(acc, true, true);
				break;
			case 0xB9:
				// LDA $hhll,Y
				acc = ram[getWord(ram[pc++], ram[pc++]) + yb];
				setFlags(acc, true, true);
				break;
			case 0xA5:
				// LDA $ll
				acc = ram[ram[pc++] & 0xff];
				setFlags(acc, true, true);
				break;
			case 0xB5:
				// LDA $ll,X
				acc = ram[(ram[pc++] & 0xff) + xb];
				setFlags(acc, true, true);
				break;
			case 0xA1:
				// LDA ($ll, X)
				acc = ram[ram[getWord(ram[pc++] + xb, ram[pc] + xb + 1)]];
				setFlags(acc, true, true);
				break;
			case 0xB1:
				// LDA ($ll), Y
				acc = ram[ram[(getWord(ram[pc++], ram[pc] + 1)) + y]];
				setFlags(acc, true, true);
				break;
			case 0xA2:
				// LDX #$nn
				x = getWord(ram[pc++], ram[pc++]);
				setFlags(x, true, true);
				break;
			case 0xAE:
				// LDX $hhll
				x = ram[getWord(ram[pc++], ram[pc++])];
				setFlags(x, true, true);
				break;
			case 0xBE:
				// LDX $hhll,Y
				x = ram[getWord(ram[pc++], ram[pc++]) + yb];
				setFlags(x, true, true);
				break;
			case 0xA6:
				// LDX $ll
				x = ram[ram[pc++] & 0xff];
				setFlags(x, true, true);
				break;
			case 0xB6:
				// LDX $ll,Y
				x = ram[(ram[pc++] & 0xff) + yb];
				setFlags(x, true, true);
				break;
			case 0xA0:
				// LDY #$nn
				y = getWord(ram[pc++], ram[pc++]);
				setFlags(y, true, true);
				break;
			case 0xAC:
				// LDY $hhll
				y = ram[getWord(ram[pc++], ram[pc++])];
				setFlags(y, true, true);
				break;
			case 0xBC:
				// LDY $hhll,X
				y = ram[getWord(ram[pc++], ram[pc++]) + xb];
				setFlags(y, true, true);
				break;
			case 0xA4:
				// LDY $ll
				y = ram[ram[pc++] & 0xff];
				setFlags(y, true, true);
				break;
			case 0xB4:
				// LDY $ll,X
				y = ram[(ram[pc++] & 0xff) + xb];
				setFlags(y, true, true);
				break;
			case 0x8D:
				// STA $hhll
				ram[getWord(ram[pc++], ram[pc++])] = accb;
				break;
			case 0x9D:
				// STA $hhll,X
				ram[getWord(ram[pc++], ram[pc++]) + xb] = accb;
				break;
			case 0x99:
				// STA $hhll,Y
				ram[getWord(ram[pc++], ram[pc++]) + yb] = accb;
				break;
			case 0x85:
				// STA $ll
				ram[ram[pc++] & 0xff] = accb;
				break;
			case 0x95:
				// LDA $ll,X
				ram[(ram[pc++] & 0xff) + xb] = accb;
				break;
			case 0x81:
				// STA ($ll, X)
				ram[ram[getWord(ram[pc++] + xb, ram[pc] + xb + 1)]] = accb;
				break;
			case 0x91:
				// STA ($ll), Y
				ram[ram[(getWord(ram[pc++], ram[pc] + 1)) + y]] = accb;
				break;
			case 0x8E:
				// STX $hhll
				ram[getWord(ram[pc++], ram[pc++])] = xb;
				break;
			case 0x86:
				// STX $ll
				ram[ram[pc++] & 0xff] = xb;
				break;
			case 0x96:
				// STX $ll,Y
				ram[(ram[pc++] & 0xff) + yb] = xb;
				break;
			case 0x8C:
				// STY $hhll
				ram[getWord(ram[pc++], ram[pc++])] = yb;
				break;
			case 0x84:
				// STY $ll
				ram[ram[pc++] & 0xff] = yb;
				break;
			case 0x94:
				// STY $ll,X
				ram[(ram[pc++] & 0xff) + xb] = yb;
				break;
			case 0xAA:
				// TAX
				x = accb;
				setFlags(x, true, true);
				break;
			case 0xA8:
				// TAY
				y = accb;
				setFlags(y, true, true);
				break;
			case 0x8A:
				// TXA
				acc = xb;
				setFlags(acc, true, true);
				break;
			case 0x98:
				// TYA
				acc = yb;
				setFlags(acc, true, true);
				break;
			case 0xBA:
				// TSX
				x = spb;
				setFlags(spb, true, true);
				break;
			case 0x9A:
				// TXS
				stackPointer = xb;
				break;
			case 0x29:
				// AND #$nn
				acc &= getWord(ram[pc++], ram[pc++]);
				setFlags(acc, true, true);
				break;
			case 0x2D:
				// AND $hhll
				acc &= ram[getWord(ram[pc++], ram[pc++])];
				setFlags(acc, true, true);
				break;
			case 0x3D:
				// AND $hhll,X
				acc &= ram[getWord(ram[pc++], ram[pc++]) + xb];
				setFlags(acc, true, true);
				break;
			case 0x39:
				// AND $hhll,Y
				acc &= ram[getWord(ram[pc++], ram[pc++]) + yb];
				setFlags(acc, true, true);
				break;
			case 0x25:
				// AND $ll
				acc &= ram[ram[pc++] & 0xff];
				setFlags(acc, true, true);
				break;
			case 0x35:
				// AND $ll,X
				acc &= ram[(ram[pc++] & 0xff) + xb];
				setFlags(acc, true, true);
				break;
			case 0x21:
				// AND ($ll, X)
				acc &= ram[ram[getWord(ram[pc++] + xb, ram[pc] + xb + 1)]];
				setFlags(acc, true, true);
				break;
			case 0x31:
				// AND ($ll), Y
				acc &= ram[ram[(getWord(ram[pc++], ram[pc] + 1)) + y]];
				setFlags(acc, true, true);
				break;
			case 0x09:
				// OR #$nn
				acc |= getWord(ram[pc++], ram[pc++]);
				setFlags(acc, true, true);
				break;
			case 0x0D:
				// OR $hhll
				acc |= ram[getWord(ram[pc++], ram[pc++])];
				setFlags(acc, true, true);
				break;
			case 0x1D:
				// OR $hhll,X
				acc |= ram[getWord(ram[pc++], ram[pc++]) + xb];
				setFlags(acc, true, true);
				break;
			case 0x19:
				// OR $hhll,Y
				acc |= ram[getWord(ram[pc++], ram[pc++]) + yb];
				setFlags(acc, true, true);
				break;
			case 0x05:
				// OR $ll
				acc |= ram[ram[pc++] & 0xff];
				setFlags(acc, true, true);
				break;
			case 0x15:
				// OR $ll,X
				acc |= ram[(ram[pc++] & 0xff) + xb];
				setFlags(acc, true, true);
				break;
			case 0x01:
				// OR ($ll, X)
				acc |= ram[ram[getWord(ram[pc++] + xb, ram[pc] + xb + 1)]];
				setFlags(acc, true, true);
				break;
			case 0x11:
				// OR ($ll), Y
				acc |= ram[ram[(getWord(ram[pc++], ram[pc] + 1)) + y]];
				setFlags(acc, true, true);
				break;
			case 0x49:
				// EOR #$nn
				acc ^= getWord(ram[pc++], ram[pc++]);
				setFlags(acc, true, true);
				break;
			case 0x4D:
				// EOR $hhll
				acc ^= ram[getWord(ram[pc++], ram[pc++])];
				setFlags(acc, true, true);
				break;
			case 0x5D:
				// EOR $hhll,X
				acc ^= ram[getWord(ram[pc++], ram[pc++]) + xb];
				setFlags(acc, true, true);
				break;
			case 0x59:
				// EOR $hhll,Y
				acc ^= ram[getWord(ram[pc++], ram[pc++]) + yb];
				setFlags(acc, true, true);
				break;
			case 0x45:
				// EOR $ll
				acc ^= ram[ram[pc++] & 0xff];
				setFlags(acc, true, true);
				break;
			case 0x55:
				// EOR $ll,X
				acc ^= ram[(ram[pc++] & 0xff) + xb];
				setFlags(acc, true, true);
				break;
			case 0x41:
				// EOR ($ll, X)
				acc ^= ram[ram[getWord(ram[pc++] + xb, ram[pc] + xb + 1)]];
				setFlags(acc, true, true);
				break;
			case 0x51:
				// EOR ($ll), Y
				acc ^= ram[ram[(getWord(ram[pc++], ram[pc] + 1)) + y]];
				setFlags(acc, true, true);
				break;
			case 0x69:
				// ADC #$nn
				ac = status & 1;
				if (!decimal) {
					acc += getWord(ram[pc++], ram[pc++]) + ac;
				} else {
					acc = addBCD(addBCD(accb, ac), getWord(ram[pc++], ram[pc++]));
				}
				setFlags(acc, accb, true, true, true, true);
				acc &= 0xff;
				break;
			case 0x6D:
				// ADC $hhll
				ac = status & 1;
				if (!decimal) {
					acc += ram[getWord(ram[pc++], ram[pc++])] + ac;
				} else {
					acc = addBCD(addBCD(accb, ac), ram[getWord(ram[pc++], ram[pc++])]);
				}
				setFlags(acc, accb, true, true, true, true);
				acc &= 0xff;
				break;
			case 0x7D:
				// ADC $hhll,X
				ac = status & 1;
				if (!decimal) {
					acc += ram[getWord(ram[pc++], ram[pc++]) + xb] + ac;
				} else {
					acc = addBCD(addBCD(accb, ac), ram[getWord(ram[pc++], ram[pc++]) + xb]);
				}
				setFlags(acc, accb, true, true, true, true);
				acc &= 0xff;
				break;
			case 0x79:
				// ADC $hhll,Y
				ac = status & 1;
				if (!decimal) {
					acc += ram[getWord(ram[pc++], ram[pc++]) + yb] + ac;
				} else {
					acc = addBCD(addBCD(accb, ac), ram[getWord(ram[pc++], ram[pc++]) + yb]);
				}
				setFlags(acc, accb, true, true, true, true);
				acc &= 0xff;
				break;
			case 0x65:
				// ADC $ll
				ac = status & 1;
				if (!decimal) {
					acc += ram[ram[pc++] & 0xff] + ac;
				} else {
					acc = addBCD(addBCD(accb, ac), ram[ram[pc++] & 0xff]);
				}
				setFlags(acc, accb, true, true, true, true);
				acc &= 0xff;
				break;
			case 0x75:
				// ADC $ll,X
				ac = status & 1;
				if (!decimal) {
					acc += ram[(ram[pc++] & 0xff) + xb] + ac;
				} else {
					acc = addBCD(addBCD(accb, ac), ram[(ram[pc++] & 0xff) + xb]);
				}
				setFlags(acc, accb, true, true, true, true);
				acc &= 0xff;
				break;
			case 0x61:
				// ADC ($ll, X)
				ac = status & 1;
				if (!decimal) {
					acc += ram[ram[getWord(ram[pc++] + xb, ram[pc] + xb + 1)]] + ac;
				} else {
					acc = addBCD(addBCD(accb, ac), ram[ram[getWord(ram[pc++] + xb, ram[pc] + xb + 1)]]);
				}
				setFlags(acc, accb, true, true, true, true);
				acc &= 0xff;
				break;
			case 0x71:
				// ADC ($ll), Y
				ac = status & 1;
				if (!decimal) {
					acc += ram[ram[(getWord(ram[pc++], ram[pc] + 1)) + y]] + ac;
				} else {
					acc = addBCD(addBCD(accb, ac), ram[ram[(getWord(ram[pc++], ram[pc] + 1)) + y]]);
				}
				setFlags(acc, accb, true, true, true, true);
				acc &= 0xff;
				break;
			case 0xE9:
				// SBC #$nn
				ac = (~(status & 1)) & 1;
				if (!decimal) {
					acc -= getWord(ram[pc++], ram[pc++]) + ac;
				} else {
					acc = subBCD(subBCD(accb, ac), getWord(ram[pc++], ram[pc++]));
				}
				setFlags(acc, accb, true, true, true, true);
				acc &= 0xff;
				break;
			case 0xED:
				// SBC $hhll
				ac = (~(status & 1)) & 1;
				if (!decimal) {
					acc -= ram[getWord(ram[pc++], ram[pc++])] + ac;
				} else {
					acc = subBCD(subBCD(accb, ac), ram[getWord(ram[pc++], ram[pc++])]);
				}
				setFlags(acc, accb, true, true, true, true);
				acc &= 0xff;
				break;
			case 0xFD:
				// SBC $hhll,X
				ac = (~(status & 1)) & 1;
				if (!decimal) {
					acc -= ram[getWord(ram[pc++], ram[pc++]) + xb] + ac;
				} else {
					acc = subBCD(subBCD(accb, ac), ram[getWord(ram[pc++], ram[pc++]) + xb]);
				}
				setFlags(acc, accb, true, true, true, true);
				acc &= 0xff;
				break;
			case 0xF9:
				// SBC $hhll,Y
				ac = (~(status & 1)) & 1;
				if (!decimal) {
					acc -= ram[getWord(ram[pc++], ram[pc++]) + yb] + ac;
				} else {
					acc = subBCD(subBCD(accb, ac), ram[getWord(ram[pc++], ram[pc++]) + yb]);
				}
				setFlags(acc, accb, true, true, true, true);
				acc &= 0xff;
				break;
			case 0xE5:
				// SBC $ll
				ac = (~(status & 1)) & 1;
				if (!decimal) {
					acc -= ram[ram[pc++] & 0xff] + ac;
				} else {
					acc = subBCD(subBCD(accb, ac), ram[ram[pc++] & 0xff]);
				}
				setFlags(acc, accb, true, true, true, true);
				acc &= 0xff;
				break;
			case 0xF5:
				// SBC $ll,X
				ac = (~(status & 1)) & 1;
				if (!decimal) {
					acc -= ram[(ram[pc++] & 0xff) + xb] + ac;
				} else {
					acc = subBCD(subBCD(accb, ac), ram[(ram[pc++] & 0xff) + xb]);
				}
				setFlags(acc, accb, true, true, true, true);
				acc &= 0xff;
				break;
			case 0xE1:
				// SBC ($ll, X)
				ac = (~(status & 1)) & 1;
				if (!decimal) {
					acc -= ram[ram[getWord(ram[pc++] + xb, ram[pc] + xb + 1)]] + ac;
				} else {
					acc = subBCD(subBCD(accb, ac), ram[ram[getWord(ram[pc++] + xb, ram[pc] + xb + 1)]]);
				}
				setFlags(acc, accb, true, true, true, true);
				acc &= 0xff;
				break;
			case 0xF1:
				// SBC ($ll), Y
				ac = (~(status & 1)) & 1;
				if (!decimal) {
					acc -= ram[ram[(getWord(ram[pc++], ram[pc] + 1)) + y]] + ac;
				} else {
					acc = subBCD(subBCD(accb, ac), ram[ram[(getWord(ram[pc++], ram[pc] + 1)) + y]]);
				}
				setFlags(acc, accb, true, true, true, true);
				acc &= 0xff;
				break;
			case 0xEE:
				// INC $hhll
				index = getWord(ram[pc++], ram[pc++]);
				ram[index] = (ram[index] + 1) & 0xff;
				setFlags(ram[index], true, true);
				break;
			case 0xFE:
				// INC $hhll,X
				index = getWord(ram[pc++], ram[pc++]) + xb;
				ram[index] = (ram[index] + 1) & 0xff;
				setFlags(ram[index], true, true);
				break;
			case 0xE6:
				// INC $ll
				index = ram[pc++] & 0xff;
				ram[index] = (ram[index] + 1) & 0xff;
				setFlags(ram[index], true, true);
				break;
			case 0xF6:
				// INC $ll,X
				index = (ram[pc++] & 0xff) + xb;
				ram[index] = (ram[index] + 1) & 0xff;
				setFlags(ram[index], true, true);
				break;
			case 0xCE:
				// DEC $hhll
				index = getWord(ram[pc++], ram[pc++]);
				ram[index] = (ram[index] - 1) & 0xff;
				setFlags(ram[index], true, true);
				break;
			case 0xDE:
				// DEC $hhll,X
				index = getWord(ram[pc++], ram[pc++]) + xb;
				ram[index] = (ram[index] - 1) & 0xff;
				setFlags(ram[index], true, true);
				break;
			case 0xC6:
				// DEC $ll
				index = ram[pc++] & 0xff;
				ram[index] = (ram[index] - 1) & 0xff;
				setFlags(ram[index], true, true);
				break;
			case 0xD6:
				// DEC $ll,X
				index = (ram[pc++] & 0xff) + xb;
				ram[index] = (ram[index] - 1) & 0xff;
				setFlags(ram[index], true, true);
				break;
			case 0xE8:
				// INX
				x = (xb + 1) & 0xff;
				setFlags(x, true, true);
				break;
			case 0xC8:
				// INY
				y = (yb + 1) & 0xff;
				setFlags(y, true, true);
				break;
			case 0xCA:
				// DEX
				x = (xb - 1) & 0xff;
				setFlags(x, true, true);
				break;
			case 0x88:
				// DEY
				y = (yb - 1) & 0xff;
				setFlags(y, true, true);
				break;
			case 0x0A:
				// ASL
				acc = asl(accb);
				break;
			case 0x0E:
				// ASL $hhll
				index = getWord(ram[pc++], ram[pc++]);
				ram[index] = asl(ram[index]);
				break;
			case 0x1E:
				// ASL $hhll,X
				index = getWord(ram[pc++], ram[pc++]) + xb;
				ram[index] = asl(ram[index]);
				break;
			case 0x06:
				// ASL $ll
				index = ram[pc++] & 0xff;
				ram[index] = asl(ram[index]);
				break;
			case 0x16:
				// ASL $ll,X
				index = (ram[pc++] & 0xff) + xb;
				ram[index] = asl(ram[index]);
				break;
			case 0x4A:
				// LSR
				acc = lsr(accb);
				break;
			case 0x4E:
				// LSR $hhll
				index = getWord(ram[pc++], ram[pc++]);
				ram[index] = lsr(ram[index]);
				break;
			case 0x5E:
				// LSR $hhll,X
				index = getWord(ram[pc++], ram[pc++]) + xb;
				ram[index] = lsr(ram[index]);
				break;
			case 0x46:
				// LSR $ll
				index = ram[pc++] & 0xff;
				ram[index] = lsr(ram[index]);
				break;
			case 0x56:
				// LSR $ll,X
				index = (ram[pc++] & 0xff) + xb;
				ram[index] = lsr(ram[index]);
				break;
			case 0x2A:
				// ROL
				acc = rol(accb);
				break;
			case 0x2E:
				// ROL $hhll
				index = getWord(ram[pc++], ram[pc++]);
				ram[index] = rol(ram[index]);
				break;
			case 0x3E:
				// ROL $hhll,X
				index = getWord(ram[pc++], ram[pc++]) + xb;
				ram[index] = rol(ram[index]);
				break;
			case 0x26:
				// ROL $ll
				index = ram[pc++] & 0xff;
				ram[index] = rol(ram[index]);
				break;
			case 0x36:
				// ROL $ll,X
				index = (ram[pc++] & 0xff) + xb;
				ram[index] = rol(ram[index]);
				break;
			case 0x6A:
				// ROR
				acc = ror(accb);
				break;
			case 0x6E:
				// ROR $hhll
				index = getWord(ram[pc++], ram[pc++]);
				ram[index] = ror(ram[index]);
				break;
			case 0x7E:
				// ROR $hhll,X
				index = getWord(ram[pc++], ram[pc++]) + xb;
				ram[index] = ror(ram[index]);
				break;
			case 0x66:
				// ROR $ll
				index = ram[pc++] & 0xff;
				ram[index] = ror(ram[index]);
				break;
			case 0x76:
				// ROR $ll,X
				index = (ram[pc++] & 0xff) + xb;
				ram[index] = ror(ram[index]);
				break;
			case 0xC9:
				// CMP #$nn
				tmp = accb - getWord(ram[pc++], ram[pc++]);
				setFlags(tmp, accb, true, true, false, true);
				break;
			case 0xCD:
				// CMP $hhll
				tmp = accb - ram[getWord(ram[pc++], ram[pc++])];
				setFlags(tmp, accb, true, true, false, true);
				break;
			case 0xDD:
				// CMP $hhll,X
				tmp = accb - ram[getWord(ram[pc++], ram[pc++]) + xb];
				setFlags(tmp, accb, true, true, false, true);
				break;
			case 0xD9:
				// CMP $hhll,Y
				tmp = accb - ram[getWord(ram[pc++], ram[pc++]) + yb];
				setFlags(tmp, accb, true, true, false, true);
				break;
			case 0xC5:
				// CMP $ll
				tmp = accb - ram[ram[pc++] & 0xff];
				setFlags(tmp, accb, true, true, false, true);
				break;
			case 0xD5:
				// CMP $ll,X
				tmp = accb - ram[(ram[pc++] & 0xff) + xb];
				setFlags(tmp, accb, true, true, false, true);
				break;
			case 0xC1:
				// CMP ($ll, X)
				tmp = accb - ram[ram[getWord(ram[pc++] + xb, ram[pc] + xb + 1)]];
				setFlags(tmp, accb, true, true, false, true);
				break;
			case 0xD1:
				// CMP ($ll), Y
				tmp = accb - ram[ram[(getWord(ram[pc++], ram[pc] + 1)) + y]];
				setFlags(tmp, accb, true, true, false, true);
				break;
			case 0xE0:
				// CPX #$nn
				tmp = xb - getWord(ram[pc++], ram[pc++]);
				setFlags(tmp, xb, true, true, false, true);
				break;
			case 0xEC:
				// CPX $hhll
				tmp = xb - ram[getWord(ram[pc++], ram[pc++])];
				setFlags(tmp, xb, true, true, false, true);
				break;
			case 0xE4:
				// CPX $ll
				tmp = xb - ram[ram[pc++] & 0xff];
				setFlags(tmp, xb, true, true, false, true);
				break;
			case 0xC0:
				// CPY #$nn
				tmp = yb - getWord(ram[pc++], ram[pc++]);
				setFlags(tmp, yb, true, true, false, true);
				break;
			case 0xCC:
				// CPY $hhll
				tmp = yb - ram[getWord(ram[pc++], ram[pc++])];
				setFlags(tmp, yb, true, true, false, true);
				break;
			case 0xC4:
				// CPY $ll
				tmp = yb - ram[ram[pc++] & 0xff];
				setFlags(tmp, yb, true, true, false, true);
				break;
			case 0x2C:
				// BIT $hhll
				tmp = ram[getWord(ram[pc++], ram[pc++])] & 0xFF;
				status = (status & 0b00111111) | (tmp & 0b11000000);
				tmp &= accb;
				setFlags(tmp, false, true);
				break;
			case 0x24:
				// BIT $ll
				tmp = ram[ram[pc++] & 0xff] & 0xFF;
				status = (status & 0b00111111) | (tmp & 0b11000000);
				tmp &= accb;
				setFlags(tmp, false, true);
				break;
			case 0x4C:
				// JMP $hhll
				pc = ram[getWord(ram[pc++], ram[pc++])];
				break;
			case 0x6C:
				// JMP ($hhll)
				lo = ram[pc++];
				hi = ram[pc++];
				tmp = ram[getWord(lo, hi)];
				lo = (lo + 1) & 0xff;
				pc = getWord(tmp, ram[getWord(lo, hi)]);
				break;
			case 0x20:
				// JSR $hhll
				tmp = ram[getWord(ram[pc], ram[++pc])];
				push(machine, getHigh(pc));
				push(machine, getLow(pc));
				pc = tmp;
				break;

			default:
				throw new RuntimeException("Illegal opcode: " + cmd);
			}

		} while (!brk);

		if (commandListener != null) {
			commandListener.commandExecuted(pc);
		}
	}

	private int asl(int a) {
		a &= 0xFF;
		a = a << 1;
		int cf = (a & 0b0000000100000000) >> 8;
		a &= 0xFF;
		status = (status & 0b11111110) | cf;
		setFlags(a, true, true);
		return a;
	}

	private int rol(int a) {
		a &= 0xFF;
		int cf = (a & 0b0000000010000000) >> 7;
		a = (a << 1) | (status & 1);
		a &= 0xFF;
		status = (status & 0b11111110) | cf;
		setFlags(a, true, true);
		return a;
	}

	private int lsr(int a) {
		a &= 0xFF;
		int cf = a & 1;
		a = a >> 1;
		status = (status & 0b11111110) | cf;
		setFlags(a, true, true);
		return a;
	}

	private int ror(int a) {
		a &= 0xFF;
		int cf = a & 1;
		a = (a >> 1) | ((status & 1) << 7);
		status = (status & 0b11111110) | cf;
		setFlags(a, true, true);
		return a;
	}

	private int addBCD(int a, int b) {
		int o1 = convertFromBCD(a);
		int o2 = convertFromBCD(b);
		int r = o1 + o2;
		return convertToBCD(r);
	}

	private int subBCD(int a, int b) {
		int o1 = convertFromBCD(a);
		int o2 = convertFromBCD(b);
		int r = o1 - o2;
		return convertToBCD(r);
	}

	private int convertToBCD(int r) {
		return (r % 10) + ((r / 10) << 4);
	}

	private int convertFromBCD(int a) {
		return (a & 0b00001111) + 10 * ((a & 0b11110000) >> 4);
	}

	private void setFlags(int a, boolean negative, boolean zero) {
		setFlags(a, a, negative, zero, false, false);
	}

	private void setFlags(int a, int oldA, boolean negative, boolean zero, boolean overflow, boolean carry) {
		if (negative) {
			status = (status & 0b01111111) | (a & 0b10000000);
		}
		if (zero) {
			int zeroFlag = (a & 0xff) == 0 ? 0 : 0b10;
			status = (status & 0b01111111) | zeroFlag;
		}
		if (overflow) {
			int ov = (oldA >= -128 && a < 127) || (oldA <= 127 && a > 127) ? 0b01000000 : 0;
			status = (status & 0b10111111) | ov;
		}
		if (carry) {
			if (a > oldA) {
				boolean decimal = (status & 0b00001000) > 0;
				int cf = decimal ? (a > 0b10011001 ? 1 : 0) : (a > 255 ? 1 : 0);
				status = (status & 0b11111110) | cf;
			} else if (a < oldA) {
				int cf = (a < 0 ? 0 : 1);
				status = (status & 0b11111110) | cf;
			}
		}
	}

	private int getLow(int val) {
		return val % 256;
	}

	private int getHigh(int val) {
		return val / 256;
	}

	private int getWord(int lo, int hi) {
		return (hi & 0xff) << 8 | (lo & 0xff);
	}

	private int pop(Machine machine) {
		int[] ram = machine.getRam();
		int val = ram[0x100 + stackPointer];
		stackPointer++;
		return val;
	}

	private void push(Machine machine, int value) {
		int[] ram = machine.getRam();
		ram[0x100 + stackPointer] = value & 0xff;
		stackPointer--;
	}

	public boolean isExitOnBreak() {
		return exitOnBreak;
	}

	public void setExitOnBreak(boolean exitOnBreak) {
		this.exitOnBreak = exitOnBreak;
	}
}