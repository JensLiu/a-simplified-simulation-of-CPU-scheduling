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

    // block interruption
    private ReentrantLock __intLock = new ReentrantLock();

    public static final int TIMESLICE_MS = 1000;
    public static final int PROGRAMME_SLICE = 2;

    //    ArrayList<CPU> cpus;
    CPU __cpu; // assume single core
    Timer __timer;
    ArrayList<SysProcess> __ready = new ArrayList<>();
    ArrayList<SysProcess> __all = new ArrayList<>();


    public Kernel() {
        __cpu = new CPU();
        __timer = new Timer(this, __intLock);
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

        SysProcess process = __cpu.getCurProc();
        if (process != null) {
            __ready.add(process); // add it to ready queue
            __cpu.saveContext(); // eq to __cpu.storeContext(__cpu.getCurProc())
        }

        // get the next process, set context
        SysProcess nextProc = __getNextTask();

        for (SysProcess p : __all) {
            p.updateCounterOnSchedule();
        }

        if (nextProc != null) {
            __cpu.switchTo(nextProc);
        } else {
            System.out.println("[Kernel]: no process");
        }
    }

    private SysProcess __getNextTask() {
        // sort ready queue in the descending order of counter
        __ready.sort((o1, o2) -> o2.getCounter() - o1.getCounter());
        if (__ready.size() > 0) {
            SysProcess next = __ready.remove(0); // remove next task from ready queue, return it to execute
            return next;
        } else {
            return null;
        }
    }

     void __timerInterruptHandler() {

        System.out.println("[Kernel Interrupt]: timer interrupt");
        SysProcess p = __cpu.getCurProc();
        if (p != null) {
            p.updateCounterOnTimer();
        }

        // print
         System.out.print("\t[kernel]: all processes: \t");
        for (SysProcess pr : __all) {
            System.out.print("\t" + pr.hashCode() + ": " + pr.getCounter());
        }
         System.out.println();
         System.out.println("[Kernel Interrupt]: end timer interrupt");
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

            while (!__intLock.tryLock())
                ;

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

            __intLock.unlock();

        }
    }

}
