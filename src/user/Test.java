package user;

import kernel.programme.Programme;

public class Test {
    public static void main(String[] args) {
        Programme prog = new Programme();
        prog.text.instructions.add("MOV $1 %r1");
        prog.text.instructions.add("MOV $2 %r2");
        prog.text.instructions.add("ADD %r1 r2");
        prog.text.instructions.add("DISP %r1");

        Programme prog1 = new Programme();
        prog.text.instructions.add("MOV $2 %r1");
        prog.text.instructions.add("MOV $1 %r2");
        prog.text.instructions.add("ADD %r1 %r2");
        prog.text.instructions.add("DISP %r1");

        String[] split = "$111".split("\\$");
        System.out.println(split[1]);

    }
}
