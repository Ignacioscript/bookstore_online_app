package org.ignacioScript.co.model;

import org.ignacioScript.co.util.IdGenerator;
import org.ignacioScript.co.validation.GenreValidator;

public class Genre {
    private int genreId;
    private String genreName;
    private String genreDescription;

    public Genre(String genreName, String genreDescription) {
        this.genreId = IdGenerator.generateId();
        setGenreName(genreName);
        setGenreDescription(genreDescription);
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public int getGenreId() {
        return genreId;
    }

    public String getGenreName() {
        return genreName;
    }

    public String getGenreDescription() {
        return genreDescription;
    }



    public void setGenreName(String genreName) {
        GenreValidator.validateProperNoun(genreName);
        this.genreName = genreName;
    }

    public void setGenreDescription(String genreDescription) {
        GenreValidator.validateDescription(genreDescription, 200, 50);
        this.genreDescription = genreDescription;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Genre{");
        sb.append("genreId=").append(genreId);
        sb.append(", genreName='").append(genreName).append('\'');
        sb.append(", genreDescription='").append(genreDescription).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String toCsvString() {
        return String.join(",",
                String.valueOf(this.getGenreId()),
                this.getGenreName(),
                this.getGenreDescription()
                );
    }
}
