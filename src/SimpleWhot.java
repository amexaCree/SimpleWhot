import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
public class SimpleWhot {

    static Scanner scanner = new Scanner(System.in);
    static Settings settings = new Settings();

    public static void main(String[] args) {

        SimpleWhot simpleWhot = new SimpleWhot();

//        Messenger.welcome();
//        Messenger.menu();

        System.out.println("Welcome To Simple WHot 1.0");
        SimpleWhot.gameStartMenu();
    }

    public static void gameStartMenu() {

//        Hashtable HandMap = new Hashtable();
//
//        private void buildHandMap() {
//            HandMap.put("CIRCLESet",  new ArrayList<Integer>());
//            HandMap.put("SQUARESet",  new ArrayList<Integer>());
//            HandMap.put("CROSSSet",  new ArrayList<Integer>());
//
//            HandMap.replace("CIRCLESet", );
//
//
//        }


        System.out.println("Menu");
        System.out.println("1. New Game");
        System.out.println("2. Exit");

        int option = getMenuOption();

        switch (option) {
            case 1: SimpleWhot.startGame(); break;
            case 2: SimpleWhot.exit(); break;
            default: break;
        }
    }

    public static void startGame() {
        System.out.println("Please enter your name: ");
        String playerName = scanner.next();

        Game game = new Game(playerName);
        game.dealCards();

        while (!game.isOver()) {
            //System.out.println("   A   ");
            game.viewStats();
            System.out.println("It's <<"+ game.players.get(game.activeTurn).getName() + ">>'s turn to play");
            System.out.println("<<"+game.players.get(game.activeTurn).getName() + ">>'s hand:");

            //game.getActivePlayer().chooseMove;
            game.chooseMove();
            game.nextTurn();
            //System.out.println("   A2   ");
        }

        System.out.println("");
        System.out.println("<<"+ game.players.get(game.activeTurn).getName() + ">> won the game!");

        System.out.println("");
        SimpleWhot.gameStartMenu();
    }

    public static int getMenuOption() {
        while (true) {
            try {
                int option = scanner.nextInt();
                return option;
            } catch (Exception error) {
                System.out.print("Please enter a number corresponding to an option..\n");
                /* TODO -
                *  Currently only throws if input is not a number...
                *  There is no handling for when input is greater than 2 (there are only 2 options).
                *  Need to add handling for when number greater than 2 is inputted.
                */
                scanner.nextLine();
            }
        }
    }

    public static void exit() {
        System.out.print("Bye!");
    }
}
