package Scheduler;

import java.util.ArrayList;
import java.util.List;

import Cores.SlaveCore;
import Memory.SharedMemory;
import Process.Process;
import Process.ReadyQueue;
import Monitoring.ExecutionMonitor;

public class Scheduler {
    private final ReadyQueue readyQueue;
    private final SharedMemory memory;
    public final List<SlaveCore> slaveCores;
    private final int quantum;
    private final List<Process> waitingProcesses; 

    public Scheduler(ReadyQueue readyQueue, SharedMemory memory, int numCores, int quantum) {
        this.readyQueue = readyQueue;
        this.memory = memory;
        this.quantum = quantum;
        this.slaveCores = new ArrayList<>();
        this.waitingProcesses = new ArrayList<>();
        for (int i = 1; i <= numCores; i++) {
            slaveCores.add(new SlaveCore(i, memory));
        }
    }

    public void schedule() {
        int numberOfSchedulingRounds = 1;
    
        while (!readyQueue.isEmpty() || !waitingProcesses.isEmpty()) {
            System.out.println();
            System.out.println("//////////////////SCHEDULING ROUND: " + numberOfSchedulingRounds + "//////////////////");
            numberOfSchedulingRounds++;
    
            // Sort the ready queue by the number of instructions
            synchronized (readyQueue) {
                readyQueue.sortByInstructionCount();
            }
    
            List<Thread> threads = new ArrayList<>(); 
    
            for (SlaveCore core : slaveCores) {
                // If the ready queue is empty, refill it from the waiting list
                if (readyQueue.isEmpty() && !waitingProcesses.isEmpty()) {
                    synchronized (waitingProcesses) {
                        synchronized (readyQueue) {
                            for (Process process : waitingProcesses) {
                                readyQueue.addProcess(process);
                                System.out.println("Process " + process.getProcessId() + " moved from waiting list to Ready Queue.");
                            }
                            waitingProcesses.clear(); // Clear the waiting list after moving processes
                        }
                    }
    
                    // Re-sort the ready queue after refilling it
                    synchronized (readyQueue) {
                        readyQueue.sortByInstructionCount();
                    }
                }
    
                // Assign a process to the core
                synchronized (readyQueue) {
                    if (!readyQueue.isEmpty()) {
                        Process process = readyQueue.getNextProcess();
                        core.assignProcess(process, quantum);
                        System.out.println("Core " + core.getCoreId() + " is assigned Process " + process.getProcessId());
                    } else {
                        System.out.println("Core " + core.getCoreId() + " is idle.");
                    }
                }
    
                // Create a new thread to run the core
                Thread thread = new Thread(core); 
                threads.add(thread);
                thread.start();
            }
    
            // Wait for all threads to complete their execution
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    
            ExecutionMonitor.printStatus(readyQueue, memory, slaveCores);
    
            // Requeue processes that are not complete into the waiting list
            for (SlaveCore core : slaveCores) {
                Process process = core.getCurrentProcess();
                if (process != null) {
                    if (process.getProgramCounter() < process.getInstructions().size()) {
                        synchronized (waitingProcesses) {
                            waitingProcesses.add(process);
                            System.out.println("Process " + process.getProcessId() + " preempted and added to the Waiting List.");
                        }
                    }
                    core.removeProcess(); // Clear the core for the next process
                }
            }
    
            System.out.println("----Waiting list after round----");
            printWaitingList();
            System.out.println("-----------------");
        }
    
        System.out.println("All processes have been executed!");
        System.out.println("----FINAL MONITOR STATUS----");
        ExecutionMonitor.printStatus(readyQueue, memory, slaveCores);
    }
    
    private void printWaitingList() {
        synchronized (waitingProcesses) {
            System.out.println("Waiting List:");
            for (Process process : waitingProcesses) {
                System.out.println("Process ID: " + process.getProcessId() + ", Remaining Instructions: "
                        + (process.getInstructions().size() - process.getProgramCounter()));
            }
        }
    }   
}