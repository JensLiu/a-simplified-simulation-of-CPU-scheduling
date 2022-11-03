package kernel;

import kernel.Kernel;

public class Timer extends Thread {

    Kernel __kernel;

    Timer(Kernel kernel) {
        __kernel = kernel;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(Kernel.TIMESLICE_MS);
            } catch (InterruptedException e) {
                System.out.println("[FATAL ERROR]: TIMER CORRUPTED");
            }

            __kernel.__timerInterruptHandler();

        }
    }
}
