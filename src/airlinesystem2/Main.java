/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airlinesystem2;

/**
 *
 * @author user
 */
import static airlinesystem2.Staff.readStaffFromFile;
import java.util.*;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.AbstractMap.SimpleEntry;
import java.text.ParseException;

public class Main {

    public static ArrayList<Flight> flights;
    public static ArrayList<Booking> bookings = new ArrayList<>();

    public static void main(String[] args) {
        Staff login = new Staff();
        Scanner sc = new Scanner(System.in);
        boolean validLoginOption, continueLogin = true, correctLogin;
        int loginChoice = 0;

        do {
            // clear screen
            clearScreen();
            // print login title and logo
            loginLogo();
            do {
                try {
                    System.out.print("Are you a new user (1) or existing user (2) (0 to exit): ");
                    loginChoice = sc.nextInt();
                    validLoginOption = true;
                    sc.nextLine();

                } catch (Exception ex) {
                    validLoginOption = false;
                    System.out.println("Please enter digit only!\n");
                    sc.nextLine();
                }
            } while (!validLoginOption);

            switch (loginChoice) {
                case 1 -> {
                    askStaffRegister();
                }
                case 2 -> {
                    do {
                        continueLogin = true;
                        correctLogin = true;
                        // allow user to enter username
                        System.out.print("Enter username: ");
                        String username = sc.nextLine();
                        login.setUsername(username);

                        // allow user to enter password
                        System.out.print("Enter password: ");
                        String password = sc.nextLine();
                        login.setPassword(password);

                        // check whether the user input is same as existing username and password
                        if (login.authenticateRegLogin(1)) {
                            System.out.println("Login successful!");
                            String loginStaffId = login.getStaffId();
                            pressEnterContinue();
                            homeMenu(loginStaffId);
                            continueLogin = askConfirm(2) == 1;
                            if (!continueLogin) {
                                System.out.println("\n\n<< THANK YOU FOR USING OUR AIRLINE TICKETING SYSTEM  Bye Bye :) :) >>");
                            }
                        } else {
                            System.out.println("Error! Invalid username or password.");
                            if (askConfirm(2) == 1) {
                                correctLogin = false;
                            } else {
                                correctLogin = true;
                                pressEnterContinue();
                            }
                        }
                    } while (!correctLogin);
                }
                case 0 -> {
                    if (askConfirm(1) == 1) {
                        System.out.println("\n\n<< THANK YOU FOR USING OUR AIRLINE TICKETING SYSTEM  Bye Bye :) :) >>");
                        System.exit(0);
                    }
                    pressEnterContinue();
                }
                default ->
                    System.out.print("Invalid choice.");
            }
            // register
            // login
            // exit
            // other choice
        } while (continueLogin);
        sc.close();
    }

    public static void askStaffRegister() {
        Scanner sc = new Scanner(System.in);
        String username, password, name, dOB, icNo, phoneNo, homeAddress, inputGender;
        char gender;
        boolean validRegister = false, continueRegis = false;
        Staff login = new Staff();
        do {
            do {
                // allow user to input the username
                do {
                    System.out.print("Enter new username (max 10 characters): ");
                    username = sc.nextLine().trim();
                    login.setUsername(username); // for authentication purpose

                    if (username.length() > 10) {
                        System.out.println("Your username length should not more than 10 !");
                    } else if (!login.authenticateRegLogin(2)) {
                        System.out.println("The username entered has already existed !");
                    }
                } while (username.length() > 10 || !login.authenticateRegLogin(2));

                // allow user to input the password
                do {
                    System.out.print("Enter new password (max 15 characters): ");
                    password = sc.nextLine().trim(); // remove heading and end backspace
                    login.setPassword(password);

                    if (password.length() > 15) {
                        System.out.println("Your password should not more than 15 !");
                    } else if (!login.authenticateRegLogin(3)) {
                        System.out.println("The password entered has already existed !");
                    }
                } while (password.length() > 15 || !login.authenticateRegLogin(3));

                // allow user to input the name
                do {
                    System.out.print("Enter name (max 30 characters and only alphabet): ");
                    name = sc.nextLine().trim();

                    // Check for alphabet name , space and length
                    if (!name.matches("[a-zA-Z\\s]+") || name.length() > 30) {
                        System.out.println("Your name should be less than 30 characters and contain only alphabets!");
                    }
                } while (!name.matches("[a-zA-Z\\s]+") || name.length() > 30);

                // allow user to input the gender
                do {
                    System.out.print("Enter gender (M/F): ");
                    inputGender = sc.nextLine().toUpperCase();
                    gender = inputGender.charAt(0);

                    if ((gender != 'M' && gender != 'F') || inputGender.length() > 1) {
                        System.out.println("Your gender must be M (m) or F (f)!");
                    }
                } while ((gender != 'M' && gender != 'F') || inputGender.length() > 1);

                // allow user to input the date of birth
                do {
                    System.out.print("Enter date of birth (eg: 01-01-1990): ");
                    dOB = sc.nextLine();

                    // Regular expression pattern for date format XX-XX-XXXX
                    if (!dOB.matches(
                            "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-(19[9][0-9]|200[0-9]|20[0-1][0-9]|202[0-3])$")) {
                        System.out.println(
                                "Your birth date should be in the range 01-01-1990 to 31-12-2023 and follow the correct date format (DD-MM-YYYY).");
                    }

                } while (!dOB.matches(
                        "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-(19[9][0-9]|200[0-9]|20[0-1][0-9]|202[0-3])$"));

                // allow user to input the ic number
                do {
                    System.out.print("Enter IC number (eg: 040310-10-2193) : ");
                    icNo = sc.nextLine();
                    login.setIcNo(icNo);

                    // Regular expression pattern for ic format
                    if (!icNo.matches("\\d{6}-\\d{2}-\\d{4}")) {
                        System.out.println("You should follow the example format given !");
                    } else if (!login.authenticateRegLogin(4)) {
                        System.out.println("The ic no entered has already existed !");
                    }

                } while (!icNo.matches("\\d{6}-\\d{2}-\\d{4}") || !login.authenticateRegLogin(4));

                // allow user to input the phone 1
                do {
                    System.out.print("Enter phone number (eg: 012-2342345) : ");
                    phoneNo = sc.nextLine();
                    login.setPhoneNo(phoneNo);

                    // Regular expression pattern for phone number
                    if (!phoneNo.matches("\\d{3}-\\d{7}")) {
                        System.out.println("You should follow the example given format !");
                    } else if (!login.authenticateRegLogin(5)) {
                        System.out.println("The hp no entered has already existed !");
                    }

                } while (!phoneNo.matches("\\d{3}-\\d{7}") || !login.authenticateRegLogin(5));

                do {
                    // allow user to input the home address
                    System.out.print("Enter home address : ");
                    homeAddress = sc.nextLine();

                    // check home address is empty string or not
                    if (homeAddress.length() == 0) {
                        System.out.println("You must enter one home address !");
                    }
                } while (homeAddress.length() == 0);

                // confirm register or not
                if (askConfirm(3) == 1) {
                    System.out.println("You have successfully register !");
                    validRegister = true;
                }

            } while (!validRegister);

            // store login
            login = new Staff(username, password, name, gender, dOB, icNo, phoneNo, homeAddress);
            // auto generate ID
            login.autoGenerateID();
            System.out.println("Your new staff ID is : " + login.getStaffId());
            login.setNewStaffData();
            login.storeRegister();

            // ask continue to add or not
            if (askConfirm(6) == 0) {
                pressEnterContinue();
                continueRegis = true;
            }

        } while (!continueRegis);

    }

