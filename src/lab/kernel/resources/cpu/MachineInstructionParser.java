package lab.kernel.resources.cpu;

public class MachineInstructionParser {

    /**
     * parse machine code
     * @param cpu
     * @param code
     */
    public static void executeOneInstruction(CPU cpu, MachineInstruction code) {

        System.out.println(code.toString());

        switch (code.op) {
            case MachineInstruction.ADD: {
                cpu.__doAddOp(code.src, code.dst);
                break;
            }
            case MachineInstruction.MOV: {
                cpu.__doMovOp(code.src, code.dst);
            }
        }
    }
}
