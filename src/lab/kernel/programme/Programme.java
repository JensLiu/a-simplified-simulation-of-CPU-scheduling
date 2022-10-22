package lab.kernel.programme;

import lab.kernel.programme.code.Text;

public class Programme {
    // programme points to its own virtual address space, no need to worry about the physical address
    Text text;

    /**
     * get instruction of the programme at its virtual address (code segment)
     * @param i virtual address in code segment
     * @return instruction string, null if out of bounds
     */
    public String getInstructionAt(int i) {
        if (i >= text.instructions.size())
            return null;
        return text.instructions.get(i);
    }

    /**
     * get data of the programme at its virtual address (data segment)
     * @param i virtual address in data segment
     * @return
     */
    public int getDataAt(int i) {
        // TODO: return data at virtual address
        return i;
    }

}