    public static void homeMenu(String loginStaffId) {
        Scanner sc = new Scanner(System.in);
        // display logo

        int homeMainOption = 0;
        boolean validHomeOption = false;
        do {
            homeMenuLogo();
            try {
                System.out.print("Select an option (1-6): ");
                homeMainOption = sc.nextInt();
                sc.nextLine();
                validHomeOption = false;
                switch (homeMainOption) {
                    case 1 -> {
                        int option;
                        do {
                            option = 0;
                            try {
                                clearScreen();
                                //------------------------------------ staff module done by Jimmy Chan Kah Lok---------------------------------------
                                staffLogo();
                                System.out.print("Select an Option (1 - 4)--> ");
                                option = sc.nextInt();
                                sc.nextLine();
                                switch (option) {
                                    case 1 -> {
                                        pressEnterContinue();
                                        displayStaffInformationList();
                                        pressEnterContinue();
                                    }
                                    case 2 -> {
                                        Boolean exitSearchFUNCTION;
                                        do {
                                            exitSearchFUNCTION = true;
                                            pressEnterContinue();
                                            searchStaffLogo();
                                            System.out.print(" Enter staff ID (e.g.: S01) (0 = exit): ");
                                            String searchId = sc.nextLine();
                                            if (!searchId.equals("0")) {
                                                exitSearchFUNCTION = searchStaff(searchId);
                                                pressEnterContinue();
                                            }
                                        } while (!exitSearchFUNCTION);
                                        break;
                                    }
                                    case 3 -> {
                                        int option1;
                                        do {
                                            option1 = 0;
                                            pressEnterContinue();
                                            modifyStaffLogo();
                                            try {
                                                System.out.print("Select an Option (1- 4) --> ");
                                                option1 = sc.nextInt();
                                                sc.nextLine();
                                                switch (option1) {
                                                    case 1 -> {
                                                        Boolean exitModifyNameFUNCTION;
                                                        do {
                                                            exitModifyNameFUNCTION = true;
                                                            clearScreen();
                                                            System.out.print(
                                                                    "Enter staff ID to modify Staff Name(e.g., S01) (0 to exit): ");
                                                            String searchId = sc.nextLine();
                                                            if (!searchId.equals("0")) {
                                                                exitModifyNameFUNCTION = modifyStaffName(searchId);
                                                                pressEnterContinue();
                                                            }
                                                        } while (!exitModifyNameFUNCTION);
                                                    }
                                                    case 2 -> {
                                                        Boolean exitModifyPhoneFUNCTION;
                                                        do {
                                                            exitModifyPhoneFUNCTION = true;
                                                            clearScreen();
                                                            System.out.print(
                                                                    "Enter staff ID to modify Phone No.(e.g., S01) (0 to exit): ");
                                                            String searchId = sc.nextLine();
                                                            if (!searchId.equals("0")) {
                                                                exitModifyPhoneFUNCTION = modifyStaffPhoneNo(searchId);
                                                            }
                                                            pressEnterContinue();
                                                        } while (!exitModifyPhoneFUNCTION);
                                                    }
                                                    case 3 -> {
                                                        Boolean exitModifyAddressFUNCTION;
                                                        do {
                                                            exitModifyAddressFUNCTION = true;
                                                            clearScreen();
                                                            System.out.print(
                                                                    "Enter staff ID to modify address(e.g., S01) (0 to exit): ");
                                                            String searchId = sc.nextLine();
                                                            if (!searchId.equals("0")) {
                                                                exitModifyAddressFUNCTION = modifyStaffAddress(
                                                                        searchId);
                                                            }
                                                            pressEnterContinue();
                                                        } while (!exitModifyAddressFUNCTION);
                                                    }
                                                    case 4 -> {
                                                        pressEnterContinue();
                                                    }
                                                    default -> {
                                                        System.out.println(
                                                                "Invalid Option. Please enter number 1-4 only!");
                                                        pressEnterContinue();
                                                    }
                                                }
                                            } catch (Exception ex) {
                                                System.out.println("\nPlease enter number 1-4 only!");
                                                pressEnterContinue();
                                                sc.nextLine();
                                            }
                                        } while (option1 < 4 || option1 > 4);
                                    }
                                    case 4 -> {
                                        if (askConfirm(1) == 0) {
                                            option = 0;
                                        }
                                        validHomeOption = false;
                                        pressEnterContinue();
                                        break;
                                    }
                                    default -> {
                                        System.out.println("Invalid Option. Please enter number 1-4 only!");
                                        pressEnterContinue();
                                    }

                                }
                            } catch (Exception e) {
                                System.out.println("\nPlease enter number 1-4 only!");
                                pressEnterContinue();
                                sc.nextLine();
                            }
                        } while (option > 4 || option < 4);
                        break;
                    }
                    case 2 -> {
                        int selection;
                        do {
                            selection = 0;
                            try {
                                pressEnterContinue();
                                memberLogo();
                                System.out.print("Select an option (1 - 6) --> ");
                                selection = sc.nextInt();
                                sc.nextLine();
                                switch (selection) {
                                    case 1: {
                                        boolean temp;
                                        do {
                                            temp = false;
                                            pressEnterContinue();
                                            addMemberLogo();
                                            temp = addMember();
                                        } while (!temp);

                                        break;
                                    }
                                    case 2: {
                                        boolean temp;
                                        do {
                                            temp = false;
                                            pressEnterContinue();
                                            modifyMemberLogo();
                                            System.out.print(
                                                    "Enter your member ID to modify (eg: M01 or m01) (0 to exit): ");
                                            String memberId = sc.nextLine().toUpperCase();
                                            if (!memberId.equals("0")) {
                                                temp = modifyMember(memberId);
                                            } else {
                                                temp = true;
                                            }
                                        } while (!temp);
                                        break;
                                    }
                                    case 3: {
                                        boolean temp;
                                        do {
                                            temp = false;
                                            pressEnterContinue();
                                            searchMemberLogo();

                                            System.out.print(
                                                    "Enter your member ID to search (eg: M01 or m01) (0 to exit): ");
                                            String memberId = sc.nextLine().toUpperCase();
                                            if (!memberId.equals("0")) {
                                                temp = searchMember(memberId);
                                            } else {
                                                temp = true;
                                            }
                                        } while (!temp);
                                        break;
                                    }
                                    case 4: {
                                        boolean temp;
                                        do {
                                            temp = false;
                                            pressEnterContinue();
                                            deleteMemberLogo();

                                            System.out
                                                    .print("\nEnter your member ID to delete (eg: M01 or m01) (0 to exit): ");
                                            String memberId = sc.nextLine().toUpperCase();
                                            if (!memberId.equals("0")) {
                                                temp = deleteMember(memberId);
                                            } else {
                                                temp = true;
                                            }
                                        } while (!temp);
                                        break;
                                    }
                                    case 5: {
                                        boolean temp;
                                        do {
                                            temp = false;
                                            pressEnterContinue();
                                            rechargeAmountLogo();

                                            System.out.print(
                                                    "Enter your member ID to recharge money (eg: M01 or m01) (0 to exit): ");
                                            String memberId = sc.nextLine().toUpperCase();
                                            if (!memberId.equals("0")) {
                                                temp = rechargeMoney(memberId);
                                            } else {
                                                temp = true;
                                            }
                                        } while (!temp);

                                        break;
                                    }
                                    case 6: {
                                        pressEnterContinue();
                                        validHomeOption = false;
                                        clearScreen();
                                        break;
                                    }
                                    default: {
                                        System.out.println("Invalid Option. Please enter number 1-6 only!");
                                    }
                                }
                            } catch (Exception e) {
                                System.out.println("Invalid input! Please only enter 1 to 6!");
                                sc.nextLine();
                            }

                        } while (selection != 6);
                        break;
                    }
                    case 3 -> {
                        pressEnterContinue();
                        startBookingProcess(loginStaffId);
                    }
                    case 4 -> {
                        pressEnterContinue();
                        startTicketingProcess();
                    }
                    case 5 -> {
                        pressEnterContinue();
                        startPaymentProcess();
                    }
                    case 6 -> {
                        if (askConfirm(1) == 1) {
                            pressEnterContinue();
                            validHomeOption = true;
                        } else {
                            validHomeOption = false;
                            pressEnterContinue();
                        }
                    }
                    default -> {
                        System.out.println("Invalid option. Please choose 1 to 6.");
                        pressEnterContinue();
                    }
                }
            } catch (Exception ex) {
                validHomeOption = false;
                System.out.println("Please enter digit only!\n");
                sc.nextLine();
                pressEnterContinue();
            }

        } while (!validHomeOption);
    }

    public static boolean addMember() {
        boolean validation;
        String name, inputGender, dob, icNo, phoneNo, address, inputAccountNo = " ";
        char gender;
        int accountNo;
        boolean temp;
        Scanner sc = new Scanner(System.in);
        Person person = new Person() {
        };
        Member member = new Member();
        validation = false;

        do {
            do {
                System.out.println("Enter Member Details:");
                System.out.print("Name (max 30 characters and only alphabet) (0 to exit): ");
                name = sc.nextLine().trim();
                if (name.equals("0")) {
                    return true;
                } else if (!name.matches("[a-zA-Z\\s]+") || name.length() > 30) {
                    System.out.println("Your name should be less than 30 characters and contain only alphabets!");
                } else {
                    member.setName(name);
                }
            } while (!name.matches("[a-zA-Z\\s]+") || name.length() > 30);

            do {
                System.out.print("Gender (M/F) (0 to exit): ");
                inputGender = sc.nextLine().toUpperCase();
                gender = inputGender.charAt(0);
                if (gender == '0') {
                    return true;
                } else if ((gender != 'M' && gender != 'F') || inputGender.length() > 1) {
                    System.out.println("Your gender must be M (m) or F (f)!");
                } else {
                    member.setGender(gender);
                }
            } while ((gender != 'M' && gender != 'F') || inputGender.length() > 1);

            do {
                System.out.print("Date Of Birth (eg: 01-01-1990) (0 to exit): ");
                dob = sc.nextLine();
                if (dob.equals("0")) {
                    return true;
                } else if (!dob.matches(
                        "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-(19[9][0-9]|200[0-9]|20[0-1][0-9]|202[0-3])$")) {
                    System.out.println(
                            "Your birth date should be in the range 01-01-1990 to 31-12-2023 and follow the correct date format (DD-MM-YYYY).");
                } else {
                    member.setdOB(dob);
                }
            } while (!dob
                    .matches("^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-(19[9][0-9]|200[0-9]|20[0-1][0-9]|202[0-3])$"));

            do {
                System.out.print("IC Number (eg: 040310-10-2193) (0 to exit): ");
                icNo = sc.nextLine();
                member.setIcNo(icNo);
                if (icNo.equals("0")) {
                    return true;
                } else if (!icNo.matches("\\d{6}-\\d{2}-\\d{4}")) {
                    System.out.println("You should follow the example format given !");
                } else if (!member.authenticateRegLogin(1)) {
                    System.out.println("The ic no entered has already existed !");
                } else {
                    member.setIcNo(icNo);
                }
            } while (!icNo.matches("\\d{6}-\\d{2}-\\d{4}") || !member.authenticateRegLogin(1));

            do {
                System.out.print("Phone Number (eg: 012-2342345) (0 to exit): ");
                phoneNo = sc.nextLine();
                member.setPhoneNo(phoneNo);
                if (phoneNo.equals("0")) {
                    return true;
                } else if (!phoneNo.matches("\\d{3}-\\d{7}")) {
                    System.out.println("You should follow the example given format !");
                } else if (!member.authenticateRegLogin(2)) {
                    System.out.println("The hp no entered has already existed !");
                } else {
                    member.setPhoneNo(phoneNo);
                }
            } while (!phoneNo.matches("\\d{3}-\\d{7}") || !member.authenticateRegLogin(2));

            do {
                System.out.print("Home Address (0 to exit): ");
                address = sc.nextLine();
                if (address.equals("0")) {
                    return true;
                } else if (address.length() == 0) {
                    System.out.println("You must enter one home address !");
                } else {
                    member.setHomeAddress(address);
                }
            } while (address.length() == 0);

            do {
                do {
                    System.out.print("Account Number (0 to exit): ");
                    inputAccountNo = sc.nextLine();
                    if (inputAccountNo.equals("0")&& inputAccountNo.length() == 1) {
                        return true;
                    }
                    else if (!inputAccountNo.matches("\\d+") || inputAccountNo.length() != 10) {
                        System.out.println("Please only enter integer and the length of the account number must be 10!");
                    }else if (inputAccountNo.charAt(0) == '0')
                        System.out.println("The first number should not be zero!");
                } while (!inputAccountNo.matches("\\d+") || inputAccountNo.length() != 10 || inputAccountNo.charAt(0) == '0');

                accountNo = Integer.parseInt(inputAccountNo);
                member.setAccountNo(accountNo);

                if (!member.authenticateRegLogin(3)) {
                    System.out.println("The account no entered has already existed !");
                } else {
                    break;
                }

            } while (!member.authenticateRegLogin(3));

            do {
                System.out.print("Enter the amount to recharge: ");
                temp = false;
                double rechargeAmount = sc.nextDouble();
                if (rechargeAmount != 0.0) {
                    member.rechargeAmount(rechargeAmount);
                    temp = true;
                } else {
                    return true;
                }
            } while (!temp);

            member.saveMemberDataToFile();
            validation = true;
            System.out.println("Member added successfully!");
        } while (!validation);

        return validation;

    }

