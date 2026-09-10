/**
 * מחלקת מנהל מערכת - מחלקת בסיס.
 * RestAdmin יורשת ממנה.
 *
 * חומר נלמד: מחלקות, Encapsulation, הורשה, Getters/Setters.
 */
public class Admin {

    protected String adminName;
    protected String username;
    protected String password;

    public Admin(String adminName, String username, String password) {
        this.adminName = adminName;
        this.username  = username;
        this.password  = password;
    }

    /** בדיקת כניסה */
    public boolean validateLogin(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public String getAdminName() { return adminName; }
    public String getUsername()  { return username; }
    public String getPassword()  { return password; }

    public void setAdminName(String n) { this.adminName = n; }
    public void setUsername(String u)  { this.username = u; }
    public void setPassword(String p)  { this.password = p; }

    public String toString() {
        return "מנהל מערכת: " + adminName + " | משתמש: " + username;
    }
}
