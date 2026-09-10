/**
 * CuisineType Enum - עבודה 4 - חלק ט
 * סוגי מטבחים שונים במערכת
 * משמש ב-ComboBox בממשק המשתמש
 */
public enum CuisineType {
    ITALIAN("אִיטַלְקִי"),
    CHINESE("סִינִי"),
    JAPANESE("יַפָּנִי"),
    MEDITERRANEAN("תִּיכוֹן יַמִּי"),
    MEXICAN("מקסיקני"),
    INDIAN("הִנְדִּי"),
    FRENCH("צָרְפָתִי"),
    AMERICAN("אמריקני"),
    VEGETARIAN("וֶגָטָרִי"),
    VEGAN("טִבְעוֹנִי");

    private final String hebrewName;

    CuisineType(String hebrewName) {
        this.hebrewName = hebrewName;
    }

    public String getHebrewName() {
        return hebrewName;
    }

    @Override
    public String toString() {
        return hebrewName;
    }
}
