package kernel;

import kernel.process.SysProcess;
import kernel.programme.Programme;
import kernel.sysrecources.cpu.CPU;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Kernel {

    public static final int TIMESLICE_MS = 1;
    public static final int PROGRAMME_SLICE = 2;

    //    ArrayList<CPU> cpus;
    CPU __cpu; // assume single core
    ArrayList<SysProcess> __ready;
    ArrayList<SysProcess> __all;
//    ArrayList<TaskStruct> blocked;


    private void __schedule() {

        __ready.add(__cpu.getCurProc());
        __cpu.saveContext(); // eq to __cpu.storeContext(__cpu.getCurProc())

        // get the next process, set context
        SysProcess nextProc = __getNextTask();
        __cpu.switchToTask(nextProc);


    }
    private SysProcess __getNextTask() {
        SysProcess next = __ready.remove(0); // remove next task from ready queue
        return next;
    }

    public void runKernel() {
        CPU cpu = __cpu;
    }

    public void createProcess(Programme programme) {
        SysProcess process = SysProcess.getInstance(programme, 3); // assume user programmes are of the lowest priority
        process.setState(SysProcess.TASK_RUNNING);
        __all.add(process);
        __ready.add(process);
    }

    public void __timerInterruptHandler() {

        for (SysProcess p : __all) {
            p.handleTimer();
        }

    }

}
