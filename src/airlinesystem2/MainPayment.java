package airlinesystem2;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author user
 */
import static airlinesystem2.Payment.readPaymentData;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.*;

public class MainPayment {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int displaySelection;
        int menuExit = 0, exit = 0;
        do {
            paymentLogo();
            System.out.print("\nEnter your selection : ");
            displaySelection = sc.nextInt();
            menuExit = 0;
            switch (displaySelection) {

                case 1 -> {
                    pressAnyContinue();
                    exit = displayPayment();
                }
                case 2 -> {
                    do {
                        try {
                            exit = 0;
                            //press any continue and clear screen 
                            pressAnyContinue();
                            sc.nextLine();
                            paymentReportLogo();
                            paymentReportMenu();
                            System.out.print(
                                    "\nEnter your selection : ");
                            displaySelection = sc.nextInt();
                            switch (displaySelection) {
                                case 1 -> {
                                    pressAnyContinue();
                                    displayReport();
                                }
                                case 2 -> {
                                    pressAnyContinue();
                                    displayStaffCommissionReport();
                                }
                                case 3 -> {
                                    if (askConfirm(1) == 1) {
                                        exit = 1;
                                        pressAnyContinue();
                                    }
                                }
                                default ->
                                    System.out.println("You must enter between 1 - 3!");
                            }
                        } catch (Exception ex) {
                            System.out.println("You must enter integer!");
                            sc.nextLine();
                        }
                    } while (exit != 1);
                }
                case 3 -> {
                    if (askConfirm(1) == 1) {
                        System.exit(0);
                    } else {
                        pressAnyContinue();
                    }
                }
                default -> {
                }
            }
        } while (menuExit != 1);

    }

    public static int displayPayment() {
        Scanner sc = new Scanner(System.in);
        int selectPayment = 0, continueDisplay = 0, exit = 0;

        do {
            try {
                displayPaymentLogo();
                menyDisplayPayment();
                System.out.print(
                        "\nEnter your selection : ");
                selectPayment = sc.nextInt();
                String paymentID, pICID, memberID;
                int notFound = 1;
                exit = 0;
                switch (selectPayment) {
                    case 1 -> {
                        clearScreen();
                        displayPaymentLogo();
                        displayAllPayment();
                    }
                    case 2 -> {
                        do {
                            do {
                                sc.nextLine();
                                notFound = 1;
                                continueDisplay = 0;
                                System.out.print("Enter a Payment ID (0 to Exit):");
                                paymentID = sc.nextLine().toUpperCase();
                                if (paymentID.equals("0")) {
                                    if (askConfirm(1) == 1) {
                                        continueDisplay = 1;
                                        break;
                                    } else {
                                        notFound = 0;
                                    }
                                }
                            } while (notFound == 0);

                            if (continueDisplay == 0) {
                                clearScreen();
                                displayPaymentLogo();
                                if (!displaySpecificPayment(selectPayment, paymentID)) {
                                    System.out.println("\nPayment ID Not Found!");
                                    break;
                                }
                            } else {
                                break;
                            }
                        } while (notFound == 0);
                    }
                    case 3 -> {
                        do {
                            do {
                                sc.nextLine();
                                notFound = 1;
                                continueDisplay = 0;
                                System.out.print("Enter a Person Incharge ID (0 to Exit):");
                                pICID = sc.nextLine().toUpperCase();
                                if (pICID.equals("0")) {
                                    if (askConfirm(1) == 1) {
                                        continueDisplay = 1;
                                        break;
                                    } else {
                                        notFound = 0;
                                    }
                                }
                            } while (notFound == 0);
                            if (continueDisplay == 0) {
                                clearScreen();

                                displayPaymentLogo();
                                if (!displaySpecificPayment(selectPayment, pICID)) {
                                    System.out.println("\nPerson Incharge ID Not Found!");
                                    break;
                                }
                            } else {
                                break;
                            }
                        } while (notFound == 0);
                    }
                    case 4 -> {
                        do {
                            do {
                                notFound = 1;
                                sc.nextLine();
                                continueDisplay = 0;
                                System.out.print("Enter a Memeber ID (0 to Exit):");
                                memberID = sc.nextLine().toUpperCase();
                                if (memberID.equals("0")) {
                                    if (askConfirm(1) == 1) {
                                        continueDisplay = 1;
                                        break;
                                    } else {
                                        notFound = 0;
                                    }
                                }
                            } while (notFound == 0);
                            if (continueDisplay == 0) {
                                clearScreen();
                                displayPaymentLogo();
                                if (!displaySpecificPayment(selectPayment, memberID)) {
                                    System.out.println("\nMember ID Not Found!");
                                    break;
                                }
                            } else {
                                break;
                            }
                        } while (notFound == 0);
                    }
                    case 5 -> {
                        if (askConfirm(1) == 1) {
                            selectPayment = 5;
                            exit = 1;
                        }
                    }
                    default -> {
                        System.out.println("Invalid Input!");
                        sc.nextLine();
                        clearScreen();
                    }
                }
            } catch (Exception ex) {
                System.out.println("You must enter integer!");
                sc.nextLine();
            }
            pressAnyContinue();
        } while (selectPayment != 5);
        return exit;
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

    public static void displayReport() {
        Payment payment = new Payment();
        Payment.readPaymentData();
        ArrayList<String[]> paymentDetails = Payment.getPaymentDetails();
        double subTotalPayment = 0;
        readPaymentData();
        System.out.println("\t\t\t\t\t =====================================================");
        System.out.println("\t\t\t\t\t|             DISPLAY TOTAL PAYMENT REPORT            |");
        System.out.println("\t\t\t\t\t|=====================================================|");
        System.out.println("\t\t\t\t\t| Payment ID | Member ID | Total Payment Price (RM)   |");
        System.out.println("\t\t\t\t\t|============|===========|============================|");

        for (String[] paymentInfo : paymentDetails) {
            System.out.printf("\t\t\t\t\t|    %-7s |    %-7s| %17.2f          |\n",
                    paymentInfo[0], paymentInfo[4],
                    Double.parseDouble(paymentInfo[1])
            );
            subTotalPayment += Double.parseDouble(paymentInfo[1]);
        }

        System.out.println("\t\t\t\t\t|=====================================================|");
        System.out.printf("\t\t\t\t\t|                        Grand Total (RM) : %9.2f |\n", subTotalPayment);
        System.out.printf("\t\t\t\t\t|                Grand Service Charge (RM):  %8.2f |\n", Payment.getServiceCharge());
        System.out.printf("\t\t\t\t\t|                      Grand Discount (RM):  %8.2f |\n", Payment.getDiscount());
        System.out.println("\t\t\t\t\t =====================================================");
        System.out.printf("\t\t\t\t\t              Final Total Price Earned (RM): %.2f\n", Payment.getGrandTotalPay());
    }

    public static void displayStaffCommissionReport() {
        Payment payment = new Payment();
        Payment.readPaymentData();
        ArrayList<String[]> paymentDetails = Payment.getPaymentDetails();
        double commission = 0;
        double total = 0;

        System.out.println("\t\t\t\t\t ================================================================");
        System.out.println("\t\t\t\t\t|          DISPLAY TOTAL STAFF COMMISSION EARNED REPORT          |");
        System.out.println("\t\t\t\t\t|================================================================|");
        System.out.println("\t\t\t\t\t| Person Incharge ID | Payment ID  |   Total Payment Price (RM)  |");
        System.out.println("\t\t\t\t\t|====================|=============|=============================|");

        for (String[] paymentInfo : paymentDetails) {
            System.out.printf("\t\t\t\t\t|         %-10s |     %-7s |           %-17.2f |\n",
                    paymentInfo[3],
                    paymentInfo[0],
                    Double.parseDouble(paymentInfo[1]));
        }

        commission = Payment.getGrandTotalPay() * Payment.getCOMMISSION_RATE();
        System.out.println("\t\t\t\t\t ================================================================");
        System.out.format("\t\t\t\t\t                             Total Comission Earned (RM): %.2f\n", commission);
    }

    public static void displayAllPayment() {
        Payment payment = new Payment();
        Payment.readPaymentData();
        ArrayList<String[]> paymentDetails = Payment.getPaymentDetails();
        System.out.println("\t ==========================================================================================");
        System.out.println("\t|                                DISPLAY ALL PAYMENT DETAILS                               |");
        System.out.println("\t|==========================================================================================|");
        System.out.println("\t| Payment ID | Total Payment Price (RM)   | Payment Date | Person Incharge ID | Member ID  |");
        System.out.println("\t|============|============================|==============|====================|============|");

        for (String[] paymentInfo : paymentDetails) {
            System.out.printf("\t|    %-7s | %17.2f          |  %-12s|        %-11s |    %-7s |\n",
                    paymentInfo[0],
                    Double.parseDouble(paymentInfo[1]),
                    paymentInfo[2],
                    paymentInfo[3],
                    paymentInfo[4]);
        }
        System.out.println("\t ==========================================================================================");
    }

    public static boolean displaySpecificPayment(int paymentType, String searchID) {
        int found = 0;
        Payment payment = new Payment();
        Payment.readPaymentData();
        ArrayList<String[]> paymentDetails = Payment.getPaymentDetails();
        System.out.println("\t ==========================================================================================");
        System.out.println("\t|                             DISPLAY SPECIFIC PAYMENT DETAILS                             |");
        System.out.println("\t|==========================================================================================|");
        System.out.println("\t| Payment ID | Total Payment Price (RM)   | Payment Date | Person Incharge ID | Member ID  |");
        System.out.println("\t|============|============================|==============|====================|============|");

        for (String[] paymentInfo : paymentDetails) {
            if (paymentType == 2 && searchID.equals(paymentInfo[0])) {
                System.out.printf("\t|    %-7s | %17.2f          |  %-12s|        %-11s |    %-7s |\n",
                        paymentInfo[0],
                        Double.parseDouble(paymentInfo[1]),
                        paymentInfo[2],
                        paymentInfo[3],
                        paymentInfo[4]);
                found++;
            } else if (paymentType == 3 && searchID.equals(paymentInfo[3])) {
                System.out.printf("\t|    %-7s | %17.2f          |  %-12s|        %-11s |    %-7s |\n",
                        paymentInfo[0],
                        Double.parseDouble(paymentInfo[1]),
                        paymentInfo[2],
                        paymentInfo[3],
                        paymentInfo[4]);
                found++;
            } else if (paymentType == 4 && searchID.equals(paymentInfo[4])) {
                System.out.printf("\t|    %-7s | %17.2f          |  %-12s|        %-11s |    %-7s |\n",
                        paymentInfo[0],
                        Double.parseDouble(paymentInfo[1]),
                        paymentInfo[2],
                        paymentInfo[3],
                        paymentInfo[4]);
                found++;
            }

        }
        System.out.println("\t ==========================================================================================");

        return found > 0;
    }

    public static void displayPaymentLogo() {
        System.out.println("\t         _____                                 _     _____       _        _ _       ");
        System.out.println("\t        |  __ \\                               | |   |  __ \\     | |      (_) |     ");
        System.out.println("\t        | |__) |_ _ _   _ _ __ ___   ___ _ __ | |_  | |  | | ___| |_ __ _ _| |___ ");
        System.out.println("\t        |  ___/ _` | | | | '_ ` _ \\ / _ \\ '_ \\| __| | |  | |/ _ \\ __/ _` | | / __|");
        System.out.println("\t        | |  | (_| | |_| | | | | | |  __/ | | | |_  | |__| |  __/ || (_| | | \\__ \\");
        System.out.println("\t        |_|   \\__,_|\\__, |_| |_| |_|\\___|_| |_|\\__| |_____/ \\___|\\__\\__,_|_|_|___/");
        System.out.println("\t                    __/ /                                                         ");
        System.out.println("\t                   |___/                                                         \n");
    }

    public static void menyDisplayPayment() {
        System.out.println("---------------------------------------------------------------------------------------------------------------");
        System.out.println("\t         | 1.  Display All Payment Details                                      |");
        System.out.println("\t         |                                                                      |");
        System.out.println("\t         | 2.  Display Payment Details By Payment ID                            |");
        System.out.println("\t         |                                                                      |");
        System.out.println("\t         | 3.  Display Payment Details By Person Incharge ID (Staff ID)         |");
        System.out.println("\t         |                                                                      |");
        System.out.println("\t         | 4.  Display Payment Details By Member ID                             |");
        System.out.println("\t         |                                                                      |");
        System.out.println("\t         | 5.  Exit Payment Details                                             |");
        System.out.println("---------------------------------------------------------------------------------------------------------------");
    }

    public static void paymentLogo() {
        System.out.println("\t\t        _____                                 _   ");
        System.out.println("\t\t       |  __ \\                              | |  ");
        System.out.println("\t\t       | |__) |_ _ _   _ _ __ ___   ___ _ __ | |_ ");
        System.out.println("\t\t       |  ___/ _` | | | | '_ ` _ \\ / _ \\ '_ \\| __|");
        System.out.println("\t\t       | |  | (_| | |_| | | | | | |  __/ | | | |_ ");
        System.out.println("\t\t       |_|   \\__,_|\\__, |_| |_| |_|\\___|_| |_|\\__|");
        System.out.println("\t\t                   ___/ |                         ");
        System.out.println("\t\t                  |____/                          \n");

        System.out.println("-------------------------------------------------------------------------------------------");
        System.out.println("\t\t     | 1.  Display Payment Details                 |");
        System.out.println("\t\t     |                                             |");
        System.out.println("\t\t     | 2.  Display Payment Report                  |");
        System.out.println("\t\t     |                                             |");
        System.out.println("\t\t     | 3.  Exit Payment                            |");
        System.out.println("-------------------------------------------------------------------------------------------");
    }

    public static void paymentReportLogo() {
        System.out.println("\t   _____                                 _     _____                       _   ");
        System.out.println("\t  |  __ \\                               | |   |  __ \\                     | |  ");
        System.out.println("\t  | |__) |_ _ _   _ _ __ ___   ___ _ __ | |_  | |__) |___ _ __   ___  _ __| |_ ");
        System.out.println("\t  |  ___/ _` | | | | '_ ` _ \\ / _ \\ '_ \\| __| |  _  // _ \\ '_ \\ / _ \\| '__| __|");
        System.out.println("\t  | |  | (_| | |_| | | | | | |  __/ | | | |_  | | \\ \\  __/ |_) | (_) | |  | |_ ");
        System.out.println("\t  |_|   \\__,_|\\__, |_| |_| |_|\\___|_| |_|\\__| |_|  \\_\\___| .__/ \\___/|_|   \\__|");
        System.out.println("\t               __/ |                                     | |                   ");
        System.out.println("\t              |___/                                      |_|                   \n");
    }

    public static void paymentReportMenu() {
        System.out.println("---------------------------------------------------------------------------------------------------");
        System.out.println("                      | 1.  Display Total Payment Report               |");
        System.out.println("                      |                                                |");
        System.out.println("                      | 2.  Display Total Commission Earned Report     |");
        System.out.println("                      |                                                |");
        System.out.println("                      | 3.  Exit Payment Report                        |");
        System.out.println("---------------------------------------------------------------------------------------------------");
    }

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
            } catch (Exception ex) {
                validExit = false;
            }
            System.out.println("Please enter y (Y) or n (N) only!");
        } while (!validExit);
        return 0;
    }

    // press any continue
    public static void pressAnyContinue() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Press any key to continue...");
        sc.nextLine();
        clearScreen();
    }

}
