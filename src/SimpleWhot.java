import java.util.ArrayList;
import java.util.Scanner;
public class SimpleWhot {

    Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        SimpleWhot simpleWhot = new SimpleWhot();

//        Messenger.welcome();
//        Messenger.menu();

        System.out.println("Welcome To Simple WHot 1.0");
        System.out.println("Menu");
        System.out.println("1. New Game");
        System.out.println("2. Exit");

        int option = simpleWhot.getMenuOption();

        switch (option) {
            case 1: simpleWhot.startGame(); break;
            case 2: simpleWhot.exit(); break;
            default: break;
        }
    }

    public void startGame() {
        System.out.println("Please enter your name: ");

        String playerName = scanner.next();

        Game game = new Game(playerName);
        game.dealCards();
        for(Player player: game.players) {
            System.out.println(player.info());
        }
        System.out.println("");
        game.viewStats();

        while (!game.isOver()) {
            System.out.println(game.players.get(game.activeTurn).getName() + " to play");
            System.out.println("Please pick a card to play (enter number corresponding to card)");
            System.out.println(game.players.get(game.activeTurn).getName() + "'s hand:");
            game.showHand(game.players.get(game.activeTurn));
            System.out.println("or pick new card from deck (enter 0)");

            int selection = playerSelect();
            game.play(selection);



            game.viewStats();
        }
    }

    public int getMenuOption() {
        while (true) {
            try {
                int option = scanner.nextInt();
                return option;
            } catch (Exception error) {
                System.out.print("Please enter a number corresponding to an option..\n");
                scanner.nextLine();
            }
        }
    }

    public int playerSelect() {
        while (true) {
            try {
                int option = scanner.nextInt();
                return option;
            } catch (Exception error) {
                System.out.println(error);
                System.out.println("Please enter number corresponding to card you want to play");
                System.out.println("or enter 0 to pick a new card\n");
                scanner.nextLine();
            }
        }
    }

    public int play() {
        while (true) {
            try {

            } catch (Exception error) {
                System.out.println("\n");
                scanner.nextLine();
            }
        }
    }

    public void userSelection(int input, String menu) {


    }

    public void exit() {

    }
}
