package org.example;

import org.example.citoyen.Citoyen;
import org.example.citoyen.CitoyenDAO;
import org.example.helpers.DatesAndStrings;
import org.example.helpers.FormattedDateMatcher;
import org.example.passeport.Passeport;
import org.example.passeport.PasseportDAO;

import java.util.*;

public class Main {
    public static void gestionDePasseport() {
        Scanner scanner = new Scanner(System.in);
        int userOperation = 0;
        boolean startAgain = true;
        boolean bError = false;


        while (startAgain){
            do {
                System.out.println("Bienvenu sur le menu de gestion des passeports !");
                System.out.println("Veuillez choisir une des opertions suivantes:");

                System.out.println("1- Créer un passeport");
                System.out.println("2- Recherche d’un passeport par son numéro");
                System.out.println("3- Afficher tous les passeports");
                System.out.println("4- Modifier un passeport");
                System.out.println("5- Supprimer un passeport");
                System.out.println("6- Afficher les passeports expirés");

                try {
                    System.out.print("\n Votre choix est: ");
                    userOperation = scanner.nextInt();
                    scanner.nextLine();
                    bError = false;

                } catch (InputMismatchException exception) {
                    bError = true;
                    System.out.println("Il faut saisir un entier !");
                }
            } while (bError);

                if (userOperation < 1 && userOperation > 6) {
                    System.out.println("Votre choix doit etre entre 1 et 6");
                }
            } while (userOperation < 1 && userOperation > 6);

            switch (userOperation) {
                case 1:
                    long numeroPasseport = 0L;
                    String dateExpiration = DatesAndStrings.fromatDate(new Date());
                    bError = false;

                    System.out.println("##### Création d'un passeport #####");

                    do {
                        try {
                            System.out.print("Veuillez saisir le numéro de passeport: ");
                            numeroPasseport = scanner.nextLong();
                            bError = false;
                        } catch (Exception exception) {
                            bError = true;
                            System.out.println("Le numéro doit etre un entier !");
                        }

                        scanner.nextLine();
                    } while (bError);

                    do {
                        try {
                            System.out.print("Veuillez saisir la date d'éxpiration (JJ/MM/AAAA): ");
                            dateExpiration = scanner.nextLine();
                            bError = false;
                        } catch (Exception exception) {
                            System.out.println("La date doit etre sous la forme (JJ/MM/AAAA) !");
                        }

                        scanner.nextLine();
                    } while (!FormattedDateMatcher.matches(dateExpiration) && !bError);

                    PasseportDAO.createPassePort(new Passeport(numeroPasseport, dateExpiration));
                    break;
                case 2:
                    long numeroPasseportToEdit;
                    System.out.println("##### Recherche d’un passeport par son numéro #####");

                    try {
                        System.out.println("Veuillez siasir un numero de passeport: ");
                        numeroPasseportToEdit = scanner.nextLong();
                        scanner.nextLine();

                        Optional<Passeport> passeportByNumber = PasseportDAO.getPasseportByNumber(numeroPasseportToEdit);
                        if (passeportByNumber.isPresent()) {
                            System.out.println(passeportByNumber.get());
                        } else {
                            System.out.println("Pas de passeport avec ce numero !");
                        }
                    } catch(InputMismatchException exception) {
                        // exception.printStackTrace();
                        System.out.println("Le numero de passeport doit etre un entier !");
                    }

                    break;
                case 3:
                    System.out.println("##### La liste de tous les Passeports #####");
                    List<Passeport> passeportList = PasseportDAO.getAllPasseport();

                    if (passeportList.size() > 0) {
                        passeportList.forEach(passeport -> System.out.println(passeport));
                    } else {
                        System.out.println("Il n'y a aucun Passeport dans pour le moment !");
                    }
                    break;
                case 4:
                    System.out.println("##### Modifier un Passeport #####");

                    System.out.print("Veuillez saisir le numero de Passeport a modifier: ");
                    long passeportNumberToEdit = scanner.nextLong();
                    scanner.nextLine();

                    Optional<Passeport> optionalPasseportToEdit = PasseportDAO.getPasseportByNumber(passeportNumberToEdit);

                    if (optionalPasseportToEdit.isEmpty()) {
                        System.out.println("Pas de Passeport Avec Le numéro '" + passeportNumberToEdit + "' !");
                    } else {
                        Passeport passeportToEdit = optionalPasseportToEdit.get();
                        String newDateExpiration = DatesAndStrings.fromatDate(new Date());

                        do {
                            try {
                                System.out.println("Veuillez saisir la nouvelle date d'éxpiration (JJ/MM/AAAA): ");
                                System.out.print("(" + passeportToEdit.getDateExpiration() + ") -> ?: ");
                                newDateExpiration = scanner.nextLine();

                                passeportToEdit.setDateExpiration(newDateExpiration);

                            } catch (Exception exception) {
                                System.out.println("La date doit etre sous la forme (JJ/MM/AAAA) !");
                            }
                        } while (!FormattedDateMatcher.matches(newDateExpiration));

                        PasseportDAO.editPasseport(passeportToEdit);
                    }

                    break;
                case 5:
                    System.out.println("##### La Suppression D'un Passeport #####");
                    long numeroPasseportToDelete;

                    try {
                        System.out.print("Veuillez saisir le numéro du passeport a supprimer: ");
                        numeroPasseportToDelete = scanner.nextLong();
                        scanner.nextLine();

                        int deleteCount = PasseportDAO.deletePasseportByNumber(numeroPasseportToDelete);

                        if (deleteCount == 0) {
                            System.out.println("Pas de passeport avec ce numéro !");
                        } else {
                            System.out.println("Passeport Supprimer !");
                        }

                    } catch (InputMismatchException exception) {
                        System.out.println("Le numéro doit etre un entier !");
                    }
                    break;
                case 6:
                    System.out.println("##### Liste des Passeports Expirés #####");
                    List<Passeport> expiredPasseports = PasseportDAO.getExpiedPasseports();

                    if (expiredPasseports.size() == 0) {
                        System.out.println("Pas de passeport dans cette liste !");
                    } else {
                        expiredPasseports.forEach(passeport -> System.out.println(passeport));
                    }
                    break;
                default:
                    System.out.println("Invalide Choice !");
            }

            System.out.print("\nVous voulez refaire le traitement (y/n) ?: ");
            String startAgainString = scanner.nextLine();

            startAgain = startAgainString.equalsIgnoreCase("y"); // True || False

        }

