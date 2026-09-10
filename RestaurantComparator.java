import java.util.Comparator;

/**
 * RestaurantComparator - עבודה 3 - חלק א
 * Comparator המשווה מסעדות לפי דירוג
 * מיון: מדירוג גבוה לדירוג נמוך
 */
public class RestaurantComparator implements Comparator<Restaurant> {
    
    /**
     * השוואה לפי דירוג מסעדה
     * דירוג גבוה יותר = מקום ראשון בסדר ממויין
     */
    @Override
    public int compare(Restaurant r1, Restaurant r2) {
        return Double.compare(r2.getRating(), r1.getRating());
    }
}
