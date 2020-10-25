import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    public int id;
    public ArrayList<Player> players = new ArrayList<Player>();
    public int activeTurn = 0;
    public boolean activeTurnPlayed = false;
    public boolean skipNextTurn = false;
    public WhotCard.Suit nextSuitMatch = null;

    private WhotDeck deck = new WhotDeck();
    public ArrayList<WhotCard> table = new ArrayList<WhotCard>();
    public WhotDeck gameMarket;

    public ArrayList<WhotCard> history = new ArrayList<WhotCard>();

    public Game () {
    }

    public Game (String playerName) {
        Player player1 = new Player(playerName);
        ComputerPlayer computer = new ComputerPlayer();
        Player player2 = (Player) computer;
        players.add(player1);
        //player2.setAsComputer();
        //player2.setName("Computer");
        players.add(player2);
    }

    public Game (String [] playerNames) {
        for(String playerName: playerNames) {
            Player player = new Player(playerName);
            players.add(player);
        }
    }

    public Game (ArrayList<Player> newPlayers) {
        this.players = newPlayers;
        this.activeTurn = 0;
    }

    public void dealCards() {
        this.deck.shuffle();
        this.shareCards(Settings.gameStartHandSize);
        this.table.add(deck.drawFromTop());
        while (getLastPlayedCard().hasSpecialAction()) {
            System.out.println("Centre Card: "+ getLastPlayedCard().info());
            System.out.println("New centre card placed from deck.");
            this.table.add(deck.drawFromTop());
        }
        this.gameMarket = deck;

        /* TODO -
            game market and game deck are essentially the same thing.
            may need to rename gameDeck or just get rid of gameMarket later.
         */

    }

    private void shareCards(int numberOfCards) {
        for (int i = 0; i < numberOfCards ; i++) {
            for(Player player: players) {
                player.pickCard(deck.drawFromTop());
            }
        }
        ComputerPlayer computer = (ComputerPlayer) players.get(1);
        computer.printHandMap();
    }

    public void addToTable(WhotCard card) {
        this.table.add(card);
        this.history.add(card);
    }

    public WhotCard getLastPlayedCard() {
        return table.get(table.size() - 1);
    }

    private Player getNextPlayer() {
        int nextPlayerPosition = activeTurn + 1;
        if (nextPlayerPosition >= players.size()) {
            nextPlayerPosition = nextPlayerPosition - players.size();
        }
        return players.get(nextPlayerPosition);
    }

    private Player getActivePlayer() {
        return  players.get(activeTurn);
    }

    public boolean tableMatch(WhotCard card) {
        WhotCard lastPlayed = getLastPlayedCard();
        if (nextSuitMatch != null) {
            if (card.getSuit() == nextSuitMatch) { nextSuitMatch = null; return true;}
            return card.getSuit() == nextSuitMatch;
        }
        return lastPlayed.getSuit() == card.getSuit() || lastPlayed.getRank() == card.getRank();
    }

    public boolean validMove(WhotCard card) {
        return tableMatch(card) || card.getSpecialAction() == WhotCard.SpecialAction.WILDCARD;
    }

    public void chooseMove() {
        //System.out.println("   B   ");


        if (getActivePlayer().isComputer()) {
            ComputerPlayer computer = (ComputerPlayer) getActivePlayer();

            String option = computer.decideMove(getLastPlayedCard(), nextSuitMatch);
            System.out.println("computer option:"+option);
            System.out.println("");
//            play(option);
//
//            return;
        }


        Scanner scanner = new Scanner(System.in);
        showHand(players.get(activeTurn));

        System.out.println("Please pick a card to play (enter number corresponding to card)");
        System.out.println("Or pick new card from deck (enter 0)");
        System.out.println("(Hint: Choose a card that matches in rank or number to the centre card.)");

        while (!activeTurnPlayed) {
            //System.out.println("   B1   ");
            try {
                // int option = scanner.nextInt();
                String option = scanner.nextLine();
                play(option);
            } catch (java.util.InputMismatchException error) {
                //System.out.println(error); //java.util.InputMismatchException
                System.out.println("Please enter number corresponding to card you want to play.");
                System.out.println("Or enter 0 to pick a new card from deck.");
                scanner.nextLine();
            }
        }

        return;
    }

    public void play(String input) {
        String[] selections = input.split("[\\s+\\D]");

        if (selections.length == 0 || selections[0].isEmpty()) {
            System.out.println("Please enter number corresponding to card you want to play.");
            System.out.println("Or enter 0 to pick a new card from deck.");
            return;
        }

        /*
        if (selections.length > 1) {
            // check have to check that all have same rank and matching rank of centre card,
            // also have to check that they are not special card.
            return;
        }
        */

        for (String i:selections) {
            System.out.println("Selection: "+ i);
        }

        int selection = Integer.parseInt(selections[0]);

        Player activePlayer = players.get(activeTurn);

        if (selection > activePlayer.handSize()) {
            System.out.println("Selection out of range. Please select an option from 0 to " + activePlayer.handSize() + ".");
            return;
        }

        if (selection == 0) {
            activePlayer.pickCard(deck.drawFromTop());
            activeTurnPlayed = true;
            System.out.println(activePlayer.getName() +" picked new card from deck.");
            return;
        }

        WhotCard chosenCard = activePlayer.getCard(selection - 1);

        if (validMove(chosenCard)) {
            System.out.println(activePlayer.getName() +" played " + chosenCard.info());
            WhotCard playedCard = activePlayer.playCard(selection - 1);
            addToTable(playedCard);
            activeTurnPlayed = true;
            if (playedCard.hasSpecialAction()) {
                specialAction(playedCard);
            }
        }
        else {
            System.out.println("Selected card doesn't match center, please try again.");
            return;
        }

        /* Todo -  Add ability to play more than one card
         */
        if (selections.length > 1) {

            for (Object i : selections) {

            }

            //System.out.println(" Multiple card play only allowed for non special cards of matching ranks. Please try again."); return;
        }
        else {

        }

        return;
    }


    private void specialAction(WhotCard specialCard) {

        WhotCard.SpecialAction specialAction = specialCard.getSpecialAction();
        switch (specialAction) {
            case SUSPENSION:
                suspendNextPlayer();
                return;
            case PICKTWO:
                pickTwo();
                return;
            case PICKTHREE:
                pickThree();
                return;
            case HOLDON:
                holdOn();
                return;
            case ANYONE:
                anyOne();
                return;
            case GENERALMART:
                generalMart();
                return;
            case WILDCARD:
                chooseNextSuit();
                return;
        }
    }

    private void suspendNextPlayer() {
        System.out.println("");
        System.out.println("Suspension!");
        skipNextTurn = true;
        System.out.println(getNextPlayer().getName() + " skips a turn.");
        return;
    }

    private void pickTwo() {
        System.out.println("");
        System.out.println("Pick Two!");
        Player nextPlayer = getNextPlayer();
        System.out.println(nextPlayer.getName()+" picks two and skips turn.");
        for (int i = 0; i < 2 ; i++) {
            nextPlayer.pickCard(deck.drawFromTop());
        }
        skipNextTurn = true;
        return;
    }

    private void pickThree() {
        System.out.println("");
        System.out.println("Pick Three!");
        activeTurnPlayed = false;
        nextTurn();
        pickThreeResponse();
        //System.out.println("");
        //System.out.println(getNextPlayer().getName()+" picks three and skips turn.");
        return;
    }


    private void pickThreeResponse() {
        Player activePlayer = getActivePlayer();
        System.out.println(activePlayer.getName() + " to respond to Pick Three.");
        Scanner scanner = new Scanner(System.in);
        showHand(players.get(activeTurn));

        System.out.println("Please pick a card to play (enter number corresponding to card)");
        System.out.println("Or pick three cards from deck (enter 0)");

        while (!activeTurnPlayed) {
            //System.out.println("   D1 - pick three response   ");
            System.out.println("(Hint: You can only block action by playing another 5 card.)");
            try {
                int option = scanner.nextInt();

                if (option <= activePlayer.handSize()) {
                    if (option == 0) {
                        for (int i = 0; i < 3 ; i++) {
                            activePlayer.pickCard(deck.drawFromTop());
                        }
                        System.out.println("<<" + activePlayer.getName() + ">> picked Three and skipped turn.");
                        activeTurnPlayed = true;
                        return;
                    }
                    else {
                        WhotCard chosenCard = activePlayer.getCard(option - 1);
                        if (chosenCard.getSpecialAction() == WhotCard.SpecialAction.PICKTHREE) {
                           System.out.println(activePlayer.getName() + " played " + chosenCard.info());
                           System.out.println("Pick Three Blocked!");
                           WhotCard playedCard = getActivePlayer().playCard(option - 1);
                           addToTable(playedCard);
                           activeTurnPlayed = true;
                        }
                        else {
                            throw new Exception();
                        }
                    }
                }
                else {
                    System.out.println("If you don't have a card to block enter 0 to pick three.");
                }

            } catch (Exception error) {
                System.out.println("Please pick a card to play (enter number corresponding to card)");
                System.out.println("Or pick three cards from deck (enter 0)");
                scanner.nextLine();
            }
        }

        //System.out.println("   D - pick three response   ");
        return;
    }

    private void holdOn() {
        System.out.println("");
        System.out.println("Hold On!");
        System.out.println("It's <<" + players.get(activeTurn).getName() + ">> to play again:");
        //System.out.println("   D - hold on   ");
        activeTurnPlayed = false;
        chooseMove();
        return;
    }

    private void anyOne() {
        System.out.println("");
        System.out.println("Any One!");
        System.out.println("It's " + players.get(activeTurn).getName() + " to play again:");
        activeTurnPlayed = false;
        chooseMove();
        /* allow non-matching card */
        return;
    }

    private void generalMart() {
        System.out.println("");
        System.out.println("Go Gen!");
        activeTurn = nextPosition();
        for (int i = 0; i < players.size() - 1 ; i++) {
            Player player = players.get(activeTurn);
            System.out.println(player.getName()+" picks a card.");
            player.pickCard(deck.drawFromTop());
            activeTurn = nextPosition();
        }
        System.out.println("");
        System.out.println("It's <<" + players.get(activeTurn).getName() + ">> to play again:");
        //System.out.println("   D - gen mart   ");
        activeTurnPlayed = false;
        chooseMove();
        return;
    }

    private void chooseNextSuit() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("You have played a wild card!");
        nextSuitMatch = null;

        while (nextSuitMatch == null) {
            System.out.println("Please enter number corresponding to suit you want to be played next.");
            System.out.println("1. CIRCLE,  2. TRIANGLE,  3. CROSS,  4. SQUARE,  5. STAR");
            try {
                int option = scanner.nextInt();

                if (option > 5 || option < 1) {
                    throw new Exception();
                }
                else {
                    switch (option) {
                        case 1:
                            nextSuitMatch = WhotCard.Suit.CIRCLE;
                            return;
                        case 2:
                            nextSuitMatch = WhotCard.Suit.TRIANGLE;
                            return;
                        case 3:
                            nextSuitMatch = WhotCard.Suit.CROSS;
                            return;
                        case 4:
                            nextSuitMatch = WhotCard.Suit.SQUARE;
                            return;
                        case 5:
                            nextSuitMatch = WhotCard.Suit.STAR;
                            return;
                    }
                }

            } catch (Exception error) {
                scanner.nextLine();
            }
        }
        return;
    }

    private int nextPosition() {
        int next = activeTurn + 1;
        if (next >= players.size()) {
            next = next - players.size();
        }
        return next;
    }

    public void nextTurn() {
        //System.out.println("   E    (processing who to play next...)");
        if (isOver()) return;
        if (skipNextTurn) { activeTurn++; skipNextTurn = false; }
        activeTurn = nextPosition();
        activeTurnPlayed = false;
        //System.out.println("   E2   ");
        return;
    }

    // consider putting this in Player class.
    public void showHand(Player player) {
        int option = 0;
        for(WhotCard card: player.getHand()) {
            option++;
            System.out.println(option + ". " +card.info());
        }
    }

    public void viewStats() {
        String tableLabel = "Last played";
        System.out.println("---------------------------------------------------");
        if (table.size() > 1) {
            //System.out.println("Player 1 has: " + players.get(0).handSize() + " cards.");
            //System.out.println("Player 2 has: " + players.get(1).handSize() + " cards.");
        }
        for(Player player: players) {
            System.out.println("<< "+player.getName()+" >> has " + player.handSize() + " cards");
        }
        System.out.println("");

        if (table.size() == 1) { tableLabel ="Center Card";}
        System.out.println(tableLabel+": "+ getLastPlayedCard().info());
        if (nextSuitMatch != null) {
            System.out.println("Next Suit to Play: "+ nextSuitMatch);
        }
        System.out.println("---------------------------------------------------");
        //  System.out.println("Hidden pile: "+deck.size()+" cards");
    }

    public void viewPlayers() {
        for(Player player: players) {
            System.out.println(player.info());
        }
    }

    public boolean isOver() {
        for (Player player: players) {
            if (player.handSize() < 1 && !getLastPlayedCard().hasSpecialAction()) return true;
        }
        return false;
    }

}
