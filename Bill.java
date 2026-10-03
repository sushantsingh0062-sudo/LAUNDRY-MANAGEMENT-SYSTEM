
/**
 * Bill class responsible for generating receipt details using StringBuilder.
 */
public class Bill {
    private String billId;
    private Order order;
    private double totalAmount;

    public Bill(String billId, Order order) {
        this.billId = billId;
        this.order = order;
        this.totalAmount = order.calculateTotalAmount();
    }

    public String generateReceipt() {
        StringBuilder sb = new StringBuilder();
        sb.append("====================================================\n");
        sb.append("          LAUNDRY & DRY CLEANING RECEIPT            \n");
        sb.append("====================================================\n");
        sb.append("Bill ID        : ").append(billId).append("\n");
        sb.append("Order ID       : ").append(order.getOrderId()).append("\n");
        sb.append("Customer Name  : ").append(order.getCustomer().getName()).append("\n");
        sb.append("Phone Number   : ").append(order.getCustomer().getPhoneNumber()).append("\n");
        sb.append("Order Date     : ").append(order.getOrderDate()).append("\n");
        sb.append("Ready Date     : ").append(order.getReadyDate()).append("\n");
        sb.append("----------------------------------------------------\n");
        sb.append(String.format("%-14s %-14s %-6s %-10s\n", "Garment", "Service", "Qty", "Amount"));
        sb.append("----------------------------------------------------\n");

        for (Garment g : order.getGarments()) {
            sb.append(String.format("%-14s %-14s %-6d Rs.%.2f\n",
                    g.getGarmentType(), g.getServiceType(), g.getQuantity(), g.getTotalPrice()));
        }

        sb.append("----------------------------------------------------\n");
        sb.append("Total Amount   : Rs. ").append(String.format("%.2f", totalAmount)).append("\n");
        sb.append("Payment Status : ").append(order.getPaymentStatus()).append("\n");
        sb.append("Current Stage  : ").append(order.getStatus()).append("\n");
        sb.append("====================================================\n");
        sb.append("       Thank you for your business! Have a great day! \n");
        sb.append("====================================================\n");

        return sb.toString();
    }

    public String getBillId() {
        return billId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}
