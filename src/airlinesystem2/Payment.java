/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airlinesystem2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author user
 */
public class Payment {

    private static double payment, totalPayment, subTotalPayment, flightWayFactor, bookingPrice, discount,
            serviceCharge, grandTotalPay;
    private Member memberPay;
    private Staff personInCharge;
    private Flight flightPay;
    private Booking bookingPay;
    private String paymentID;
    private String currentBillDate;
    private static int seatQuantity = 0;
    private static final double DISCOUNT_RATE = 0.1, SERVICE_RATE = 0.05,COMMISSION_RATE=0.03;
    private static double memberAccountBalance = 0.0;
    private static String PAYMENT_PATH = "Payment.txt";
    private static final ArrayList<String[]> memberDetails = new ArrayList<>();
    private static final ArrayList<String[]> paymentDetails = new ArrayList<>();
    
    //constructor 
    public Payment() {
        memberDetails.clear();
        currentBillDate = " ";
        flightPay = new Flight();
        bookingPay = new Booking();
        personInCharge = new Staff();
        memberPay = new Member();
        
    }

    public Payment(Member memberPay, Flight flightPay, Staff personInCharge, Booking bookingPay, String paymentID, String currentBillDate) {
        this.memberPay = memberPay;
        this.personInCharge = personInCharge;
        this.bookingPay = bookingPay;
        this.paymentID = paymentID;
        this.currentBillDate = currentBillDate;
    }

    //composition 
    public Payment(String paymentID) {
        this.paymentID = paymentID;
    }

    public void setMemberPay(Member memberPay) {
        this.memberPay = memberPay;
    }

    public void setPersonInCharge(Staff personInCharge) {
        this.personInCharge = personInCharge;
    }

