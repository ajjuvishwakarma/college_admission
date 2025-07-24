package collegeadmission.com;

public class Amount {
    private int amountId;
    private String description;
    private double amount;

    // Constructor
    public Amount(int amountId, String description, double amount) {
        this.amountId = amountId;
        this.description = description;
        this.amount = amount;
    }

    // Getters
    public int getAmountId() {
        return amountId;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    // To show in comboBox
    @Override
    public String toString() {
        return description + " (₹" + amount + ")";
    }
}