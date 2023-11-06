/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airlinesystem2;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Chang Zhi Cong user
 */
public abstract class Person{
    private String name;
    private char gender;
    private String dOB; /*date of Birth*/
    private String icNo;
    private String phoneNo;
    private String homeAddress;

    //no argu constructor 
    public  Person() {
        this("", ' ',"","","", "");
    }
    
    //argu constructor
    public Person(String name, char gender, String dOB, String icNo, String phoneNo, String homeAddress) {
        this.name = name;
        this.gender = gender;
        this.dOB = dOB;
        this.icNo = icNo;
        this.phoneNo = phoneNo;
        this.homeAddress = homeAddress;
    }

    public Person(String name, String phoneNo, String dOB){
        this.name = name;
        this.phoneNo = phoneNo;
        this.dOB = dOB;
    }

    public Person(String icNo){
        this.icNo = icNo;
    }

    /*Getter method*/
    public String getName() {
        return name;
    }

    public char getGender() {
        return gender;
    }
    
    public String getdOB() {
        return dOB;
    }

    public String getIcNo() {
        return icNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    /*Setter method*/
    public void setName(String name) {
        this.name = name;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public void setdOB(String dOB) {
        this.dOB = dOB;
    }

    public void setIcNo(String icNo) {
        this.icNo = icNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    @Override
    public String toString(){
         return name  + "|" + gender + "|" + dOB + "|" +  icNo + "|" +  phoneNo + "|" +  homeAddress;
    }
}
