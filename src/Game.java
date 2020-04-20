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
    private Settings settings;

    public Game () {
    }

    public Game (String playerName, Settings settings) {
        Player player1 = new Player(playerName);
        Player player2 = new Player();
        players.add(player1);
        player2.setAsComputer();
        player2.setName("Computer");
        players.add(player2);

        settings = settings;
    }

    public Game (String [] playerNames, Settings settings) {
        for(String playerName: playerNames) {
            Player player = new Player(playerName);
            players.add(player);

            settings = settings;
        }
    }

    public Game (ArrayList<Player> newPlayers) {
        this.players = newPlayers;
        this.activeTurn = 0;
    }

    public void dealCards() {
        this.deck.shuffle();
        this.shareCards(settings.gameStartHandSize);
        this.table.add(deck.drawFromTop());
        while (getLastPlayedCard().getSpecialAction() == WhotCard.SpecialAction.WILDCARD) {
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
        System.out.println("   B   ");
        Scanner scanner = new Scanner(System.in);

        showHand(players.get(activeTurn));

        System.out.println("Please pick a card to play (enter number corresponding to card)");
        System.out.println("Or pick new card from deck (enter 0)");
        System.out.println("(Hint: Choose a card that matches in rank or number to the centre card.)");

        while (!activeTurnPlayed) {
            System.out.println("   B1   ");
            try {
                int option = scanner.nextInt();
                play(option);
            } catch (java.util.InputMismatchException error) {
                //System.out.println(error); //java.util.InputMismatchException
                System.out.println("Please enter number corresponding to card you want to play.");
                System.out.println("Or enter 0 to pick a new card from deck.");
                scanner.nextLine();
            }
        }

        System.out.println("   B2   ");
        return;
    }

    public void play(int selection) {
        Player activePlayer = players.get(activeTurn);
        if (selection <= activePlayer.handSize()) {
            if( selection == 0) {
                activePlayer.pickCard(deck.drawFromTop());
                activeTurnPlayed = true;
                System.out.println(activePlayer.getName() +" picked new card from deck.");
            }
            else{
                WhotCard chosenCard = activePlayer.getCard(selection - 1);
                if (validMove(chosenCard)) {
                    System.out.println(activePlayer.getName() +" played " + chosenCard.info());
                    WhotCard playedCard = activePlayer.playCard(selection - 1);
                    addToTable(playedCard);
                    activeTurnPlayed = true;
                    if (playedCard.hasSpecialAction()) {
                        specialAction(playedCard);
                        System.out.println("   C2   ");
                    }
                }
                else {

                    System.out.println("Selected card doesn't match center, please try again.");
                }
            }
        }
        else {
            System.out.println("Selection out of range. Please select an option from 0 to " + activePlayer.handSize()+".");
        }
        System.out.println("   C3   (activeTurnPlayed: "+ activeTurnPlayed +")");
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
        System.out.println("Suspension!");
        skipNextTurn = true;
        System.out.println("");
        System.out.println(getNextPlayer().getName() + " skips a turn.");
        return;
    }

    private void pickTwo() {
        System.out.println("Pick Two!");
        Player nextPlayer = getNextPlayer();
        System.out.println("");
        System.out.println(nextPlayer.getName()+" picks two and skips turn.");
        for (int i = 0; i < 2 ; i++) {
            nextPlayer.pickCard(deck.drawFromTop());
        }
        skipNextTurn = true;
        return;
    }

    private void pickThree() {
        System.out.println("Pick Three!");
        //System.out.println("");
        //System.out.println(getNextPlayer().getName()+" picks three and skips turn.");
        return;
    }

    private void holdOn() {
        System.out.println("Hold On!");
        System.out.println("It's <<" + players.get(activeTurn).getName() + ">> to play again:");
        System.out.println("   D - hold on   ");
        activeTurnPlayed = false;
        chooseMove();
        return;
    }

    private void anyOne() {
        System.out.println("Any One!");
        System.out.println("It's " + players.get(activeTurn).getName() + " to play again:");
        activeTurnPlayed = false;
        chooseMove();
        /* allow non-matching card */
        return;
    }

    private void generalMart() {
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
        System.out.println("   D - gen mart   ");
        activeTurnPlayed = false;
        chooseMove();
        return;
    }

    private void chooseNextSuit() {
        //System.out.println("Choose next suit special action!");
        Scanner scanner = new Scanner(System.in);
        System.out.println("You have played a wild card!");

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
        System.out.println("   E    (processing who to play next...)");
        if (isOver()) return;
        if (skipNextTurn) { activeTurn++; skipNextTurn = false; }
        activeTurn = nextPosition();
        activeTurnPlayed = false;
        System.out.println("   E2   ");
        return;
    }

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
            if (player.handSize() < 1) return true;
        }
        return false;
    }

}
