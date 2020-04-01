import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class WhotDeck {
    private ArrayList<WhotCard> cards = new ArrayList<WhotCard>();

    public WhotDeck() {

        for(WhotCard.Suit suit: WhotCard.Suit.values()) {
            //System.out.println(suit);
            for(int rank: suit.allRanks()) {
                //System.out.println(rank);
                cards.add( new WhotCard(suit, rank) );
            }
        }

        //System.out.println(cards.size() + "" + cards.get(10).info());
        //for(WhotCard card: cards) {
        //    System.out.println(card.info());
        //}
    }

    public WhotCard drawFromTop() {
        WhotCard drawnCard = this.cards.get(cards.size() - 1);
        this.cards.remove(cards.size() - 1);
        return drawnCard;
    }

    public void shuffle() {
        Collections.shuffle(this.cards, new Random());
    }

    public void add(WhotCard card) {
        cards.add(card);
    }

    public int size() {
        return cards.size();
    }

    public WhotCard drawAndReplace(int index) {
        return cards.get(index);
    }

    public WhotCard top() {return this.drawAndReplace(cards.size()-1);}

    /* This method may not seem necessary in this game at the moment
     * but may be I'll keep incase it becomes useful in future

    public WhotCard drawAtRandom() {
        Random rand = new Random();
        int drawAt = rand.nextInt(cards.size());
        WhotCard drawnCard = this.cards.get(drawAt);
        this.cards.remove(drawAt);
        return drawnCard;
    } */
}
