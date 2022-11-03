package user;

import kernel.programme.Programme;
import kernel.sysrecources.cpu.CPU;

public class Test {
    public static void main(String[] args) {


        Programme prog1 = new Programme();
        prog1.text.instructions.add("MOV $2 %r1");
        prog1.text.instructions.add("MOV $1 %r2");
        prog1.text.instructions.add("ADD %r1 %r2");
        prog1.text.instructions.add("DISP %r1");



//        String[] split = "$111".split("\\$");
//        System.out.println(split[1]);

    }
}
