package Process;
import java.util.List;

public class Process {
    int processId;
    List<String> instructions;
    int programCounter;
    int memoryBoundaryStart;
    int memoryBoundaryEnd;

    public Process(int processId, List<String> instructions, int memoryBoundaryStart, int memoryBoundaryEnd) {
        this.processId = processId;
        this.instructions = instructions;
        this.programCounter = 0;
        this.memoryBoundaryStart = memoryBoundaryStart;
        this.memoryBoundaryEnd = memoryBoundaryEnd;
    }

    public void incrementProgramCounter() {
        programCounter++;
    }

    public int getProcessId() {
        return processId;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public int getProgramCounter() {
        return programCounter;
    }

    public int getMemoryBoundaryStart() {
        return memoryBoundaryStart;
    }

    public int getMemoryBoundaryEnd() {
        return memoryBoundaryEnd;
    }
}
