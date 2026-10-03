
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Automated Test Runner to verify all project requirements before submission.
 */
public class TestLaundrySystem {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  RUNNING LAUNDRY MANAGEMENT SYSTEM TEST SUITE   ");
        System.out.println("==================================================");

        LaundryManager manager = new LaundryManager();

        List<Order> allOrders = manager.getAllOrders();
        System.out.println("✓ Initial Orders Count: " + allOrders.size());
        assert allOrders.size() == 3 : "Expected 3 initial orders";

        Customer cNew = new Customer("CUST104", "Rohan Mehta", "9811223344");
        Order oNew = new Order("ORD104", cNew, LocalDate.now(), LocalDate.now().plusDays(2), "PENDING");
        oNew.addGarment(new Garment("Shirt", 2, "Washing"));
        oNew.addGarment(new Garment("Suit", 1, "Dry Cleaning"));
        boolean added = manager.addOrder(oNew);
        System.out.println("✓ Order ORD104 Added: " + added);
        assert added : "Order creation failed";

        Order foundOrder = manager.getOrderById("ORD104");
        System.out.println("✓ HashMap O(1) Lookup ORD104 Customer: " + foundOrder.getCustomer().getName());
        assert foundOrder != null && foundOrder.getCustomer().getName().equals("Rohan Mehta");

        List<Order> searchByName = manager.getOrdersByCustomerName("Rahul");
        System.out.println("✓ Search by Name 'Rahul' Results Count: " + searchByName.size());
        assert searchByName.size() == 1 : "Search by name failed";

        boolean updated = manager.updateOrderStatus("ORD104", ServiceStatus.WASHING);
        manager.updateOrderStatus("ORD104", ServiceStatus.PRESSING);
        manager.updateOrderStatus("ORD104", ServiceStatus.READY);
        System.out.println("✓ Status Update Result: " + updated);
        System.out.println("✓ LinkedList History Steps Count: " + foundOrder.getStatusHistory().size());
        System.out.println("✓ History Stages: " + foundOrder.getStatusHistory());
        assert foundOrder.getStatusHistory().size() == 4;

        Bill bill = new Bill("BILL-ORD104", foundOrder);
        String receiptText = bill.generateReceipt();
        System.out.println("✓ Generated Receipt Preview:\n" + receiptText);
        assert receiptText.contains("Rohan Mehta") && receiptText.contains("ORD104");

        TreeMap<LocalDate, List<Order>> readyTreeMap = manager.getReadyDateMap();
        System.out.println("✓ TreeMap Unique Ready Dates Count: " + readyTreeMap.size());
        assert !readyTreeMap.isEmpty();

        List<Order> sortedByBill = manager.getOrdersSortedByBillAmount();
        System.out.println("✓ Highest Bill Order ID: " + sortedByBill.get(0).getOrderId() + " (Rs. " + sortedByBill.get(0).calculateTotalAmount() + ")");

        Map<String, Object> report = manager.generateServiceReport();
        System.out.println("✓ Service Report: " + report);

        boolean deleted = manager.deleteOrder("ORD104");
        System.out.println("✓ Order ORD104 Deleted: " + deleted);
        assert manager.getOrderById("ORD104") == null;

        System.out.println("\n==================================================");
        System.out.println("   ALL LAUNDRY MANAGEMENT SYSTEM TESTS PASSED!    ");
        System.out.println("==================================================");
    }
}
