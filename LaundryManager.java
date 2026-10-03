
import java.time.LocalDate;
import java.util.*;

/**
 * LaundryManager manages all orders using Java Collections:
 * - ArrayList<Order> for maintaining order list
 * - HashMap<String, Order> for quick Order ID lookup
 * - TreeMap<LocalDate, List<Order>> for keeping orders sorted by Ready Date
 */
public class LaundryManager {
    private List<Order> orderList;
    private Map<String, Order> orderMap;
    private TreeMap<LocalDate, List<Order>> readyDateMap;

    public LaundryManager() {
        this.orderList = new ArrayList<>();
        this.orderMap = new HashMap<>();
        this.readyDateMap = new TreeMap<>();
        loadSampleData();
    }

    public boolean addOrder(Order order) {
        if (orderMap.containsKey(order.getOrderId())) {
            return false;
        }
        orderList.add(order);
        orderMap.put(order.getOrderId(), order);

        readyDateMap.putIfAbsent(order.getReadyDate(), new ArrayList<>());
        readyDateMap.get(order.getReadyDate()).add(order);
        return true;
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orderList);
    }

    public Order getOrderById(String orderId) {
        return orderMap.get(orderId);
    }

    public List<Order> getOrdersByCustomerName(String customerName) {
        List<Order> matchingOrders = new ArrayList<>();
        if (customerName == null || customerName.trim().isEmpty()) {
            return matchingOrders;
        }
        String searchKey = customerName.trim().toLowerCase();
        for (Order order : orderList) {
            if (order.getCustomer().getName().toLowerCase().contains(searchKey)) {
                matchingOrders.add(order);
            }
        }
        return matchingOrders;
    }

    public boolean updateOrderStatus(String orderId, ServiceStatus newStatus) {
        Order order = orderMap.get(orderId);
        if (order != null) {
            order.updateStatus(newStatus);
            return true;
        }
        return false;
    }

    public boolean updatePaymentStatus(String orderId, String paymentStatus) {
        Order order = orderMap.get(orderId);
        if (order != null) {
            order.setPaymentStatus(paymentStatus);
            return true;
        }
        return false;
    }

    public boolean deleteOrder(String orderId) {
        Order order = orderMap.get(orderId);
        if (order != null) {
            orderList.remove(order);
            orderMap.remove(orderId);

            List<Order> listOnDate = readyDateMap.get(order.getReadyDate());
            if (listOnDate != null) {
                listOnDate.remove(order);
                if (listOnDate.isEmpty()) {
                    readyDateMap.remove(order.getReadyDate());
                }
            }
            return true;
        }
        return false;
    }

    public List<Order> getOrdersSortedByOrderDate() {
        List<Order> sortedList = new ArrayList<>(orderList);
        sortedList.sort(Comparator.comparing(Order::getOrderDate));
        return sortedList;
    }

    public List<Order> getOrdersSortedByBillAmount() {
        List<Order> sortedList = new ArrayList<>(orderList);
        sortedList.sort(Comparator.comparingDouble(Order::calculateTotalAmount).reversed());
        return sortedList;
    }

    public TreeMap<LocalDate, List<Order>> getReadyDateMap() {
        return readyDateMap;
    }

    public Map<String, Object> generateServiceReport() {
        Map<String, Object> report = new LinkedHashMap<>();
        int totalOrders = orderList.size();
        int receivedCount = 0;
        int washingCount = 0;
        int pressingCount = 0;
        int readyCount = 0;
        double totalRevenue = 0;

        for (Order order : orderList) {
            totalRevenue += order.calculateTotalAmount();
            switch (order.getStatus()) {
                case RECEIVED: receivedCount++; break;
                case WASHING: washingCount++; break;
                case PRESSING: pressingCount++; break;
                case READY: readyCount++; break;
            }
        }

        report.put("Total Orders", totalOrders);
        report.put("RECEIVED Stage", receivedCount);
        report.put("WASHING Stage", washingCount);
        report.put("PRESSING Stage", pressingCount);
        report.put("READY Stage", readyCount);
        report.put("Total Revenue (Rs.)", String.format("%.2f", totalRevenue));

        return report;
    }

    private void loadSampleData() {
        Customer c1 = new Customer("CUST101", "Rahul Sharma", "9876543210");
        Order o1 = new Order("ORD101", c1, LocalDate.now().minusDays(2), LocalDate.now().plusDays(1), "PAID");
        o1.addGarment(new Garment("Shirt", 3, "Washing"));
        o1.addGarment(new Garment("Pant", 2, "Dry Cleaning"));
        o1.addGarment(new Garment("Jacket", 1, "Pressing"));
        addOrder(o1);

        Customer c2 = new Customer("CUST102", "Priya Singh", "9123456789");
        Order o2 = new Order("ORD102", c2, LocalDate.now().minusDays(1), LocalDate.now().plusDays(2), "PENDING");
        o2.addGarment(new Garment("Saree", 2, "Dry Cleaning"));
        o2.addGarment(new Garment("Suit", 1, "Washing"));
        o2.updateStatus(ServiceStatus.WASHING);
        addOrder(o2);

        Customer c3 = new Customer("CUST103", "Amit Verma", "9988776655");
        Order o3 = new Order("ORD103", c3, LocalDate.now(), LocalDate.now().plusDays(3), "PAID");
        o3.addGarment(new Garment("T-Shirt", 5, "Washing"));
        o3.addGarment(new Garment("Shirt", 2, "Pressing"));
        o3.updateStatus(ServiceStatus.WASHING);
        o3.updateStatus(ServiceStatus.PRESSING);
        o3.updateStatus(ServiceStatus.READY);
        addOrder(o3);
    }
}
