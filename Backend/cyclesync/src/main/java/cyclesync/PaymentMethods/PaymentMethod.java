package cyclesync.PaymentMethods;

import javax.persistence.*;

@Entity
public class PaymentMethod {

    // Enum to represent the different types of payment method
    public enum PaymentType {
        CREDIT_CARD,
        PAYPAL,
        APPLE_PAY
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private PaymentType type;

    private String cardHolderName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    private String cardNumber;

    private String expiryDate; // Format "MM/YY"

    private String cvv;

    public PaymentMethod() {
        // Default constructor
    }

    public PaymentMethod(PaymentType type, String cardHolderName, String name, String cardNumber, String expiryDate, String cvv) {
        this.type = type;
        this.cardHolderName = cardHolderName;
        this.name = name;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

    public PaymentType getType() {
        return type;
    }

    public void setType(PaymentType type) {
        this.type = type;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}