package user.test;

import kernel.programme.Programme;
import kernel.sysrecources.cpu.CPU;

public class CPU_Test {
    public static void main(String[] args) {
        Programme prog = new Programme();
        prog.text.instructions.add("MOV %r1 $1");
        prog.text.instructions.add("DISP %r1");
        prog.text.instructions.add("MOV %r2 $2");
        prog.text.instructions.add("DISP %r1");
        prog.text.instructions.add("DISP %r2");
        prog.text.instructions.add("ADD %r1 %r2");
        prog.text.instructions.add("DISP %r1");
        prog.text.instructions.add("DISP %r2");

        CPU cpu = new CPU();
//        cpu.test_execProgramme(prog);
    }
}
