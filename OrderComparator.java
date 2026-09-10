import java.util.Comparator;

/**
 * OrderComparator - עבודה 3 - חלק א
 * Comparator המשווה הזמנות לפי מחיר סופי
 * מיון: ממחיר גבוה למחיר נמוך
 */
public class OrderComparator implements Comparator<Order> {
    
    /**
     * השוואה לפי המחיר הסופי של ההזמנה
     * מחיר גבוה יותר = מקום ראשון בסדר ממויין
     */
    @Override
    public int compare(Order o1, Order o2) {
        return Double.compare(o2.getFinalPrice(), o1.getFinalPrice());
    }
}
