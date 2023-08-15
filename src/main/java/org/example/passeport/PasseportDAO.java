package org.example.passeport;

import org.example.database.DatabaseConnection;
import org.example.helpers.DatesAndStrings;

import java.sql.*;
import java.text.ParseException;
import java.util.*;
import java.util.Date;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PasseportDAO {
    public static void createPassePort(Passeport passeport) {
        Optional<Passeport> passeportByNumber = getPasseportByNumber(passeport.getNumeroPasseport());

        try (Connection connection = DatabaseConnection.connectToDB();) {
            if (passeportByNumber.isEmpty()) {
                System.out.println("Ce numéro de Passeport exist déja !");
            } else  {
                if (DatesAndStrings.compareDatesString(DatesAndStrings.fromatDate(new Date()), passeport.getDateExpiration())) {
                    PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO passeport (numeroPasseport, dateExpiration) VALUES (?, ?)");
                    preparedStatement.setLong(1, passeport.getNumeroPasseport());
                    preparedStatement.setString(2, passeport.getDateExpiration());
                }
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        } catch (ParseException exception) {
            exception.printStackTrace();
            System.out.println("Essayez de nouveau avec une autre date !");
        }
    }

    public static Optional<Passeport> getPasseportByNumber(long numeroPasseport) {
        Passeport selectedPasseport = null;

        try (Connection connection = DatabaseConnection.connectToDB();) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM passeport WHERE numeroPasseport = ?");
            preparedStatement.setLong(1, numeroPasseport);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String dateExpiration = resultSet.getString("dateExpiration");

                selectedPasseport = new Passeport(id, numeroPasseport, dateExpiration);
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        catch (IndexOutOfBoundsException exception) {
            exception.printStackTrace();
        }

        return Optional.ofNullable(selectedPasseport);
    }

    public static Optional<Passeport> getPasseportById(int id) {
        Passeport selectedPasseport = null;

        try (Connection connection = DatabaseConnection.connectToDB();) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM passeport WHERE id = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                long numeroPasseport = resultSet.getLong("numeroPasseport");
                String dateExpiration = resultSet.getString("dateExpiration");

                selectedPasseport = new Passeport(id, numeroPasseport, dateExpiration);
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        catch (IndexOutOfBoundsException exception) {
            exception.printStackTrace();
        }

        return Optional.ofNullable(selectedPasseport);
    }

    public static List<Passeport> getAllPasseport () {
        List<Passeport> passeportList = new ArrayList<>();

        try (Connection connection = DatabaseConnection.connectToDB();) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM passeport");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Long numeroPasseport = resultSet.getLong("numeroPasseport");
                String dateExpiration = resultSet.getString("dateExpiration");

                passeportList.add(new Passeport(id, numeroPasseport, dateExpiration));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return passeportList;
    }

    public static void editPasseport (Passeport passeport) {
        try (Connection connection = DatabaseConnection.connectToDB();) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE passeport SET numeroPasseport = ?, dateExpiration = ? WHERE id = ?");
            preparedStatement.setLong(1, passeport.getNumeroPasseport());
            preparedStatement.setString(2, passeport.getDateExpiration());
            preparedStatement.setInt(3, passeport.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static int deletePasseportByNumber (long numeroPasseport) {
        int deleteCount = 0;

        try (Connection connection = DatabaseConnection.connectToDB();) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM passeport WHERE numeroPasseport = ?");
            preparedStatement.setLong(1, numeroPasseport);

            deleteCount = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return deleteCount;
    }

    public static List<Passeport> getExpiedPasseports() {
        List<Passeport> expiredPasseports;
        String todaysDate = DatesAndStrings.fromatDate(new Date());

        List<Passeport> passeportList = getAllPasseport();

        Predicate<Passeport> passeportPredicate = (Passeport passeport) -> {
                try {
                    return DatesAndStrings.compareDatesString(passeport.getDateExpiration(), todaysDate);
                } catch (ParseException exception) {
                    System.out.println(exception);
                }

                return false;
        };

        expiredPasseports = passeportList.stream()
                .filter(passeport -> passeportPredicate.test(passeport))
                .collect(Collectors.toList());


        return expiredPasseports;

    }

    public static int deletePasseportById (int idPasseport) {
        int deleteCount = 0;

        try (Connection connection = DatabaseConnection.connectToDB();) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM passeport WHERE id = ?");
            preparedStatement.setLong(1, idPasseport);

            deleteCount = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return deleteCount;
    }
}
