package Monitoring;
import java.util.List;

import Cores.SlaveCore;
import Memory.SharedMemory;
import Process.ReadyQueue;

public class ExecutionMonitor {
    public static void printStatus(ReadyQueue readyQueue, SharedMemory memory, List<SlaveCore> cores) {
        System.out.println();
        System.out.println("=== Execution Status ===");
        readyQueue.printQueue();

        for (SlaveCore core : cores) {
            Integer currentProcessId = core.getCurrentProcessId();
            if (currentProcessId != null) {
                System.out.println("Core " + core.getCoreId() + ": Executing Process " + currentProcessId);
            } else {
                System.out.println("Core " + core.getCoreId() + ": Idle");
            }
        }
        memory.printState();
        System.out.println("========================");
        System.out.println();
    }
}
