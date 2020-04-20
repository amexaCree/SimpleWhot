import java.util.Hashtable;


public class Settings {

    //Game Constraints
    static Hashtable<Integer, WhotCard.SpecialAction> specialActions = new Hashtable<Integer, WhotCard.SpecialAction>();
    static int gameStartHandSize = 7;

    Settings() {
        SetSpecialActions();
    }

    private static void SetSpecialActions() {
        specialActions.put(1, WhotCard.SpecialAction.HOLDON);
        specialActions.put(2, WhotCard.SpecialAction.PICKTWO);
        //specialActions.put(5, WhotCard.SpecialAction.PICKTHREE);
        specialActions.put(8, WhotCard.SpecialAction.SUSPENSION);
        specialActions.put(14, WhotCard.SpecialAction.GENERALMART);
        specialActions.put(20, WhotCard.SpecialAction.WILDCARD);
    }


}