    public void setBooking(Booking bookingPay) {
        this.bookingPay = bookingPay;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public String getCurrentBillDate() {
        return currentBillDate;
    }

    public String getMemberPayId() {
        if (memberPay == null) {
            memberPay = new Member();
        }
        return memberPay.getMemberId();
    }

    public static double getDISCOUNT_RATE() {
        return DISCOUNT_RATE;
    }

    public static double getSERVICE_RATE() {
        return SERVICE_RATE;
    }

    public static double getCOMMISSION_RATE() {
        return COMMISSION_RATE;
    }

    public static double getGrandTotalPay() {
        return grandTotalPay;
    }

    public static double getDiscount() {
        return discount;
    }

    public static double getTotalPayment() {
        return totalPayment;
    }

    public static double getSubTotalPayment() {
        return subTotalPayment;
    }

    public static double getServiceCharge() {
        return serviceCharge;
    }
    
    public static ArrayList<String[]> getPaymentDetails() {
        return paymentDetails;
    }
    
    public double generateBill(ArrayList<Flight> flights, ArrayList<Booking> bookings) throws ParseException{
        clearPayment();
        // Get the current date in the specified format
        Date billDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        currentBillDate = dateFormat.format(billDate); // Format the current date as a string

        // Generate a unique payment bill ID (e.g., increment a counter)
        int paymentBillCounter = 1; // Initialize this counter as needed
        System.out.println("                                    ====================================================================================================================");
        System.out.println("                                                                                    Payment bill                                                        ");
        System.out.println("                                    ====================================================================================================================");
        System.out.printf("                                     Payment bill ID   : %s\n", autoGeneratePaymentId());
        System.out.printf("                                     Payment bill date : %s\n", currentBillDate);
        System.out.println("                                    --------------------------------------------------------------------------------------------------------------------");
        System.out.println("                                     Selected Flight ID    Selected Seat Quantity     Selected Booking Type      Selected Flight Way      Total Pay (RM)");

        // Loop through the bookings to display and calculate payments        
        for (Booking booking : bookings) {
            flightPay.setFlightID(booking.getSelectedFlightId());
            seatQuantity = booking.getSelectedSeatQty();

            //flight type = booking type
            bookingPay.setSelectedFlightType(booking.getSelectedFlightType());

            bookingPay.setSelectedWay(booking.getSelectedWay());

            // Calculate the payment for the current booking
            payment = calculateTotalPayment(booking, flights);

            // Center-align the text with padding using the alignCenter method and print the booking
            stringFormat(flightPay.getFlightID(), seatQuantity, bookingPay.getSelectedFlightType(), bookingPay.getSelectedWay());

            // Add the payment to the total
            subTotalPayment += payment;
        }
        //calculation for discount 
        discount = subTotalPayment * DISCOUNT_RATE;
        serviceCharge = (subTotalPayment - discount) * SERVICE_RATE;
        grandTotalPay = subTotalPayment - discount + serviceCharge;
        // Print the total payment
        System.out.printf("                                    --------------------------------------------------------------------------------------------------------------------\n");
        System.out.printf("                                                                                                               Sub Total Payment (RM) :     %10.2f%s\n", subTotalPayment, " ");

        System.out.println("                                                                                                           (-)         Discount (10%) :     " + String.format("%10.2f", discount));
        System.out.println("                                                                                                           (+) Service Charge (0.05%) :     " + String.format("%10.2f", serviceCharge));
        System.out.printf("                                    --------------------------------------------------------------------------------------------------------------------\n");
        System.out.printf("                                                                                                                 Grand Total Pay (RM) :     %10.2f \n", grandTotalPay);
        System.out.printf("                                    --------------------------------------------------------------------------------------------------------------------\n");

        return grandTotalPay;
    }

    //toString method
    @Override
    public String toString() {
        return paymentID + "|" + grandTotalPay + "|" + currentBillDate;
    }

    //clear payment 
    public static void clearPayment() {
        payment = 0.0;
        totalPayment = 0.0;
        subTotalPayment = 0.0;
        flightWayFactor = 0.0;
        bookingPrice = 0.0;
        discount = 0.0;
        serviceCharge = 0.0;
        grandTotalPay = 0.0;
    }

    public void stringFormat(String flightId, int seatQuantity, String bookingType, String flightWay) {
        String formattedFlightId = alignCenter(flightId, 10);
        String formattedSeatQuantity = alignCenter(String.valueOf(seatQuantity), 40);
        String formattedBookingType = alignCenter(bookingType, 12);
        String formattedFlightType = alignCenter(flightWay, 40);
        System.out.printf("                                         %s%s%s%s%-10.2f\n",
                formattedFlightId, formattedSeatQuantity, formattedBookingType, formattedFlightType, payment);
    }

    public double calculateTotalPayment(Booking bookings, ArrayList<Flight> flights) {
        Flight selectedFlight = null;
        for (Flight flight : flights) {
            if (bookings.getSelectedFlightId().equals(flight.getFlightID())) {
                selectedFlight = flight;
                break;
            }
        }

        if (selectedFlight != null) {
            // Determine booking type factor based on the selected booking type
            switch (bookings.getSelectedFlightType()) {
                case "Economy" ->
                    bookingPrice = selectedFlight.getEconPrice();
                case "Business" ->
                    bookingPrice = selectedFlight.getBusinessPrice();
                case "Premium" ->
                    bookingPrice = selectedFlight.getPremiumPrice();
                default -> {
                }
            }

            // Determine flight way factor based on the selected flight way
            switch (bookings.getSelectedWay()) {
                case "One way" ->
                    flightWayFactor = 1.0; // No price change for one way
                case "Two way" ->
                    flightWayFactor = 2.0; // Price factor is 2 for two way
            }
            // Handle invalid flight way
            // Calculate the total payment for this booking
            totalPayment = bookingPrice * flightWayFactor * bookings.getSelectedSeatQty();
        }
        return totalPayment;
    }

    // enter-align text with padding
    public static String alignCenter(String text, int totalWidth) {
        // Calculate the number of spaces needed on each side to center-align the text
        int padding = (totalWidth - text.length()) / 2;
        int odd = (totalWidth - text.length()) % 2; // Check odd

        // Create a format string with padding on both sides
        String format = "%" + padding + "s%s%" + padding + "s";
        if (odd != 0) {
            format += " ";
        }

        // Use String.format to center-align the text
        return String.format(format, "", text, "");
    }

    
    //Try to invoked to Main//check account balance
    public static boolean checkAccountBalance(String enteredMemberId, double payAmount) {
        boolean accountCanPay = false;
        try {
            try (BufferedReader reader = new BufferedReader(new FileReader(Member.getMEMBER_FILE()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] details = line.split("\\|");
                    String memberId = details[0];
                    String accountNo = details[7];
                    Double accountBalance = Double.valueOf(details[8]);
                    //check whether the account balance is sufficient or not to pay the bill;
                    if (memberId.equals(enteredMemberId) && accountBalance >= payAmount) {
                        memberAccountBalance = accountBalance - payAmount;
                        details[8] = Double.toString(memberAccountBalance);
                        System.out.println(" =====================================================");
                        System.out.println("|      Your Latest Account Balance Information        |");
                        System.out.println("|=====================================================|");
                        System.out.printf("| Member  ID : %-10s                             |\n", enteredMemberId);
                        System.out.printf("| Account No : %-10s                             |\n", accountNo);
                        System.out.printf("| Account Balance (RM):  %-8.2f                     |\n", memberAccountBalance);
                        System.out.println(" =====================================================");
                        accountCanPay = true;
                    }
                    memberDetails.add(details);
                }
                if (accountCanPay) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error opening the file !\n");
        }
        return false;
    }

    //store account balance 
    public void storeAccountBalance() {
        try {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(Member.getMEMBER_FILE(), false))) {
                for (String[] memberPayment : memberDetails) {
                    String stringMemberPayment = String.join("|", memberPayment);
                    writer.write(stringMemberPayment);
                    writer.newLine();
                }
                writer.close();
            }
        } catch (IOException e) {
            System.out.println("Error to write the data into the file");
        }
        memberDetails.clear();
    }

