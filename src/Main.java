import java.io.IOException;

import Cores.MasterCore;
import Memory.SharedMemory;
import Parser.ProgramParser;
import Process.Process;
import Process.ReadyQueue;
import Scheduler.Scheduler;

public class Main {
    public static void main(String[] args) throws IOException {

        SharedMemory memory = new SharedMemory();
        ReadyQueue readyQueue = new ReadyQueue();

        Process process1 = ProgramParser.parseProgram("src/Programs/Program_1.txt", 1, 0, 10);
        Process process2 = ProgramParser.parseProgram("src/Programs/Program_2.txt", 2, 10, 16);
        Process process3 = ProgramParser.parseProgram("src/Programs/Program_3.txt", 3, 16, 19);

        readyQueue.addProcess(process1);
        readyQueue.addProcess(process2);
        readyQueue.addProcess(process3);

        int numCores = 2;
        int quantum = 2; 
        Scheduler scheduler = new Scheduler(readyQueue, memory, numCores, quantum);
        
        MasterCore masterCore = new MasterCore(scheduler);

        readyQueue.sortByInstructionCount();
        System.out.println("----Ready queue at the start----");
        readyQueue.printQueue();
        System.out.println("------------------------");

        masterCore.scheduleTasks();
    }
}
