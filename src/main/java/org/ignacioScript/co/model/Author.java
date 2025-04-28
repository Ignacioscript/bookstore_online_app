package org.ignacioScript.co.model;

import org.ignacioScript.co.util.IdGenerator;
import org.ignacioScript.co.validation.AuthorValidator;

public class Author {
    private int AuthorId;
    private String firstName;
    private String lastName;
    private String bio;
    private String nationality;

    public Author() {
    }


    public Author(String firstName, String lastName, String bio, String nationality) {
        AuthorId = IdGenerator.generateId();
        setFirstName(firstName);
        setLastName(lastName);
        setBio(bio);
        setNationality(nationality);
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
        final StringBuilder sb = new StringBuilder("Author{");
        sb.append("AuthorId=").append(AuthorId);
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", bio='").append(bio).append('\'');
        sb.append(", nationality='").append(nationality).append('\'');
        sb.append('}');
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
