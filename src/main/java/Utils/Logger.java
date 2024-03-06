package Utils;

import com.diogonunes.jcolor.Attribute;
import com.diogonunes.jcolor.AnsiFormat;
import com.diogonunes.jcolor.Command;
import static com.diogonunes.jcolor.Ansi.colorize;


public class Logger {
    public static void printOk(String msg) {
        AnsiFormat ok = new AnsiFormat(Attribute.GREEN_TEXT());
        System.out.println(colorize(msg, ok));
    }

    public static void printErr(String msg) {
        AnsiFormat err = new AnsiFormat(Attribute.RED_TEXT());
        System.out.println(colorize(msg, err));
    }

    public static void print(String msg) {
        System.out.println(msg);
    }
}
