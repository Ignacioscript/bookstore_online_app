package org.ignacioScript.co.validation;

public class CustomerValidator extends Validator{

    public static void loyaltyPointsValidator(int points, int minPoints, int maxPoints) {
        if (points <= 0) {
            throw new IllegalArgumentException("invalid input points cannot be zero or negative number ");
        }
        if (points > maxPoints) {
            throw new IllegalArgumentException("invalid input maximum of points permitted: " + maxPoints);
        }
        if (points < minPoints) {
            throw new IllegalArgumentException("invalid input minimum of points permitted: " + minPoints);
        }
    }

}
