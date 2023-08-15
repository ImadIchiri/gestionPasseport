package org.example.passeport;

public class Passeport {
    private static int currentId = 0;
    private int id;
    private long numeroPasseport;
    private String dateExpiration; // 01/01/2022

    public Passeport(int id, long numeroPasseport, String dateExpiration) {
        this.id = id; // Coming From The DataBase
        this.numeroPasseport = numeroPasseport;
        this.dateExpiration = dateExpiration;
    }

    public Passeport(long numeroPasseport, String dateExpiration) {
        this.id = currentId; // Starts By N° = 0
        this.numeroPasseport = numeroPasseport;
        this.dateExpiration = dateExpiration;

        currentId++;
    }

    public int getId() {
        return id;
    }

    public long getNumeroPasseport() {
        return numeroPasseport;
    }
    public void setNumeroPasseport(long numeroPasseport) {
        this.numeroPasseport = numeroPasseport;
    }

    public String getDateExpiration() {
        return dateExpiration;
    }
    public void setDateExpiration(String dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    @Override
    public String toString() {
        return "Passeport{" +
                "id='" + id + '\'' +
                ", numeroPasseport=" + numeroPasseport +
                ", dateExpiration='" + dateExpiration + '\'' +
                '}';
    }
}
