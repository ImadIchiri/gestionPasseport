package org.example.data;

import org.example.citoyen.Citoyen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InitialData {
    public static void main(String[] args) {
        List<Citoyen> citoyenList = new ArrayList<>(Arrays.asList(
                new Citoyen(12345, "Ichiri", "Imad", 21),
                new Citoyen(67891, "Mouad", "Morade", 23),
                new Citoyen(11121, "Ettalbi", "Mohammed", 22),
                new Citoyen(13141, "Ermili", "Mohammed", 20),
                new Citoyen(15161, "Bouchtarat", "Hamza", 24),
                new Citoyen(17181, "Mouhsin", "Hamza", 27),
                new Citoyen(19202, "Samsam", "Bilal", 26),
                new Citoyen(21222, "Rais", "Redouan", 22),
                new Citoyen(23242, "Nasiri", "Aymen", 21),
                new Citoyen(25267, "El Malki", "Tahir", 21)
        ));

        // citoyenList.forEach(citoyen -> CitoyenMetier.createCitoyen(citoyen));
    }
}
