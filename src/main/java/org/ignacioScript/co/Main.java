package org.ignacioScript.co;


import org.ignacioScript.co.controller.MenuController;
import org.ignacioScript.co.util.ASCIIArtGenerator;

public class Main {
    public static void main(String[] args) {

        ASCIIArtGenerator artGenerator = new ASCIIArtGenerator();
        try {
            artGenerator.printTextArt("Library", ASCIIArtGenerator.ART_SIZE_SMALL, ASCIIArtGenerator.ASCIIArtFont.ART_FONT_SANS_SERIF, "*");
        } catch (Exception e) {
            e.printStackTrace();
        }


        MenuController.runMenu();
    }

    //TODO create logic to set payments book price per quantity to put it in the order


}

