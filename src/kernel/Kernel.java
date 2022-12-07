package kernel;

import kernel.process.SysProcess;
import kernel.programme.Programme;
import kernel.sysrecources.cpu.CPU;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Kernel {

    public static final int TIMESLICE_MS = 1000;
    public static final int PROGRAMME_SLICE = 2;

    //    ArrayList<CPU> cpus;
    CPU __cpu; // assume single core
    Timer __timer;
    ArrayList<SysProcess> __ready = new ArrayList<>();
    ArrayList<SysProcess> __all = new ArrayList<>();

    // prevent executing instruction when handling interruption
    ReentrantLock lock_mutex = new ReentrantLock();

    public Kernel() {
        __cpu = new CPU();
        __timer = new Timer(this);
    }

    public void loadProgramme(Programme programme) {
        createProcess(programme);
        __schedule();
    }
    public void createProcess(Programme programme) {
        SysProcess process = SysProcess.getInstance(programme, 3); // assume user programmes are of the lowest priority
        process.setState(SysProcess.TASK_RUNNING);
        __all.add(process);
        __ready.add(process);
    }

    private void __schedule() {
        System.out.println("[Kernel]: schedule------------------------------------------------------------------");
        for (SysProcess p : __all) {
            p.updateCounterOnSchedule();
        }

        SysProcess process = __cpu.getCurProc();
        if (process != null) {
            __ready.add(process); // add it to ready queue
            __cpu.saveContext(); // eq to __cpu.storeContext(__cpu.getCurProc())
        }

//        System.out.println("[Kernel]: no process, try to reschedule");

        // get the next process, set context
        SysProcess nextProc = __getNextTask();
        if (nextProc != null) {
            __cpu.switchToTask(nextProc);
        } else {
            System.out.println("[Kernel]: no process");
        }

    }

    private SysProcess __getNextTask() {
        __ready.sort(new Comparator<SysProcess>() { // sort ready queue in the descending order of counter
            @Override
            public int compare(SysProcess o1, SysProcess o2) {
                return o2.getCounter() - o1.getCounter();
            }
        });

//        System.out.println(__ready);
        if (__ready.size() > 0) {
            SysProcess next = __ready.remove(0); // remove next task from ready queue, return it to execute
            return next;
        } else {
            return null;
        }
    }

     void __timerInterruptHandler() {

//        System.out.println("[Kernel Interrupt]: timer interrupt ***************************************");
        SysProcess p = __cpu.getCurProc();
        if (p != null) {
            p.updateCounterOnTimer();
//            System.out.println("[Kernel]:" + p.toString() +  " counter = " + p.getCounter() + "\t");
        }

        // show
//         System.out.println("[kernel]: all processes: ");
//        for (SysProcess pr : __all) {
//            System.out.println(pr + "\t" + pr.getCounter());
//        }
//         System.out.println("[Kernel Interrupt]: end timer interrupt ************************************");

    }

    public void startKernel() {
        __timer.start();
        __runKernel();
    }

    void __runKernel() {
        while (true) {
            try {
                Thread.sleep(800);
            } catch (Exception e) {
                e.printStackTrace();
            }

            SysProcess process = __cpu.getCurProc();

            // executing and scheduling
            if (process != null && process.getCounter() > 0) {
                if (!__cpu.execOneInstruction()) {
                    __all.remove(process);
                    __cpu.removeCurProc();
                }
            } else {
                __schedule();
            }
        }
    }

}