    public static Boolean modifyMember(String memberId) {
        boolean validation;
        String name, inputGender, dob, icNo, phoneNo, address, inputAccountNo = " ";
        char gender;
        int accountNo, choice = 0, found = 0;
        Member member = new Member();
        Member.readMemberData();
        ArrayList<String[]> memberDetails = Member.getMemberData();

        validation = false;
        try {
            for (String[] memberData : memberDetails) {
                if (memberData[0].equals(memberId)) {
                    found++;
                    do {
                        Scanner sc = new Scanner(System.in);
                        System.out.println("Choose a detail to modify :");
                        System.out.println("===========================");
                        System.out.println("1. Name");
                        System.out.println("2. Gender");
                        System.out.println("3. Date of Birth");
                        System.out.println("4. IC Number");
                        System.out.println("5. Phone Number");
                        System.out.println("6. Home Address");
                        System.out.println("7. Account No");
                        System.out.println("8. Exit");

                        System.out.print("Select an option : ");
                        choice = sc.nextInt();
                        sc.nextLine();

                        switch (choice) {
                            case 1 -> {
                                do {
                                    System.out.print(
                                            "Enter new name (max 30 characters and only alphabet) (0 to exit): ");
                                    name = sc.nextLine().trim();
                                    if (name.equals("0")) {
                                        return false;
                                    } else if (!name.matches("[a-zA-Z\\s]+") || name.length() > 30) {
                                        System.out.println(
                                                "Your name should be less than 30 characters and contain only alphabets!");
                                    } else {
                                        member.setName(name);
                                    }
                                } while (!name.matches("[a-zA-Z\\s]+") || name.length() > 30);
                            }
                            case 2 -> {
                                do {
                                    System.out.print("Enter new gender (M/F) (0 to exit): ");
                                    inputGender = sc.nextLine().toUpperCase();
                                    gender = inputGender.charAt(0);
                                    if (gender == '0') {
                                        return false;
                                    } else if ((gender != 'M' && gender != 'F') || inputGender.length() > 1) {
                                        System.out.println("Your gender must be M (m) or F (f)!");
                                    } else {
                                        member.setGender(gender);
                                    }
                                } while ((gender != 'M' && gender != 'F') || inputGender.length() > 1);
                            }
                            case 3 -> {
                                do {
                                    System.out.print("Enter new date of birth (eg: 01-01-1990) (0 to exit): ");
                                    dob = sc.nextLine();
                                    if (dob.equals("0")) {
                                        return false;
                                    } else if (!dob.matches(
                                            "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-(19[9][0-9]|200[0-9]|20[0-1][0-9]|202[0-3])$")) {
                                        System.out.println(
                                                "Your birth date should be in the range 01-01-1990 to 31-12-2023 and follow the correct date format (DD-MM-YYYY).");
                                    } else {
                                        member.setdOB(dob);
                                    }
                                } while (!dob.matches(
                                        "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-(19[9][0-9]|200[0-9]|20[0-1][0-9]|202[0-3])$"));
                            }
                            case 4 -> {
                                do {
                                    System.out.print("Enter new IC number (eg: 040310-10-2193) (0 to exit): ");
                                    icNo = sc.nextLine();
                                    if (icNo.equals("0")) {
                                        return false;
                                    } else if (!icNo.matches("\\d{6}-\\d{2}-\\d{4}")) {
                                        System.out.println("You should follow the example format given !");
                                    } else if (!member.authenticateRegLogin(1)) {
                                        System.out.println("The ic no entered has already existed !");
                                    } else {
                                        member.setIcNo(icNo);
                                    }
                                } while (!icNo.matches("\\d{6}-\\d{2}-\\d{4}") || !member.authenticateRegLogin(1));
                            }
                            case 5 -> {
                                do {
                                    System.out.print("Enter new phone number (eg: 012-2342345) (0 to exit): ");
                                    phoneNo = sc.nextLine();
                                    if (phoneNo.equals("0")) {
                                        return false;
                                    } else if (!phoneNo.matches("\\d{3}-\\d{7}")) {
                                        System.out.println("You should follow the example given format !");
                                    } else if (!member.authenticateRegLogin(2)) {
                                        System.out.println("The hp no entered has already existed !");
                                    } else {
                                        member.setPhoneNo(phoneNo);
                                    }
                                } while (!phoneNo.matches("\\d{3}-\\d{7}") || !member.authenticateRegLogin(2));
                            }
                            case 6 -> {
                                do {
                                    System.out.print("Enter new home address (0 to exit): ");
                                    address = sc.nextLine();
                                    if (address.equals("0")) {
                                        return false;
                                    } else if (address.length() == 0) {
                                        System.out.println("You must enter one home address !");
                                    } else {
                                        member.setHomeAddress(address);
                                    }
                                } while (address.length() == 0);
                            }
                            case 7 -> {
                                do {
                                    do {
                                        System.out.print("Enter new account number (0 to exit): ");
                                        inputAccountNo = sc.nextLine();
                                        if (inputAccountNo.equals("0") && inputAccountNo.length() == 1) {
                                            return false;
                                        } else if (!inputAccountNo.matches("\\d+") || inputAccountNo.length() != 10) {
                                            System.out.println("Please only enter integer and the length of the account number must be 10!");
                                        } else if (inputAccountNo.charAt(0) == '0')
                                            System.out.println("The first number should not be zero!");
                                    } while (!inputAccountNo.matches("\\d+") || inputAccountNo.length() != 10 || inputAccountNo.charAt(0) == '0');
                                    accountNo = Integer.parseInt(inputAccountNo);
                                    member.setAccountNo(accountNo);
                                    if (!member.authenticateRegLogin(3)) {
                                        System.out.println("The account no entered is existed !");
                                    }
                                } while (!member.authenticateRegLogin(3));
                            }

                            case 8 -> {
                                pressEnterContinue();
                                return false;
                            }
                            default -> {
                                System.out.println("Invalid option. Please choose 1 to 7.");
                                pressEnterContinue();
                            }
                        }
                    } while (choice > 8);

                    // modify member data
                    for (String[] memberInfo : memberDetails) {
                        if (memberInfo[0].equals(memberId)) {
                            switch (choice) {
                                case 1 ->
                                    memberInfo[1] = member.getName();
                                case 2 ->
                                    memberInfo[2] = Character.toString(member.getGender());
                                case 3 ->
                                    memberInfo[3] = member.getdOB();
                                case 4 ->
                                    memberInfo[4] = member.getIcNo();
                                case 5 ->
                                    memberInfo[5] = member.getPhoneNo();
                                case 6 ->
                                    memberInfo[6] = member.getHomeAddress();
                                case 7 ->
                                    memberInfo[7] = Integer.toString(member.getAccountNo());
                            }
                        }
                    }
                    Member.writeNewMemberDataFile(memberDetails);
                    System.out.println("Member details modified successfully!");
                    validation = true;
                }
            }
            if (found == 0) {
                System.out.println("Invalid input! Please only enter correct member id or 0 to exit!");
                validation = false;
            }
        } catch (Exception e) {
            System.out.println("Invalid memebr Id or selection !");
        }
        return validation;
    }

    public static boolean searchMember(String memberId) {
        boolean validation;
        int found = 0;
        Person person = new Person() {
        };
        Member member = new Member();
        Member.readMemberData();
        ArrayList<String[]> memberDetails = Member.getMemberData();

        validation = false;
        for (String[] memberData : memberDetails) {
            if (memberData[0].equals(memberId)) {
                found++;
                System.out.println(
                        "=============================================================================================================================================");
                System.out.println(
                        "|           |            |        |               |                |              |                      |                |                 |");
                System.out.println(
                        "| Member ID | Name       | Gender | Date Of Birth | IC Number      | Phone Number | Home Address         | Account Number | Balance         |");
                System.out.println(
                        "|           |            |        |               |                |              |                      |                |                 |");
                System.out.println(
                        "=============================================================================================================================================");
                System.out.println(
                        "|           |            |        |               |                |              |                      |                |                 |");
                System.out.printf("| %-9s | %-10s | %-6s | %-13s | %-14s | %-12s | %-20s | %-14d | %-10.2f      |\n",
                        memberData[0], memberData[1], memberData[2], memberData[3], memberData[4], memberData[5],
                        memberData[6], Integer.parseInt(memberData[7]), Double.parseDouble(memberData[8]));
                System.out.println(
                        "|           |            |        |               |                |              |                      |                |                 |");
                System.out.println(
                        "=============================================================================================================================================");
            }
        }
        if (found == 0) {
            System.out.println("Invalid member id!");
            validation = false;
        }
        return validation;
    }

    public static boolean deleteMember(String memberId) {
        boolean validation;
        char confirm;
        int found = 0;
        Scanner sc = new Scanner(System.in);
        Member member = new Member();
        Member.readMemberData();
        ArrayList<String[]> memberDetails = Member.getMemberData();

        validation = false;
        for (String[] memberData : memberDetails) {
            if (memberData[0].equals(memberId)) {
                found++;
                System.out.println(
                        "=============================================================================================================================================");
                System.out.println(
                        "|           |            |        |               |                |              |                      |                |                 |");
                System.out.println(
                        "| Member ID | Name       | Gender | Date Of Birth | IC Number      | Phone Number | Home Address         | Account Number | Balance         |");
                System.out.println(
                        "|           |            |        |               |                |              |                      |                |                 |");
                System.out.println(
                        "=============================================================================================================================================");
                System.out.println(
                        "|           |            |        |               |                |              |                      |                |                 |");
                System.out.printf("| %-9s | %-10s | %-6s | %-13s | %-14s | %-12s | %-20s | %-14d | %-10.2f      |\n",
                        memberData[0], memberData[1], memberData[2], memberData[3], memberData[4], memberData[5],
                        memberData[6], Integer.parseInt(memberData[7]), Double.parseDouble(memberData[8]));
                System.out.println(
                        "|           |            |        |               |                |              |                      |                |                 |");
                System.out.println(
                        "=============================================================================================================================================\n");
                System.out.print("Are you confirm to delete this member? (y = yes/n = no): ");
                confirm = sc.nextLine().toLowerCase().charAt(0);
                if (confirm == 'y') {
                    ArrayList<String[]> updatedMemberDetails = new ArrayList<>();
                    for (String[] currentMemberData : memberDetails) {
                        if (!currentMemberData[0].equals(memberId)) {
                            updatedMemberDetails.add(currentMemberData);
                        }
                    }
                    Member.writeNewMemberDataFile(updatedMemberDetails);
                    System.out.println("The particular member has deleted successfully!");
                    validation = true;
                } else {
                    if(confirm != 'n')
                        System.out.println("Please enter valid character (y (Y) /n (N))");
                    validation = false;
                }
            }
        }
        if (found == 0) {
            System.out.println("Invalid member id!");
            validation = false;
        }
        return validation;
    }

