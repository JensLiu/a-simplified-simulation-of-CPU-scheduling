package kernel.sysrecources.cpu;

public class MachineInstructionExecutor {

    /**
     * parse machine code
     * @param cpu
     * @param code
     */
    public static void executeOneInstruction(CPU cpu, MachineInstruction code) {

        System.out.println("[Machine Code Executor]: execute " + code.toString());

        switch (code.op) {
            case MachineInstruction.ADD: {
                cpu.__doAddOp(code.dst, code.src);
                break;
            }
            case MachineInstruction.MOV: {
                cpu.__doMovOp(code.dst, code.src);
                break;
            }
            case MachineInstruction.DISP: {
                cpu.__doDispOp(code.src);
            }
            case MachineInstruction.NOP: {
                cpu.__doNop();
            }
            case MachineInstruction.JMP: {
                cpu.__doJmpOp(code.src);
            }
        }
    }
}
