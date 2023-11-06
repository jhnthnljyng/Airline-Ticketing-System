package airlinesystem2;

/**
 *
 * @author JIMMY
 */
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Staff extends Person implements Systematic {

    private String staffId;
    private String username;
    private String staffPassword;
    private String newStaffData;
    private static final String STAFF_FILE = "Staff.txt";

    //constructor 
    public Staff() {
        super();
        this.staffId = " ";
        this.username = " ";
        this.staffPassword = " ";
        this.newStaffData = " ";
    }

    public Staff(String username, String staffPassword, String name, char gender, String dOB, String icNo, String phoneNo, String homeAddress) {
        super(name, gender, dOB, icNo, phoneNo, homeAddress);
        this.username = username;
        this.staffPassword = staffPassword;
    }

    public Staff(String staffId, String username, String staffPassword, String name, char gender,
            String dOB, String icNo, String phoneNo, String homeAddress) {
        super(name, gender, dOB, icNo, phoneNo, homeAddress);
        this.staffId = staffId;
        this.username = username;
        this.staffPassword = staffPassword;
    }

    //getter method
    public String getStaffId() {
        return staffId;
    }

    public String getUsername() {
        return username;
    }

    public String getStaffPassword() {
        return staffPassword;
    }

    //setter method
    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.staffPassword = password;
    }

    /* extra method */
    public void setNewStaffData() {
        newStaffData = staffId + "|" + username + "|" + staffPassword + "|" + super.toString();
    }

    // store register id
    public void storeRegister() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(STAFF_FILE, true));
            writer.write(this.newStaffData);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            System.out.println("Error to write the data into the file");
        }
    }

    // authenticate method
    public boolean authenticateRegLogin(int authenticateType) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(STAFF_FILE));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] details = line.split("\\|");
                String username = details[1];
                String password = details[2];
                String icNo = details[6];
                String hpNo = details[7];

                switch (authenticateType) {
                    case 1 -> {
                        if (username.equals(this.username) && password.equals(this.staffPassword)) {
                            this.staffId = details[0];
                            reader.close(); // to return true when login username and password same as registered
                            // username and password
                            return true;
                        }
                    }
                    case 2 -> {
                        if (username.equals(this.username)) {
                            reader.close();
                            return false; // to return false when new username is same as existing username
                        }
                    }
                    case 3 -> {
                        if (password.equals(this.staffPassword)) {
                            reader.close();
                            return false; // to return false when new username is same as existing username
                        }
                    }
                    case 4 -> {
                        if (icNo.equals(super.getIcNo())) {
                            reader.close();
                            return false; // to return false when entered ic is same as existing is no
                        }
                    }
                    case 5 -> {
                        if (hpNo.equals(super.getPhoneNo())) {
                            reader.close();
                            return false; // to return false when entered hp no is same as existing hp no 
                        }
                    }
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + STAFF_FILE);
        } catch (IOException e) {
            System.out.println("Unable to open the file !\n");
        }
        // authenticateType is comparing data when login
        if (authenticateType == 1) {
            return false;
        }
        // authenticateType is comparing data when register
        return true;
    }

    // auto generate id method
    @Override
    public void autoGenerateID() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(STAFF_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split("\\|");
                String staffID = details[0];
                String prefix = staffID.substring(0, 1); // abstract the S from the string
                int staffNo = Integer.parseInt(staffID.substring(1)); // abstract the integer and increase by one
                staffNo++;
                this.staffId = prefix + String.format("%02d", staffNo); // store inside a string called staff ID
            }
            reader.close();
        } catch (IOException e) {

            System.out.println("Unable to open the file !\n");
        }
    }

    //Method to display object staff info in table
    public void displayStaffInfoInTable() {
        System.out.printf("%-9s %-22s %-10s %-15s %-17s %-15s %-20s%n", getStaffId(), getName(),
                getGender(), getdOB(), getIcNo(), getPhoneNo(), getHomeAddress());
    }

    // Method to read staff information from a text file
    public static List<Staff> readStaffFromFile() throws IOException {
        List<Staff> staffList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("Staff.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");

                String staffId = parts[0];
                String username = parts[1];
                String staffPassword = parts[2];
                String name = parts[3];
                char gender = parts[4].charAt(0);
                String dOB = parts[5];
                String icNo = parts[6];
                String phoneNo = parts[7];
                String homeAddress = parts[8];

                Staff staff = new Staff(staffId, username, staffPassword, name, gender, dOB, icNo,
                        phoneNo, homeAddress);
                staffList.add(staff);

            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return staffList;
    }

    // Method to search for a staff by staff ID
    public static Staff searchStaffById(List<Staff> staffList, String staffId) {
        for (Staff staff : staffList) {
            if (staff.getStaffId().equalsIgnoreCase(staffId)) {
                return staff; // Return the Staff object if found
            }
        }
        return null; // Return null if not found
    }

    // Method to save updated staff information to the file
    public static void saveStaffToFile(List<Staff> staffList) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(STAFF_FILE))) {
            for (Staff staff : staffList) {
                writer.printf("%s|%s|%s|%s|%s|%s|%s|%s|%s%n",
                        staff.getStaffId(), staff.getUsername(), staff.getStaffPassword(), staff.getName(), staff.getGender(),
                        staff.getdOB(), staff.getIcNo(), staff.getPhoneNo(), staff.getHomeAddress());
            }
        }
    }

    // Method to display object staff data
    public void displayParticularStaffData() {
        System.out.printf("\t\t\t Staff ID     : %s\n", staffId);
        System.out.printf("\t\t\t Staff Name   : %s\n", getName());
        System.out.printf("\t\t\t Gender       : %s\n", getGender());
        System.out.printf("\t\t\t DOB          : %s\n", getdOB());
        System.out.printf("\t\t\t IC Number    : %s\n", getIcNo());
        System.out.printf("\t\t\t Phone No.    : %s\n", getPhoneNo());
        System.out.printf("\t\t\t Home Address : %s\n", getHomeAddress());

    }

}
