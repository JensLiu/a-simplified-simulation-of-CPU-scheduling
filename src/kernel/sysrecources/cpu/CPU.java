package kernel.sysrecources.cpu;

import kernel.process.SysProcess;
import kernel.programme.Programme;

public class CPU {
    private int __cpuid; // for multi core cpu
    private SysProcess __curProc;

    // registers
    int __ip; // instruction pointer, lets pretend that each instruction has the same length
              // and use the index of the instruction array (an array of strings) as __ip
    int[] __commRegs = new int[10]; // %r0 to %r10
    int[] __flagRegs = new int[1]; // flag registers

    /**
     * CPU executes one instruction (ip is the virtual address
     * @return
     */
    public boolean execOneInstruction() {
        MachineInstruction instruction = __curProc.getInstructionAt(__ip);
        if (instruction == null)
            return false;
        MachineInstructionExecutor.executeOneInstruction(this, instruction);
        return true;
    }

    /**
     * CPU stores current context to the given pcb
     * @param proc
     * @return
     */
    public boolean storeContext(SysProcess proc) {
        proc.setInstructionPointer(__ip);
        return true;
    }

    /**
     * CPU save the context of current executing process to its pcb
     * @return
     */
    public boolean saveContext() {
        storeContext(__curProc);
        return true;
    }

    /**
     * CPU switch to its next task by recovering the next task's context
     * @param task
     * @return
     */
    public boolean switchToTask(SysProcess task) {
        // setup context
        __ip = task.getInstructionPointer();

        // change the reference
        __curProc = task;
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

//    public void test_execProgramme(Programme p) {
//        __curProc = SysProcess.getInstance(p);
//        while (execOneInstruction())
//            ;
//    }


    // ----------------------------------- cpu execution at machine level ---------------------------------------------
    void __doAddOp(String dst, String src) {
        System.out.println("[CPU To Execute]: add " + src + " " + dst);
        int srcRegNo = __parseRegister(src);
        int dstRegNo = __parseRegister(dst);
        __commRegs[dstRegNo] = __commRegs[srcRegNo] + __commRegs[dstRegNo];
        System.out.println("[CPU ADD]: %r" + dst + " <- " + __commRegs[dstRegNo]);
        __ip++; // points to next instruction
    }

    void __doMovOp(String dst, String src) {
        System.out.println("[CPU To Execute]: mov " + src + " " + dst);

        int srcType = __whichTypeOfArg(src);
        int dstType = __whichTypeOfArg(dst);

        if (srcType == IMMEDNUM && dstType == REGISTER) { // supports moving a immediate number to a register
            int dstRegNo = __parseRegister(dst);
            __commRegs[dstRegNo] = __parseImmediateNumber(src);
            System.out.println("[CPU MOV]: %r" + dstRegNo + " <- " + __commRegs[dstRegNo]);
        }
        __ip++; // points to next instruction
    }

    void __doJmpOp(String addrString) {
        System.out.println("[CPU To Execute]: jmp " + addrString);
        try {
            int addr = Integer.parseInt(addrString);
            __ip = addr;
        } catch (Exception e) {
            System.out.println("[CPU ERROR] invalid address");
        }
    }

    void __doDispOp(String src) {
        int commRegNo = __parseRegister(src);
        System.out.println("[CPU Instruction]: disp %r" + commRegNo);
        System.out.println(__commRegs[commRegNo]);
        __ip++;
    }

    final int IMMEDNUM = 0;
    final int REGISTER = 1;
    final int MEMADDR = 2;
//    final int
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
//                e.printStackTrace();
                return -1;
            }
        }
        return -1;
    }

}