    //store payment record 
    public void storePaymentBill(String personInchargeId, String enteredMemberID) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PAYMENT_PATH, true))) {
            writer.write(toString() + "|" + personInchargeId + "|" + enteredMemberID);
            writer.newLine();
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + PAYMENT_PATH);
        } catch (IOException e) {
            System.out.println("Error to write the data into the file");
        }
    }

    //autogenerate payment id 
    public String autoGeneratePaymentId() {
        try {
            int paymentNo = 0;
            BufferedReader reader = new BufferedReader(new FileReader(PAYMENT_PATH));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split("\\|");
                String staffID = details[0];
                String prefix = staffID.substring(0, 1); // abstract the T from the string
                paymentNo = Integer.parseInt(staffID.substring(1)); // abstract the integer and increase by one
                paymentNo++;
                this.paymentID = prefix + String.format("%02d", paymentNo); // store inside a string called ticket ID
            }
            reader.close();
        } catch (IOException ex) {
            System.out.println("Error to open the file!\n");
        }
        return paymentID;
    }

    //read payment data 
    public static void readPaymentData() {
        paymentDetails.clear();
        clearPayment();
        try {
            try (BufferedReader reader = new BufferedReader(new FileReader(PAYMENT_PATH))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] details = line.split("\\|");
                    paymentDetails.add(details);                                       // store details into member data array
                    subTotalPayment+=Double.parseDouble(details[1]);
                }
                serviceCharge = subTotalPayment * SERVICE_RATE;
                discount = (subTotalPayment + serviceCharge) * DISCOUNT_RATE;
                grandTotalPay = (subTotalPayment + serviceCharge) - discount;
            }
        } catch (IOException ex) {
            System.out.println("Unable to open the file!");
        }
    }

}