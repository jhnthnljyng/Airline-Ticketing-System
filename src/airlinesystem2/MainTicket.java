package airlinesystem2;

import static airlinesystem2.Ticket.readTicketData;
import java.util.*;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class MainTicket {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean validTicketOpt = true;
        int ticketOpt;
        clearScreen();
        do {
            ticketLogo();
            try {
                System.out.print("Select an option (1-3) : ");
                ticketOpt = sc.nextInt();
                switch (ticketOpt) {
                    case 1 -> {
                        pressAnyContinue();
                        viewSuccessBookedTicket();
                    }
                    case 2 -> {
                        pressAnyContinue();
                        viewTicketReport();
                    }
                    case 3 -> {
                        if (askConfirm(1) == 1) {
                            System.exit(0);
                        }
                    }
                    default -> {
                        System.out.println("Please enter 1- 3 digit only");
                        pressAnyContinue();
                    }
                }
            } catch (Exception ex) {
                System.out.println("You can only select digit number (1-3) !");
                pressAnyContinue();
                sc.nextLine();
            }
        } while (validTicketOpt);
        sc.close();
    }

    public static void viewSuccessBookedTicket() {
        Scanner sc = new Scanner(System.in);
        boolean validViewTicket = true;
        int viewTicketOpt;
        do {
            successTicketLogo();
            try {
                System.out.print("Select an option (1-3): ");
                viewTicketOpt = sc.nextInt();
                switch (viewTicketOpt) {
                    case 1 -> {
                        System.out.println("Finding all the ticket .....\n");
                        try {
                            pressAnyContinue();
                            viewAllTicket();
                            pressAnyContinue();
                        } catch (Exception ex) {
                            System.out.println("An error occurred while viewing all tickets: " + ex.getMessage());
                        }
                    }
                    case 2 -> {
                        pressAnyContinue();
                        viewSpecificTicket();
                    }
                    case 3 -> {
                        if (askConfirm(1) == 1) {
                            validViewTicket = false;
                        }
                        pressAnyContinue();
                    }
                    default -> {
                        System.out.println("You should enter 1 - 3 digit only !");
                        pressAnyContinue();
                    }
                }
            } catch (Exception ex) {
                System.out.println("You must enter digits (1-3) !");
                pressAnyContinue();
                sc.nextLine();
            }
        } while (validViewTicket);
    }

    //view specific ticket 
    public static void viewSpecificTicket() {
        Scanner sc = new Scanner(System.in);

        boolean validViewSpecTicket = true;
        int viewSpecifcTicketOpt;
        do {
            specificTicketLogo();
            try {
                System.out.print("Select an option (1-4): ");
                viewSpecifcTicketOpt = sc.nextInt();
                switch (viewSpecifcTicketOpt) {
                    case 1 -> {
                        pressAnyContinue();
                        viewByTicketID();
                    }
                    case 2 -> {
                        pressAnyContinue();
                        viewByMemberID();
                    }
                    case 3 -> {
                        pressAnyContinue();
                        viewByFlightID();
                    }

                    case 4 -> {
                        if (askConfirm(1) == 1) {
                            validViewSpecTicket = false;
                        }
                        pressAnyContinue();
                    }
                }
            } catch (Exception ex) {
                System.out.println("You must enter digits (1-4) !");
                pressAnyContinue();
                sc.nextLine();
            }
        } while (validViewSpecTicket);
    }

    // view specific
    public static void viewByTicketID() {
        Scanner sc = new Scanner(System.in);
        boolean exitViewTicketID = true;
        do {
            try {
                System.out.print("Enter existing ticket ID (eg. T01 /t01) (0 to exit ): ");
                String srchTicketID = sc.nextLine().toUpperCase();
                if (srchTicketID.equals("0")) {
                    int ticketExitNo = askConfirm(1);
                    pressAnyContinue();
                    if (ticketExitNo == 1) {
                        exitViewTicketID = false;
                    }
                } else {
                    System.out.printf("Finding the particular %s ID ........\n", srchTicketID);
                    System.out.println("Result: \n");
                    if (!viewSpecificTicket(1, srchTicketID)) {
                        System.out.println("Unable to find the particular ID ticket !");
                        pressAnyContinue();
                    } else {
                        pressAnyContinue();
                    }
                }
            } catch (Exception ex) {
                System.out.println("Please enter in String format (eg T01) !");
            }
        } while (exitViewTicketID);
    }

    //view by member id 
    public static void viewByMemberID() {
        Scanner sc = new Scanner(System.in);
        boolean exitViewBookingID = true;
        do {
            try {
                System.out.print("Enter existing member ID (eg. M01 /m01) (0 to exit): ");
                String srchMemberID = sc.nextLine().toUpperCase();
                if (srchMemberID.equals("0")) {
                    int ticketExitNo = askConfirm(1);
                    pressAnyContinue();
                    if (ticketExitNo == 1) {
                        exitViewBookingID = false;
                    }
                } else {
                    System.out.printf("\nFinding the particular %s ID ........\n", srchMemberID);
                    System.out.println("Result: \n");
                    if (!viewSpecificTicket(2, srchMemberID)) {
                        System.out.println("Unable to find the particular ID ticket !");
                        pressAnyContinue();
                    } else {
                        pressAnyContinue();
                    }
                }
            } catch (Exception ex) {
                System.out.println("Please enter in String format (eg M01) !");
            }
        } while (exitViewBookingID);
    }

    //view by flight id 
    public static void viewByFlightID() {
        Scanner sc = new Scanner(System.in);
        Booking booking = new Booking();
        boolean exitViewMemberID = true;
        do {
            try {
                System.out.print("Enter existing flight ID (eg. F01 /f01) (0 to exit): ");
                String srchFlightID = sc.nextLine().toUpperCase();

                if (srchFlightID.equals("0")) {
                    if (askConfirm(1) == 1) {
                        exitViewMemberID = false;
                    }
                    pressAnyContinue();
                } else {
                    System.out.printf("\nFinding the particular %s ID ........\n", srchFlightID);
                    System.out.println("Result: \n");
                    if (!viewSpecificTicket(3, srchFlightID)) {
                        System.out.println("Unable to find the particular ID ticket !");
                        pressAnyContinue();
                    } else {
                        pressAnyContinue();
                    }
                }
            } catch (Exception ex) {
                System.out.println("Please enter in String format (eg F01) !");
            }
        } while (exitViewMemberID);
    }

    // ask confirm
    public static int askConfirm(int typeConfirm) {
        Scanner sc = new Scanner(System.in);
        boolean validExit = false;
        do {
            try {
                switch (typeConfirm) {
                    case 1 ->
                        System.out.print("Confirm to exit ? (y/n) ");
                    case 2 ->
                        System.out.print("Continue to login ? (y/n) ");
                    case 3 ->
                        System.out.print("Confirm to register ? (y/n) ");
                    default ->
                        System.out.print("Continue register ? (y/n) ");
                }
                char exitOption = sc.next().toLowerCase().charAt(0);
                if (exitOption == 'y') {
                    return 1;
                } else if (exitOption == 'n') {
                    return 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
                validExit = false;
            }
            System.out.println("Please enter y (Y) or n (N) only!");
        } while (!validExit);
        return 0;
    }

    //view ticket report 
    public static void viewTicketReport() {
        int reportOpt = 0;
        boolean validReport = true;
        Scanner sc = new Scanner(System.in);
        do {
            ticketReportLogo();
            try {
                System.out.print("Select an option (1-3): ");
                reportOpt = sc.nextInt();
                switch (reportOpt) {
                    case 1 -> {
                        pressAnyContinue();
                        totalticketTypeReport();
                    }
                    case 2 -> {
                        pressAnyContinue();
                        specificDateReport();
                    }
                    case 3 -> {
                        if (askConfirm(1) == 1) {
                            validReport = false;
                        }
                        pressAnyContinue();
                    }
                }
            } catch (Exception ex) {
                System.out.println("Please enter digits only (1-3) !");
                sc.nextLine();
                validReport = true;
                pressAnyContinue();
            }
        } while (validReport);
    }

    //view total flight type report 
    public static void totalticketTypeReport() {
        Ticket ticket = new Ticket();
        EconomyTicket ecoTicket = new EconomyTicket() {
        };      //abstract class to be instantiated , put {}
        BusinessTicket bussTicket = new BusinessTicket() {
        };
        PremiumTicket preTicket = new PremiumTicket() {
        };

        Ticket.readTicketData();
        ArrayList<String[]> ticketData = Ticket.getTicketData();
        // Print the table header
        System.out.println("                      ================================================");
        System.out.println("                     |   Total For Each Type of Ticket Sales Report   |");
        System.out.println("                     |================================================|");
        System.out.println("                     |  Ticket ID  |   Ticket Type  | Ticket Quantity |");
        System.out.println("                     |_____________|________________|_________________|");

        // Process each ticket record and print the details in table form
        for (String[] ticketRecord : ticketData) {
            String ticketId = ticketRecord[0];
            String ticketType = ticketRecord[4];
            int ticketQuantity = Integer.parseInt(ticketRecord[8]);

            // Print the ticket details in table form
            System.out.printf("                     | %7s     | %11s    | %8d        |\n", ticketId, ticketType, ticketQuantity);

            // Process tickets
            ticket.processTicket(ticketRecord);
            ecoTicket.processTicket(ticketRecord);
            bussTicket.processTicket(ticketRecord);
            preTicket.processTicket(ticketRecord);

        }
        // Print the table footer with total tickets sold
        System.out.println("                      ================================================");
        System.out.println("                      Total Tickets Sold: " + ticket.getTotalTicketsSold());
        System.out.println("                      Total Economy Tickets  Sold: " + ecoTicket.getTotalTicketsSold());
        System.out.println("                      Total Business Tickets Sold: " + bussTicket.getTotalTicketsSold());
        System.out.println("                      Total Premium Tickets  Sold: " + preTicket.getTotalTicketsSold());
        pressAnyContinue();
    }

    //view specific date sales ticket report 
    public static boolean viewSpecficDateReport(String initialDate, String finalDate) throws ParseException {

        EconomyTicket ecoTicket = new EconomyTicket() {
        };      //abstract class to be instantiated , put {}
        BusinessTicket bussTicket = new BusinessTicket() {
        };
        PremiumTicket preTicket = new PremiumTicket() {
        };
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
        Ticket ticket = new Ticket();
        Ticket.readTicketData();
        int found = 0;
        ArrayList<String[]> ticketData = Ticket.getTicketData();
        
        try {
            Date startDate = dateFormat.parse(initialDate);
            Date endDate = dateFormat.parse(finalDate);

            //Print the table manner 
            System.out.println("Result: ");
            System.out.println("                      ===============================================================");
            System.out.printf("                     |       Ticket Sales from %s to %s Report       |\n", initialDate, finalDate);
            System.out.println("                     |===============================================================|");
            System.out.println("                     |  Ticket ID  |      Ticket Type       |     Ticket Quantity    |");
            System.out.println("                     |_____________|________________________|________________________|");

            // Process each ticket record and print the details in table form
            for (String[] ticketRecord : ticketData) {
                String ticketId = ticketRecord[0];
                String ticketType = ticketRecord[4];
                String dateStr = ticketRecord[7]; //  date is in string[7]
                Date ticketDate = dateFormat.parse(ticketRecord[7]);
                int ticketQuantity = Integer.parseInt(ticketRecord[8]);

                // Print the ticket details in table form
                if (ticketDate.compareTo(startDate) >= 0 && ticketDate.compareTo(endDate) <= 0) {
                    System.out.printf("                     | %7s     | %14s         | %11d            |\n", ticketId, ticketType, ticketQuantity);
                    // Process tickets
                    ticket.processTicket(ticketRecord);
                    ecoTicket.processTicket(ticketRecord);
                    bussTicket.processTicket(ticketRecord);
                    preTicket.processTicket(ticketRecord);
                    found++;
                }
            }
            // Print the table footer with total tickets sold
            System.out.println("                      ===============================================================");
            System.out.println("                      Total Tickets Sold: " + ticket.getTotalTicketsSold());
            System.out.println("                      Total Economy Tickets  Sold: " + ecoTicket.getTotalTicketsSold());
            System.out.println("                      Total Business Tickets Sold: " + bussTicket.getTotalTicketsSold());
            System.out.println("                      Total Premium Tickets  Sold: " + preTicket.getTotalTicketsSold());
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use DD-MM-YYYY.");
        }
        return found != 0;
    }

    //view specific date ticket sales report 
    public static void specificDateReport() throws ParseException {
        String initialSearchDate, finalSearchDate;
        boolean found = false;
        Scanner sc = new Scanner(System.in);
        do {
            do {
                found = false;
                System.out.print("Enter a initial date of the report (eg. 02/04/2023) (0 to exit) : ");
                initialSearchDate = sc.nextLine().trim();
                if (initialSearchDate.equals("0")) {
                    if (askConfirm(1) == 1) {
                        pressAnyContinue();
                        return;
                    }
                } else if (!initialSearchDate.matches(
                        "^(0[1-9]|[12][0-9]|[3][0-1])/(0[1-9]|1[0-2])/(19[9][0-9]|200[0-9]|20[0-1][0-9]|202[0-3])$")) {
                    System.out.println(
                            "Your initial search date ould be in the range 01-01-1990 to 31-12-2023 and follow the correct date format (DD/MM/YYYY).");
                }
            } while (!initialSearchDate.matches("^(0[1-9]|[12][0-9]|[3][0-1])/(0[1-9]|1[0-2])/(19[9][0-9]|200[0-9]|20[0-1][0-9]|202[0-3])$"));
            do {
                System.out.print("Enter a final date of the report (eg. 26/09/2023) (0 to exit) : ");
                finalSearchDate = sc.nextLine().trim();
                if (finalSearchDate.equals("0")) {
                    if (askConfirm(1) == 1) {
                        pressAnyContinue();
                        return;
                    }
                } else if (!finalSearchDate.matches(
                        "^(0[1-9]|[12][0-9]|[3][0-1])/(0[1-9]|1[0-2])/(19[9][0-9]|200[0-9]|20[0-1][0-9]|202[0-3])$")) {
                    System.out.println(
                            "Your final search date should be in the range 01-01-1990 to 31-12-2023 and follow the correct date format (DD/MM/YYYY).");
                }
            } while (!finalSearchDate.matches("^(0[1-9]|[12][0-9]|[3][0-1])/(0[1-9]|1[0-2])/(19[9][0-9]|200[0-9]|20[0-1][0-9]|202[0-3])$"));
            if (!viewSpecficDateReport(initialSearchDate, finalSearchDate)) {
                System.out.println("\nNo result found ! Please try to enter another specific date.");
            } else {
                found = false;
            }

            pressAnyContinue();
        } while (!found);
    }

    //view specific ticket data 
    public static boolean viewSpecificTicket(int viewTypeNum, String idSearched) {
        Ticket ticket = new Ticket();
        Ticket.readTicketData();
        ArrayList<String[]> ticketData = Ticket.getTicketData();
        int found = 0;
        System.out.println("                          ===============================================================================================================================");
        System.out.printf("                         |                                                       View %s Ticket                                                         |\n", idSearched);
        System.out.println("                         |===============================================================================================================================|");
        System.out.println("                         |  Ticket ID |  Member ID | Flight ID | Gate No | Flight Type | Flight Way |   Destination   | Departure Date | Ticket Quantity |");
        System.out.println("                         |____________|____________|___________|_________|_____________|____________|_________________|________________|_________________|");
        for (String[] ticketDetails : ticketData) {
            if (viewTypeNum == 1 && idSearched.equals(ticketDetails[0])) {
                System.out.printf("                         |%8s    |%8s    |%7s    |%6s   |%10s   |%10s  |%13s    |%13s   |%9d        |\n",
                        ticketDetails[0], ticketDetails[1], ticketDetails[2], ticketDetails[3], ticketDetails[4], ticketDetails[5], ticketDetails[6], ticketDetails[7], Integer.parseInt(ticketDetails[8]));
                found++;
            } else if (viewTypeNum == 2 && idSearched.equals(ticketDetails[1])) {
                System.out.printf("                         |%8s    |%8s    |%7s    |%6s   |%10s   |%10s  |%13s    |%13s   |%9d        |\n",
                        ticketDetails[0], ticketDetails[1], ticketDetails[2], ticketDetails[3], ticketDetails[4], ticketDetails[5], ticketDetails[6], ticketDetails[7], Integer.parseInt(ticketDetails[8]));
                found++;
            } else if (viewTypeNum == 3 && idSearched.equals(ticketDetails[2])) {
                System.out.printf("                         |%8s    |%8s    |%7s    |%6s   |%10s   |%10s  |%13s    |%13s   |%9d        |\n",
                        ticketDetails[0], ticketDetails[1], ticketDetails[2], ticketDetails[3], ticketDetails[4], ticketDetails[5], ticketDetails[6], ticketDetails[7], Integer.parseInt(ticketDetails[8]));
                found++;
            }
        }
        System.out.println("                          ===============================================================================================================================\n");
        return found > 0;
    }

    //view all ticket 
    public static void viewAllTicket() {
        Ticket ticket = new Ticket();
        Ticket.readTicketData();
        ArrayList<String[]> ticketData = Ticket.getTicketData();
        readTicketData();
        System.out.println("                      ===============================================================================================================================");
        System.out.println("                     |                                                          View All Ticket                                                      |");
        System.out.println("                     |===============================================================================================================================|");
        System.out.println("                     |  Ticket ID |  Member ID | Flight ID | Gate No | Flight Type | Flight Way |   Destination   | Departure Date | Ticket Quantity |");
        System.out.println("                     |____________|____________|___________|_________|_____________|____________|_________________|________________|_________________|");

        for (String[] ticketDetails : ticketData) {
            System.out.printf("                     |%8s    |%8s    |%7s    |%6s   |%10s   |%10s  |%13s    |%13s   |%9d        |\n",
                    ticketDetails[0], ticketDetails[1], ticketDetails[2], ticketDetails[3], ticketDetails[4], ticketDetails[5], ticketDetails[6], ticketDetails[7], Integer.parseInt(ticketDetails[8]));
        }
        System.out.println("                      ===============================================================================================================================\n");
    }

    // ticket logo
    public static void successTicketLogo() {
        System.out.println("");
        System.out.println("                   __   ___              _____ _    _       _    ");
        System.out.println("                   \\ \\ / (_)_____ __ __ |_   _(_)__| |_____| |_  ");
        System.out.println("                    \\ V /| / -_) V  V /   | | | / _| / / -_)  _| ");
        System.out.println("                     \\_/ |_\\___|\\_/\\_/    |_| |_\\__|_\\_\\___|\\__| \n");
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("                 |  1. View All Success Booking Ticket             |");
        System.out.println("                 |                                                 |");
        System.out.println("                 |  2. View Specific Booking Ticket                |");
        System.out.println("                 |                                                 |");
        System.out.println("                 |  3. Exit View Ticket                            |");
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("");
    }

    public static void ticketLogo() {
        System.out.println("                           _______ _      _        _   ");
        System.out.println("                          |__   __(_)    | |      | |");
        System.out.println("                             | |   _  ___| | _____| |");
        System.out.println("                             | |  | |/ __| |/ / _ \\ __|");
        System.out.println("                             | |  | | (__|   <  __/ |");
        System.out.println("                             |_|  |_|\\___|_|\\_\\___|\\__|\n");

        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("                       |1. View Successful Booking Ticket   |");
        System.out.println("                       |                                    |");
        System.out.println("                       |2. View Ticket Report               |");
        System.out.println("                       |                                    |");
        System.out.println("                       |3. Exit Ticket                      |");
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("");
    }

    public static void ticketReportLogo() {
        System.out.println("                __      ___                 _____                       _   ");
        System.out.println("                \\ \\    / (_)               |  __ \\                     | |  ");
        System.out.println("                 \\ \\  / / _  _____      __ | |__) |___ _ __   ___  _ __| |_ ");
        System.out.println("                  \\ \\/ / | |/ _ \\ \\ /\\ / / |  _  // _ \\ '_ \\ / _ \\| '__| __|");
        System.out.println("                   \\  /  | |  __/\\ V  V /  | | \\ \\  __/ |_) | (_) | |  | |_ ");
        System.out.println("                    \\/   |_|\\___| \\_/\\_/   |_|  \\_\\___| .__/ \\___/|_|   \\__|");
        System.out.println("                                                       | |");
        System.out.println("                                                       |_|");

        System.out.println("---------------------------------------------------------------------------------------------");
        System.out.println("                         |1. View Total Flight Type Ticket           |");
        System.out.println("                         |                                           |");
        System.out.println("                         |2. View Specific Date Ticket Sales Report  |");
        System.out.println("                         |                                           |");
        System.out.println("                         |3. Exit Ticket                             |");
        System.out.println("---------------------------------------------------------------------------------------------");
    }

    public static void specificTicketLogo() {
        System.out.println("");
        System.out.println("                __   ___              ___              _  __ _      _____ _    _       _   ");
        System.out.println("               \\ \\ / (_)_____ __ __ / __|_ __  ___ __(_)/ _(_)__  |_   _(_)__| |_____| |_ ");
        System.out.println("                \\ V /| / -_) V  V / \\__ \\ '_ \\/ -_) _| |  _| / _|   | | | / _| / / -_)  _|");
        System.out.println("                 \\_/ |_\\___|\\_/\\_/  |___/ .__/\\___\\__|_|_| |_\\__|   |_| |_\\__|_\\_\\___|\\__| ");
        System.out.println("                              |_|                                                ");
        
        System.out.println("-----------------------------------------------------------------------------------------------------------------");
        System.out.println("            |1. View by ticket id                                                            |");
        System.out.println("            |                                                                                |");
        System.out.println("            |2. View by member id                                                            |");
        System.out.println("            |                                                                                |");
        System.out.println("            |3. View by flight id                                                            |");
        System.out.println("            |                                                                                |");
        System.out.println("            |4. Exit                                                                         |");
        System.out.println("-----------------------------------------------------------------------------------------------------------------");
        System.out.println("");
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

}
