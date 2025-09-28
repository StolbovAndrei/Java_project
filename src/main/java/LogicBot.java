import java.util.Scanner;

public class LogicBot {
    public void start() {
        System.out.println(Messages.firstMessage);
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
                    Echo.echo();
                    break;
                }
            }
        }
    }

    public static void help() {
        System.out.println(Messages.helpMessage);
    }

    public static void exit() {
        System.out.println("До свидания!");
        System.exit(0);
    }
}
