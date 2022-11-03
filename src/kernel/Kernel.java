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
    public static final int PROGRAMME_SLICE = 3;

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
        System.out.println("[Kernel]: schedule");
        for (SysProcess p : __all) {
            p.handleSchedule();
        }

        SysProcess process = __cpu.getCurProc();
        if (process != null) {
            __ready.add(process);
            __cpu.saveContext(); // eq to __cpu.storeContext(__cpu.getCurProc())
        }

        // get the next process, set context
        SysProcess nextProc = __getNextTask();
        __cpu.switchToTask(nextProc);

    }

    private SysProcess __getNextTask() {
        __ready.sort(new Comparator<SysProcess>() {
            @Override
            public int compare(SysProcess o1, SysProcess o2) {
                return o2.getCounter() - o1.getCounter();
            }
        });
        System.out.println(__ready);
        SysProcess next = __ready.remove(0); // remove next task from ready queue
        return next;
    }

     void __timerInterruptHandler() {

        System.out.print("[Kernel Interrupt]: timer interrupt \t");
        SysProcess p = __cpu.getCurProc();
        if (p != null) {
            p.handleTimer();
            System.out.print("[Kernel]:" + p.toString() +  " counter = " + p.getCounter() + "\t");
        }
         System.out.println("[kernel]: all processes: ");
        for (SysProcess pr : __all) {
            System.out.println(pr + "\t" + pr.getCounter());
        }

         System.out.println("[Kernel Interrupt]: end timer interrupt");

    }

    public void startKernel() {
        __timer.start();
        __runKernel();
    }

    void __runKernel() {
        while (true) {
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
            SysProcess process = __cpu.getCurProc();
            if (process != null && process.getCounter() > 0) {
                __cpu.execOneInstruction();
            } else {
                __schedule();
            }
        }
    }

}
