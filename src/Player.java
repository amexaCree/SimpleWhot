import java.util.ArrayList;

public class Player {
    public int id;
    public String name;
    private ArrayList<WhotCard> hand = new ArrayList<WhotCard>();
    private boolean isComputer = false;

    public Player() {
    }

    public Player(String name) {
        this.name = name;
    }

    public void setAsComputer() {
        this.isComputer = true;
    }


    public int handSize() {
        return this.hand.size();
    }

    public void pickCard(WhotCard cardPicked) {
        this.hand.add(cardPicked);
    }

    public WhotCard playCard(int cardOption) {
        WhotCard chosenCard = this.hand.get(cardOption);
        this.hand.remove(cardOption);
        return chosenCard;
    }

    public WhotCard getCard(int index) {
        return this.hand.get(index);
    }

    public ArrayList<WhotCard> getHand() {
        return this.hand;
    }

    public String getName() {
        if (this.name != null) {return this.name;}
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String info() {
        return this.name +" has "+ this.handSize() +"cards. (isComputer: "+ this.isComputer+")";
    }
}
