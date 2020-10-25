
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class ComputerPlayer extends Player {
    boolean isHandMapped = false;
    private ArrayList<String> availableSetLabels = new ArrayList<String>();
    private ArrayList<CardSet> cardSets = new ArrayList<CardSet>();
    private Hashtable<String, CardSet> cardSetMap = new Hashtable<String, CardSet>();

    public ComputerPlayer() {
        super();
        this.setAsComputer();
//        this.createCardSetMap();
        this.buildSuitMap();
        this.buildRankMap();
    }

    public ComputerPlayer(Player activePlayer) {
        super();
        this.setAsComputer();
//        this.createCardSetMap();
        this.buildSuitMap();
        this.buildRankMap();
    }


    public void pickCard(WhotCard cardPicked) {
        if (isHandMapped) { isHandMapped = false; }
        super.pickCard(cardPicked);
        addToMap(cardPicked);
    }

    public WhotCard playCard(int cardOption) {
//        removeRef(cardOption);
        removeFromMap(cardOption);
        return super.playCard(cardOption);
    }

    public String decideMove(WhotCard centreCard, WhotCard.Suit nextSuit) {
        int move = 0;
        int move2 = checkHand(centreCard.getSuit(), centreCard.getRank(), centreCard.hasSpecialAction(), nextSuit);
        String option = "["+move+"]";
        return option;
    }


    ArrayList<WhotCard> computerHand = getHand();
    WhotCard.Suit suitToMatch;
    int rankToMatch;

    ArrayList<Integer> suitMatches = new ArrayList<Integer>();
    ArrayList<Integer> rankMatches = new ArrayList<Integer>();
    ArrayList<Integer> ranks = new ArrayList<Integer>();

    int wildCards = 0;

//    private HashMap HandMap = new HashMap();
//    private void buildHandMap() {}

    private HashMap HandSuitMap = new HashMap();
    private HashMap HandRankMap = new HashMap();

    // saves array of ranks of a suit and array of suit of a rank
    // would it be better off saving indexes instead?
    // but then indexes can change when a card is played.

    private void buildSuitMap() {
        HandSuitMap.put("CIRCLE_set", new ArrayList<Integer>());
        HandSuitMap.put("SQUARE_set", new ArrayList<Integer>());
        HandSuitMap.put("CROSS_set", new ArrayList<Integer>());
        HandSuitMap.put("TRIANGLE_set", new ArrayList<Integer>());
        HandSuitMap.put("STAR_set", new ArrayList<Integer>());
    }
    private void buildRankMap() {
        HandRankMap.put( "value_1",  new ArrayList<WhotCard.Suit>());
        HandRankMap.put( "value_2",  new ArrayList<WhotCard.Suit>());
        HandRankMap.put( "value_3",  new ArrayList<WhotCard.Suit>());
        HandRankMap.put( "value_4",  new ArrayList<WhotCard.Suit>());
        HandRankMap.put( "value_5",  new ArrayList<WhotCard.Suit>());
        HandRankMap.put( "value_7",  new ArrayList<WhotCard.Suit>());
        HandRankMap.put( "value_8",  new ArrayList<WhotCard.Suit>());
        HandRankMap.put("value_10",  new ArrayList<WhotCard.Suit>());
        HandRankMap.put("value_11",  new ArrayList<WhotCard.Suit>());
        HandRankMap.put("value_12",  new ArrayList<WhotCard.Suit>());
        HandRankMap.put("value_13",  new ArrayList<WhotCard.Suit>());
        HandRankMap.put("value_14",  new ArrayList<WhotCard.Suit>());
    }

    private void addToMap(WhotCard card) {
        if (card.getSuit() == WhotCard.Suit.WHOT) {
            wildCards++;
        }
        else {
            WhotCard.Suit suit = card.getSuit();
            int rank = card.getRank();

            ArrayList suitSet = (ArrayList) HandSuitMap.get(suit+"_set");
            ArrayList rankSet = (ArrayList) HandRankMap.get("value_"+rank);

            suitSet.add(rank);
            rankSet.add(suit);


            HandSuitMap.put(suit+"_set", suitSet);
            HandRankMap.put("value_"+rank, rankSet);
        }
    }

    private void removeFromMap (int index) {
        WhotCard card = this.getCard(index);

        if (card.getSuit() == WhotCard.Suit.WHOT) {
            wildCards--;
        }
        else {
            WhotCard.Suit suit = card.getSuit();
            int rank = card.getRank();

            ArrayList suitSet = (ArrayList) HandSuitMap.get(suit+"_set");
            ArrayList rankSet = (ArrayList) HandRankMap.get("value_"+rank);

            suitSet.removeIf(n -> n.equals(rank));
            rankSet.removeIf(n -> n == suit);


            HandSuitMap.put(suit+"_set", suitSet);
            HandRankMap.put("value_"+rank, rankSet);
        }
    }

    public void printHandMap() {
        for (Object i : HandSuitMap.keySet()) {
            System.out.println(i);
            ArrayList set = (ArrayList) HandSuitMap.get(i);
            for (Object item: set) {
                System.out.println("   "+item);
            }
        }
        for (Object i : HandRankMap.keySet()) {
            System.out.println(i);
            ArrayList set = (ArrayList) HandRankMap.get(i);
            for (Object item: set) {
                System.out.println("   "+item);
            }
        }
        System.out.println(wildCards+" WHOT'20 CARDS");
    }

    private WhotCard.Suit chooseNextSuit() {
        WhotCard.Suit suit = null;

        return suit;
    }

    private int checkHand(WhotCard.Suit centerSuit, int centerRank, boolean specialRank, WhotCard.Suit nextSuit) {
        int move = 0;

        ArrayList<WhotCard.Suit> r = getRankMatches(centerRank);
        ArrayList<Integer> s = getSuitMatches(centerSuit);

        int sitch = getSitch(r, s);

        System.out.println("");
        System.out.println("centerRank: " + centerRank);
        System.out.println("centerSuit: " + centerSuit);
        System.out.println("RankMatches: " + r);
        System.out.println("SuitMatches: " + s);
        System.out.println("Sitch: " + sitch);

        return move;
    }

    private ArrayList<WhotCard.Suit> getRankMatches(int rank) {
        ArrayList<WhotCard.Suit> set = new ArrayList<WhotCard.Suit>();
        set = (ArrayList) HandRankMap.get("value_"+rank);
        return set;
    }

    private ArrayList<Integer> getSuitMatches(WhotCard.Suit suit) {
        ArrayList<Integer> set = new ArrayList<Integer>();
        set = (ArrayList) HandSuitMap.get(suit+"_set");
        return set;
    }

    private int getSitch(ArrayList<WhotCard.Suit> r, ArrayList<Integer> s) {
        int sitch = 0;

        if (r.size() == 0 && s.size() == 0) {
            if (wildCards > 0) {
                sitch = 1;
            }
        }

        if (r.size() == 0) {

        }

        return sitch;
    }

    /*** Situation Map
     *  Sitch 0 == pick card     (r=s=0, w=0)
     *  Sitch 1 == wildcard      (r=s=0, w>0)
     *
     *  Sitch 2 == single option (r=0, s=1 OR r=1, s=0)
     *
     *
     */






    /***
    ArrayList<Integer> suitMatchSpecialCards = new ArrayList<Integer>();

    private int checkHand(WhotCard.Suit centerSuit, int centerRank, boolean specialRank, WhotCard.Suit nextSuit) {
        suitToMatch = centerSuit;
        rankToMatch = centerRank;

        if (nextSuit != null) { suitToMatch = nextSuit; rankToMatch = 0;}


        for (WhotCard card: computerHand) {
            ranks.add(card.getRank());

            if (card.getSpecialAction() == WhotCard.SpecialAction.WILDCARD) {
                wildCards.add(computerHand.indexOf(card));
            }
            if (card.getSuit() == suitToMatch) {
                suitMatches.add(computerHand.indexOf(card));

                if (card.hasSpecialAction()) {
                    suitMatchSpecialCards.add(computerHand.indexOf(card));
                }

            }
            else {
                if (card.getSuit() == WhotCard.Suit.CIRCLE) {
                    CIRCLESet.add(computerHand.indexOf(card));
                }
                if (card.getSuit() == WhotCard.Suit.SQUARE) {
                    SQUARESet.add(computerHand.indexOf(card));
                }
                if (card.getSuit() == WhotCard.Suit.TRIANGLE) {
                    TRIANGLESet.add(computerHand.indexOf(card));
                }
                if (card.getSuit() == WhotCard.Suit.CROSS) {
                    CROSSSet.add(computerHand.indexOf(card));
                }
                if (card.getSuit() == WhotCard.Suit.STAR) {
                    STARSet.add(computerHand.indexOf(card));
                }
            }
            if (card.getRank() == rankToMatch) {
                rankMatches.add(computerHand.indexOf(card));
            }
        }


        if (suitMatches.size() == 0 && rankMatches.size() == 0 ) {
            if (wildCards.size() == 0) {
                return 0;
            }
            else {
                return wildCards.get(0);
            }
        }

        if (suitMatches.size() == 1 && rankMatches.size() == 0 ) {
            return suitMatches.get(0);
        }

        if (suitMatches.size() == 0 && rankMatches.size() == 1 ) {
            return rankMatches.get(0);
        }

        if (suitMatches.size() == 1 && rankMatches.size() == 1 ) {

                return rankMatches.get(0);
//            }
//            else {
//                return suitMatches.get(0);
//            }

        }


//        if (suitMatches.size() == 1 && rankMatches.size() == 1 ) {
//            ArrayList<Integer> rankMatchSuitMatches = new ArrayList<Integer>();
//            ArrayList<Integer> rankMatchSuitMatchesSpecialCards = new ArrayList<Integer>();
//            for (int i=0; i<computerHand.size(); i++) {
//                if (!suitMatches.contains(i) && !rankMatches.contains(i)) {
//                    if (computerHand.get(rankMatches.get(0)).getSuit() == computerHand.get(i).getSuit()) {
//                        rankMatchSuitMatches.add(i);
//                        if (computerHand.get(i).hasSpecialAction()) {
//                            rankMatchSuitMatchesSpecialCards.add(i);
//                        }
//                    }
//                }
//            }
//
//            if (rankMatchSuitMatchesSpecialCards.size() > suitMatchSpecialCards.size()) {
//                return rankMatches.get(0);
//            }
//            else {
//                return suitMatches.get(0);
//            }
//
//        }


        if (suitMatches.size() > 1 && rankMatches.size() == 0 ) {
            if (suitMatchSpecialCards.size() > 0) {
                if (suitMatches.size() > suitMatchSpecialCards.size()) {
                    for (int index: suitMatches) {

                    }
                }
            }

        }
        if (suitMatches.size() > 1 && rankMatches.size() == 1 ) {

        }

        if (suitMatches.size() == 0 && rankMatches.size() > 1 ) {

        }


        if (suitMatches.size() == 1 && rankMatches.size() > 1 ) {

        }

        if (suitMatches.size() > 1 && rankMatches.size() > 1 ) {

        }

        return 0;
    }
 ***/

    private int wildCardSuitPicker() {
        return 1;
    }

    private int moveCalculator() {
        return 0;
    }

    private ArrayList<Integer> buildSpecialActionChain(int startIndex) {
        int pointer = 0;
        ArrayList<Integer> specialActionChain = new ArrayList<Integer>();
        specialActionChain.add(startIndex);
        ArrayList<WhotCard> computerHand = getHand();

        while (true) {
            int currentIndex = specialActionChain.get(pointer);
            WhotCard.Suit suitToLink = computerHand.get(currentIndex).getSuit();
            int rankToLink = computerHand.get(currentIndex).getRank();
            ArrayList<Integer> possibleSuitPlays = new ArrayList<Integer>();
            ArrayList<Integer> possibleRankPlays = new ArrayList<Integer>();

            for (int i = 0; i < computerHand.size(); i++) {

                if (computerHand.get(i).getSuit() == suitToLink) {
                    possibleSuitPlays.add(i);
                }
                if (computerHand.get(i).getRank() == rankToLink) {
                    possibleRankPlays.add(i);
                }
            }
        }


    }

    private void removeRef(int index) {
        WhotCard card = this.getCard(index);
        WhotCard.Suit suit = card.getSuit();
        int rank = card.getRank();
    }

    private void mapHand() {
        ArrayList<WhotCard> computerHand = getHand();
        for(WhotCard card: computerHand) {
            int index = computerHand.indexOf(card);
            CardSet.CardReference ref = new CardSet.CardReference(card, index);
            String rank = ""+card.getRank();
            String suit = card.getSuit().toString();
            updateCardSet(rank, ref);
            updateCardSet(suit, ref);
        }

        isHandMapped = true;
    }

    private void updateCardSet(String label, CardSet.CardReference ref) {
        CardSet cardset = getCardSet(label);
        cardset.addCardRef(ref);
        cardSetMap.replace(label, cardset);
    }

//    private void removeRefCardSet(String label, index) {
//        CardSet cardset = getCardSet(label);
//        cardset.removeCardRef(index);
//        cardSetMap.replace(label, cardset);
//    }

    private CardSet getCardSet(String label) {
        return cardSetMap.get(label);
    }

    private void createCardSetMap() {
        cardSetMap.put("1", new CardSet.RankSet(1));
        cardSetMap.put("2", new CardSet.RankSet(2));
        cardSetMap.put("3", new CardSet.RankSet(3));
        cardSetMap.put("4", new CardSet.RankSet(4));
        cardSetMap.put("5", new CardSet.RankSet(5));
        cardSetMap.put("7", new CardSet.RankSet(7));
        cardSetMap.put("8", new CardSet.RankSet(8));
        cardSetMap.put("10", new CardSet.RankSet(10));
        cardSetMap.put("11", new CardSet.RankSet(11));
        cardSetMap.put("12", new CardSet.RankSet(12));
        cardSetMap.put("13", new CardSet.RankSet(13));
        cardSetMap.put("14", new CardSet.RankSet(14));
        cardSetMap.put("WHOT", new CardSet.SuitSet(WhotCard.Suit.WHOT));
        cardSetMap.put("CIRCLE", new CardSet.SuitSet(WhotCard.Suit.CIRCLE));
        cardSetMap.put("TRIANGLE", new CardSet.SuitSet(WhotCard.Suit.TRIANGLE));
        cardSetMap.put("CROSS", new CardSet.SuitSet(WhotCard.Suit.CROSS));
        cardSetMap.put("SQUARE", new CardSet.SuitSet(WhotCard.Suit.SQUARE));
        cardSetMap.put("STAR", new CardSet.SuitSet(WhotCard.Suit.STAR));
    }
}