    public static boolean rechargeMoney(String memberId) {
        boolean validation;
        double amount = 0;
        double balance = 0;
        int found = 0;
        Member member = new Member();
        Scanner sc = new Scanner(System.in);
        Member.readMemberData();
        ArrayList<String[]> memberDetails = Member.getMemberData();

        validation = false;
        for (String[] memberData : memberDetails) {
            if (memberData[0].equals(memberId)) {
                found++;
                balance = Double.parseDouble(memberData[8]);
                System.out.print("Current balance: ");
                System.out.println(balance);
                System.out.println();
                System.out.print("Recharge Amount (0 to exit): ");
                amount = sc.nextDouble();
                if (amount == 0) {
                    validation = false;
                    break;
                } else if (amount > 0) {
                    balance += amount;
                    System.out.println("Account recharge successfully!\nNew balance: " + balance);
                    memberData[8] = Double.toString(balance);
                    Member.writeNewMemberDataFile(memberDetails);
                    validation = true;
                } else {
                    System.out.println("Invalid recharge amount. Please enter a positive amount");
                    validation = false;
                    pressEnterContinue();
                }
            }
        }
        if (found == 0) {
            System.out.println("Invalid member id!");
            validation = false;
        }
        return validation;
    }

    public static void displayStaffInformationList() {
        try {
            List<Staff> staffList = readStaffFromFile();
            displayStaffInformationLogo();
            displayHeaderStaff();
            for (Staff staff : staffList) {
                staff.displayStaffInfoInTable();
            }
        } catch (IOException ex) {
            System.out.println("Can't read the staff data!");
        }
    }

    public static Boolean searchStaff(String searchId) {
        Boolean exitSearchFUNCTION;
        try {
            List<Staff> staffList = Staff.readStaffFromFile();

            Staff foundStaff = Staff.searchStaffById(staffList, searchId);

            if (foundStaff != null) {
                System.out.println("\n Staff found:");
                foundStaff.displayParticularStaffData();
                return exitSearchFUNCTION = false;
            } else {
                System.out.println("\nStaff not found.");
                return exitSearchFUNCTION = false;
            }

        } catch (IOException ex) {
            System.out.println("Can't read the staff data!");
            return exitSearchFUNCTION = false;
        }
    }

    public static Boolean modifyStaffName(String searchId) {
        Boolean exitModifyNameFUNCTION = false, validNewName;
        String newName;
        try {
            List<Staff> staffList = Staff.readStaffFromFile();
            Scanner scanner = new Scanner(System.in);

            Staff foundStaff = Staff.searchStaffById(staffList, searchId);
            if (foundStaff != null) {
                System.out.println("\nStaff found:");
                foundStaff.displayParticularStaffData();
                System.out.println("\nEnter new information -->");
                do {
                    validNewName = true;
                    System.out.print("Enter New Name (max 30 characters) : ");
                    newName = scanner.nextLine();
                    if (!newName.matches("[a-zA-Z\\s]+") || newName.length() > 30) {
                        System.out.println("Please enter the name not more than 30 characters and in alphabet !");
                        validNewName = false;
                    }
                } while (!validNewName);
                foundStaff.setName(newName);
                Staff.saveStaffToFile(staffList);
                System.out.println("\nNew Staff Name updated and saved.");
            } else {
                System.out.println("\nStaff not found.");
                exitModifyNameFUNCTION = false;
            }
        } catch (IOException e) {
            System.out.println("Error read and write the staff info.");
            exitModifyNameFUNCTION = false;
        }
        return exitModifyNameFUNCTION;
    }

    public static Boolean modifyStaffPhoneNo(String searchId) {
        Boolean exitModifyPhoneFUNCTION = false;
        try {
            String phoneNo;

            List<Staff> staffList = Staff.readStaffFromFile();
            Scanner sc = new Scanner(System.in);
            Boolean correctPhoneFormat = true;
            Staff foundStaff = Staff.searchStaffById(staffList, searchId);
            if (foundStaff != null) {
                System.out.println("\nStaff found:");
                foundStaff.displayParticularStaffData();

                System.out.print("Enter new phone number (eg: 012-2342345) : ");
                phoneNo = sc.nextLine();
                foundStaff.setPhoneNo(phoneNo);
                // Regular expression pattern for phone number
                if (!phoneNo.matches("\\d{3}-\\d{7}")) {
                    System.out.println("You should follow the example given format !");
                    exitModifyPhoneFUNCTION = false;
                    correctPhoneFormat = false;
                } else if (!foundStaff.authenticateRegLogin(5)) {
                    System.out.println("The hp no entered has already existed !");
                    exitModifyPhoneFUNCTION = false;
                    correctPhoneFormat = false;
                }
                if (correctPhoneFormat) {
                    Staff.saveStaffToFile(staffList);
                    System.out.println("\nNew Staff Phone No. updated and saved.");
                }
            } else {
                System.out.println("\nStaff not found.");
                exitModifyPhoneFUNCTION = false;
            }
        } catch (IOException e) {
            System.out.println("Error read and write the staff info.");
            exitModifyPhoneFUNCTION = false;
        }
        return exitModifyPhoneFUNCTION;
    }

    public static Boolean modifyStaffAddress(String searchId) {
        Boolean exitModifyAddressFUNCTION = false;
        try {
            String homeAddress;
            Boolean correcthomeAddressFormat = true;
            List<Staff> staffList = Staff.readStaffFromFile();
            Scanner sc = new Scanner(System.in);

            Staff foundStaff = Staff.searchStaffById(staffList, searchId);
            if (foundStaff != null) {
                System.out.println("\nStaff found:");
                foundStaff.displayParticularStaffData();

                System.out.print("Enter home address : ");
                homeAddress = sc.nextLine();
                foundStaff.setHomeAddress(homeAddress);
                // Regular expression pattern for phone number
                if (homeAddress.length() == 0) {
                    System.out.println("You must enter one home address !");
                    exitModifyAddressFUNCTION = false;
                    correcthomeAddressFormat = false;
                }
                if (correcthomeAddressFormat) {
                    Staff.saveStaffToFile(staffList);
                    System.out.println("\nNew Staff Home Address updated and saved.");
                }

            } else {
                System.out.println("\nStaff not found.");
                exitModifyAddressFUNCTION = false;
            }
        } catch (IOException e) {
            System.out.println("Error read and write the staff info.");
            exitModifyAddressFUNCTION = false;
        }
        return exitModifyAddressFUNCTION;
    }

