package user;

import kernel.Kernel;
import kernel.programme.Programme;
import kernel.sysrecources.cpu.CPU;

public class Test {
    public static void main(String[] args) {


        Programme prog1 = new Programme();
        for (int i = 0; i < 1000; i++) {
//            prog1.text.instructions.add("MOV $2 %r1"); // 0
//            prog1.text.instructions.add("MOV $1 %r2"); // 1
//            prog1.text.instructions.add("ADD %r1 %r2"); // 2
//            prog1.text.instructions.add("DISP %r1"); // 3
//            prog1.text.instructions.add("JMP 2"); // 4
            prog1.text.instructions.add("NOP");
//            prog1.text.instructions.add("NOP");
//            prog1.text.instructions.add("NOP");
        }

        Programme prog2 = new Programme();
        for (int i = 0; i < 1000; i++) {
//            prog2.text.instructions.add("MOV $2 %r1");
//            prog2.text.instructions.add("MOV $1 %r2");
//            prog2.text.instructions.add("ADD %r1 %r2");
//            prog2.text.instructions.add("DISP %r1");
            prog2.text.instructions.add("NOP");
//            prog2.text.instructions.add("NOP");
//            prog2.text.instructions.add("NOP");
        }

        Programme prog3 = new Programme();
        for (int i = 0; i < 1000; i++) {
            prog3.text.instructions.add("NOP");
        }

        Kernel kernel = new Kernel();
        kernel.createProcess(prog1);
        kernel.createProcess(prog2);
        kernel.createProcess(prog3);
        kernel.startKernel();


//        String[] split = "$111".split("\\$");
//        System.out.println(split[1]);

    }
}
