
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
}

