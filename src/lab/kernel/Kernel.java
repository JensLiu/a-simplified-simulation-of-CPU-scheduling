package lab.kernel;

import lab.kernel.pcb.TaskStruct;
import lab.kernel.programme.Programme;
import lab.kernel.resources.cpu.CPU;

import java.util.ArrayList;

public class Kernel {
//    ArrayList<CPU> cpus;
    CPU __cpu; // assume single core
    ArrayList<TaskStruct> __ready;
//    ArrayList<TaskStruct> blocked;


    private void __schedule() {
        __ready.add(__cpu.getCurTask());
        __cpu.saveContext(); // eq to cpu.storeContext(cpu.getCurrentExecutingTask())

        // get next process, reset context
        TaskStruct nextTask = __getNextTask();
        __cpu.switchToTask(nextTask);
    }
    private TaskStruct __getNextTask() {
        TaskStruct nextTask = __ready.remove(0); // remove next task from ready queue
        return nextTask;
    }

    public void runKernel() {
        CPU cpu = __cpu;
    }

    public void createTask(Programme programme) {
        // TODO: load programme to task
        TaskStruct task = new TaskStruct();
        task.setState(TaskStruct.TASK_RUNNING);
        __ready.add(task);
    }



}
