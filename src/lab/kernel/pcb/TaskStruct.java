package lab.kernel.pcb;

import lab.kernel.programme.Programme;
import lab.kernel.programme.code.Text;
import lab.kernel.resources.cpu.CPU;

import java.util.ArrayList;

public class TaskStruct {

    // let's pretend it can access the code in the programme
    Programme __programme;

    // process states according to the linux kernel
    public static int TASK_RUNNING = 0;
    public static int TASK_INTERRUPTIBLE = 1;
    public static int TASK_UNINTERRUPTIBLE = 2;
    public static int TASK_STOPPED = 3;
    public static int TASK_TRACED = 4;

    // pid allocation control
    private static ArrayList<Boolean> __pid_map;
    private static int __PID_MAX = 100;

    // initialise pid_map
    static {
        __pid_map = new ArrayList<>(__PID_MAX);
        for (int i = 0; i < __pid_map.size(); ++i) {
            __pid_map.set(i, false);
        }
    }


    // factory
    public static synchronized TaskStruct getInstance(Programme p) {
        TaskStruct pd = new TaskStruct();
        pd.__programme = p;
//        pd.__pid = __get_next_pid();
        return pd;
    }

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


    // process identifier
    int __pid;

    // hardware resources
    CPU __cpu;
    Text __code;

    // context
    int __ip;

    // execution status
    int __state;
    int __exit_state;

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

    public Programme getProgramme() {
        return __programme;
    }
}
