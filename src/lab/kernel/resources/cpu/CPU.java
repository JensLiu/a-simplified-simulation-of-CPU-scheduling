package lab.kernel.resources.cpu;

import lab.kernel.pcb.TaskStruct;

public class CPU {
    private int __cpuid; // for multi core cpu
    private TaskStruct __curTask;

    // registers
    int __ip; // instruction pointer

    /**
     * CPU executes one instruction (ip is the virtual address
     * @return
     */
    public boolean execOneInstruction() {
        String code = __curTask.getProgramme().getInstructionAt(__ip);
        if (code.length() == 0)
            return false;
        MachineInstruction instruction = MachineInstruction.parseToMachineCode(code);
        MachineInstructionParser.executeOneInstruction(this, instruction);
        return true;
    }

    /**
     * CPU stores current context to the given pcb
     * @param task
     * @return
     */
    public boolean storeContext(TaskStruct task) {
        task.setInstructionPointer(__ip);
        return true;
    }

    /**
     * CPU save the context of current executing process to its pcb
     * @return
     */
    public boolean saveContext() {
        storeContext(__curTask);
        return true;
    }

    /**
     * CPU switch to its next task by recovering the next task's context
     * @param task
     * @return
     */
    public boolean switchToTask(TaskStruct task) {
        __ip = task.getInstructionPointer();
        return true;
    }

    public TaskStruct getCurTask() {
        return __curTask;
    }

    public int getIp() {
        return __ip;
    }

    public boolean setIp(int ip) {
        __ip = ip;
        return true;
    }

    public void runCPU() {

    }


    // ----------------------------------- cpu execution at machine level ---------------------------------------------
    void __doAddOp(String src, String dst) {
        System.out.println("add " + src + " " + dst);
        __ip++; // points to next instruction
    }

    void __doMovOp(String src, String dst) {
        System.out.println("mov " + src + " " + dst);
        __ip++; // points to next instruction
    }

    void __doJmpOp(int addr) {
        System.out.println("jmp " + addr);
        __ip = addr;
    }

}