    public static void gestionDeCitoyens() {
            Scanner scanner = new Scanner(System.in);
            int userOperation = 0;
            boolean startAgain = true;
            boolean bError = false;


            while (startAgain){
                do {
                    System.out.println("Bienvenu sur le menu de gestion des Citoyens !");
                    System.out.println("Veuillez choisir une des opertions suivantes:");

                    System.out.println("1- Créer un Citoyen");
                    System.out.println("2- Recherche d’un Citoyen par son numéro");
                    System.out.println("3- Afficher tous les Citoyens");
                    System.out.println("4- Modifier un Citoyen");
                    System.out.println("5- Supprimer un Citoyen");
                    System.out.println("6- Attribuer un Passeport á un Citoyen");
                    System.out.println("7- Rechercher un Citoyen pas numéro de Passeport");
                    System.out.println("8- Rechercher un Passeport par numéro du Citoyen");

                    do {
                        try {
                            System.out.print("\n Votre choix est: ");
                            userOperation = scanner.nextInt();
                            scanner.nextLine();
                            bError = false;

                        } catch (InputMismatchException exception) {
                            bError = true;
                            System.out.println("Il faut saisir un entier !");
                        }
                    } while (bError);

                    if (userOperation < 1 && userOperation > 8) {
                        System.out.println("Votre choix doit etre entre 1 et 8");
                    }
                } while (userOperation < 1 && userOperation > 8);

                switch (userOperation) {
                    case 1:
                        long numeroCitoyen = 0L;
                        String nomCitoyen = "", prenomCitoyen = "";
                        int ageCitoyen = 0;

                        bError = false;

                        System.out.println("##### Création d'un Citoyen #####");

                        do {
                            try {
                                System.out.print("Numéro de Citoyen: ");
                                numeroCitoyen = scanner.nextLong();
                                bError = false;
                            } catch (InputMismatchException exception) {
                                bError = true;
                                System.out.println("Le numéro doit etre un entier !");
                            }

                            scanner.nextLine();
                        } while (bError);

                        do {
                            try {
                                System.out.print("Nom: ");
                                nomCitoyen = scanner.nextLine();
                                bError = false;
                            } catch (InputMismatchException exception) {
                                bError = true;
                                System.out.println("Le Nom doit etre une chaine des caractères !");
                            }

                        } while (bError);

                        do {
                            try {
                                System.out.print("Prenom: ");
                                prenomCitoyen = scanner.nextLine();
                                bError = false;
                            } catch (InputMismatchException exception) {
                                bError = true;
                                System.out.println("Le Prenom doit etre une chaine des caractères !");
                            }

                        } while (bError);

                        do {
                            try {
                                System.out.print("Age: ");
                                ageCitoyen = scanner.nextInt();
                                scanner.nextLine();
                                bError = false;
                            } catch (InputMismatchException exception) {
                                bError = true;
                                System.out.println("L'Age doit etre un entier !");
                            }

                        } while (bError);

                        CitoyenDAO.createCitoyen(new Citoyen(numeroCitoyen, nomCitoyen, prenomCitoyen, ageCitoyen));
                        break;
                    case 2:
                        long numeroCitoyenToLookFor;
                        System.out.println("##### Recherche d’un Citoyen par son numéro #####");

                        try {
                            System.out.print("Veuillez siasir le numero de Citoyen: ");
                            numeroCitoyenToLookFor = scanner.nextLong();
                            scanner.nextLine();

                            Optional<Citoyen> citoyenByNumber = CitoyenDAO.getCitoyenByNumber(numeroCitoyenToLookFor);
                            if (citoyenByNumber.isPresent()) {
                                System.out.println(citoyenByNumber.get());
                            } else {
                                System.out.println("Pas de Citoyen avec ce numéro !");
                            }
                        } catch(InputMismatchException exception) {
                            // exception.printStackTrace();
                            System.out.println("Le numero de passeport doit etre un entier !");
                        }

                        break;
                    case 3:
                        System.out.println("##### La liste de tous les Citoyens #####");
                        List<Citoyen> citoyenList = CitoyenDAO.getAllCitoyens();

                        if (citoyenList.size() > 0) {
                            citoyenList.forEach(citoyen -> System.out.println(citoyen));
                        } else {
                            System.out.println("Il n'y a aucun Citoyen pour le moment !");
                        }
                        break;
                    case 4:
                        System.out.println("##### Modifier un Citoyen #####");

                        System.out.print("Veuillez saisir le numéro de Citoyen a modifier: ");
                        long citoyenNumberToEdit = scanner.nextLong();
                        scanner.nextLine();

                        Optional<Citoyen> optionalCitoyenToEdit = CitoyenDAO.getCitoyenByNumber(citoyenNumberToEdit);

                        if (optionalCitoyenToEdit.isEmpty()) {
                            System.out.println("Pas de Citoyen Avec Le numéro '" + citoyenNumberToEdit + "' !");
                        } else {
                            Citoyen citoyenToEdit = optionalCitoyenToEdit.get();
                            String editedNomCitoyen = "", editedPrenomCitoyen = "";
                            int editedAgeCitoyen = 0;

                            try {
                                System.out.println("Veuillez saisir le nouveau Nom : ");
                                System.out.print("(" + citoyenToEdit.getNomCitoyen() + ") -> ?: ");
                                editedNomCitoyen = scanner.nextLine();

                                citoyenToEdit.setNomCitoyen(editedNomCitoyen);

                            } catch (Exception exception) {
                                System.out.println("Le Nom doit etre une chaine des caractères !");
                            }

                            try {
                                System.out.println("Veuillez saisir le nouveau Prenom : ");
                                System.out.print("(" + citoyenToEdit.getPrenomCitoyen() + ") -> ?: ");
                                editedPrenomCitoyen = scanner.nextLine();

                                citoyenToEdit.setPrenomCitoyen(editedPrenomCitoyen);

                            } catch (Exception exception) {
                                System.out.println("Le Prenom doit etre une chaine des caractères !");
                            }

                            try {
                                System.out.println("Veuillez saisir le nouveau Age : ");
                                System.out.print("(" + citoyenToEdit.getAgeCitoyen() + ") -> ?: ");
                                editedAgeCitoyen = scanner.nextInt();
                                scanner.nextLine();

                                citoyenToEdit.setAgeCitoyen(editedAgeCitoyen);

                            } catch (Exception exception) {
                                System.out.println("L'Age doit Etre un Entier' !");
                            }

                            CitoyenDAO.editCitoyen(citoyenToEdit);
                        }

                        break;
                    case 5:
                        System.out.println("##### La Suppression D'un Passeport #####");
                        long numeroCitoyenToDelete;

                        try {
                            System.out.print("Veuillez saisir le numéro du Citoyen a supprimer: ");
                            numeroCitoyenToDelete = scanner.nextLong();
                            scanner.nextLine();

                            int deleteCount = CitoyenDAO.deleteCitoyen(numeroCitoyenToDelete);

                            if (deleteCount == 0) {
                                System.out.println("Pas de Citoyen avec ce numéro !");
                            } else {
                                System.out.println("Citoyen Supprimer !");
                            }

                        } catch (InputMismatchException exception) {
                            System.out.println("Le numéro doit etre un entier !");
                        }
                        break;
                    case 6:
                        System.out.println("##### Attribuer un Passeport á un Citoyen #####");
                        long numeroPasseportAttribuer = 0L, numeroCitoyenAttribuer = 0L;
                        bError = false;

                    do {
                        try {
                            System.out.print("Veuillez saisir le numéro du Citoyen: ");
                            numeroCitoyenAttribuer = scanner.nextLong();
                            scanner.nextLine();
                            bError = false;

                        } catch (InputMismatchException exception) {
                            bError = true;
                            System.out.println("Le numéro doit etre un entier !");
                        }
                    } while (bError);

                   List<Passeport> passeportList = PasseportDAO.getAllPasseport();

                   if (passeportList.size() > 0) {
                       System.out.println("La liste des Passeport: ");

                       passeportList.forEach(passeport -> System.out.println(passeport));
                   }

                    do {
                        try {
                            System.out.print("Veuillez saisir le numéro du Passeport: ");
                            numeroPasseportAttribuer = scanner.nextLong();
                            scanner.nextLine();
                            bError = false;

                        } catch (InputMismatchException exception) {
                            bError = true;
                            System.out.println("Le numéro doit etre un entier !");
                        }
                    } while (bError);

                    CitoyenDAO.assignPasseportToCitoyen(numeroCitoyenAttribuer, numeroPasseportAttribuer);

                    break;
                    case 7:
                        System.out.println("##### Rechercher un Citoyen pas numéro de Passeport #####");
                        long numeroPasseportPouRechercher = 0L;
                        try {
                            System.out.println("Veuillez siasir un numero de Passeport: ");
                            numeroPasseportPouRechercher = scanner.nextLong();
                            scanner.nextLine();

                            Optional<Citoyen> citoyenByNumberPasseport = CitoyenDAO.getCitoyenByNumeroPasseport(numeroPasseportPouRechercher);
                            if (citoyenByNumberPasseport.isPresent()) {
                                System.out.println(citoyenByNumberPasseport.get());
                            } else {
                                System.out.println("Pas de Citoyen avec ce numéro de Passeport !");
                            }
                        } catch(InputMismatchException exception) {
                            // exception.printStackTrace();
                            System.out.println("Le numero de Passeport doit etre un entier !");
                        }
                        break;
                    case 8:
                        System.out.println("##### Rechercher un Passeport pas numéro de Citoyen #####");
                        long numeroCitoyenPouRechercher = 0L;

                        try {
                            System.out.println("Veuillez siasir un numéro de Citoyen: ");
                            numeroCitoyenPouRechercher = scanner.nextLong();
                            scanner.nextLine();

                            Optional<Passeport> passeportByNumberCitoyen = CitoyenDAO.getPasseportByNumeroCitoyen(numeroCitoyenPouRechercher);
                            if (passeportByNumberCitoyen.isPresent()) {
                                System.out.println(passeportByNumberCitoyen.get());
                            } else {
                                System.out.println("Pas de Passeport avec ce numéro de Citoyen !");
                            }
                        } catch(InputMismatchException exception) {
                            // exception.printStackTrace();
                            System.out.println("Le numero de Citoyen doit etre un entier !");
                        }
                    default:
                        System.out.println("Invalide Choice !");
                }

                System.out.print("\nVous voulez refaire le traitement (y/n) ?: ");
                String startAgainString = scanner.nextLine();

                startAgain = startAgainString.equalsIgnoreCase("y"); // True || False

            }
    }

    public static void main(String[] args) {
        int userInput;
        Scanner scanner = new Scanner(System.in);
        String backToMenu = "y";

        do {
            System.out.println("\n1- La gestion des passeports");
            System.out.println("2- la gestion des citoyens");

            do {
                System.out.print("\nLe nombre de votre choix est -> ");
                userInput = scanner.nextInt();

                if (userInput != 1 && userInput != 2) {
                    System.out.println("Les deux choix possibles sont: 1 et 2 !");
                }
            } while (userInput != 1 && userInput != 2);

            switch (userInput) {
                case 1:
                    gestionDePasseport();
                    break;
                case 2:
                    gestionDeCitoyens();
                    break;
                default:
                    System.out.println("INVALIDE CHOICE !");
            }

            scanner.nextLine();
            System.out.print("Retour au menu principale (y/n)?: ");
            backToMenu = scanner.nextLine();

            if (!backToMenu.equalsIgnoreCase("Y")) {
                System.out.println("Happy To See You Again ^_^");
            }

        } while (backToMenu.equalsIgnoreCase("Y"));

    }
}