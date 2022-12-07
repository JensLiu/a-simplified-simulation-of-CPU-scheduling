package user;

import kernel.Kernel;
import kernel.programme.Programme;
import kernel.sysrecources.cpu.CPU;

public class Test {
    public static void main(String[] args) {
        Programme prog = new Programme();
        prog.text.instructions.add("JMP 0");
        Kernel kernel = new Kernel();
        kernel.createProcess(prog);
        kernel.createProcess(prog);
        kernel.createProcess(prog);
        kernel.startKernel();
    }
}
