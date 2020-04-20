import java.util.Arrays;

public class WhotCard {
    private Suit suit;
    private int rank;


    public WhotCard(Suit CardSuit, int CardRank) {
        this.suit = CardSuit;
        this.rank = CardRank;
    }

    public String info() {
        return this.suit + "'" + this.rank;
    }

    public enum Suit {
        CIRCLE,
        TRIANGLE,
        CROSS,
        SQUARE,
        STAR,
        WHOT;

        public int[] allRanks (){
            int[] fullSuitRanks = {1, 2, 3, 4, 5, 7, 8, 10, 11, 12, 13, 14};
            int[] lessSuitRanks = {1, 2, 3, 5, 7, 10, 11, 13, 14};
            int[] starSuitRanks = {1, 2, 3, 4, 5, 7, 8};
            int[] whotSuitRanks = {20, 20, 20, 20, 20};

            switch (this) {
                case CIRCLE:
                case TRIANGLE:
                    return fullSuitRanks;
                case CROSS:
                case SQUARE:
                    return lessSuitRanks;
                case STAR: return starSuitRanks;
                case WHOT: return whotSuitRanks;
            }

            return fullSuitRanks;
        }
    }

    public Suit getSuit() {
        return this.suit;
    }

    public int getRank() {
        return this.rank;
    }

    private int[] specialEffectRanks = {1, 2, 5, 8, 14, 20};

    public Boolean hasSpecialAction() {
       //return Arrays.asList(specialEffectRanks).contains(suit);
        return getSpecialAction() != null;
    }

    public SpecialAction getSpecialAction() {
        SpecialAction specialAction = Settings.specialActions.get(rank);
        return specialAction;
    }

    public enum SpecialAction {
        SUSPENSION,
        /* Next players turn is skipped. */
        PICKTWO,
        /* Next player must pick two cards from deck. */
        PICKTHREE,
        /* Next player must pick three cards from deck. Or may block by playing same
           rank over last played card is available.*/
        HOLDON, ANYONE,
        /* Only one of these can be active in a game.
           HOLDON - player gets a second turn, but card to be played must match last card as usual;
           ANYONE - player gets a second turn and can play any card, matching or not, over the last played card;
        */
        GENERALMART,
        /* All other players must pick one card from deck in order of their turns,
           then player gets a second turn. */
        WILDCARD;
        /* Player gets to choose what suit is played over this card.
           (Player states his choice starting with "I need..") */
    }
}

