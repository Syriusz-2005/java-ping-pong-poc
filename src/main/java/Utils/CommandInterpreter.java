package Utils;

import java.util.Scanner;
import java.util.function.Function;

public class CommandInterpreter {
    private final String prefix;
    public CommandInterpreter(String prefix) {
        this.prefix = prefix;
    }

    public void listen(Function<String[], Boolean> onCommand) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(prefix);
            String line = scanner.nextLine();
            String[] args = line.split(" ");
            boolean result = onCommand.apply(args);
            if (!result) {
                return;
            }
        }
    }
}
