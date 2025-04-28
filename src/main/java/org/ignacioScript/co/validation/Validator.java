package org.ignacioScript.co.validation;

import org.ignacioScript.co.util.NumericUtils;
import org.ignacioScript.co.util.StringUtils;


import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class Validator  {


    public static void validateId(int id) {
        if (id <= 0 ) {
            throw new IllegalArgumentException("Invalid id, must be a positive number");
        }
        if (id > 10000) {
            throw new IllegalArgumentException("Id has reached max capacity");
        }
    }

    public static void validateProperNoun(String name) {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Input cannot be empty");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("Input should be less than 100 characters");
        }
        if (name.length() < 2) {
            throw new IllegalArgumentException("Input is to short at least 2 or more characters");
        }
        if (!name.matches("^[A-Za-z\\s,'-]+$")) {
            throw new IllegalArgumentException("Input contains invalid characters");
        }

        StringUtils.capitalizeName(name);

    }

    public static void validateMail(String mail) {
        if (mail == null || mail.trim().isEmpty()) {
            throw new IllegalArgumentException("Input cannot be empty");
        }
        if (mail.length() > 50) {
            throw new IllegalArgumentException("mail should be shorter");
        }
        if (mail.length() < 2) {
            throw new IllegalArgumentException("mail is too short");
        }
        if (!mail.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        StringUtils.toLowerCase(mail);
    }

    public static void validateAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Input cannot be empty");
        } if (address.length() > 100) {
            throw new IllegalArgumentException("Input too long max 500 characters" );
        }
        if (address.length() < 5) {
            throw new IllegalArgumentException("Input too short min 10 characters");
        }

        if (!address.matches("^[A-Za-z0-9\\s.,'-]+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        StringUtils.toLowerCase(address);
    }


    public static void validateOnlyNumbers(String number, int maxNumber, int minNumber) {
        if (number == null || number.trim().isEmpty()) {
            throw new IllegalArgumentException("Input cannot be empty");
        }
        if ((number.length() > maxNumber) || (number.length() < minNumber)) {
            throw new IllegalArgumentException("Input should be between: " + minNumber +  " and " + maxNumber + " characters ");
        }

        if (!number.matches("^[0-9\\s'-]+$")) {
            throw new IllegalArgumentException("Only numbers are valid characters");
        }

    }

    public static void validateDescription(String description, int maxSize, int minSize) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Input cannot be empty");
        }
        if (description != null && description.length() > maxSize) {
            throw new IllegalArgumentException("Description is too long. Max " + maxSize + " characters");
        }
        if (description != null && description.length() < minSize) {
            throw new IllegalArgumentException("Description is too short. Min " + minSize + " characters");
        }
        if (description != null && description.matches(".*(offensive|prohibited).*")) {
            throw new IllegalArgumentException("Description contains prohibited content");
        }
    }


    public static void validateDate(LocalDate date) {

        if (date == null) {
            throw new DateTimeException("Input cannot be empty");
        }


        if (date.isBefore(LocalDate.now())) {
            throw new DateTimeException("Input cannot be in the past");
        }

        if (date.isAfter(LocalDate.now())) {
            throw new DateTimeException("Input cannot be in the future");
        }

    }


    public static void validateUnits(double unit, double minUnit, double maxUnit) {
        if (unit <= 0.0) {
            throw new IllegalArgumentException("Unit Price cannot be 0 or negative number");
        }
        if (unit < minUnit ) {
            throw new IllegalArgumentException("The minimum unit price is " + minUnit);
        }
        if (unit > maxUnit) {
            throw new IllegalArgumentException("The maximum unit price is " + maxUnit);
        }
        NumericUtils.roundDouble(unit, 2);
    }


    public static  void validateUnits(int unit, int minUnit, int maxUnit ) {
        if (unit <= 0) {
            throw new IllegalArgumentException("Unit cannot be 0 or negative number");
        }
        if (unit < minUnit ) {
            throw new IllegalArgumentException("The minimum unit is " + minUnit);
        }
        if (unit > maxUnit) {
            throw new IllegalArgumentException("The maximum unit  is " + maxUnit);
        }
    }



}
