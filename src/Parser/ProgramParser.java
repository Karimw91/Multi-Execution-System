package Parser;
import java.io.*;
import java.util.*;

import Process.Process;

public class ProgramParser {
    public static Process parseProgram(String filePath, int processId, int memoryStart, int memoryEnd) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String> instructions = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                instructions.add(line.trim());
            }
            return new Process(processId, instructions, memoryStart, memoryEnd);
        }
    }
}
