package org.ignacioScript.co.model;

import org.ignacioScript.co.util.IdGenerator;
import org.ignacioScript.co.validation.CustomerValidator;

import java.time.LocalDate;

public class Customer {

    private int customerId;
    private String firstName;
    private String lastName;
    private String mail;
    private String phoneNumber;
    private String address;
    private LocalDate registrationDate;
    private int loyaltyPoints;

    public Customer(String firstName, String lastName, String mail, String phoneNumber, String address, LocalDate registrationDate, int loyaltyPoints) {
        this.customerId = getCustomerId();
        setFirstName(firstName);
        setLastName(lastName);
        setMail(mail);
        setPhoneNumber(phoneNumber);
        setAddress(address);
        setRegistrationDate(registrationDate);
        setLoyaltyPoints(loyaltyPoints);
    }

    // Default constructor for deserialization


    public Customer(int customerId, String firstName, String lastName, String mail, String phoneNumber, String address, LocalDate registrationDate, int loyaltyPoints) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.registrationDate = registrationDate;
        this.loyaltyPoints = loyaltyPoints;
    }

    public Customer() {
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    //Getters
    public int getCustomerId() {
        return customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMail() {
        return mail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    //Setters


    public void setFirstName(String firstName) {
        CustomerValidator.validateProperNoun(firstName);
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        CustomerValidator.validateProperNoun(lastName);
        this.lastName = lastName;
    }

    public void setMail(String mail) {
        CustomerValidator.validateMail(mail);
        this.mail = mail;
    }

    public void setPhoneNumber(String phoneNumber) {
        CustomerValidator.validateOnlyNumbers(phoneNumber, 20, 5);
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        CustomerValidator.validateAddress(address);
        this.address = address;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        CustomerValidator.validateDate(registrationDate);
        this.registrationDate = registrationDate;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        CustomerValidator.loyaltyPointsValidator(loyaltyPoints, 10, 100);
        this.loyaltyPoints = loyaltyPoints;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Customer{");
        sb.append("customerId=").append(customerId);
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", mail='").append(mail).append('\'');
        sb.append(", phoneNumber='").append(phoneNumber).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append(", registrationDate=").append(registrationDate);
        sb.append(", loyaltyPoints=").append(loyaltyPoints);
        sb.append('}');
        return sb.toString();
    }

    public String toCsvString() {
        return String.join(",",
                String.valueOf(this.getCustomerId()),
                this.getFirstName(),
                this.getLastName(),
                this.getMail(),
                this.getPhoneNumber(),
                this.getAddress(),
                String.valueOf(this.getRegistrationDate()),
                String.valueOf(this.getLoyaltyPoints()));

    }
}
