package org.example.citoyen;

public class Citoyen {
    private int id;
    private long numeroCitoyen;
    private String nomCitoyen;
    private String prenomCitoyen;
    private int ageCitoyen;
    private int idPasseport;

    // Created By Developer (Comming From DataBase)
    public Citoyen(int id, long numeroCitoyen, String nomCitoyen, String prenomCitoyen, int ageCitoyen, int idPasseport) {
        this.id = id;
        this.numeroCitoyen = numeroCitoyen;
        this.nomCitoyen = nomCitoyen;
        this.prenomCitoyen = prenomCitoyen;
        this.ageCitoyen = ageCitoyen;
        this.idPasseport = idPasseport;
    }

    // Created By The Users
    public Citoyen(long numeroCitoyen, String nomCitoyen, String prenomCitoyen, int ageCitoyen) {
        this.numeroCitoyen = numeroCitoyen;
        this.nomCitoyen = nomCitoyen;
        this.prenomCitoyen = prenomCitoyen;
        this.ageCitoyen = ageCitoyen;
    }

    public int getId() {
        return id;
    }

    public long getNumeroCitoyen() {
        return numeroCitoyen;
    }

    public String getNomCitoyen() {
        return nomCitoyen;
    }
    public void setNomCitoyen(String nomCitoyen) {
        this.nomCitoyen = nomCitoyen;
    }

    public String getPrenomCitoyen() {
        return prenomCitoyen;
    }
    public void setPrenomCitoyen(String prenomCitoyen) {
        this.prenomCitoyen = prenomCitoyen;
    }

    public int getAgeCitoyen() {
        return ageCitoyen;
    }
    public void setAgeCitoyen(int ageCitoyen) {
        this.ageCitoyen = ageCitoyen;
    }

    public int getIdPasseport() { return this.idPasseport; }
    public void setIdPasseport(int idPasseport) { this.idPasseport = idPasseport; }

    @Override
    public String toString() {
        return "Citoyen{" +
                "id=" + id +
                ", numeroCitoyen=" + numeroCitoyen +
                ", nomCitoyen='" + nomCitoyen + '\'' +
                ", prenomCitoyen='" + prenomCitoyen + '\'' +
                ", ageCitoyen=" + ageCitoyen +
                ", idPasseport=" + idPasseport +
                '}';
    }
}
