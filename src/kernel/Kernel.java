package kernel;

import kernel.process.SysProcess;
import kernel.programme.Programme;
import kernel.sysrecources.cpu.CPU;

import java.util.ArrayList;

public class Kernel {
    //    ArrayList<CPU> cpus;
    CPU __cpu; // assume single core
    ArrayList<SysProcess> __ready;
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
        SysProcess process = SysProcess.getInstance(programme);
        process.setState(SysProcess.TASK_RUNNING);
        __ready.add(process);
    }

    public void __timerInterruptHandler() {
//        __cpu.timerInterput
    }

}
