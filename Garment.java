
/**
 * Garment class representing individual item details in an order.
 */
public class Garment {
    private String garmentType;
    private int quantity;
    private String serviceType;
    private double unitPrice;
    private double totalPrice;

    public Garment(String garmentType, int quantity, String serviceType) {
        this.garmentType = garmentType;
        this.quantity = quantity;
        this.serviceType = serviceType;
        this.unitPrice = calculateUnitPrice(garmentType, serviceType);
        this.totalPrice = this.unitPrice * quantity;
    }

    public static double calculateUnitPrice(String garmentType, String serviceType) {
        double basePrice = 50;

        switch (garmentType) {
            case "Shirt": basePrice = 50; break;
            case "Pant": basePrice = 70; break;
            case "T-Shirt": basePrice = 40; break;
            case "Jacket": basePrice = 120; break;
            case "Saree": basePrice = 150; break;
            case "Suit": basePrice = 200; break;
            default: basePrice = 50; break;
        }

        switch (serviceType) {
            case "Washing": return basePrice;
            case "Dry Cleaning": return basePrice + 50;
            case "Pressing": return 20;
            default: return basePrice;
        }
    }

    public String getGarmentType() {
        return garmentType;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getServiceType() {
        return serviceType;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    @Override
    public String toString() {
        return garmentType + " (" + serviceType + ") x " + quantity + " = Rs. " + totalPrice;
    }
}
