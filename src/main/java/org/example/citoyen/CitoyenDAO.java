package org.example.citoyen;

import org.example.database.DatabaseConnection;
import org.example.passeport.Passeport;
import org.example.passeport.PasseportDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CitoyenDAO {
    public static void createCitoyen(Citoyen citoyen) {
        Optional<Citoyen> citoyenByNumber = getCitoyenByNumber(citoyen.getNumeroCitoyen());

        try (Connection connection = DatabaseConnection.connectToDB();) {
            if (citoyenByNumber.isPresent()) {
                System.out.println("\nCe numéro de Citoyen exist déja !");
            }

            if (citoyenByNumber.isEmpty()){
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO citoyen (numeroCitoyen, nomCitoyen, prenomCitoyen, ageCitoyen) VALUES (?, ?, ?, ?)"
                );
                preparedStatement.setLong(1, citoyen.getNumeroCitoyen());
                preparedStatement.setString(2, citoyen.getNomCitoyen());
                preparedStatement.setString(3, citoyen.getPrenomCitoyen());
                preparedStatement.setInt(4, citoyen.getAgeCitoyen());

                preparedStatement.executeUpdate();
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static Optional<Citoyen> getCitoyenByNumber(long numeroCitoyen) {
        Citoyen selectedCitoyen = null;

        try (Connection connection = DatabaseConnection.connectToDB();) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM citoyen WHERE numeroCitoyen = ?");
            preparedStatement.setLong(1, numeroCitoyen);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nomCitoyen = resultSet.getString("nomCitoyen");
                String prenomCitoyen = resultSet.getString("prenomCitoyen");
                int ageCitoyen = resultSet.getInt("ageCitoyen");
                int idPasseport = resultSet.getInt("idPasseport");

                selectedCitoyen = new Citoyen(id, numeroCitoyen, nomCitoyen, prenomCitoyen, ageCitoyen, idPasseport);
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        catch (IndexOutOfBoundsException exception) {
            exception.printStackTrace();
        }

        return Optional.ofNullable(selectedCitoyen);
    }

    public static List<Citoyen> getAllCitoyens() {
        List<Citoyen> citoyenList = new ArrayList<>();

        try (Connection connection = DatabaseConnection.connectToDB();) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM citoyen;");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                long numeroCitoyen = resultSet.getLong("numeroCitoyen");
                String nomCitoyen = resultSet.getString("nomCitoyen");
                String prenomCitoyen = resultSet.getString("prenomCitoyen");
                int ageCitoyen = resultSet.getInt("ageCitoyen");
                int idPasseport = resultSet.getInt("idPasseport");

                citoyenList.add(new Citoyen(id, numeroCitoyen, nomCitoyen, prenomCitoyen, ageCitoyen, idPasseport));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return citoyenList;
    }

    public static void editCitoyen(Citoyen citoyen) {
        try (Connection connection = DatabaseConnection.connectToDB();) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE citoyen SET nomCitoyen = ?, prenomCitoyen = ?, ageCitoyen = ?, idPasseport = ? WHERE id = ?"
            );

            preparedStatement.setString(1, citoyen.getNomCitoyen());
            preparedStatement.setString(2, citoyen.getPrenomCitoyen());
            preparedStatement.setInt(3, citoyen.getAgeCitoyen());
            preparedStatement.setInt(4, citoyen.getIdPasseport());
            preparedStatement.setInt(5, citoyen.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static int deleteCitoyen (long numeroCitoyen) {
        int deleteCount = 0;
        // Passeport Should Be Deleted First /!\
        // We Look For The 'Citoyen' In Order To Get (His/Here) 'id'
        Optional<Citoyen> optionalCitoyenToDelete = getCitoyenByNumber(numeroCitoyen);

        if (optionalCitoyenToDelete.isEmpty()) {
            System.out.println("Pas de Citoyen Avec Ce Numero !");
        } else {
            Citoyen citoyenToDelete = optionalCitoyenToDelete.get();
            PasseportDAO.deletePasseportById(citoyenToDelete.getIdPasseport());

            // We Don't Need To Check If The (id == Null)

            try (Connection connection = DatabaseConnection.connectToDB();) {
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM citoyen where numeroCitoyen = ?");
                preparedStatement.setLong(1, numeroCitoyen);

                deleteCount = preparedStatement.executeUpdate();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }

        }

        return deleteCount;
    }

    public static Optional<Citoyen> getCitoyenByNumeroPasseport(long numeroPasseport) {
        Citoyen citoyenByNumeroPasseport = null;

        try (Connection connection = DatabaseConnection.connectToDB();) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM citoyen INNER JOIN passeport ON citoyen.idPasseport = passeport.id WHERE passeport.numeroPasseport = ?"
            );
            preparedStatement.setLong(1, numeroPasseport);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                long numeroCitoyen = resultSet.getLong("numeroCitoyen");
                String nomCitoyen = resultSet.getString("nomCitoyen");
                String prenomCitoyen = resultSet.getString("prenomCitoyen");
                int ageCitoyen = resultSet.getInt("ageCitoyen");
                int idPasseport = resultSet.getInt("idPasseport");

                citoyenByNumeroPasseport = new Citoyen(id, numeroCitoyen, nomCitoyen, prenomCitoyen, ageCitoyen, idPasseport);
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return Optional.ofNullable(citoyenByNumeroPasseport);
    }

    /***
     *
     * @param numeroCitoyen
     * @param numeroPasseport
     *
     * What This Method Do:
     *      1-  Get 'Citoyen' By Number
     *      2-  Check The Value Of 'idPasseport' (Should Be Null)
     *      3-  Get Passport By Number (In Order To Have Access To Its id)
     *      4-  Check If Any User Already Has This Passport
     *      5-  Call @editCitoyen(Citoyen citoyen)
     */
    public static void assignPasseportToCitoyen(long numeroCitoyen, long numeroPasseport) {
        Optional<Citoyen> optionalCitoyen = getCitoyenByNumber(numeroCitoyen);

        if (optionalCitoyen.isEmpty()) {
            System.out.println("Pas de Citoyen Avec Ce Numero !");
        }

         if (optionalCitoyen.isPresent()) {
            Citoyen citoyen = optionalCitoyen.get();
            int passeportIdForCitoyen = citoyen.getIdPasseport();

            // If 'Citoyen' Already Has A Passport
            if (passeportIdForCitoyen != 0) {
                System.out.println("Ce Citoyen à deja un Passeport !");
            }

             // If 'Citoyen' Doesn't Have A Passport Yet
            if (passeportIdForCitoyen == 0) {
                // Get The 'Passeport' And Check If Any Citoyen Already Has This Passeport (using: idPasseport)
                Optional<Passeport> optionalPasseportByNumber = PasseportDAO.getPasseportByNumber(numeroPasseport);

                if (optionalPasseportByNumber.isEmpty()) {
                    System.out.println("Pas de Passeport Avec Ce Numéro !");
                }

                if (optionalPasseportByNumber.isPresent()) {
                    Passeport passeport = optionalPasseportByNumber.get();

                    try (Connection connection = DatabaseConnection.connectToDB();) {
                        // Check If This Passeport Already Given To A 'Citoyen'
                        PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM citoyen WHERE idPasseport = ?");
                        preparedStatement.setInt(1, passeport.getId());
                        ResultSet resultSet = preparedStatement.executeQuery();
                        resultSet.next();
                        int numberOfCitoyensHasThisPassport = resultSet.getInt(1);

                        if (numberOfCitoyensHasThisPassport != 0) {
                            System.out.println("Ce Passeport est déja donnée A un Autre Citoyen !");
                        }

                        if (numberOfCitoyensHasThisPassport == 0) {
                            int id = citoyen.getId();
                            String nomCitoyen = citoyen.getNomCitoyen();
                            String prenomCitoyen = citoyen.getPrenomCitoyen();
                            int ageCitoyen = citoyen.getAgeCitoyen();
                            int idPasseport = passeport.getId();

                            editCitoyen(new Citoyen(id, numeroCitoyen, nomCitoyen, prenomCitoyen, ageCitoyen, idPasseport));
                        }
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    }

                }

            }
        }

        try (Connection connection = DatabaseConnection.connectToDB();) {

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static Optional<Passeport> getPasseportByNumeroCitoyen(long numeroCitoyen) {
        Optional<Citoyen> optionalCitoyen = getCitoyenByNumber(numeroCitoyen);
        Passeport passeport = null;

        if (optionalCitoyen.isEmpty()) {
            System.out.println("Pas de Citoyen Avec Ce Numero !");
        } else {
            Optional<Passeport> passeportByNumeroCitoyen = PasseportDAO.getPasseportById(optionalCitoyen.get().getIdPasseport());
            passeport = passeportByNumeroCitoyen.get();
        }

        return Optional.ofNullable(passeport);
    }
}
