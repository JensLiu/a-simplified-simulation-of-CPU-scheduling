package kernel.programme.code;

import java.util.ArrayList;

public class Text {
    public ArrayList<String> instructions;

    public Text() {
        instructions = new ArrayList<>();
    }

    public Text(Text another) {
        instructions = new ArrayList<>(another.instructions); // deep copy of the instruction set
    }

}
