import java.util.ArrayList;

public class Game {
    public int id;
    public ArrayList<Player> players = new ArrayList<Player>();
    public int activeTurn = 0;
    public int startingHandSize = 7;

    private WhotDeck deck = new WhotDeck();
    public WhotDeck table = new WhotDeck();
    public WhotDeck gameMarket;

    public ArrayList<WhotCard> history;

    public Game () {
    }

    public Game (String playerName) {
        Player player1 = new Player(playerName);
        Player player2 = new Player();
        players.add(player1);
        player2.setAsComputer();
        player2.setName("Computer");
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
        for (int i = 0; i < startingHandSize ; i++) {
            for(Player player: players) {
                player.pickCard(deck.drawFromTop());
            }
        }
        this.table.add(deck.drawFromTop());
        this.gameMarket = deck;

        /* TODO -
            game market and game deck are essentially the same thing.
            may need to rename gameDeck or just get rid of gameMarket later.
         */

    }

    public boolean tableMatch(WhotCard card) {
        if(table.top().getSuit() == card.getSuit()){
            return true;
        }
        if(table.top().getRank() == card.getRank()){
            return true;
        }
        return false;
    }

    public void play(int selection) {
        Player activePlayer = players.get(activeTurn);
        if (selection <= activePlayer.handSize()) {
            if( selection == 0) {
                activePlayer.pickCard(deck.drawFromTop());
            }
            else{
                WhotCard chosenCard = activePlayer.getCard(selection - 1);
                if (tableMatch(chosenCard)) {
                    WhotCard playedCard = activePlayer.playCard(selection - 1);
                    table.add(playedCard);
                }
                else {

                }
            }
//
            endTurn();
        }
        else {
            System.out.println("invalid selection, please try again");
        }

    }

    private void endTurn() {
        activeTurn++;
        if (activeTurn == players.size()){
            activeTurn = 0;
        }
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
        if (table.size() == 0) { tableLabel ="Table";}
        System.out.println(tableLabel+": "+table.drawAndReplace(table.size() - 1).info());
        System.out.println("Hidden pile: "+deck.size()+" cards");
        if (table.size() > 0) {
            System.out.println("Player 1 has: " + players.get(0).handSize() + " cards");
            System.out.println("Player 2 has: " + players.get(1).handSize() + " cards");
            System.out.println("");
        }
    }

    public void viewPlayers() {
        for(Player player: players) {
            System.out.println(player.info());
        }
    }

    public boolean isOver() {
        return false;
    }



}
