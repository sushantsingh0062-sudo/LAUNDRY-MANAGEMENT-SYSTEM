
/**
 * Helper class for validating form input fields.
 */
public class ValidationUtils {

    public static void validateCustomerName(String name) throws IllegalArgumentException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be empty.");
        }
        if (!name.matches("[a-zA-Z\\s]+")) {
            throw new IllegalArgumentException("Customer name must contain only alphabets and spaces.");
        }
    }

    public static void validatePhoneNumber(String phone) throws IllegalArgumentException {
        if (phone == null || !phone.matches("\\d{10}")) {
            throw new IllegalArgumentException("Phone number must be exactly 10 numeric digits.");
        }
    }

    public static void validateQuantity(String qtyStr) throws IllegalArgumentException {
        if (qtyStr == null || qtyStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Garment quantity cannot be empty.");
        }
        try {
            int qty = Integer.parseInt(qtyStr.trim());
            if (qty <= 0) {
                throw new IllegalArgumentException("Garment quantity must be greater than 0.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Garment quantity must be a valid number.");
        }
    }

    public static void validateOrderId(String orderId) throws IllegalArgumentException {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be empty.");
        }
    }
}
