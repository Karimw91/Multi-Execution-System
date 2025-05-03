package Memory;
import java.util.concurrent.ConcurrentHashMap;

public class SharedMemory {
    private final ConcurrentHashMap<String, Double> memory = new ConcurrentHashMap<>();

    public synchronized void write(String variable, Double value) {
        memory.put(variable, value);
    }

    public synchronized Double read(String variable) {
        return memory.getOrDefault(variable, 0.0);
    }

    public synchronized void printState() {
        System.out.println("Memory State: " + memory);
    }
}
