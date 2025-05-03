package Cores;

import Scheduler.Scheduler;

public class MasterCore {
    private Scheduler scheduler;


    public MasterCore(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void scheduleTasks() {
        scheduler.schedule();
    }
}
