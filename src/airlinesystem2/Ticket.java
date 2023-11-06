package airlinesystem2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Ticket {

    private String ticketID;
    private Booking bookingDetails; // composition
    private Flight flightDetails; // association
    private Member memberDetails;
    private String gateNo;
    private static int ticketNo, ticketNum;
    protected int totalTicketSold; //protected 
    private static String TICKET_PATH = "Ticket.txt";
    private static ArrayList<String[]> ticketData = new ArrayList<>();

    // constructor
    public Ticket() {
        gateNo = " ";
        ticketData.clear();
        totalTicketSold = 0;
        bookingDetails = new Booking();
        flightDetails = new Flight();
        memberDetails = new Member();
    }

    public Ticket(Booking bookingDetails, Flight flightDetails, Member memberDetails) {
        this.bookingDetails = bookingDetails;
        this.flightDetails = flightDetails;
        this.memberDetails = memberDetails;
    }

    public String getTicketID() {
        return ticketID;
    }

    public Booking getBookingDetails() {
        return bookingDetails;
    }

    public Member getMemberDetails() {
        return memberDetails;
    }

    public String getGateNo() {
        return gateNo;
    }

    public String getMemberID(String memberID) {
        if (memberDetails == null) {
            memberDetails = new Member();
        }
        return memberDetails.getMemberId();
    }

    public String getFlightID() {
        if (flightDetails == null) {
            flightDetails = new Flight();
        }
        return flightDetails.getFlightID();
    }

    public void setTicketID(String ticketID) {
        this.ticketID = ticketID;
    }

    public static ArrayList<String[]> getTicketData() {
        return ticketData;
    }

    // tostring method
    @Override
    public String toString() {
        return ticketID;
    }
    
    //generate ticket
    public void generateTicket(ArrayList<Booking> bookingData, ArrayList<String[]> flightData) {
        ticketNum = 1;
        for (Booking booking : bookingData) {
            for (String[] flight : flightData) {
                // Check if the selectedFlightId matches the first element of the flight array
                if (booking.getSelectedFlightId().equals(flight[0])) {
                    ticketID = autoGenerateTicketID();
                    gateNo = autoGenerateGateNo(booking.getSelectedFlightId());
                    System.out.println(" =================================================");
                    System.out.printf("|              Member %s %dth Ticket              |\n", booking.getMemberId(),
                            ticketNum);
                    System.out.println("|=================================================|");
                    System.out.printf("|Ticket  id: %-37s|\n", ticketID);
                    System.out.printf("|Flight  id: %-37s|\n", booking.getSelectedFlightId());
                    System.out.printf("|Flight type: %-36s|\n", booking.getSelectedFlightType());
                    System.out.printf("|Flight destination: %-29s|\n", flight[4]);
                    System.out.printf("|Flight departure date: %-26s|\n", flight[5]);
                    System.out.printf("|Selected Flight way: %-28s|\n", booking.getSelectedWay());
                    System.out.printf("|Booked seat quantity: %-27d|\n", booking.getSelectedSeatQty());
                    System.out.printf("|Gate No: %-40s|\n", gateNo);
                    System.out.println(" =================================================");
                    ticketNum++;
                    storeTicketID(booking.getMemberId(), ticketID, booking.getSelectedFlightId(),
                            booking.getSelectedFlightType(),
                            booking.getSelectedSeatQty(), booking.getSelectedWay(), flight[4], flight[5], gateNo);
                }
            }
        }
    }

    // store ticket id
    public void storeTicketID(String memberId, String ticketID, String flightID, String flightType, int seatQty,
            String selectedWay, String destination, String departDate, String gateNo) {
        try {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(TICKET_PATH, true))) {
                String ticketDetails = ticketID + "|" + memberId + "|" + flightID + "|" + gateNo + "|" + flightType + "|"
                        + selectedWay + "|" + destination + "|" + departDate + "|" + String.valueOf(seatQty);
                writer.write(ticketDetails);
                writer.newLine();
                writer.close();
            }
        } catch (IOException e) {
            System.out.println("Error to write the data into the file");
        }
    }

    // read ticket data
    public static void readTicketData() {
        ticketData.clear();
        try {
            try (BufferedReader reader = new BufferedReader(new FileReader(TICKET_PATH))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] details = line.split("\\|");
                    ticketData.add(details); // Add each ticket data to the list
                    
                }
                reader.close();
            }
        } catch (IOException ex) {
            System.out.println("Unable to open the file!");
        }
    }
    
    // auto generate ticket id
    public String autoGenerateTicketID() {
        ticketNo = 0;
        try {
            try (BufferedReader reader = new BufferedReader(new FileReader(TICKET_PATH))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] details = line.split("\\|");
                    String staffID = details[0];
                    String prefix = staffID.substring(0, 1); // abstract the T from the string
                    ticketNo = Integer.parseInt(staffID.substring(1)); // abstract the integer and increase by one
                    ticketNo++;
                    this.ticketID = prefix + String.format("%02d", ticketNo); // store inside a string called ticket ID
                }
            }
        } catch (IOException ex) {
            System.out.println("Error to open the file!\n");
        }
        return ticketID;
    }

    // auto generate gate no
    // it cannot be implements in the Systematic since method header is different
    public String autoGenerateGateNo(String flightID) {
        switch (flightID) {
            case "F01" ->
                gateNo = "G01";
            case "F02" ->
                gateNo = "G02";
            case "F03" ->
                gateNo = "G03";
            case "F04" ->
                gateNo = "G04";
            case "F05" ->
                gateNo = "G05";
        }
        return gateNo;
    }
    
    //process ticket 
    public void processTicket(String[] ticketData) {
        int quantity = Integer.parseInt(ticketData[8]); //convert string into integer 
        totalTicketSold += quantity;
    }

    public int getTotalTicketsSold() {
        return totalTicketSold;
    }
}
