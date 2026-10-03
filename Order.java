
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Order class representing a laundry order placed by a customer.
 */
public class Order {
    private String orderId;
    private Customer customer;
    private List<Garment> garments; // Stored using ArrayList
    private LocalDate orderDate;
    private LocalDate readyDate;
    private ServiceStatus status;
    private LinkedList<ServiceStatus> statusHistory; // History using LinkedList
    private String paymentStatus;

    public Order(String orderId, Customer customer, LocalDate orderDate, LocalDate readyDate, String paymentStatus) {
        this.orderId = orderId;
        this.customer = customer;
        this.garments = new ArrayList<>();
        this.orderDate = orderDate;
        this.readyDate = readyDate;
        this.paymentStatus = paymentStatus;
        this.status = ServiceStatus.RECEIVED;
        
        this.statusHistory = new LinkedList<>();
        this.statusHistory.add(ServiceStatus.RECEIVED);
    }

    public void addGarment(Garment garment) {
        this.garments.add(garment);
    }

    public void updateStatus(ServiceStatus newStatus) {
        this.status = newStatus;
        this.statusHistory.add(newStatus);
    }

    public double calculateTotalAmount() {
        double total = 0;
        for (Garment g : garments) {
            total += g.getTotalPrice();
        }
        return total;
    }

    public int getTotalGarmentCount() {
        int count = 0;
        for (Garment g : garments) {
            count += g.getQuantity();
        }
        return count;
    }

    public String getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<Garment> getGarments() {
        return garments;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public LocalDate getReadyDate() {
        return readyDate;
    }

    public void setReadyDate(LocalDate readyDate) {
        this.readyDate = readyDate;
    }

    public ServiceStatus getStatus() {
        return status;
    }

    public LinkedList<ServiceStatus> getStatusHistory() {
        return statusHistory;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
