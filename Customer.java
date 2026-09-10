/**
 * מחלקת Customer - עבודה 3
 * עדכון: implements Comparable<Customer>
 * מיון לפי יתרת זיכוי מהגבוה לנמוך
 */
public class Customer implements Comparable<Customer> {
    private int customerId;
    private String firstName;
    private String lastName;
    private String street;
    private String city;
    private String zipCode;
    private String phone;
    private String email;
    private double creditBalance;

    public Customer(int customerId, String firstName, String lastName, String street, 
                   String city, String zipCode, String phone, String email, double creditBalance) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
        this.phone = phone;
        this.email = email;
        this.creditBalance = creditBalance;
    }

    public int getCustomerId() { return customerId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getZipCode() { return zipCode; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public double getCreditBalance() { return creditBalance; }

    public void setStreet(String street) { this.street = street; }
    public void setCity(String city) { this.city = city; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setCreditBalance(double creditBalance) { this.creditBalance = creditBalance; }
    public void deductFromBalance(double amount) { this.creditBalance -= amount; }
    public void addToBalance(double amount) { this.creditBalance += amount; }

    @Override
    public int compareTo(Customer other) {
        return Double.compare(other.creditBalance, this.creditBalance);
    }

    @Override
    public String toString() {
        return "Customer{" + "ID=" + customerId + ", Name=" + firstName + " " + lastName +
                ", Balance=" + String.format("%.2f", creditBalance) + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Customer customer = (Customer) obj;
        return customerId == customer.customerId;
    }
}
