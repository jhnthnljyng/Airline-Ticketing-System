package airlinesystem2;

import java.util.*;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;

public class MainBooking {

    public static ArrayList<Flight> flights;
    public static ArrayList<Booking> bookings = new ArrayList<>();
   
    public static void startBookingProcess() {
        Scanner sc = new Scanner(System.in);
        boolean validExitBooking = true;

        do {
            clearScreen();
            bigbookingLogo();
            try {
                System.out.print("Select an option (1 -4): ");
                int optBooking = sc.nextInt();
                sc.nextLine();
                switch (optBooking) {
                    case 1 -> {
                        pressAnyContinue();
                        chckBookingMemberID();
                    }
                    case 2 -> {
                        pressAnyContinue();
                        displayBookings();
                        pressAnyContinue();
                    }
                    case 3 -> {
                        pressAnyContinue();
                        cancelBooking();
                    }
                    case 4 -> {
                        if (askConfirm(1) == 1) {
                            validExitBooking = false;
                        }
                        pressAnyContinue();
                    }
                    default -> {
                        System.out.println("You should enter 1 to 4 only.");
                        pressAnyContinue();
                    }
                }
            } catch (Exception ex) {
                System.out.println("Please enter digits only (1-4) !");
                pressAnyContinue();
                sc.nextLine();
            }
        } while (validExitBooking);
    }

    public static void main(String[] args) {
        startBookingProcess();
    }

    // add booking function 
    public static void chckBookingMemberID() throws ParseException {
        Scanner sc = new Scanner(System.in);
        boolean exitAddBooking = true;
        do {
            Booking booking = new Booking();
            Booking.clearSelectedSeatQty();
            //add booking logo
            addBookingLogo();
            System.out.print("Enter the existing member ID (eg: M01 or m01) (0 to exit): ");
            String enteredMemberID = sc.nextLine().toUpperCase();
            booking.setMemberId(enteredMemberID);
            booking.setStaffId("S01");

            //check whether the member id is equals to 0 or not 
            if (enteredMemberID.equals("0")) {
                if (askConfirm(1) == 1) {
                    exitAddBooking = false;
                    pressAnyContinue();
                    break;
                }
                pressAnyContinue();
            } //check whether the member id exists or not 
            else if (booking.authenticateBooking()) {
                System.out.println("Member ID is found !");
                pressAnyContinue();
                askBookingDetails(enteredMemberID, booking.getStaffId());
            } else {
                System.out.println("Member ID not found !");
                pressAnyContinue();
            }
            //press any key to continue method

        } while (exitAddBooking);
    }

