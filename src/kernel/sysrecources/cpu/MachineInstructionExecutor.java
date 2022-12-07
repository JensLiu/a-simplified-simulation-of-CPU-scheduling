package kernel.sysrecources.cpu;

public class MachineInstructionExecutor {

    public static void executeOneInstruction(CPU cpu, MachineInstruction code) {

//        System.out.println("[Machine Code Executor]: execute " + code.toString());

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
                break;
            }
            case MachineInstruction.NOP: {
                cpu.__doNop();
                break;
            }
            case MachineInstruction.JMP: {
                cpu.__doJmpOp(code.src);
                break;
            }
        }
    }
}
