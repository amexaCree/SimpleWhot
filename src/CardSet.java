import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class CardSet {

    public ArrayList<CardReference> cards = new ArrayList<CardReference>();
//    public HashSet<CardReference> cards = new ArrayList<CardReference>();
    public int numberOfSpecialCards = 0;
    static ArrayList<String> availableRefs = new ArrayList<String>();
    String label;


    public void addCardRef(CardReference ref) {
        cards.add(ref);
    }

//    public void removeCardRef(int index) {
//        CardReference card = cards.get(index);
//        availableRefs.add(suit);
//    }

    public int getCardCount() {
        return cards.size();
    }


//    public String codeCardSetLabel(WhotCard.Suit) {
//        label
//        return label
//    }
    public static class CardReference {
        int index;
        WhotCard card;

        public CardReference(WhotCard card, int index) {
            this.card = card;
            this.index = index;
        }
    }

    public static class SuitSet extends CardSet {
        WhotCard.Suit suit;
        SuitSet(WhotCard.Suit suit) {
            this.suit = suit;
            label = suit.toString();
        }
        public void addCardRef(CardReference ref) {
            super.addCardRef(ref);
            String rank = ref.card.getRank() + "";
            availableRefs.add(rank);
        }
//        public void removeCardRef(CardReference ref) {
//            super.addCardRef(ref);
//            String suit = ref.card.getSuit().toString();
//            availableRefs.add(suit);
//        }
    }

    public static class RankSet extends CardSet {
        int rank;
        WhotCard.SpecialAction specialAction = null;
        RankSet (int rank) {
            this.rank = rank;
            label = ""+rank;
        }
        public void addCardRef(CardReference ref) {
            super.addCardRef(ref);
            String suit = ref.card.getSuit().toString();
            availableRefs.add(suit);
        }
//        public void removeCardRef(CardReference ref) {
//            super.addCardRef(ref);
//            String suit = ref.card.getSuit().toString();
//            availableRefs.add(suit);
//        }
    }
}
