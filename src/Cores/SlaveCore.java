package Cores;

import java.util.Scanner;

import Memory.SharedMemory;
import Process.Process;

public class SlaveCore extends Thread {
    private final int coreId;
    private final SharedMemory memory;
    private Process currentProcess;
    private int quantum;
    private static final Object inputLock = new Object();

    public SlaveCore(int coreId, SharedMemory memory) {
        this.coreId = coreId;
        this.memory = memory;
    }

    public void assignProcess(Process process, int quantum) {
        this.currentProcess = process;
        this.quantum = quantum;
    }

    public int getCoreId() {
        return coreId;
    }

    public Integer getCurrentProcessId() {
        return currentProcess != null ? currentProcess.getProcessId() : null;
    }

    public void removeProcess() {
        this.currentProcess = null;
    }

    public Process getCurrentProcess() {
        return currentProcess;
    }

    @Override
    public void run() {
        if (currentProcess != null) {
            System.out.println("Core " + coreId + " is executing Process " + currentProcess.getProcessId());
            int executedInstructions = runForQuantum(quantum);

            if (currentProcess.getProgramCounter() < currentProcess.getInstructions().size()) {
                System.out.println("Process " + currentProcess.getProcessId() + " preempted after executing " + executedInstructions + " instructions.");
            } else {
                System.out.println("Process " + currentProcess.getProcessId() + " completed.");
            }
        }
    }

    public int runForQuantum(int quantum) {
        int executedInstructions = 0;
        while (executedInstructions < quantum && currentProcess.getProgramCounter() < currentProcess.getInstructions().size()) {
            String instruction = currentProcess.getInstructions().get(currentProcess.getProgramCounter());
            executeInstruction(instruction);
            currentProcess.incrementProgramCounter();
            executedInstructions++;
        }
        return executedInstructions;
    }

    private void executeInstruction(String instruction) {
        String[] parts = instruction.split(" ");
        switch (parts[0]) {
            case "assign":
                if (parts[2].equals("input")) {
                    // Synchronize input to avoid overlapping input instructions
                    synchronized (inputLock) {
                        Scanner scanner = new Scanner(System.in);
                        System.out.print("Enter an integer for variable " + parts[1] + " in core " + coreId + ": ");
                        Double number = scanner.nextDouble();
                        memory.write(parts[1], number); 
                        System.out.println("Core " + coreId + " assigned " + number + " to " + parts[1]);
                    }
                } else if (parts[2].equals("add")) {
                    Double result = memory.read(parts[3]) + memory.read(parts[4]);
                    memory.write(parts[1], result);
                    System.out.println("Core " + coreId + " calculated " + parts[1] + " = " + result);

                } else if (parts[2].equals("subtract")) {
                    Double result = memory.read(parts[3]) - memory.read(parts[4]);
                    memory.write(parts[1], result);
                    System.out.println("Core " + coreId + " calculated " + parts[1] + " = " + result);

                } else if (parts[2].equals("multiply")) {
                    Double result = memory.read(parts[3]) * memory.read(parts[4]);
                    memory.write(parts[1], result);
                    System.out.println("Core " + coreId + " calculated " + parts[1] + " = " + result);

                } else if (parts[2].equals("divide")) {
                    Double result = memory.read(parts[3]) / memory.read(parts[4]);
                    memory.write(parts[1], result);
                    System.out.println("Core " + coreId + " calculated " + parts[1] + " = " + result);
                }
                break;

            case "print":
                System.out.println("PRINT Core: " + coreId + " Variable:  " + parts[1] + ": " + memory.read(parts[1]));
                break;

            default:
                System.out.println("Core " + coreId + " received unknown instruction: " + instruction);
        }
    }
}