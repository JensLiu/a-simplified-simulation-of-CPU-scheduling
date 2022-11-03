package kernel.process;

import kernel.Kernel;
import kernel.programme.Programme;
import kernel.programme.code.Text;
import kernel.sysrecources.cpu.CPU;
import kernel.sysrecources.cpu.MachineInstruction;

public class SysProcess {
    // programme class is an abstraction for running process
    // programme points to its own virtual address space, no need to worry about the physical address

    // hardware resources
    CPU __cpu; // executing core

    // virtual memory
    Text __textSeg; // code segment of the process

    // context
    int __ip; // CPU instruction pointer register

    // execution status
    int __state;
    int __exit_state;

    // priority
    int __priority;
    int __counter;


    /**
     * get instruction of the programme  iatts virtual address (code segment)
     *
     * @param i virtual address in code segment
     * @return instruction string, null if out of bounds
     */
//    public String getInstructionAt(int i) {
//        if (i >= __textSeg.instructions.size())
//            return null;
//        return __textSeg.instructions.get(i);
//    }

    /**
     * get data of the programme at its virtual address (data segment)
     *
     * @param i virtual address in data segment
     * @return
     */
    public int getDataAt(int i) {
        // TODO: return data at virtual address
        return i;
    }

    public static synchronized SysProcess getInstance(Programme programme, int priority) {
        SysProcess process = new SysProcess();
        process.__textSeg = new Text(programme.text); // deep copy of the text segment in the code
        process.__ip = 0; // set instruction pointer to 0 of the code segment
        process.__priority = priority;
        process.__counter = Kernel.PROGRAMME_SLICE;
        return process;
    }

    public void setInstructionPointer(int ip) {
        __ip = ip;
    }

    public int getInstructionPointer() {
        return __ip;
    }

    public int getState() {
        return __state;
    }

    public void setState(int __state) {
        this.__state = __state;
    }

    public MachineInstruction getInstructionAt(int ip) {
        try {
            String instruction = __textSeg.instructions.get(ip);
            return MachineInstruction.parseToMachineCode(instruction);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public void handleTimer() {
        __counter--;
    }

    public void handleSchedule() {
        __counter = (__counter >> 1) + __priority;
    }

    public int getCounter() {
        return __counter;
    }

    public boolean outOfTimeSlice() {
        return __counter <= 0;
    }

    public static int TASK_RUNNING = 0;
    public static int TASK_INTERRUPTIBLE = 1;
    public static int TASK_UNINTERRUPTIBLE = 2;
    public static int TASK_STOPPED = 3;
    public static int TASK_TRACED = 4;


    // process identifier
//    int __pid;
//    // pid allocation control
//    private static ArrayList<Boolean> __pid_map;
//    private static int __PID_MAX = 100;
//
//    // initialise pid_map
//    static {
//        __pid_map = new ArrayList<>(__PID_MAX);
//        for (int i = 0; i < __pid_map.size(); ++i) {
//            __pid_map.set(i, false);
//        }
//    }

    //    private static int __get_next_pid() {
//        // get the first unused pid
//        int i = 0;
//        for (; i <= __pid_map.size() && !__pid_map.get(i); i++)
//            ;
//        if (i > __PID_MAX) {
//
//        }
//        return 1;
//    }

}