    //ask booking details 
    private static void askBookingDetails(String enteredMemberID, String personInchargeId) throws ParseException {
        Boolean invalidBookDetails = false;
        Scanner sc = new Scanner(System.in);
        int selectedSeatQty = 0;
        String flightType;
        do {
            Booking booking = new Booking();
            Member member = new Member();
            Payment payment = new Payment();
            Ticket ticket = new Ticket();

            booking.setMemberId(enteredMemberID);
            flights = Flight.displayFlightDetails();
            do {
                try {
                    invalidBookDetails = false;
                    System.out.print("Select a flight (1 - 5) (0 to Exit): ");
                    int selectedFlightNo = sc.nextInt();

                    switch (selectedFlightNo) {
                        case 1 ->
                            booking.setSelectedFlightID("F01");
                        case 2 ->
                            booking.setSelectedFlightID("F02");
                        case 3 ->
                            booking.setSelectedFlightID("F03");
                        case 4 ->
                            booking.setSelectedFlightID("F04");
                        case 5 ->
                            booking.setSelectedFlightID("F05");
                        case 0 -> {
                            if (askConfirm(1) == 1) {
                                pressAnyContinue();
                                return;
                            }
                            invalidBookDetails = true;
                        }
                        default -> {
                            invalidBookDetails = true;
                            System.out.println("Invalid input. Please enter 1, 2, 3, 4, 5 or 0 to exit.");
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("Please enter digits (1 - 4)!");
                    sc.nextLine();
                    invalidBookDetails = true;
                }

            } while (invalidBookDetails);
            do {
                do {
                    try {
                        invalidBookDetails = false;
                        System.out.print("Select booking seat quantity (1 - 100) (0 to Exit): ");
                        selectedSeatQty = sc.nextInt();
                        booking.setSelectedSeatQty(selectedSeatQty);

                        //ask sentinel value 
                        if (selectedSeatQty == 0) {
                            if (askConfirm(1) == 1) {
                                pressAnyContinue();
                                return;
                            }
                            invalidBookDetails = true;
                        } //check if selected seat qty is more than 100
                        else if (selectedSeatQty > 100) {
                            System.out.println("Your selected seat quantity should within the range 1 - 100 !");
                            invalidBookDetails = true;
                        }
                    } catch (Exception ex) {
                        System.out.println("Please enter digits (1 - 100)!");
                        sc.nextLine();
                        invalidBookDetails = true;
                    }
                } while (invalidBookDetails);

                sc.nextLine();
                do {
                    invalidBookDetails = false;
                    System.out.print("Select a flight type (E = Economy, B = Business, P = Premium) (0 to Exit): ");
                    flightType = sc.nextLine().toUpperCase();

                    switch (flightType) {
                        case "E" ->
                            booking.setSelectedFlightType("Economy");
                        case "B" ->
                            booking.setSelectedFlightType("Business");
                        case "P" ->
                            booking.setSelectedFlightType("Premium");
                        case "0" -> {
                            if (askConfirm(1) == 1) {
                                pressAnyContinue();
                                return;
                            }
                            invalidBookDetails = true;
                        }
                        default -> {
                            invalidBookDetails = true;
                            System.out.println("Invalid input. Please enter E, B, P, or 0 to exit.");
                        }
                    }
                } while (invalidBookDetails);
                if (!Booking.chckTotalSelectedSeatQty(booking.getSelectedFlightType(), selectedSeatQty, booking.getSelectedFlightId())){
                    System.out.println("Your selected seat quantity has over the maximum available seats of the selected flight id !");
                    booking.deductTotalSeatTypeQty();
                    invalidBookDetails = true;
                } //check whether the seat quantity has over the available seats 
            } while (invalidBookDetails);
            do {
                invalidBookDetails = false;
                System.out.print("Select one way (W) or two way (T) (0 to Exit): ");
                String flightWay = sc.nextLine().toUpperCase();
                switch (flightWay) {
                    case "W" ->
                        booking.setSelectedWay("One way");
                    case "T" ->
                        booking.setSelectedWay("Two way");
                    case "0" -> {
                        if (askConfirm(1) == 1) {
                            pressAnyContinue();
                            return;
                        }
                        invalidBookDetails = true;
                    }
                    default -> {
                        invalidBookDetails = true;
                        System.out.println("Invalid input. Please enter W, T or 0 to exit.");
                    }
                }
            } while (invalidBookDetails);

            //print out the latest add booking details
            System.out.println(booking.viewBooking());
            if (askConfirm(4) == 1) { //want to add or not 
                bookings.add(booking);
                System.out.println("<< Successfully add booking ! Please select n (N) to proceed to the payment >>\n");
                if (askConfirm(5) == 0) { //want to continue or not 
                    pressAnyContinue();
                    double grandTotalPayBill = payment.generateBill(flights, bookings);
                    String billOpt;
                    do {
                        invalidBookDetails = false;
                        System.out.print("Does the member want to pay for the bill ? (y/n) ");
                        billOpt = sc.nextLine().toLowerCase();
                        if (!billOpt.equals("y") && !billOpt.equals("n")) {
                            invalidBookDetails = true;
                            System.out.println("Please enter y (Y) or n (N)");
                        }
                    } while (invalidBookDetails);
                    if (billOpt.equals("y")) {
                        System.out.printf("We are checking whether the member %s has the ability to pay for the bill....\n\n", booking.getMemberId());
                        if (Payment.checkAccountBalance(booking.getMemberId(), grandTotalPayBill)) {
                            //store data into text file
                            Flight.reduceSeat(booking.getSelectedSeatQty(), booking.getSelectedFlightId(), booking.getSelectedFlightType());  //reduce seat number
                            booking.storeBooking(bookings, personInchargeId, enteredMemberID);   //store booking data into booking.txt file 
                            payment.storePaymentBill(personInchargeId, enteredMemberID);                     //store payment bill 
                            payment.storeAccountBalance();                                                  //store account balance to the particular member id account 

                            System.out.printf("  <<Yay, the member have successfully pay the bill.>>\n\nPress any continue to view the member %s ticket\n", booking.getMemberId()); //generate bill successful message 
                            pressAnyContinue();
                            Flight flight = new Flight();
                            Flight.readFlightData();
                            ArrayList<String[]> flightData = Flight.getFlightData();
                            ticket.generateTicket(bookings, flightData);
                            bookings.clear();
                            //continue to add ?
                            if (askConfirm(5) == 1) {
                                pressAnyContinue();
                                invalidBookDetails = true;
                            } else {
                                pressAnyContinue();
                            }
                        } else { //when member does not have sufficient money
                            System.out.println("The member has not enough money to pay the bill. The member needs to recharge account balance !");
                            flights.clear();    //clear array list
                            bookings.clear();
                            pressAnyContinue();
                        }
                    } else if (billOpt.equals("n")) { //when member don't want to pay the money
                        if (askConfirm(5) == 1) {
                            pressAnyContinue();
                            invalidBookDetails = true;
                        } else {
                            bookings.clear();
                            pressAnyContinue();
                            invalidBookDetails = false;
                        }
                    }
                } else { //when member want to reselect the booking 
                    pressAnyContinue();
                    invalidBookDetails = true;
                }
            } else {
                pressAnyContinue();
                invalidBookDetails = true;
            }
        } while (invalidBookDetails);
    }

    // confirm exit
    public static int askConfirm(int typeConfirm) {
        Scanner sc = new Scanner(System.in);
        boolean validExit = false;
        do {
            switch (typeConfirm) {
                case 1 ->
                    System.out.print("Confirm to exit ? (y/n) ");
                case 2 ->
                    System.out.print("Continue to login ? (y/n) ");
                case 3 ->
                    System.out.print("Confirm to register ? (y/n) ");
                case 4 ->
                    System.out.print("Confirm to proceed ? (y/n) ");
                case 5 ->
                    System.out.print("Continue to add ? (y/n) ");
                default ->
                    System.out.print("Continue register ? (y/n) ");
            }
            String exitOption = sc.next().toLowerCase();
            if (exitOption.equals("y")) {
                return 1;
            } else if (exitOption.equals("n")) {
                return 0;
            }
            System.out.println("Please enter y (Y) or n (N) only!");
        } while (!validExit);
        return 0;
    }

    // clear screen
    public static void clearScreen() {
        try {
            Robot rob = new Robot();
            try {
                rob.keyPress(KeyEvent.VK_CONTROL); // press "CTRL"
                rob.keyPress(KeyEvent.VK_L); // press "L"
                rob.keyRelease(KeyEvent.VK_L); // unpress "L"
                rob.keyRelease(KeyEvent.VK_CONTROL); // unpress "CTRL"
                Thread.sleep(100); // add delay in milisecond, if not there will automatically stop after clear
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    // press any continue
    public static void pressAnyContinue() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Press any key to continue...");
        sc.nextLine();
        clearScreen();
    }

    public static void displayBookings() {
        displayBookingLogo();
        Booking.readBookingData();
        ArrayList<String[]> bookingData = Booking.getBookingData();
        // Print the header template
        System.out.println("                  ===============================================================================================================================");
        System.out.println("                 |                                                      BOOKING DETAILS                                                          |");
        System.out.println("                 |===============================================================================================================================|");
        System.out.println("                 | Booking | Staff ID  | Member ID |  Flight ID  |  Seat Selected  |       Selected        |  Selected Flight  |     Booking     |");
        System.out.println("                 |   ID    |           |           |             |    Quantity     |      Flight Type      |        Way        |       Date      |");
        System.out.println("                 |_________|___________|___________|_____________|_________________|_______________________|___________________|_________________|");
        for (String[] bookingDetails : bookingData) {
            System.out.printf("                 |   %-4s  |    %-5s  |    %-5s  |     %-4s    |       %-5s     |        %-8s       |      %-6s      |    %-9s   |\n",
                    bookingDetails[0], bookingDetails[1], bookingDetails[2], bookingDetails[3], bookingDetails[4], bookingDetails[5], bookingDetails[6], bookingDetails[7]);
        }
        System.out.println("                 |_________|___________|___________|_____________|_________________|_______________________|___________________|_________________|\n");
    }

    public static void cancelBooking() {
        Scanner sc = new Scanner(System.in);
        String bookingId;
        boolean validExit = true;
        Booking booking = new Booking();
        do {
            validExit = false ;
            cancelBookingLogo();
            System.out.print("Enter the Booking ID to cancel (e.g., B01) (0 to exit): ");
            bookingId = sc.nextLine().toUpperCase();
            if (bookingId.equals("0")) {
                if (askConfirm(1) == 1) {
                    pressAnyContinue();
                    validExit = true;
                } else {
                    pressAnyContinue();
                    validExit = false;
                }
            } else {
                //extract booking details
                booking.extractBookingDetails(bookingId);

                if (removeLineFromFile("Booking.txt", bookingId)) {
                    System.out.println("Successfully removed booking: " + bookingId);

                    // Convert B01 -> P01 or T01
                    String paymentId = "P" + bookingId.substring(1);
                    String ticketId = "T" + bookingId.substring(1);

                    String memberId;
                    double amountPaid;

                    // Retrieve payment details
                    SimpleEntry<String, Double> paymentDetails = retrievePaymentDetails(paymentId);
                    memberId = paymentDetails.getKey();
                    amountPaid = paymentDetails.getValue();

                    // Update member balance
                    updateMemberBalance(memberId, amountPaid);

                    removeLineFromFile("Payment.txt", paymentId);
                    removeLineFromFile("Ticket.txt", ticketId);

                    // Update flight seats
                    updateFlightSeats(booking.getSelectedFlightId(), booking.getSelectedSeatQty(), booking.getSelectedFlightType());
                    pressAnyContinue();
                } else {
                    System.out.println("Booking ID not found!");
                    pressAnyContinue();
                }
            }
        } while (!validExit);
    }

    private static SimpleEntry<String, Double> retrievePaymentDetails(String paymentId) {
        try {
            Path path = Paths.get("Payment.txt");
            List<String> lines = Files.readAllLines(path);

            for (String line : lines) {
                if (line.startsWith(paymentId)) {
                    String[] parts = line.split("\\|");
                    String memberId = parts[parts.length - 1].trim();
                    double amount = Double.parseDouble(parts[1].trim());
                    return new SimpleEntry<>(memberId, amount);
                }
            }
        } catch (IOException e) {
            System.out.println("Error processing the Payment.txt file");
            e.printStackTrace();
        }
        return new SimpleEntry<>(null, 0.0);
    }

    private static void updateMemberBalance(String memberId, double amount) {
        try {
            Path path = Paths.get("Member.txt");
            List<String> lines = Files.readAllLines(path);
            List<String> updatedLines = new ArrayList<>();

            for (String line : lines) {
                if (line.startsWith(memberId)) {
                    String[] parts = line.split("\\|");
                    double currentBalance = Double.parseDouble(parts[parts.length - 1].trim());
                    double updatedBalance = currentBalance + amount;
                    parts[parts.length - 1] = String.valueOf(updatedBalance);
                    updatedLines.add(String.join("|", parts));
                    System.out.println("Updated balance after refund for member " + memberId + ": " + updatedBalance);
                } else {
                    updatedLines.add(line);
                }
            }

            Files.write(path, updatedLines);
        } catch (IOException e) {
            System.out.println("Error processing the Member.txt file");
            e.printStackTrace();
        }
    }

    private static boolean removeLineFromFile(String filePath, String id) {
        try {
            Path path = Paths.get(filePath);
            List<String> lines = Files.readAllLines(path);
            boolean removed = false;

            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).startsWith(id)) {
                    lines.remove(i);
                    removed = true;
                    break;
                }
            }
            if (removed) {
                Files.write(path, lines);
                return true;
            }
        } catch (IOException e) {
            System.out.println("Error processing the file: " + filePath);
            e.printStackTrace();
        }
        return false;
    }

    private static void updateFlightSeats(String selectedFlightID, int selectedSeatQty, String selectedFlightType) {
        Flight.readFlightData();
        ArrayList<String[]> flightLines = Flight.getFlightData();
        for (String[] flightInfo : flightLines) {
            {
                if (flightInfo[0].equals(selectedFlightID)) {
                    switch (selectedFlightType) {
                        case "Economy" ->
                            flightInfo[6] = String.valueOf(Integer.parseInt(flightInfo[6]) + selectedSeatQty);
                        case "Business" ->
                            flightInfo[7] = String.valueOf(Integer.parseInt(flightInfo[7]) + selectedSeatQty);
                        case "Premium" ->
                            flightInfo[8] = String.valueOf(Integer.parseInt(flightInfo[8]) + selectedSeatQty);
                    }
                    // Printing the updated seats
                    System.out.println("Updated seats for Flight ID " + selectedFlightID + ":");
                    System.out.println("Economy seats available     : " + flightInfo[6]);
                    System.out.println("Business seats available    : " + flightInfo[7]);
                    System.out.println("Premium seats available     : " + flightInfo[8]);
                    break;
                }
            }
        }
        Flight.writeNewFlightDataFile(flightLines);
    }
    
    // Invoked to Main logo
    public static void bigbookingLogo() {
        System.out.println("\t\t  ____              _    _");
        System.out.println("\t\t |  _ \\            | |  (_)            ");
        System.out.println("\t\t | |_) | ___   ___ | | ___ _ __   __ _ ");
        System.out.println("\t\t |  _ < / _ \\ / _ \\| |/ / | '_ \\ / _` |");
        System.out.println("\t\t | |_) | (_) | (_) |   <| | | | | (_| |");
        System.out.println("\t\t |____/ \\___/ \\___/|_|\\_\\_|_| |_|\\__, |");
        System.out.println("\t\t                                  __/ |");
        System.out.println("\t\t                                 |___/ ");

        System.out.println("-----------------------------------------------------------------------------");
        System.out.println("\t\t  |1. Add Booking                      |");
        System.out.println("\t\t  |                                    |");
        System.out.println("\t\t  |2. Display booking                  |");
        System.out.println("\t\t  |                                    |");
        System.out.println("\t\t  |3. Cancel booking                   |");
        System.out.println("\t\t  |                                    |");
        System.out.println("\t\t  |4. Exit booking                     |");
        System.out.println("------------------------------------------------------------------------------\n");
    }

    public static void addBookingLogo() {
        System.out.println("\t\t              _     _   _                 _    _             ");
        System.out.println("\t\t     /\\      | |   | | | |               | |  (_)            ");
        System.out.println("\t\t    /  \\   __| | __| | | |__   ___   ___ | | ___ _ __   __ _ ");
        System.out.println("\t\t   / /\\ \\ / _` |/ _` | | '_ \\ / _ \\ / _ \\| |/ / | '_ \\ / _` |");
        System.out.println("\t\t  / ____ \\ (_| | (_| | | |_) | (_) | (_) |   <| | | | | (_| |");
        System.out.println("\t\t /_/    \\_\\__,_|\\__,_| |_.__/ \\___/ \\___/|_|\\_\\_|_| |_|\\__, |");
        System.out.println("\t\t                                                        __/ |");
        System.out.println("\t\t                                                       |___/ ");
    }

    public static void displayBookingLogo() {
        System.out.println("\t\t\t\t              _____  _           _               ____              _    _             ");
        System.out.println("\t\t\t\t             |  __ \\(_)         | |             |  _ \\            | |  (_)            ");
        System.out.println("\t\t\t\t             | |  | |_ ___ _ __ | | __ _ _   _  | |_) | ___   ___ | | ___ _ __   __ _ ");
        System.out.println("\t\t\t\t             | |  | | / __| '_ \\| |/ _` | | | | |  _ < / _ \\ / _ \\| |/ / | '_ \\ / _` |");
        System.out.println("\t\t\t\t             | |__| | \\__ \\ |_) | | (_| | |_| | | |_) | (_) | (_) |   <| | | | | (_| |");
        System.out.println("\t\t\t\t             |_____/|_|___/ .__/|_|\\__,_|\\__, | |____/ \\___/ \\___/|_|\\_\\_|_| |_|\\__, |");
        System.out.println("\t\t\t\t                          | |             __/ |                                  __/ |");
        System.out.println("\t\t\t\t                          |_|            |___/                                  |___/ \n\n");
    }

    public static void cancelBookingLogo() {
        System.out.println("\t\t   _____                     _   ____               _    _             ");
        System.out.println("\t\t  / ____|                   | | |  _ \\            | |  (_)            ");
        System.out.println("\t\t | |     __ _ _ __   ___ ___| | | |_) | ___   ___ | | ___ _ __   __ _ ");
        System.out.println("\t\t | |    / _` | '_ \\ / __/ _ \\ | |  _ < / _ \\ / _ \\| |/ / | '_ \\ / _` |");
        System.out.println("\t\t | |___| (_| | | | | (_|  __/ | | |_) | (_) | (_) |   <| | | | | (_| |");
        System.out.println("\t\t  \\_____\\__,_|_| |_|\\___\\___|_| |____/ \\___/ \\___/|_|\\_\\_|_| |_|\\__, |");
        System.out.println("\t\t                                                                 __/ |");
        System.out.println("\t\t                                                                |___/ ");

    }
}
