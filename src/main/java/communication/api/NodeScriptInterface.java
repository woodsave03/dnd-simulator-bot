package communication.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NodeScriptInterface {
    public static void main(String[] args) {
        try {
            // Command to execute the Node.js script
            ProcessBuilder pb = new ProcessBuilder("node", "sandbox.js", "https://api.open5e.com/v1/?format=json");
            Process process = pb.start();

            // Read output from the script
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            // Wait for the process to complete
            process.waitFor();

            // Print the result
            System.out.println("Fetched Data: " + output.toString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
