package kernel.sysrecources.cpu;

import kernel.process.SysProcess;
import kernel.programme.Programme;

public class CPU {
    private int __cpuid; // for multi core cpu
    private SysProcess __curProc;

    public static final int N_COMMREGS = 10;
    public static final int N_RLAGREGS = 1;

    // registers
    int __ip; // instruction pointer, lets pretend that each instruction has the same length
              // and use the index of the instruction array (an array of strings) as __ip
    int[] __commRegs = new int[CPU.N_COMMREGS]; // %r0 to %r10
    int[] __flagRegs = new int[CPU.N_RLAGREGS]; // flag registers

    /**
     * CPU executes one instruction (ip is the virtual address
     * @return
     */
    public boolean execOneInstruction() {
//        System.out.println("[CPU]: to execute instruction for process " + __curProc);
        MachineInstruction instruction = __curProc.getInstructionAt(__ip);
        if (instruction == null)
            return false;
        __executeOneInstruction(instruction);
//        System.out.println("[CPU]: end of execution for process " + __curProc);
        return true;
    }

    /**
     * CPU save the context of current executing process to its pcb
     * @return
     */
    public boolean saveContext() {
        __storeContext(__curProc);
        return true;
    }

    /**
     * CPU switch to its next task by recovering the next task's context
     * @param proc
     * @return
     */
    public boolean switchTo(SysProcess proc) {
        // setup context
        __recoverContext(proc);

        // change the reference
        __curProc = proc;
        return true;
    }

    public void removeCurProc() {
        __curProc = null;
    }

    /**
     * CPU stores current context to the given pcb
     * @param proc
     * @return
     */
    private boolean __storeContext(SysProcess proc) {
        proc.setInstructionPointer(__ip);
        int[] pCommRegs = proc.getCommRegs();
        int[] pFlagRegs = proc.getFlagRegs();
        for (int i = 0; i < CPU.N_COMMREGS; i++) {
            pCommRegs[i] = __commRegs[i];
        }
        for (int i = 0; i < CPU.N_RLAGREGS; i++) {
            pFlagRegs[i] = __flagRegs[i];
        }
        return true;
    }


    private boolean __recoverContext(SysProcess proc) {
        __ip = proc.getInstructionPointer();
        int[] pCommRegs = proc.getCommRegs();
        int[] pFlagRegs = proc.getFlagRegs();
        for (int i = 0; i < CPU.N_COMMREGS; i++) {
            __commRegs[i] = pCommRegs[i];
        }
        for (int i = 0; i < CPU.N_RLAGREGS; i++) {
            __flagRegs[i] = pFlagRegs[i];
        }
        return true;
    }

    public SysProcess getCurProc() {
        return __curProc;
    }

    public int getIp() {
        return __ip;
    }

    public boolean setIp(int ip) {
        __ip = ip;
        return true;
    }

    public void __executeOneInstruction(MachineInstruction code) {
        switch (code.op) {
            case MachineInstruction.ADD: {
                __doAddOp(code.src, code.dst);
                break;
            }
            case MachineInstruction.MOV: {
                __doMovOp(code.src, code.dst);
                break;
            }
            case MachineInstruction.DISP: {
                __doDispOp(code.src);
                break;
            }
            case MachineInstruction.NOP: {
                __doNop();
                break;
            }
            case MachineInstruction.JMP: {
                __doJmpOp(code.src);
                break;
            }
        }
    }

    // ----------------------------------- cpu execution ---------------------------------------------
    void __doAddOp(String src, String dst) {
//        System.out.println("[CPU To Execute]: add " + src + " " + dst);
        int srcRegNo = __parseRegister(src);
        int dstRegNo = __parseRegister(dst);
        __commRegs[dstRegNo] = __commRegs[srcRegNo] + __commRegs[dstRegNo];
        System.out.println(__curProc.hashCode() + "\t[CPU ADD]: " + dst + " <- " + __commRegs[dstRegNo]);
        __ip++; // points to next instruction
    }

    void __doMovOp(String src, String dst) {
//        System.out.println("[CPU To Execute]: mov " + src + " " + dst);
        int srcType = __whichTypeOfArg(src);
        int dstType = __whichTypeOfArg(dst);

        if (srcType == IMMEDNUM && dstType == REGISTER) { // supports moving an immediate number to a register
            int dstRegNo = __parseRegister(dst);
            __commRegs[dstRegNo] = __parseImmediateNumber(src);
            System.out.println(__curProc.hashCode() + "\t[CPU MOV]: %r" + dstRegNo + " <- " + __commRegs[dstRegNo]);
        }
        __ip++; // points to next instruction
    }

    void __doJmpOp(String addrString) {
//        System.out.println("[CPU To Execute]: jmp " + addrString);
        try {
            int addr = Integer.parseInt(addrString);
            __ip = addr;
            System.out.println(__curProc.hashCode() + "\t[CPU JMP]: jmp " + addr);
        } catch (Exception e) {
            System.out.println(__curProc.hashCode() + "\t[CPU ERROR] invalid address");
        }
    }

    void __doDispOp(String src) {
        int commRegNo = __parseRegister(src);
        System.out.println(__curProc.hashCode() + "\t[CPU Instruction]: disp %r" + commRegNo);
        System.out.println(__curProc.hashCode() + "\t" + __commRegs[commRegNo]);
        __ip++;
    }

    void __doNop() {
        System.out.println(__curProc.hashCode() + "\t[CPU Instruction]: nop");
        __ip++;
    }

    final int IMMEDNUM = 0;
    final int REGISTER = 1;
    final int MEMADDR = 2;

    private static int __whichTypeOfArg(String arg) {
        if (arg.startsWith("$")) // immediate number
            return 0;
        else if (arg.startsWith("%")) // register
            return 1;
        else if (arg.startsWith("0x")) // mem addr
            return 2;
        else if (arg.charAt(0) >= '0' && arg.charAt(0) <= '9')
            return 3;
        else
            return -1;
    }

    private static Integer __parseImmediateNumber(String num) {
        try {
            String[] numSplit = num.split("\\$");
            return Integer.parseInt(numSplit[1]);
        } catch (Exception e) {
            return null;
        }
    }

    private static int __parseRegister(String reg) {
        if (reg.startsWith("%r")) { // common registers
            String[] regSplit = reg.split("%r");
            try {
                return Integer.parseInt(regSplit[1]);
            } catch (Exception e) {
                return -1;
            }
        }
        return -1;
    }

}
