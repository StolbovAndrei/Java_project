import java.util.Scanner;

public class Echo {

    Scanner scanner = new Scanner(System.in);

    public void echo(){
        System.out.println(Messages.echoMessage);
        Scanner in = scanner;
        while (true) {
            String newMessage = in.nextLine();
            switch (newMessage) {
                case "/help":{
                    LogicBot.help();
                    break;
                }
                case "/exit":{
                    LogicBot.exit();
                    break;
                }
                case "/back":{
                    System.out.println(Messages.firstMessage);
                    return;
                }
                default :{
                    System.out.println("Вы сказали: " + newMessage);
                }
            }
        }
    }
}