    // ---------------------------------------------------------------Booking module done by Darren----------------------------------------------------------------
    public static void startBookingProcess(String loginStaffId) {
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
                        pressEnterContinue();
                        chckBookingMemberID(loginStaffId);
                    }
                    case 2 -> {
                        pressEnterContinue();
                        displayBookings();
                        pressEnterContinue();
                    }
                    case 3 -> {
                        pressEnterContinue();
                        cancelBooking();
                    }
                    case 4 -> {
                        if (askConfirm(1) == 1) {
                            validExitBooking = false;
                        }
                        pressEnterContinue();
                    }
                    default -> {
                        System.out.println("You should enter 1 to 4 only.");
                        pressEnterContinue();
                    }
                }
            } catch (Exception ex) {
                System.out.println("Please enter digits only (1-4) !");
                pressEnterContinue();
                sc.nextLine();
            }
        } while (validExitBooking);
    }

    // add booking function
    public static void chckBookingMemberID(String loginStaffId) throws ParseException {
        Scanner sc = new Scanner(System.in);
        boolean exitAddBooking = true;
        do {
            Booking booking = new Booking();
            Booking.clearSelectedSeatQty();
            // add booking logo
            addBookingLogo();
            System.out.print("Enter the existing member ID (eg: M01 or m01) (0 to exit): ");
            String enteredMemberID = sc.nextLine().toUpperCase();
            booking.setMemberId(enteredMemberID);
            booking.setStaffId(loginStaffId);

            // check whether the member id is equals to 0 or not
            if (enteredMemberID.equals("0")) {
                if (askConfirm(1) == 1) {
                    exitAddBooking = false;
                    pressEnterContinue();
                    break;
                }
                pressEnterContinue();
            } // check whether the member id exists or not
            else if (booking.authenticateBooking()) {
                System.out.println("Member ID is found !");
                pressEnterContinue();
                askBookingDetails(enteredMemberID, booking.getStaffId());
            } else {
                System.out.println("Member ID not found !");
                pressEnterContinue();
            }
            // press any key to continue method

        } while (exitAddBooking);
    }

    // ask booking details
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
                                pressEnterContinue();
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

                        // ask sentinel value
                        if (selectedSeatQty == 0) {
                            if (askConfirm(1) == 1) {
                                pressEnterContinue();
                                return;
                            }
                            invalidBookDetails = true;
                        } // check if selected seat qty is more than 100
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
                                pressEnterContinue();
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
                if (!Booking.chckTotalSelectedSeatQty(booking.getSelectedFlightType(), selectedSeatQty,
                        booking.getSelectedFlightId())) {
                    System.out.println(
                            "Your selected seat quantity has over the maximum available seats of the selected flight id !");
                    booking.deductTotalSeatTypeQty();
                    invalidBookDetails = true;
                } // check whether the seat quantity has over the available seats
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
                            pressEnterContinue();
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

            // print out the latest add booking details
            System.out.println(booking.viewBooking());
            if (askConfirm(4) == 1) { // want to add or not
                bookings.add(booking);
                System.out.println("<< Successfully add booking ! Please select n (N) to proceed to the payment >>\n");
                if (askConfirm(5) == 0) { // want to continue or not
                    pressEnterContinue();
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
                        System.out.printf(
                                "We are checking whether the member %s has the ability to pay for the bill....\n\n",
                                booking.getMemberId());
                        if (Payment.checkAccountBalance(booking.getMemberId(), grandTotalPayBill)) {
                            // store data into text file
                            Flight.reduceSeat(booking.getSelectedSeatQty(), booking.getSelectedFlightId(),
                                    booking.getSelectedFlightType()); // reduce seat number
                            booking.storeBooking(bookings, personInchargeId, enteredMemberID); // store booking data
                            // into booking.txt file
                            payment.storePaymentBill(personInchargeId, enteredMemberID); // store payment bill
                            payment.storeAccountBalance(); // store account balance to the particular member id account

                            System.out.printf(
                                    "  <<Yay, the member have successfully pay the bill.>>\n\nPress any continue to view the member %s ticket\n",
                                    booking.getMemberId()); // generate bill successful message
                            pressEnterContinue();
                            Flight flight = new Flight();
                            Flight.readFlightData();
                            ArrayList<String[]> flightData = Flight.getFlightData();
                            ticket.generateTicket(bookings, flightData);
                            bookings.clear();
                            // continue to add ?
                            if (askConfirm(5) == 1) {
                                pressEnterContinue();
                                invalidBookDetails = true;
                            } else {
                                pressEnterContinue();
                            }
                        } else { // when member does not have sufficient money
                            System.out.println(
                                    "The member has not enough money to pay the bill. The member needs to recharge account balance !");
                            flights.clear(); // clear array list
                            bookings.clear();
                            pressEnterContinue();
                        }
                    } else if (billOpt.equals("n")) { // when member don't want to pay the money
                        if (askConfirm(5) == 1) {
                            pressEnterContinue();
                            invalidBookDetails = true;
                        } else {
                            bookings.clear();
                            pressEnterContinue();
                            invalidBookDetails = false;
                        }
                    }
                } else { // when member want to reselect the booking
                    pressEnterContinue();
                    invalidBookDetails = true;
                }
            } else {
                pressEnterContinue();
                invalidBookDetails = true;
            }
        } while (invalidBookDetails);
    }

    public static void displayBookings() {
        displayBookingLogo();
        Booking.readBookingData();
        ArrayList<String[]> bookingData = Booking.getBookingData();
        // Print the header template
        System.out.println(
                "                  ===============================================================================================================================");
        System.out.println(
                "                 |                                                      BOOKING DETAILS                                                          |");
        System.out.println(
                "                 |===============================================================================================================================|");
        System.out.println(
                "                 | Booking | Staff ID  | Member ID |  Flight ID  |  Seat Selected  |       Selected        |  Selected Flight  |     Booking     |");
        System.out.println(
                "                 |   ID    |           |           |             |    Quantity     |      Flight Type      |        Way        |       Date      |");
        System.out.println(
                "                 |_________|___________|___________|_____________|_________________|_______________________|___________________|_________________|");
        for (String[] bookingDetails : bookingData) {
            System.out.printf(
                    "                 |   %-4s  |    %-5s  |    %-5s  |     %-4s    |       %-5s     |        %-8s       |      %-6s      |    %-9s   |\n",
                    bookingDetails[0], bookingDetails[1], bookingDetails[2], bookingDetails[3], bookingDetails[4],
                    bookingDetails[5], bookingDetails[6], bookingDetails[7]);
        }
        System.out.println(
                "                 |_________|___________|___________|_____________|_________________|_______________________|___________________|_________________|\n");
    }

    public static void cancelBooking() {
        Scanner sc = new Scanner(System.in);
        String bookingId;
        boolean validExit = true;
        Booking booking = new Booking();
        do {
            validExit = false;
            cancelBookingLogo();
            System.out.print("Enter the Booking ID to cancel (e.g., B01) (0 to exit): ");
            bookingId = sc.nextLine().toUpperCase();
            if (bookingId.equals("0")) {
                if (askConfirm(1) == 1) {
                    pressEnterContinue();
                    validExit = true;
                } else {
                    pressEnterContinue();
                    validExit = false;
                }
            } else {
                // extract booking details
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
                    updateFlightSeats(booking.getSelectedFlightId(), booking.getSelectedSeatQty(),
                            booking.getSelectedFlightType());
                    pressEnterContinue();
                } else {
                    System.out.println("Booking ID not found!");
                    pressEnterContinue();
                }
            }
        } while (!validExit);
    }

    public static SimpleEntry<String, Double> retrievePaymentDetails(String paymentId) {
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

    public static void updateMemberBalance(String memberId, double amount) {
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

    // -----------------------------------------------------------Ticket module done by Chang Zhi Cong-----------------------------------------------------------
    public static void startTicketingProcess() {
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
                        pressEnterContinue();
                        viewSuccessBookedTicket();
                    }
                    case 2 -> {
                        pressEnterContinue();
                        viewTicketReport();
                    }
                    case 3 -> {
                        if (askConfirm(1) == 1) {
                            pressEnterContinue();
                            return;
                        } else {
                            pressEnterContinue();
                        }
                    }
                    default -> {
                        System.out.println("Please enter 1- 3 digit only");
                        pressEnterContinue();
                    }
                }
            } catch (Exception ex) {
                System.out.println("You can only select digit number (1-3) !");
                pressEnterContinue();
                sc.nextLine();
            }
        } while (validTicketOpt);
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
                            pressEnterContinue();
                            viewAllTicket();
                            pressEnterContinue();
                        } catch (Exception ex) {
                            System.out.println("An error occurred while viewing all tickets: " + ex.getMessage());
                        }
                    }
                    case 2 -> {
                        pressEnterContinue();
                        viewSpecificTicket();
                    }
                    case 3 -> {
                        if (askConfirm(1) == 1) {
                            validViewTicket = false;
                        }
                        pressEnterContinue();
                    }
                    default -> {
                        System.out.println("You should enter 1 - 3 digit only !");
                        pressEnterContinue();
                    }
                }
            } catch (Exception ex) {
                System.out.println("You must enter digits (1-3) !");
                pressEnterContinue();
                sc.nextLine();
            }
        } while (validViewTicket);
    }

    // view specific ticket
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
                        pressEnterContinue();
                        viewByTicketID();
                    }
                    case 2 -> {
                        pressEnterContinue();
                        viewByMemberID();
                    }
                    case 3 -> {
                        pressEnterContinue();
                        viewByFlightID();
                    }

                    case 4 -> {
                        if (askConfirm(1) == 1) {
                            validViewSpecTicket = false;
                        }
                        pressEnterContinue();
                    }
                }
            } catch (Exception ex) {
                System.out.println("You must enter digits (1-4) !");
                pressEnterContinue();
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
                    pressEnterContinue();
                    if (ticketExitNo == 1) {
                        exitViewTicketID = false;
                    }
                } else {
                    System.out.printf("Finding the particular %s ID ........\n", srchTicketID);
                    System.out.println("Result: \n");
                    if (!viewSpecificTicket(1, srchTicketID)) {
                        System.out.println("Unable to find the particular ID ticket !");
                        pressEnterContinue();
                    } else {
                        pressEnterContinue();
                    }
                }
            } catch (Exception ex) {
                System.out.println("Please enter in String format (eg T01) !");
            }
        } while (exitViewTicketID);
    }

    // view by member id
    public static void viewByMemberID() {
        Scanner sc = new Scanner(System.in);
        boolean exitViewBookingID = true;
        do {
            try {
                System.out.print("Enter existing member ID (eg. M01 /m01) (0 to exit): ");
                String srchMemberID = sc.nextLine().toUpperCase();
                if (srchMemberID.equals("0")) {
                    int ticketExitNo = askConfirm(1);
                    pressEnterContinue();
                    if (ticketExitNo == 1) {
                        exitViewBookingID = false;
                    }
                } else {
                    System.out.printf("\nFinding the particular %s ID ........\n", srchMemberID);
                    System.out.println("Result: \n");
                    if (!viewSpecificTicket(2, srchMemberID)) {
                        System.out.println("Unable to find the particular ID ticket !");
                        pressEnterContinue();
                    } else {
                        pressEnterContinue();
                    }
                }
            } catch (Exception ex) {
                System.out.println("Please enter in String format (eg M01) !");
            }
        } while (exitViewBookingID);
    }

    // view by flight id
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
                    pressEnterContinue();
                } else {
                    System.out.printf("\nFinding the particular %s ID ........\n", srchFlightID);
                    System.out.println("Result: \n");
                    if (!viewSpecificTicket(3, srchFlightID)) {
                        System.out.println("Unable to find the particular ID ticket !");
                        pressEnterContinue();
                    } else {
                        pressEnterContinue();
                    }
                }
            } catch (Exception ex) {
                System.out.println("Please enter in String format (eg F01) !");
            }
        } while (exitViewMemberID);
    }

    // view ticket report
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
                        pressEnterContinue();
                        totalticketTypeReport();
                    }
                    case 2 -> {
                        pressEnterContinue();
                        specificDateReport();
                    }
                    case 3 -> {
                        if (askConfirm(1) == 1) {
                            validReport = false;
                        }
                        pressEnterContinue();
                    }
                }
            } catch (Exception ex) {
                System.out.println("Please enter digits only (1-3) !");
                sc.nextLine();
                validReport = true;
                pressEnterContinue();
            }
        } while (validReport);
    }

    // view total flight type report
    public static void totalticketTypeReport() {
        Ticket ticket = new Ticket();
        EconomyTicket ecoTicket = new EconomyTicket() {
        }; // abstract class to be instantiated , put {}
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
            System.out.printf("                     | %7s     | %11s    | %8d        |\n", ticketId, ticketType,
                    ticketQuantity);

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
        pressEnterContinue();
    }

    // view specific date sales ticket report
    public static boolean viewSpecficDateReport(String initialDate, String finalDate) throws ParseException {

        EconomyTicket ecoTicket = new EconomyTicket() {
        }; // abstract class to be instantiated , put {}
        BusinessTicket bussTicket = new BusinessTicket() {
        };
        PremiumTicket preTicket = new PremiumTicket() {
        };
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
        Ticket ticket = new Ticket();
        Ticket.readTicketData();
        int found = 0;
        ArrayList<String[]> ticketData = Ticket.getTicketData();

        Date startDate = dateFormat.parse(initialDate);
        Date endDate = dateFormat.parse(finalDate);
        // Print the table manner
        System.out.println("Result: ");
        System.out.println("                      ===============================================================");
        System.out.printf("                     |       Ticket Sales from %s to %s Report       |\n", initialDate,
                finalDate);
        System.out.println("                     |===============================================================|");
        System.out.println("                     |  Ticket ID  |      Ticket Type       |     Ticket Quantity    |");
        System.out.println("                     |_____________|________________________|________________________|");
        // Process each ticket record and print the details in table form
        for (String[] ticketRecord : ticketData) {
            String ticketId = ticketRecord[0];
            String ticketType = ticketRecord[4];
            String dateStr = ticketRecord[7]; // date is in string[7]
            Date ticketDate = dateFormat.parse(ticketRecord[7]);
            int ticketQuantity = Integer.parseInt(ticketRecord[8]);

            // Print the ticket details in table form
            if (ticketDate.compareTo(startDate) >= 0 && ticketDate.compareTo(endDate) <= 0) {
                System.out.printf("                     | %7s     | %14s         | %11d            |\n", ticketId,
                        ticketType, ticketQuantity);
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
        return found != 0;
    }

    // view specific date ticket sales report
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
                        pressEnterContinue();
                        return;
                    }
                } else if (!initialSearchDate.matches(
                        "^(0[1-9]|[12][0-9]|[3][0-1])/(0[1-9]|1[0-2])/(19[9][0-9]|200[0-9]|20[0-1][0-9]|202[0-3])$")) {
                    System.out.println(
                            "Your initial search date ould be in the range 01-01-1990 to 31-12-2023 and follow the correct date format (DD/MM/YYYY).");
                }
            } while (!initialSearchDate.matches(
                    "^(0[1-9]|[12][0-9]|[3][0-1])/(0[1-9]|1[0-2])/(19[9][0-9]|200[0-9]|20[0-1][0-9]|202[0-3])$"));
            do {
                System.out.print("Enter a final date of the report (eg. 26/09/2023) (0 to exit) : ");
                finalSearchDate = sc.nextLine().trim();
                if (finalSearchDate.equals("0")) {
                    if (askConfirm(1) == 1) {
                        pressEnterContinue();
                        return;
                    }
                } else if (!finalSearchDate.matches(
                        "^(0[1-9]|[12][0-9]|[3][0-1])/(0[1-9]|1[0-2])/(19[9][0-9]|200[0-9]|20[0-1][0-9]|202[0-3])$")) {
                    System.out.println(
                            "Your final search date should be in the range 01-01-1990 to 31-12-2023 and follow the correct date format (DD/MM/YYYY).");
                }
            } while (!finalSearchDate.matches(
                    "^(0[1-9]|[12][0-9]|[3][0-1])/(0[1-9]|1[0-2])/(19[9][0-9]|200[0-9]|20[0-1][0-9]|202[0-3])$"));
            if (!viewSpecficDateReport(initialSearchDate, finalSearchDate)) {
                System.out.println("\nNo result found ! Please try to enter another specific date.");
            } else {
                found = false;
            }

            pressEnterContinue();
        } while (!found);
    }

    // view specific ticket data
    public static boolean viewSpecificTicket(int viewTypeNum, String idSearched) {
        Ticket ticket = new Ticket();
        Ticket.readTicketData();
        ArrayList<String[]> ticketData = Ticket.getTicketData();
        int found = 0;

        System.out.println(
                "                          ===============================================================================================================================");
        System.out.printf(
                "                         |                                                       View %s Ticket                                                         |\n",
                idSearched);
        System.out.println(
                "                         |===============================================================================================================================|");
        System.out.println(
                "                         |  Ticket ID |  Member ID | Flight ID | Gate No | Flight Type | Flight Way |   Destination   | Departure Date | Ticket Quantity |");
        System.out.println(
                "                         |____________|____________|___________|_________|_____________|____________|_________________|________________|_________________|");
        for (String[] ticketDetails : ticketData) {
            if (viewTypeNum == 1 && idSearched.equals(ticketDetails[0])) {
                System.out.printf(
                        "                         |%8s    |%8s    |%7s    |%6s   |%10s   |%10s  |%13s    |%13s   |%9d        |\n",
                        ticketDetails[0], ticketDetails[1], ticketDetails[2], ticketDetails[3], ticketDetails[4],
                        ticketDetails[5], ticketDetails[6], ticketDetails[7], Integer.parseInt(ticketDetails[8]));
                found++;
            } else if (viewTypeNum == 2 && idSearched.equals(ticketDetails[1])) {
                System.out.printf(
                        "                         |%8s    |%8s    |%7s    |%6s   |%10s   |%10s  |%13s    |%13s   |%9d        |\n",
                        ticketDetails[0], ticketDetails[1], ticketDetails[2], ticketDetails[3], ticketDetails[4],
                        ticketDetails[5], ticketDetails[6], ticketDetails[7], Integer.parseInt(ticketDetails[8]));
                found++;
            } else if (viewTypeNum == 3 && idSearched.equals(ticketDetails[2])) {
                System.out.printf(
                        "                         |%8s    |%8s    |%7s    |%6s   |%10s   |%10s  |%13s    |%13s   |%9d        |\n",
                        ticketDetails[0], ticketDetails[1], ticketDetails[2], ticketDetails[3], ticketDetails[4],
                        ticketDetails[5], ticketDetails[6], ticketDetails[7], Integer.parseInt(ticketDetails[8]));
                found++;
            }
        }
        System.out.println(
                "                          ===============================================================================================================================\n");
        return found > 0;
    }

    // view all ticket
    public static void viewAllTicket() {
        Ticket ticket = new Ticket();
        Ticket.readTicketData();
        ArrayList<String[]> ticketData = Ticket.getTicketData();
        System.out.println(
                "                      ===============================================================================================================================");
        System.out.println(
                "                     |                                                          View All Ticket                                                      |");
        System.out.println(
                "                     |===============================================================================================================================|");
        System.out.println(
                "                     |  Ticket ID |  Member ID | Flight ID | Gate No | Flight Type | Flight Way |   Destination   | Departure Date | Ticket Quantity |");
        System.out.println(
                "                     |____________|____________|___________|_________|_____________|____________|_________________|________________|_________________|");

        for (String[] ticketDetails : ticketData) {
            System.out.printf(
                    "                     |%8s    |%8s    |%7s    |%6s   |%10s   |%10s  |%13s    |%13s   |%9d        |\n",
                    ticketDetails[0], ticketDetails[1], ticketDetails[2], ticketDetails[3], ticketDetails[4],
                    ticketDetails[5], ticketDetails[6], ticketDetails[7], Integer.parseInt(ticketDetails[8]));
        }
        System.out.println(
                "                      ===============================================================================================================================\n");
    }

    // ------------------------------------------------------------------Payment module done by Lee Chun Yin----------------------------------------------------------------------
    public static void startPaymentProcess() {
        Scanner sc = new Scanner(System.in);
        int displaySelection;
        int menuExit = 0, exit = 0;
        do {
            try {
                paymentLogo();
                System.out.print("\nEnter your selection : ");
                displaySelection = sc.nextInt();
                menuExit = 0;
                switch (displaySelection) {

                    case 1 -> {
                        pressEnterContinue();
                        exit = displayPayment();
                    }
                    case 2 -> {
                        do {
                            try {
                                exit = 0;
                                // press any continue and clear screen
                                pressEnterContinue();
                                sc.nextLine();
                                paymentReportLogo();
                                paymentReportMenu();
                                System.out.print(
                                        "\nEnter your selection : ");
                                displaySelection = sc.nextInt();
                                switch (displaySelection) {
                                    case 1 -> {
                                        pressEnterContinue();
                                        displayReport();
                                    }
                                    case 2 -> {
                                        pressEnterContinue();
                                        displayStaffCommissionReport();
                                    }
                                    case 3 -> {
                                        if (askConfirm(1) == 1) {
                                            exit = 1;
                                            pressEnterContinue();
                                        }
                                    }
                                    default ->
                                        System.out.println("You must enter between 1 - 3!");
                                }
                            } catch (Exception ex) {
                                System.out.println("You must enter integer!");
                            }
                        } while (exit != 1);
                    }
                    case 3 -> {
                        if (askConfirm(1) == 1) {
                            pressEnterContinue();
                            return;
                        } else {
                            pressEnterContinue();
                        }
                    }
                    default -> {
                    }
                }
            } catch (Exception ex) {
                System.out.println("Please enter digits only ! (1 - 3)");
                menuExit = 0;
                pressEnterContinue();
                sc.nextLine();
            }
        } while (menuExit != 1);
    }

    // display payment
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
            pressEnterContinue();
        } while (selectPayment != 5);
        return exit;
    }

    // display report
    public static void displayReport() {
        Payment payment = new Payment();
        Payment.readPaymentData();
        ArrayList<String[]> paymentDetails = Payment.getPaymentDetails();
        double subTotalPayment = 0;

        System.out.println("\t\t\t\t\t =====================================================");
        System.out.println("\t\t\t\t\t|             DISPLAY TOTAL PAYMENT REPORT            |");
        System.out.println("\t\t\t\t\t|=====================================================|");
        System.out.println("\t\t\t\t\t| Payment ID | Member ID | Total Payment Price (RM)   |");
        System.out.println("\t\t\t\t\t|============|===========|============================|");

        for (String[] paymentInfo : paymentDetails) {
            System.out.printf("\t\t\t\t\t|    %-7s |    %-7s| %17.2f          |\n",
                    paymentInfo[0], paymentInfo[4],
                    Double.parseDouble(paymentInfo[1]));
            subTotalPayment += Double.parseDouble(paymentInfo[1]);
        }

        System.out.println("\t\t\t\t\t|=====================================================|");
        System.out.printf("\t\t\t\t\t|                        Grand Total (RM) : %9.2f |\n", subTotalPayment);
        System.out.printf("\t\t\t\t\t|                Grand Service Charge (RM):  %8.2f |\n",
                Payment.getServiceCharge());
        System.out.printf("\t\t\t\t\t|                      Grand Discount (RM):  %8.2f |\n", Payment.getDiscount());
        System.out.println("\t\t\t\t\t =====================================================");
        System.out.printf("\t\t\t\t\t              Final Total Price Earned (RM): %.2f\n", Payment.getGrandTotalPay());
    }

    // display staff commission report
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

    // display all payment
    public static void displayAllPayment() {
        Payment payment = new Payment();
        Payment.readPaymentData();
        ArrayList<String[]> paymentDetails = Payment.getPaymentDetails();
        System.out.println(
                "\t ==========================================================================================");
        System.out.println(
                "\t|                                DISPLAY ALL PAYMENT DETAILS                               |");
        System.out.println(
                "\t|==========================================================================================|");
        System.out.println(
                "\t| Payment ID | Total Payment Price (RM)   | Payment Date | Person Incharge ID | Member ID  |");
        System.out.println(
                "\t|============|============================|==============|====================|============|");

        for (String[] paymentInfo : paymentDetails) {
            System.out.printf("\t|    %-7s | %17.2f          |  %-12s|        %-11s |    %-7s |\n",
                    paymentInfo[0],
                    Double.parseDouble(paymentInfo[1]),
                    paymentInfo[2],
                    paymentInfo[3],
                    paymentInfo[4]);
        }
        System.out.println(
                "\t ==========================================================================================");
    }

    // display specific payment
    public static boolean displaySpecificPayment(int paymentType, String searchID) {
        int found = 0;
        Payment payment = new Payment();
        Payment.readPaymentData();
        ArrayList<String[]> paymentDetails = Payment.getPaymentDetails();
        System.out.println(
                "\t ==========================================================================================");
        System.out.println(
                "\t|                             DISPLAY SPECIFIC PAYMENT DETAILS                             |");
        System.out.println(
                "\t|==========================================================================================|");
        System.out.println(
                "\t| Payment ID | Total Payment Price (RM)   | Payment Date | Person Incharge ID | Member ID  |");
        System.out.println(
                "\t|============|============================|==============|====================|============|");

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
        System.out.println(
                "\t ==========================================================================================");

        return found > 0;
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
                Thread.sleep(50); // add delay in milisecond, if not there will automatically stop after clear
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    // press any continue
    public static void pressEnterContinue() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Press [Enter] key to continue...");
        sc.nextLine();
        clearScreen();
    }

    // -----------------------------------------------------All
    // logos-----------------------------------------------------------------
    // home logo
    public static void homeMenuLogo() {
        System.out.println("\t\t\t\t\t      ___           ___           ___       ___                       ___     ");
        System.out.println(
                "\t\t\t\t\t     /\\__\\         /\\  \\         /\\__\\     /\\  \\          ___        /\\  \\    ");
        System.out.println(
                "\t\t\t\t\t    /::|  |       /::\\  \\       /:/  /    /::\\  \\        /\\  \\      /::\\  \\   ");
        System.out.println(
                "\t\t\t\t\t   /:|:|  |      /:/\\:\\  \\     /:/  /    /:/\\:\\  \\       \\:\\  \\    /:/\\:\\  \\  ");
        System.out.println(
                "\t\t\t\t\t  /:/|:|__|__   /::\\~\\:\\  \\   /:/  /    /::\\~\\:\\  \\      /::\\__\\  /::\\~\\:\\  \\ ");
        System.out.println(
                "\t\t\t\t\t /:/ |::::\\__\\ /:/\\:\\ \\:\\__\\ /:/__/    /:/\\:\\ \\:\\__\\  __/:/\\/__/ /:/\\:\\ \\:\\__\\");
        System.out.println(
                "\t\t\t\t\t \\/__/~~/:/  / \\/__\\:\\/:/  / \\:\\  \\    \\/__\\:\\/:/  / /\\/:/  /    \\/_|::\\/:/  /");
        System.out.println(
                "\t\t\t\t\t       /:/  /       \\::/  /   \\:\\  \\        \\::/  /  \\::/__/        |:|::/  / ");
        System.out.println(
                "\t\t\t\t\t      /:/  /        /:/  /     \\:\\  \\       /:/  /    \\:\\__\\        |:|\\/__/  ");
        System.out.println(
                "\t\t\t\t\t     /:/  /        /:/  /       \\:\\__\\     /:/  /      \\/__/        |:|  |    ");
        System.out.println(
                "\t\t\t\t\t     \\/__/         \\/__/         \\/__/     \\/__/                     \\|__|    \n");

        // display main menu
        System.out.println("\t\t\t\t\t                __  __       _         __  __                  ");
        System.out.println("\t\t\t\t\t               |  \\/  |     (_)       |  \\/  |                 ");
        System.out.println("\t\t\t\t\t               | \\  / | __ _ _ _ __   | \\  / | ___ _ __  _   _ ");
        System.out.println("\t\t\t\t\t               | |\\/| |/ _` | | '_ \\  | |\\/| |/ _ \\ '_ \\| | | |");
        System.out.println("\t\t\t\t\t               | |  | | (_| | | | | | | |  | |  __/ | | | |_| |");
        System.out.println("\t\t\t\t\t               |_|  |_|\\__,_|_|_| |_| |_|  |_|\\___|_| |_|\\__,_|");
        System.out.println("\t\t\t\t\t          ==========================================================");
        System.out.println("\t\t\t\t\t         |1. Staff                                                  |");
        System.out.println("\t\t\t\t\t         |                                                          |");
        System.out.println("\t\t\t\t\t         |2. Member                                                 |");
        System.out.println("\t\t\t\t\t         |                                                          |");
        System.out.println("\t\t\t\t\t         |3. Booking                                                |");
        System.out.println("\t\t\t\t\t         |                                                          |");
        System.out.println("\t\t\t\t\t         |4. Ticket                                                 |");
        System.out.println("\t\t\t\t\t         |                                                          |");
        System.out.println("\t\t\t\t\t         |5. Payment                                                |");
        System.out.println("\t\t\t\t\t         |                                                          |");
        System.out.println("\t\t\t\t\t         |6. Exit                                                   |");
        System.out.println("\t\t\t\t\t          ==========================================================\n");
    }

    public static void loginLogo() {
        System.out.println("  _____          .__         .__         .__                .__");
        System.out.println("  /     \\ _____  |  | _____  |__|______  |  |   ____   ____ |__| ____  ");
        System.out.println(" /  \\ /  \\\\__  \\ |  | \\__  \\ |  \\_  __ \\ |  |  /  _ \\ / ___\\|  |/    \\");
        System.out.println("/    Y    \\/ __ \\|  |__/ __ \\|  ||  | \\/ |  |_(  <_> ) /_/  >  |   |  \\");
        System.out.println("\\____|__  (____  /____(____  /__||__|    |____/\\____/\\___  /|__|___|  /");
        System.out.println("\\____|__  (____  /____(____  /__||__|    |____/\\____/\\___  /|__|___|  /");
        System.out.println(" \\/     \\/          \\/                       /_____/         \\/");
        System.out.println("");
    }

    // staff logo
    public static void staffLogo() {
        System.out.println("\t\t\t    _____ _         __  __ ");
        System.out.println("\t\t\t   / ____| |       / _|/ _|");
        System.out.println("\t\t\t  | (___ | |_ __ _| |_| |_ ");
        System.out.println("\t\t\t   \\___ \\| __/ _` |  _|  _|");
        System.out.println("\t\t\t   ____) | || (_| | | | |  ");
        System.out.println("\t\t\t  |_____/ \\__\\__,_|_| |_|  \n");

        System.out.println(
                "-----------------------------------------------------------------------------");
        System.out.println("\t\t |  1. Display All Staff Information         |");
        System.out.println("\t\t |                                           |");
        System.out.println("\t\t |  2. Search Staff Information              |");
        System.out.println("\t\t |                                           |");
        System.out.println("\t\t |  3. Modify Staff Information              |");
        System.out.println("\t\t |                                           |");
        System.out.println("\t\t |  4. Exit                                  |");
        System.out.println(
                "-----------------------------------------------------------------------------");
    }

    public static void searchStaffLogo() {
        System.out.println("\t   _____                     _        _____ _         __  __ ");
        System.out.println("\t  / ____|                   | |      / ____| |       / _|/ _|");
        System.out.println("\t | (___   ___  __ _ _ __ ___| |__   | (___ | |_ __ _| |_| |_ ");
        System.out.println("\t  \\___ \\ / _ \\/ _` | '__/ __| '_ \\   \\___ \\| __/ _` |  _|  _|");
        System.out.println("\t  ____) |  __/ (_| | | | (__| | | |  ____) | || (_| | | | |  ");
        System.out.println("\t |_____/ \\___|\\__,_|_|  \\___|_| |_| |_____/ \\__\\__,_|_| |_|  \n");

    }

    public static void modifyStaffLogo() {
        System.out.println("\t __  __           _ _  __          _____ _         __  __ ");
        System.out.println("\t|  \\/  |         | (_)/ _|        / ____| |       / _|/ _|");
        System.out.println("\t| \\  / | ___   __| |_| |_ _   _  | (___ | |_ __ _| |_| |_ ");
        System.out.println("\t| |\\/| |/ _ \\ / _` | |  _| | | |  \\___ \\| __/ _` |  _|  _|");
        System.out.println("\t| |  | | (_) | (_| | | | | |_| |  ____) | || (_| | | | |  ");
        System.out.println("\t|_|  |_|\\___/ \\__,_|_|_|  \\__, | |_____/ \\__\\__,_|_| |_|  ");
        System.out.println("\t                           __/ |                          ");
        System.out.println("\t                          |___/                           ");

        System.out.println(
                "---------------------------------------------------------------------");
        System.out.println("\t\t |  1. Modify Staff Name          |");
        System.out.println("\t\t |                                |");
        System.out.println("\t\t |  2. Modify Staff Phone No.     |");
        System.out.println("\t\t |                                |");
        System.out.println("\t\t |  3. Modify Staff Adderess      |");
        System.out.println("\t\t |                                |");
        System.out.println("\t\t |  4. Exit                       |");
        System.out.println(
                "---------------------------------------------------------------------");
    }

    public static void displayHeaderStaff() {
        System.out.println(
                "========================================================================================================================");
        System.out.printf("%-9s %-20s %-11s %-18s %-15s %-15s %-20s%n", "Staff ID", "Staff Name",
                "Gender", "Date Of Birth", "IC Number", "Phone Number", "Address");
        System.out.println(
                "========================================================================================================================");
    }

    public static void displayStaffInformationLogo() {
        System.out.println("\t\t\t                 _____ _         __  __   _____        __ ");
        System.out.println("\t\t\t                / ____| |       / _|/ _| |_   _|      / _|");
        System.out.println("\t\t\t               | (___ | |_ __ _| |_| |_    | |  _ __ | |_ ___ ");
        System.out.println("\t\t\t                \\___ \\| __/ _` |  _|  _|   | | | '_ \\|  _/ _ \\");
        System.out.println("\t\t\t                ____) | || (_| | | | |    _| |_| | | | || (_) |");
        System.out.println("\t\t\t               |_____/ \\__\\__,_|_| |_|   |_____|_| |_|_| \\___/ ");
    }

    // member logo
    public static void memberLogo() {
        System.out.println("\t\t    __  __                _               ");
        System.out.println("\t\t   |  \\/  |              | |             ");
        System.out.println("\t\t   | \\  / | ___ _ __ ___ | |__   ___ _ __ ");
        System.out.println("\t\t   | |\\/| |/ _ \\ '_ ` _ \\| '_ \\ / _ \\ '__|");
        System.out.println("\t\t   | |  | |  __/ | | | | | |_) |  __/ |   ");
        System.out.println("\t\t   |_|  |_|\\___|_| |_| |_|_.__/ \\___|_|   \n");

        System.out.println(
                "-----------------------------------------------------------------------------");
        System.out.println("\t\t    |  1. Add Member                 |");
        System.out.println("\t\t    |                                |");
        System.out.println("\t\t    |  2. Modify Member              |");
        System.out.println("\t\t    |                                |");
        System.out.println("\t\t    |  3. Search Member              |");
        System.out.println("\t\t    |                                |");
        System.out.println("\t\t    |  4. Delete Member              |");
        System.out.println("\t\t    |                                |");
        System.out.println("\t\t    |  5. Recharge Money             |");
        System.out.println("\t\t    |                                |");
        System.out.println("\t\t    |  6. Exit                       |");
        System.out.println("-----------------------------------------------------------------------------");
    }

    public static void addMemberLogo() {
        System.out.println("\t              _     _   __  __                _               ");
        System.out.println("\t     /\\      | |   | | |  \\/  |              | |              ");
        System.out.println("\t    /  \\   __| | __| | | \\  / | ___ _ __ ___ | |__   ___ _ __ ");
        System.out.println("\t   / /\\ \\ / _` |/ _` | | |\\/| |/ _ \\ '_ ` _ \\| '_ \\ / _ \\ '__|");
        System.out.println("\t  / ____ \\ (_| | (_| | | |  | |  __/ | | | | | |_) |  __/ |   ");
        System.out.println("\t /_/    \\_\\__,_|\\__,_| |_|  |_|\\___|_| |_| |_|_.__/ \\___|_|   \n");
    }

    public static void modifyMemberLogo() {
        System.out.println("\t  __  __           _ _  __         __  __                _               ");
        System.out.println("\t |  \\/  |         | (_)/ _|       |  \\/  |              | |              ");
        System.out.println("\t | \\  / | ___   __| |_| |_ _   _  | \\  / | ___ _ __ ___ | |__   ___ _ __ ");
        System.out.println("\t | |\\/| |/ _ \\ / _` | |  _| | | | | |\\/| |/ _ \\ '_ ` _ \\| '_ \\ / _ \\ '__|");
        System.out.println("\t | |  | | (_) | (_| | | | | |_| | | |  | |  __/ | | | | | |_) |  __/ |");
        System.out.println("\t |_|  |_|\\___/ \\__,_|_|_|  \\__, | |_|  |_|\\___|_| |_| |_|_.__/ \\___|_|   ");
        System.out.println("\t                            __/ |                                         ");
        System.out.println("\t                           |___/                                          \n");
    }

    public static void searchMemberLogo() {
        System.out.println("\t   _____                     _       __  __                _               ");
        System.out.println("\t  / ____|                   | |     |  \\/  |              | |              ");
        System.out.println("\t | (___   ___  __ _ _ __ ___| |__   | \\  / | ___ _ __ ___ | |__   ___ _ __ ");
        System.out.println("\t  \\___ \\ / _ \\/ _` | '__/ __| '_ \\  | |\\/| |/ _ \\ '_ ` _ \\| '_ \\ / _ \\ '__|");
        System.out.println("\t  ____) |  __/ (_| | | | (__| | | | | |  | |  __/ | | | | | |_) |  __/ |   ");
        System.out.println("\t |_____/ \\___|\\__,_|_|  \\___|_| |_| |_|  |_|\\___|_| |_| |_|_.__/ \\___|_|   \n");
    }

    public static void deleteMemberLogo() {
        System.out.println("\t  _____       _      _         __  __                _               ");
        System.out.println("\t |  __ \\     | |    | |       |  \\/  |              | |              ");
        System.out.println("\t | |  | | ___| | ___| |_ ___  | \\  / | ___ _ __ ___ | |__   ___ _ __ ");
        System.out.println("\t | |  | |/ _ \\ |/ _ \\ __/ _ \\ | |\\/| |/ _ \\ '_ ` _ \\| '_ \\ / _ \\ '__|");
        System.out.println("\t | |__| |  __/ |  __/ ||  __/ | |  | |  __/ | | | | | |_) |  __/ |   ");
        System.out.println("\t |_____/ \\___|_|\\___|\\__\\___| |_|  |_|\\___|_| |_| |_|_.__/ \\___|_|   ");
    }

    public static void rechargeAmountLogo() {
        System.out.println("\t  _____           _                            __  __                        ");
        System.out.println("\t |  __ \\         | |                          |  \\/  |                       ");
        System.out.println("\t | |__) |___  ___| |__   __ _ _ __ __ _  ___  | \\  / | ___  _ __   ___ _   _ ");
        System.out.println("\t |  _  // _ \\/ __| '_ \\ / _` | '__/ _` |/ _ \\ | |\\/| |/ _ \\| '_ \\ / _ \\ | | |");
        System.out.println("\t | | \\ \\  __/ (__| | | | (_| | | | (_| |  __/ | |  | | (_) | | | |  __/ |_| |");
        System.out.println("\t |_|  \\_\\___|\\___|_| |_|\\__,_|_|  \\__, |\\___| |_|  |_|\\___/|_| |_|\\___|\\__, |");
        System.out.println("\t                                   __/ |                                __/ |");
        System.out.println("\t                                  |___/                                |___/ ");
    }

    // booking logo
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
        System.out.println(
                "\t\t\t\t              _____  _           _               ____              _    _             ");
        System.out.println(
                "\t\t\t\t             |  __ \\(_)         | |             |  _ \\            | |  (_)            ");
        System.out.println(
                "\t\t\t\t             | |  | |_ ___ _ __ | | __ _ _   _  | |_) | ___   ___ | | ___ _ __   __ _ ");
        System.out.println(
                "\t\t\t\t             | |  | | / __| '_ \\| |/ _` | | | | |  _ < / _ \\ / _ \\| |/ / | '_ \\ / _` |");
        System.out.println(
                "\t\t\t\t             | |__| | \\__ \\ |_) | | (_| | |_| | | |_) | (_) | (_) |   <| | | | | (_| |");
        System.out.println(
                "\t\t\t\t             |_____/|_|___/ .__/|_|\\__,_|\\__, | |____/ \\___/ \\___/|_|\\_\\_|_| |_|\\__, |");
        System.out.println(
                "\t\t\t\t                          | |             __/ |                                  __/ |");
        System.out.println(
                "\t\t\t\t                          |_|            |___/                                  |___/ \n\n");
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

    // ticket logo
    public static void successTicketLogo() {
        System.out.println("");
        System.out.println("                   __   ___              _____ _    _       _    ");
        System.out.println("                   \\ \\ / (_)_____ __ __ |_   _(_)__| |_____| |_  ");
        System.out.println("                    \\ V /| / -_) V  V /   | | | / _| / / -_)  _| ");
        System.out.println("                     \\_/ |_\\___|\\_/\\_/    |_| |_\\__|_\\_\\___|\\__| \n");
        System.out
                .println("------------------------------------------------------------------------------------------");
        System.out.println("                 |  1. View All Success Booking Ticket             |");
        System.out.println("                 |                                                 |");
        System.out.println("                 |  2. View Specific Booking Ticket                |");
        System.out.println("                 |                                                 |");
        System.out.println("                 |  3. Exit View Ticket                            |");
        System.out
                .println("------------------------------------------------------------------------------------------");
        System.out.println("");
    }

    public static void ticketLogo() {
        System.out.println("                           _______ _      _        _   ");
        System.out.println("                          |__   __(_)    | |      | |");
        System.out.println("                             | |   _  ___| | _____| |");
        System.out.println("                             | |  | |/ __| |/ / _ \\ __|");
        System.out.println("                             | |  | | (__|   <  __/ |");
        System.out.println("                             |_|  |_|\\___|_|\\_\\___|\\__|\n");

        System.out.println(
                "--------------------------------------------------------------------------------------------");
        System.out.println("                       |1. View Successful Booking Ticket   |");
        System.out.println("                       |                                    |");
        System.out.println("                       |2. View Ticket Report               |");
        System.out.println("                       |                                    |");
        System.out.println("                       |3. Exit Ticket                      |");
        System.out.println(
                "--------------------------------------------------------------------------------------------");
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

        System.out.println(
                "---------------------------------------------------------------------------------------------");
        System.out.println("                         |1. View Ticket Sales Report                |");
        System.out.println("                         |                                           |");
        System.out.println("                         |2. View Specific Date Ticket Sales Report  |");
        System.out.println("                         |                                           |");
        System.out.println("                         |3. Exit Ticket                             |");
        System.out.println(
                "---------------------------------------------------------------------------------------------");
    }

    public static void specificTicketLogo() {
        System.out.println("");
        System.out
                .println("                __   ___              ___              _  __ _      _____ _    _       _   ");
        System.out.println(
                "               \\ \\ / (_)_____ __ __ / __|_ __  ___ __(_)/ _(_)__  |_   _(_)__| |_____| |_ ");
        System.out.println(
                "                \\ V /| / -_) V  V / \\__ \\ '_ \\/ -_) _| |  _| / _|   | | | / _| / / -_)  _|");
        System.out.println(
                "                 \\_/ |_\\___|\\_/\\_/  |___/ .__/\\___\\__|_|_| |_\\__|   |_| |_\\__|_\\_\\___|\\__| ");
        System.out.println("                              |_|                                                ");

        System.out.println(
                "-----------------------------------------------------------------------------------------------------------------");
        System.out.println(
                "            |1. View by ticket id                                                            |");
        System.out.println(
                "            |                                                                                |");
        System.out.println(
                "            |2. View by member id                                                            |");
        System.out.println(
                "            |                                                                                |");
        System.out.println(
                "            |3. View by flight id                                                            |");
        System.out.println(
                "            |                                                                                |");
        System.out.println(
                "            |4. Exit                                                                         |");
        System.out.println(
                "-----------------------------------------------------------------------------------------------------------------");
        System.out.println("");
    }

    // payment logo
    public static void displayPaymentLogo() {
        System.out.println("\t         _____                                 _     _____       _        _ _       ");
        System.out.println("\t        |  __ \\                               | |   |  __ \\     | |      (_) |     ");
        System.out.println("\t        | |__) |_ _ _   _ _ __ ___   ___ _ __ | |_  | |  | | ___| |_ __ _ _| |___ ");
        System.out.println("\t        |  ___/ _` | | | | '_ ` _ \\ / _ \\ '_ \\| __| | |  | |/ _ \\ __/ _` | | / __|");
        System.out.println("\t        | |  | (_| | |_| | | | | | |  __/ | | | |_  | |__| |  __/ || (_| | | \\__ \\");
        System.out
                .println("\t        |_|   \\__,_|\\__, |_| |_| |_|\\___|_| |_|\\__| |_____/ \\___|\\__\\__,_|_|_|___/");
        System.out.println("\t                    __/ /                                                         ");
        System.out.println("\t                   |___/                                                         \n");
    }

    public static void menyDisplayPayment() {
        System.out.println(
                "---------------------------------------------------------------------------------------------------------------");
        System.out.println("\t         | 1.  Display All Payment Details                                      |");
        System.out.println("\t         |                                                                      |");
        System.out.println("\t         | 2.  Display Payment Details By Payment ID                            |");
        System.out.println("\t         |                                                                      |");
        System.out.println("\t         | 3.  Display Payment Details By Person Incharge ID (Staff ID)         |");
        System.out.println("\t         |                                                                      |");
        System.out.println("\t         | 4.  Display Payment Details By Member ID                             |");
        System.out.println("\t         |                                                                      |");
        System.out.println("\t         | 5.  Exit Payment Details                                             |");
        System.out.println(
                "---------------------------------------------------------------------------------------------------------------");
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

        System.out
                .println("-------------------------------------------------------------------------------------------");
        System.out.println("\t\t     | 1.  Display Payment Details                 |");
        System.out.println("\t\t     |                                             |");
        System.out.println("\t\t     | 2.  Display Payment Report                  |");
        System.out.println("\t\t     |                                             |");
        System.out.println("\t\t     | 3.  Exit Payment                            |");
        System.out
                .println("-------------------------------------------------------------------------------------------");
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
        System.out.println(
                "---------------------------------------------------------------------------------------------------");
        System.out.println("                      | 1.  Display Total Payment Report               |");
        System.out.println("                      |                                                |");
        System.out.println("                      | 2.  Display Total Commission Earned Report     |");
        System.out.println("                      |                                                |");
        System.out.println("                      | 3.  Exit Payment Report                        |");
        System.out.println(
                "---------------------------------------------------------------------------------------------------");
    }

}
