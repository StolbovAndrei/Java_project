import java.util.Scanner;

public class LogicBot {

    private final Echo echo = new Echo();

    public void start() {
        System.out.println(Messages.FIRST_MESSAGE);
        Scanner in = new Scanner(System.in);
        while (true) {
            String message = in.nextLine();
            switch (message) {
                case "/help": {
                    help();
                    break;
                }
                case "/exit": {
                    exit();
                    break;
                }
                case "/echo": {
                    echo.echo();
                    break;
                }
            }
        }
    }

    public static void help() {
        System.out.println(Messages.HELP_MESSAGE);
    }

    public static void exit() {
        System.out.println("До свидания!");
        System.exit(0);
    }

    public Echo getEcho() {
        return echo;
    }
}
