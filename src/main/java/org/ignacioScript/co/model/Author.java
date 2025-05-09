package org.ignacioScript.co.model;

import org.ignacioScript.co.util.IdGenerator;
import org.ignacioScript.co.validation.AuthorValidator;

public class Author {
    private int AuthorId;
    private String firstName;
    private String lastName;
    private String bio;
    private String nationality;

    //TODO think to implement List of books

    public Author() {
    }


    public Author(String firstName, String lastName, String bio, String nationality) {
        AuthorId = getAuthorId();
        setFirstName(firstName);
        setLastName(lastName);
        setBio(bio);
        setNationality(nationality);
    }

    public Author(int authorId, String firstName, String lastName, String bio, String nationality) {
        AuthorId = authorId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bio = bio;
        this.nationality = nationality;
    }

    public void setAuthorId(int authorId) {
        AuthorId = authorId;
    }

    public int getAuthorId() {
        return AuthorId;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        AuthorValidator.validateProperNoun(firstName);
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        AuthorValidator.validateProperNoun(lastName);
        this.lastName = lastName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        AuthorValidator.validateDescription(bio, 300, 20);
        this.bio = bio;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        AuthorValidator.validateProperNoun(nationality);
        this.nationality = nationality;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Author details: ");
        sb.append("Id: ").append(AuthorId);
        sb.append("\t, First name: '").append(firstName).append('\'');
        sb.append("\t\t, Last name: '").append(lastName).append('\'');
        sb.append("\t\t, bio: '").append(bio).append('\'');
        sb.append("\t\t, nationality: '").append(nationality).append('\'');
        sb.append('*');
        return sb.toString();
    }

    public String toCsvString() {
        return String.join(",",
                String.valueOf(this.getAuthorId()),
                this.getFirstName(),
                this.getLastName(),
                this.getBio(),
                this.getNationality()
        );
    }
}
