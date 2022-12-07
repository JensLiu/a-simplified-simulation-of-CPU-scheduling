package kernel;

import kernel.Kernel;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Timer extends Thread {

    Kernel __kernel;
    ReentrantLock __intLock;

    Timer(Kernel kernel, ReentrantLock intLock) {
        __kernel = kernel;
        __intLock = intLock;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(Kernel.TIMESLICE_MS);
            } catch (InterruptedException e) {
                System.out.println("[FATAL ERROR]: TIMER CORRUPTED");
            }

            while (!__intLock.tryLock())
                ;

            __kernel.__timerInterruptHandler();

            __intLock.unlock();
        }
    }
}
