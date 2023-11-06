package airlinesystem2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Booking implements Systematic {

    //extra method for booking
    private Member selectedMember;
    private Staff selectedStaff;
    private Flight selectedFlight;
    private Payment payment;
    private String bookingID, selectedWay, selectedFlightType, selectedFlightID;
    private int selectedSeatQty, loopGenerateID;
    private static int availableSeatEco = 0, availableSeatBus = 0, availableSeatPre = 0,
            totalSeatEcoQty = 0, totalSeatPreQty = 0, totalSeatBusQty = 0; //use for special method handling 

    private static String BOOKING_PATH = "Booking.txt";
    private static ArrayList<String[]> bookingData = new ArrayList<>(); //use for read data from booking text file 

    //constructor 
    public Booking() {
        this.bookingID = " ";
        this.selectedFlightType = " ";
        this.selectedWay = " ";
        this.selectedFlight = new Flight();
        this.selectedStaff = new Staff();
        this.selectedMember = new Member();
    }

    public Booking(Member selectedMember, Staff selectedStaff, Flight selectedFlight, Payment payment, String selectedFlightID,
            String bookingID, String selectedWay, String selectedFlightType) {
        this.selectedMember = selectedMember;
        this.selectedStaff = selectedStaff;
        this.selectedFlight = selectedFlight;
        this.payment = payment;
        this.selectedFlightID = selectedFlightID;
        this.bookingID = bookingID;
        this.selectedWay = selectedWay;
        this.selectedFlightType = selectedFlightType;
    }

    //getter method  
    public String getBookingId() {
        return bookingID;
    }

    public Member getSelectedMember() {
        return selectedMember;
    }

    public Staff getSelectedStaff() {
        return selectedStaff;
    }

    public Payment getPayment() {
        return payment;
    }

    public static String getBOOKING_PATH() {
        return BOOKING_PATH;
    }

    public String getMemberId() {
        if (selectedMember == null) {
            selectedMember = new Member();
        }
        return selectedMember.getMemberId();
    }

    public String getStaffId() {
        if (selectedStaff == null) {
            selectedStaff = new Staff();
        }
        return selectedStaff.getStaffId();
    }

    public String getFlightId() {
        if (selectedFlight == null) {
            selectedFlight = new Flight();
        }
        return selectedFlight.getFlightID();
    }

    public String getSelectedFlightId() {
        return selectedFlightID;
    }

    public String getSelectedFlightType() {
        return selectedFlightType;
    }

    public int getSelectedSeatQty() {
        return selectedSeatQty;
    }

    public String getSelectedWay() {
        return selectedWay;
    }

    public static int getTotalSeatEcoQty() {
        return totalSeatEcoQty;
    }

    public static int getTotalSeatPreQty() {
        return totalSeatPreQty;
    }

    public static int getTotalSeatBusQty() {
        return totalSeatBusQty;
    }

    public static ArrayList<String[]> getBookingData() {
        return bookingData;
    }

    public void setSelectedFlightType(String selectedFlightType) {
        this.selectedFlightType = selectedFlightType;
    }

    public void setSelectedWay(String selectedWay) {
        this.selectedWay = selectedWay;
    }

    public void setMemberId(String memberId) {
        if (selectedMember == null) {
            selectedMember = new Member();
        }
        selectedMember.setMemberId(memberId);
    }

    public void setStaffId(String staffId) {
        if (selectedStaff == null) {
            selectedStaff = new Staff();
        }
        selectedStaff.setStaffId(staffId);
    }

    public void setSelectedFlightID(String selectedFlightID) {
        this.selectedFlightID = selectedFlightID;
    }

    public void setSelectedSeatQty(int selectedSeatQty) {
        this.selectedSeatQty = selectedSeatQty;
    }

    public void deductTotalSeatTypeQty() {
        switch (selectedFlightType) {
            case "Economy" ->
                totalSeatEcoQty -= selectedSeatQty;
            case "Business" ->
                totalSeatPreQty -= selectedSeatQty;
            default -> //means that the selected flight type is equals to economy
                totalSeatPreQty -= selectedSeatQty;
        }
    }

    public static void clearSelectedSeatQty() {
        availableSeatEco = 0;
        availableSeatBus = 0;
        availableSeatPre = 0;
        totalSeatEcoQty = 0;
        totalSeatPreQty = 0;
        totalSeatBusQty = 0;
    }

    public static boolean valid_Seat_Qty(int totalSeatQty, int available) {
        return totalSeatQty <= available;
    }

    @Override
    public String toString() {
        return selectedFlightID + "|" + selectedSeatQty + "|" + selectedFlightType + "|" + selectedWay;
    }

    //extra method
    //for checking whether the customer is a member or not 
    public boolean authenticateBooking() {
        try {
            try (BufferedReader reader = new BufferedReader(new FileReader(Member.getMEMBER_FILE()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] details = line.split("\\|");
                    String memberID = details[0];
                    if (memberID.equals(selectedMember.getMemberId())) {
                        reader.close();
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to open the file !\n");
        }
        return false;
    }

    //view booking 
    public String viewBooking() {
        return """
               =====================================================================
                                      ENTERED BOOKING DETAILS                       
               =====================================================================
               Selected flight id              :   """ + " " + selectedFlightID + "\n"
                + "Selected booking seat quantity  : " + selectedSeatQty + "\n"
                + "Selected flight type            : " + selectedFlightType + "\n"
                + "Selected flight way             : " + selectedWay + "\n";

    }

    //add successful booking 
    public void storeBooking(ArrayList<Booking> bookings, String staffId, String memberId) {
        try {
            loopGenerateID = 0;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKING_PATH, true))) {
                for (Booking bookingDetails : bookings) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String currentDate = dateFormat.format(new Date());
                    autoGenerateID();
                    String stringBookingDetails = bookingID + "|" + staffId + "|" + memberId + "|"
                            + bookingDetails.toString() + "|" + currentDate;
                    writer.write(stringBookingDetails);
                    writer.newLine();
                    loopGenerateID++;
                }
                writer.close();
            }
        } catch (IOException e) {
            System.out.println("Error to write the data into the file");
        }
    }

    //auto generate booking id 
    @Override
    public void autoGenerateID() {
        int bookingNo = 0;
        String prefix = " ";
        try {
            try (BufferedReader reader = new BufferedReader(new FileReader(BOOKING_PATH))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] details = line.split("\\|");
                    String bookID = details[0];
                    prefix = bookID.substring(0, 1);
                    int currentBookingNo = Integer.parseInt(bookID.substring(1));
                    if (currentBookingNo > bookingNo) {
                        bookingNo = currentBookingNo;
                    }
                }
                bookingNo++;
                if (loopGenerateID > 0) //check if the record is more than 1
                {
                    bookingNo += loopGenerateID;
                }
                bookingID = prefix + String.format("%02d", bookingNo);
            }
        } catch (IOException | NumberFormatException ex) {
            System.out.println("Unable to open the file !\n");
        }
    }

    //read booking id 
    public static void readBookingData() {
        bookingData.clear();
        try {
            try (BufferedReader reader = new BufferedReader(new FileReader(BOOKING_PATH))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] details = line.split("\\|");
                    bookingData.add(details); // Add each booking data to the list
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + BOOKING_PATH);
        } catch (IOException ex) {
            System.out.println("Unable to open the file!");
        }
    }
    
    //check selected seat quantity 
    public static boolean chckTotalSelectedSeatQty(String selectedFlightType, int selectedSeatQty, String selectedFlightID) {        
        Flight.readFlightData();
        for (String[] flights : Flight.getFlightData()) {
            if (flights[0].equals(selectedFlightID)) {
                switch (selectedFlightType) {
                    case "Economy" -> {
                        totalSeatEcoQty += selectedSeatQty;
                        availableSeatEco = Integer.parseInt(flights[6]);
                        return Booking.valid_Seat_Qty(totalSeatEcoQty, availableSeatEco);
                    }
                    case "Business" -> {
                        totalSeatBusQty += selectedSeatQty;
                        availableSeatBus = Integer.parseInt(flights[7]);
                        return Booking.valid_Seat_Qty(totalSeatBusQty, availableSeatBus);
                    }
                    case "Premium" -> {
                        totalSeatPreQty += selectedSeatQty;
                        availableSeatPre = Integer.parseInt(flights[8]);
                        return Booking.valid_Seat_Qty(totalSeatPreQty, availableSeatPre);
                    }
                }
            }
        }
        return true;
    }

    //extract booking details 
    public void extractBookingDetails(String bookingId) {
        readBookingData();
        for (String[] line : bookingData) {
            if (line[0].equals(bookingId)) {
                selectedFlightID = line[3];
                selectedSeatQty = Integer.parseInt(line[4]);
                selectedFlightType = line[5];
            }
        }
    }
}
