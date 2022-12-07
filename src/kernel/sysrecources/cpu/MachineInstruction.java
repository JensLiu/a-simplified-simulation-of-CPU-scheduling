package kernel.sysrecources.cpu;

public class MachineInstruction {

    public int op;
    public String src;
    public String dst;

    public static final int ADD = 1;
    public static final int MOV = 2;
    public static final int DISP = 3;
    public static final int NOP = 4;
    public static final int JMP = 5;


    public static MachineInstruction parseToMachineCode(String code) {
        try {
            String[] t = code.split(" ");
            int op = __opToInt(t[0]);
            assert (op > -1);
            if (t.length == 3) {
                if (op >= 0) {
                    MachineInstruction machineCode = new MachineInstruction();
                    machineCode.op = op;
                    machineCode.dst = t[1];
                    machineCode.src = t[2];
                    return machineCode;
                }
            } else if (t.length == 2) {
                if (op >= 0) {
                    MachineInstruction machineCode = new MachineInstruction();
                    machineCode.op = op;
                    machineCode.src = t[1];
                    return machineCode;
                }
            } else if (t.length == 1) {
                MachineInstruction machineCode = new MachineInstruction();
                machineCode.op = op;
                return machineCode;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    private static int __opToInt(String op) {
        if (op == null)
            return -1;
        switch (op.toLowerCase()) {
            case "add" -> {
                return ADD;
            }
            case "mov" -> {
                return MOV;
            }
            case "disp" -> {
                return DISP;
            }
            case "nop" -> {
                return NOP;
            }
            case "jmp" -> {
                return JMP;
            }
        }
        return -1;
    }

    public String toString() {
        String opStr = "ERROR";
        switch (op) {
            case ADD -> opStr = "ADD";
            case MOV -> opStr = "MOV";
            case DISP -> opStr = "DISP";
            case NOP -> opStr = "NOP";
            case JMP -> opStr = "JMP";
        }
        return opStr + " " + src + " " + dst;
    }

}
