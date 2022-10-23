package kernel.sysrecources.cpu;

public class MachineInstruction {

    public int op;
    public String src;
    public String dst;

    public static final int ADD = 1;
    public static final int MOV = 2;
    public static final int DISP = 3;


    public static MachineInstruction parseToMachineCode(String code) {
        try {
            String[] t = code.split(" ");
            if (t.length == 3) {
                int op = __opToInt(t[0]);
                if (op >= 0) {
                    MachineInstruction machineCode = new MachineInstruction();
                    machineCode.op = op;
                    machineCode.src = t[1];
                    machineCode.dst = t[2];
                    return machineCode;
                }
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
            } case "mov" -> {
                return MOV;
            }
        }
        return -1;
    }

    public String toString() {
        String opStr = "ERROR";
        switch (op) {
            case ADD -> opStr = "ADD";
            case MOV -> opStr = "MOV";
        }
        return opStr + " " + src + " " + dst;
    }

}
