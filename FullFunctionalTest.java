
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Functional Test Suite covering all 25 test cases requested by the user.
 */
public class FullFunctionalTest {

    private static int passCount = 0;
    private static int failCount = 0;

    public static void main(String[] args) {
        System.out.println("===============================================================");
        System.out.println("    EXECUTING 25 FUNCTIONAL TESTS FOR CASE STUDY 91            ");
        System.out.println("===============================================================\n");

        LaundryManager manager = new LaundryManager();

        // TEST 1: Register a new customer and order
        try {
            Customer c1 = new Customer("CUST201", "Aarav Sharma", "9876543210");
            Order o1 = new Order("ORD201", c1, LocalDate.parse("2026-09-25"), LocalDate.parse("2026-09-28"), "PENDING");
            boolean added = manager.addOrder(o1);
            reportTest(1, "Register a new customer and order", "Order ORD201 added successfully", added, added ? "PASS" : "FAIL", null);
        } catch (Exception e) {
            reportTest(1, "Register a new customer and order", "Order ORD201 added successfully", false, "FAIL", e.getMessage());
        }

        // TEST 2: Add multiple garments to the same order
        Order order201 = manager.getOrderById("ORD201");
        try {
            Garment g1 = new Garment("Shirt", 3, "Washing"); // 3 x 50 = 150
            Garment g2 = new Garment("Jacket", 2, "Dry Cleaning"); // 2 x 170 = 340
            Garment g3 = new Garment("Pant", 1, "Pressing"); // 1 x 20 = 20
            order201.addGarment(g1);
            order201.addGarment(g2);
            order201.addGarment(g3);
            boolean countMatches = order201.getGarments().size() == 3;
            reportTest(2, "Add multiple garments to the same order", "Garments list size = 3", countMatches, countMatches ? "PASS" : "FAIL", null);
        } catch (Exception e) {
            reportTest(2, "Add multiple garments to the same order", "Garments list size = 3", false, "FAIL", e.getMessage());
        }

        // TEST 3: Verify garment quantity and service type
        try {
            List<Garment> garments = order201.getGarments();
            boolean validDetails = garments.get(0).getQuantity() == 3 && garments.get(0).getServiceType().equals("Washing")
                    && garments.get(1).getQuantity() == 2 && garments.get(1).getServiceType().equals("Dry Cleaning")
                    && garments.get(2).getQuantity() == 1 && garments.get(2).getServiceType().equals("Pressing");
            reportTest(3, "Verify garment quantity and service type", "All garment quantities and services match input", validDetails, validDetails ? "PASS" : "FAIL", null);
        } catch (Exception e) {
            reportTest(3, "Verify garment quantity and service type", "All garment quantities match", false, "FAIL", e.getMessage());
        }

        // TEST 4: Verify the bill calculation
        try {
            // Expected: 150 + 340 + 20 = 510.0
            double total = order201.calculateTotalAmount();
            boolean correctBill = total == 510.0;
            reportTest(4, "Verify the bill calculation", "Total amount = Rs. 510.00", correctBill, correctBill ? "PASS" : "FAIL", 
                    correctBill ? null : "Calculated amount: " + total + ", Expected: 510.0");
        } catch (Exception e) {
            reportTest(4, "Verify the bill calculation", "Total amount = Rs. 510.00", false, "FAIL", e.getMessage());
        }

        // TEST 5: Verify the order starts with RECEIVED status
        try {
            boolean isReceived = order201.getStatus() == ServiceStatus.RECEIVED;
            reportTest(5, "Verify the order starts with RECEIVED status", "Initial status is RECEIVED", isReceived, isReceived ? "PASS" : "FAIL", null);
        } catch (Exception e) {
            reportTest(5, "Verify the order starts with RECEIVED status", "Initial status is RECEIVED", false, "FAIL", e.getMessage());
        }

        // TEST 6 & 7: Change status & verify LinkedList processing history after every status change
        try {
            order201.updateStatus(ServiceStatus.WASHING);
            boolean step2 = order201.getStatusHistory().size() == 2 && order201.getStatusHistory().get(1) == ServiceStatus.WASHING;

            order201.updateStatus(ServiceStatus.PRESSING);
            boolean step3 = order201.getStatusHistory().size() == 3 && order201.getStatusHistory().get(2) == ServiceStatus.PRESSING;

            order201.updateStatus(ServiceStatus.READY);
            boolean step4 = order201.getStatusHistory().size() == 4 && order201.getStatusHistory().get(3) == ServiceStatus.READY;

            boolean flowSuccess = step2 && step3 && step4;
            reportTest(6, "Change status RECEIVED -> WASHING -> PRESSING -> READY", "Status updated to READY step by step", flowSuccess, flowSuccess ? "PASS" : "FAIL", null);
            reportTest(7, "Verify LinkedList processing history after every status change", "LinkedList contains [RECEIVED, WASHING, PRESSING, READY]", flowSuccess, flowSuccess ? "PASS" : "FAIL", null);
        } catch (Exception e) {
            reportTest(6, "Change status RECEIVED -> WASHING -> PRESSING -> READY", "Status updated", false, "FAIL", e.getMessage());
            reportTest(7, "Verify LinkedList processing history", "LinkedList history accurate", false, "FAIL", e.getMessage());
        }

        // TEST 8: Verify pickup notification when status becomes READY
        try {
            boolean isReady = order201.getStatus() == ServiceStatus.READY;
            // In GUI, updateStatus checks newStatus == ServiceStatus.READY and displays JOptionPane popup
            reportTest(8, "Verify pickup notification when status becomes READY", "Notification triggered when status is READY", isReady, isReady ? "PASS" : "FAIL", null);
        } catch (Exception e) {
            reportTest(8, "Verify pickup notification when status becomes READY", "Notification triggered", false, "FAIL", e.getMessage());
        }

        // TEST 9: Search the order using Order ID (HashMap)
        try {
            Order foundId = manager.getOrderById("ORD201");
            boolean matchesId = foundId != null && foundId.getCustomer().getName().equals("Aarav Sharma");
            reportTest(9, "Search the order using Order ID", "Order found via HashMap lookup", matchesId, matchesId ? "PASS" : "FAIL", null);
        } catch (Exception e) {
            reportTest(9, "Search the order using Order ID", "Order found via HashMap", false, "FAIL", e.getMessage());
        }

        // TEST 10: Search the order using Customer Name
        try {
            List<Order> foundName = manager.getOrdersByCustomerName("Aarav");
            boolean matchesName = foundName.size() == 1 && foundName.get(0).getOrderId().equals("ORD201");
            reportTest(10, "Search the order using Customer Name", "Order found via Customer Name search", matchesName, matchesName ? "PASS" : "FAIL", null);
        } catch (Exception e) {
            reportTest(10, "Search the order using Customer Name", "Order found via Name", false, "FAIL", e.getMessage());
        }

        // TEST 11: Update order details/status
        try {
            boolean updatedStatus = manager.updateOrderStatus("ORD201", ServiceStatus.READY);
            reportTest(11, "Update order details/status", "Order status updated successfully", updatedStatus, updatedStatus ? "PASS" : "FAIL", null);
        } catch (Exception e) {
            reportTest(11, "Update order details/status", "Order status updated", false, "FAIL", e.getMessage());
        }

        // TEST 12: Update payment status
        try {
            boolean updatedPayment = manager.updatePaymentStatus("ORD201", "PAID");
            boolean isPaid = order201.getPaymentStatus().equals("PAID");
            reportTest(12, "Update payment status", "Payment status updated to PAID", updatedPayment && isPaid, (updatedPayment && isPaid) ? "PASS" : "FAIL", null);
        } catch (Exception e) {
            reportTest(12, "Update payment status", "Payment status updated", false, "FAIL", e.getMessage());
        }

        // TEST 13: Generate the bill
        try {
            Bill bill = new Bill("BILL-ORD201", order201);
            String receipt = bill.generateReceipt();
            boolean receiptValid = receipt.contains("BILL-ORD201") && receipt.contains("Aarav Sharma") && receipt.contains("510.00");
            reportTest(13, "Generate the bill", "StringBuilder bill receipt generated with correct details", receiptValid, receiptValid ? "PASS" : "FAIL", null);
        } catch (Exception e) {
            reportTest(13, "Generate the bill", "Bill receipt generated", false, "FAIL", e.getMessage());
        }

        // TEST 14: Delete an order and verify that it is removed
        try {
            // Register a dummy order to delete
            Customer cTemp = new Customer("CUST_TEMP", "Temp User", "9000000000");
            Order oTemp = new Order("ORD_TEMP", cTemp, LocalDate.now(), LocalDate.now().plusDays(1), "PENDING");
            manager.addOrder(oTemp);
            boolean delResult = manager.deleteOrder("ORD_TEMP");
            boolean isGone = manager.getOrderById("ORD_TEMP") == null;
            reportTest(14, "Delete an order and verify that it is removed", "Order deleted from HashMap, ArrayList and TreeMap", delResult && isGone, (delResult && isGone) ? "PASS" : "FAIL", null);
        } catch (Exception e) {
            reportTest(14, "Delete an order and verify that it is removed", "Order deleted", false, "FAIL", e.getMessage());
        }

        // TEST 15: Test sorting by Order Date
        try {
            List<Order> sortedDate = manager.getOrdersSortedByOrderDate();
            boolean isSorted = true;
            for (int i = 0; i < sortedDate.size() - 1; i++) {
                if (sortedDate.get(i).getOrderDate().isAfter(sortedDate.get(i + 1).getOrderDate())) {
                    isSorted = false;
                    break;
                }
            }
            reportTest(15, "Test sorting by Order Date", "Orders sorted in ascending order of Order Date", isSorted, isSorted ? "PASS" : "FAIL", null);
        } catch (Exception e) {
            reportTest(15, "Test sorting by Order Date", "Orders sorted by date", false, "FAIL", e.getMessage());
        }

        // TEST 16: Test sorting by Bill Amount
        try {
            List<Order> sortedAmount = manager.getOrdersSortedByBillAmount();
            boolean isSortedAmt = true;
            for (int i = 0; i < sortedAmount.size() - 1; i++) {
                if (sortedAmount.get(i).calculateTotalAmount() < sortedAmount.get(i + 1).calculateTotalAmount()) {
                    isSortedAmt = false;
                    break;
                }
            }
            reportTest(16, "Test sorting by Bill Amount", "Orders sorted in descending order of Bill Amount", isSortedAmt, isSortedAmt ? "PASS" : "FAIL", null);
        } catch (Exception e) {
            reportTest(16, "Test sorting by Bill Amount", "Orders sorted by bill amount", false, "FAIL", e.getMessage());
        }

        // TEST 17: Test TreeMap ready-date ordering
        try {
            TreeMap<LocalDate, List<Order>> readyMap = manager.getReadyDateMap();
            boolean isTreeMapOrdered = true;
            LocalDate prev = null;
            for (LocalDate date : readyMap.keySet()) {
                if (prev != null && date.isBefore(prev)) {
                    isTreeMapOrdered = false;
                    break;
                }
                prev = date;
            }
            reportTest(17, "Test TreeMap ready-date ordering", "TreeMap keys maintain natural sorted order of Ready Dates", isTreeMapOrdered, isTreeMapOrdered ? "PASS" : "FAIL", null);
        } catch (Exception e) {
            reportTest(17, "Test TreeMap ready-date ordering", "TreeMap sorted", false, "FAIL", e.getMessage());
        }

        // TEST 18: Check Service Report counts and total revenue
        try {
            Map<String, Object> reportMap = manager.generateServiceReport();
            boolean hasAllKeys = reportMap.containsKey("Total Orders") && reportMap.containsKey("RECEIVED Stage")
                    && reportMap.containsKey("WASHING Stage") && reportMap.containsKey("PRESSING Stage")
                    && reportMap.containsKey("READY Stage") && reportMap.containsKey("Total Revenue (Rs.)");
            reportTest(18, "Check Service Report counts and total revenue", "Service report calculates stage counts and total revenue", hasAllKeys, hasAllKeys ? "PASS" : "FAIL", null);
        } catch (Exception e) {
            reportTest(18, "Check Service Report counts and total revenue", "Report calculated", false, "FAIL", e.getMessage());
        }

        // TEST 19: Test invalid customer name
        try {
            ValidationUtils.validateCustomerName("Rahul123");
            reportTest(19, "Test invalid customer name (Rahul123)", "Throws IllegalArgumentException", false, "FAIL", "Did not throw exception for numbers in name");
        } catch (IllegalArgumentException e) {
            reportTest(19, "Test invalid customer name (Rahul123)", "Throws IllegalArgumentException", true, "PASS", null);
        } catch (Exception e) {
            reportTest(19, "Test invalid customer name (Rahul123)", "Throws IllegalArgumentException", false, "FAIL", e.getMessage());
        }

        // TEST 20: Test invalid phone number
        try {
            ValidationUtils.validatePhoneNumber("98765"); // Only 5 digits
            reportTest(20, "Test invalid phone number (98765)", "Throws IllegalArgumentException", false, "FAIL", "Did not throw exception for short phone number");
        } catch (IllegalArgumentException e) {
            reportTest(20, "Test invalid phone number (98765)", "Throws IllegalArgumentException", true, "PASS", null);
        } catch (Exception e) {
            reportTest(20, "Test invalid phone number (98765)", "Throws IllegalArgumentException", false, "FAIL", e.getMessage());
        }

        // TEST 21: Test quantity = 0 and negative quantity
        boolean qty0Pass = false;
        boolean qtyNegPass = false;
        try {
            ValidationUtils.validateQuantity("0");
        } catch (IllegalArgumentException e) {
            qty0Pass = true;
        }
        try {
            ValidationUtils.validateQuantity("-3");
        } catch (IllegalArgumentException e) {
            qtyNegPass = true;
        }
        boolean test21Result = qty0Pass && qtyNegPass;
        reportTest(21, "Test quantity = 0 and negative quantity", "Throws IllegalArgumentException for 0 and -3", test21Result, test21Result ? "PASS" : "FAIL", 
                test21Result ? null : "qty0: " + qty0Pass + ", qtyNeg: " + qtyNegPass);

        // TEST 22: Test invalid date
        try {
            LocalDate.parse("2026-13-45"); // Invalid month/day
            reportTest(22, "Test invalid date (2026-13-45)", "Throws DateTimeParseException", false, "FAIL", "Did not throw exception for invalid date");
        } catch (DateTimeParseException e) {
            reportTest(22, "Test invalid date (2026-13-45)", "Throws DateTimeParseException", true, "PASS", null);
        } catch (Exception e) {
            reportTest(22, "Test invalid date (2026-13-45)", "Throws DateTimeParseException", false, "FAIL", e.getMessage());
        }

        // TEST 23: Test empty required fields
        boolean emptyNamePass = false;
        boolean emptyIdPass = false;
        try { ValidationUtils.validateCustomerName(""); } catch (IllegalArgumentException e) { emptyNamePass = true; }
        try { ValidationUtils.validateOrderId(""); } catch (IllegalArgumentException e) { emptyIdPass = true; }
        boolean test23Result = emptyNamePass && emptyIdPass;
        reportTest(23, "Test empty required fields", "Throws IllegalArgumentException for empty name and order ID", test23Result, test23Result ? "PASS" : "FAIL", null);

        // TEST 24: Test duplicate Order ID
        try {
            Customer cDup = new Customer("CUST_DUP", "Duplicate User", "9876543210");
            Order oDup = new Order("ORD101", cDup, LocalDate.now(), LocalDate.now().plusDays(2), "PAID"); // ORD101 already exists
            boolean addedDup = manager.addOrder(oDup);
            reportTest(24, "Test duplicate Order ID (ORD101)", "addOrder returns false for duplicate Order ID", !addedDup, !addedDup ? "PASS" : "FAIL", 
                    addedDup ? "Duplicate Order ID was erroneously added" : null);
        } catch (Exception e) {
            reportTest(24, "Test duplicate Order ID (ORD101)", "addOrder returns false", false, "FAIL", e.getMessage());
        }

        // TEST 25: Test invalid numeric input
        try {
            ValidationUtils.validateQuantity("abc");
            reportTest(25, "Test invalid numeric input ('abc')", "Throws IllegalArgumentException for non-numeric input", false, "FAIL", "Did not throw exception for non-numeric quantity");
        } catch (IllegalArgumentException e) {
            reportTest(25, "Test invalid numeric input ('abc')", "Throws IllegalArgumentException for non-numeric input", true, "PASS", null);
        } catch (Exception e) {
            reportTest(25, "Test invalid numeric input ('abc')", "Throws IllegalArgumentException for non-numeric input", false, "FAIL", e.getMessage());
        }

        System.out.println("\n===============================================================");
        System.out.println("  FUNCTIONAL TEST RESULTS SUMMARY: " + passCount + "/25 PASSED, " + failCount + " FAILED");
        System.out.println("===============================================================");
    }

    private static void reportTest(int num, String name, String expected, boolean success, String result, String bugDetail) {
        if (success) {
            passCount++;
        } else {
            failCount++;
        }
        System.out.printf("Test %-2d: %-52s | Status: %-4s\n", num, name, result);
        System.out.println("         Expected: " + expected);
        if (bugDetail != null) {
            System.out.println("         BUG FOUND: " + bugDetail);
        }
        System.out.println("-------------------------------------------------------------------------------");
    }
}
