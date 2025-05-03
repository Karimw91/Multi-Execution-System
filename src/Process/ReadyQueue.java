package Process;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ReadyQueue {
    private final List<Process> queue;

    public ReadyQueue() {
        this.queue = new ArrayList<>();
    }

    public synchronized void addProcess(Process process) {
        queue.add(process);
        sortByInstructionCount();
    }

    public synchronized Process getNextProcess() {
        if (!queue.isEmpty()) {
            return queue.remove(0); // Remove the first process in the queue
        }
        return null;
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    public synchronized void sortByInstructionCount() {
        Collections.sort(queue, Comparator.comparingInt(p -> p.getInstructions().size() - p.getProgramCounter()));
    }

    public synchronized void printQueue() {
        System.out.println("Ready Queue:");
        for (Process process : queue) {
            System.out.println("Process ID: " + process.getProcessId() + ", Remaining Instructions: " 
                + (process.getInstructions().size() - process.getProgramCounter()));
        }
    }
}
