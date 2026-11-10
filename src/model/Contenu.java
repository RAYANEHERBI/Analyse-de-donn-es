package model;
/**
   * Representer unr parie analysee du poisson
   * (fois, gonade , muscle...) avec la valeur associee
   * auteur : LOUNIS Mayas , HERBI Rayane
*/
public class Contenu {
    private String partie;
    private Double valeur;

    /**
        *constructur complet
        * @param partie partie analyse du poisson
        * @param valeur valeur mesuree pour cette partie 
    */

    public Contenu(String partie, Double valeur){
        this.partie=partie;
        this.valeur=valeur;
    }

    // getters & setters :

    public String getPartie() {
        return partie;
    }

    public void setPartie(String partie) {
        this.partie = partie;
    }

    public Double getValeur() {
        return valeur;
    }

    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }

    @Override
    public String toString() {
        return "Contenu{partie='" + partie + "', valeur=" + valeur + "}";
    }
}
