package airlinesystem2;

import java.io.*;
import java.util.*;

public class Member extends Person implements Systematic {

    private String memberId;
    private double balance;
    private int accountNo;
    private static final String MEMBER_FILE = "Member.txt";
    private static ArrayList<String[]> memberData = new ArrayList<>();

    public Member() {
        super();
        this.memberId = " ";
        this.balance = 0;
    }

    public Member(String memberId, String name, String phoneNo, String dOB, double balance) {
        super(name, phoneNo, dOB);
        this.memberId = memberId;
        this.balance = balance;
    }

    public String getMemberId() {
        return memberId;
    }

    public double getBalance() {
        return balance;
    }

    public int getAccountNo() {
        return accountNo;
    }

    public static ArrayList<String[]> getMemberData() {
        return memberData;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setAccountNo(int accountNo) {
        this.accountNo = accountNo;
    }

    public static String getMEMBER_FILE() {
        return MEMBER_FILE;
    }

    public void rechargeAmount(double amount) {
        if (amount > 0) {
            this.balance += amount;
            System.out.println("Account recharge successfully!\nNew balance: " + balance);
        } else {
            System.out.println("Invalid recharge amount. Please enter a positive amount");
        }
    }

    //save member data into the file 
    public void saveMemberDataToFile() {
        autoGenerateID();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MEMBER_FILE, true))) {
            writer.write(memberId + "|" + super.getName() + "|" + super.getGender() + "|" + super.getdOB() + "|" + super.getIcNo() + "|"
                    + super.getPhoneNo() + "|" + super.getHomeAddress() + "|" + this.getAccountNo() + "|" + this.getBalance());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to write the data into file!\n");
        }
    }

    //write new modify member data into the file 
    public static void writeNewMemberDataFile(ArrayList<String[]> memberData) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MEMBER_FILE, false))) {
            for (String[] memberRecord : memberData) {
                String memberFormatted = String.join("|", memberRecord);
                writer.write(memberFormatted);
                writer.newLine(); // Add a newline character after each line
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to write the data into file!\n");
        }
    }

    public static void readMemberData() {
        //clear array list 
        memberData.clear();
        try {
            try (BufferedReader reader = new BufferedReader(new FileReader(MEMBER_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] details = line.split("\\|");
                    memberData.add(details);                                                        // store details into member data array
                }
                reader.close();
            }
        } catch (IOException ex) {
            System.out.println("Unable to open the file!");
        }
    }

    @Override
    public void autoGenerateID() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(MEMBER_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] memberDetails = line.split("\\|");
                String memberID = memberDetails[0];
                String prefix = memberID.substring(0, 1);       // abstract the M from the string
                int memberNo = Integer.parseInt(memberID.substring(1)); // abstract the integer and increase by one
                memberNo++;
                this.memberId = prefix + String.format("%02d", memberNo); // store inside a string called member Id
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Unable to open the file !\n");
        }
    }

    public boolean authenticateRegLogin(int authenticateType) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(MEMBER_FILE));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] details = line.split("\\|");
                String icNo = details[4];
                String hpNo = details[5];
                String accountNo = details[7];
                switch (authenticateType) {
                    case 1 -> {
                        if (icNo.equals(super.getIcNo())) {
                            reader.close();
                            return false; // to return false when entered ic is same as existing is no
                        }
                    }
                    case 2 -> {
                        if (hpNo.equals(super.getPhoneNo())) {
                            reader.close();
                            return false; // to return false when entered hp no is same as existing hp no 
                        }
                    }
                    case 3 -> {
                        if (accountNo.equals(Integer.toString(getAccountNo()))) {
                            reader.close();
                            return false; // to return false when entered hp no is same as existing hp no 
                        }
                    }
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + MEMBER_FILE);
        } catch (IOException e) {
            System.out.println("Unable to open the file !\n");
        }
        // authenticateType is comparing data when register
        return true;
    }
}
