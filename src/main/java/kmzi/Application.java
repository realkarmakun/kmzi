package kmzi;

import kmzi.commands.Cmip;
import picocli.CommandLine;

public class Application {
    public static void main(String... args) {
        int exitCode = new CommandLine(new Cmip()).
                setExecutionExceptionHandler(new PrintExceptionMessageHandler()).execute(args);
        System.exit(exitCode);
    }
}
