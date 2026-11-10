package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un poisson étudié dans le cadre du projet EcoParasite.
 * Chaque poisson possède des caractéristiques morphologiques 
 * (espèce, taille, poids, longueur) et un taux d'infestation.
 * Le taux peut être null si la donnée est manquante.
 * La partie analysée (contenu) peut varier selon le poisson (foie, gonade, etc.)
 * 
 * Auteur : Rayane HERBI, Mayas LOUNIS
 */

public class Poisson {

    // Attributs
    private String espece;
    private Double longueur;
    private Double poids;
    private Double taille;
    private Double tauxInfestation; 
    private List<Contenu> contenus;


    // getters & setters
    public String getEspece() {
        return espece;
    }

    protected Double getLongueur() {
        return longueur;
    }

    protected Double getPoids() {
        return poids;
    }

    protected Double getTaille() {
        return taille;
    }

    public Double getTauxInfestation() {
        return tauxInfestation;
    }

    protected List<Contenu> getContenu() {
        return contenus;
    }


    protected void setEspece(String espece) {
        this.espece = espece;
    }

    protected void setLongueur(Double longueur) {
        this.longueur = longueur;
    }

    protected void setPoids(Double poids) {
        this.poids = poids;
    }

    protected void setTaille(Double taille) {
        this.taille = taille;
    }

    public void setTauxInfestation(Double tauxInfestation) {
        this.tauxInfestation = tauxInfestation;
    }

    protected void setContenu(List<Contenu> contenus) {
        this.contenus = contenus;
    }

    /**
     * Constructeur complet pour créer un poisson
     *
     * @param espece Espèce du poisson
     * @param longueur Longueur du poisson
     * @param poids Poids du poisson
     * @param taille Taille du poisson 
     * @param tauxInfestation Taux d'infestation par Anisakis (peut être null)
     * @param contenu Partie analysée du poisson (foie, gonade, etc...)
     */

    public Poisson(String espece, Double longueur, Double poids, Double taille, Double tauxInfestation, List<Contenu> contenus) {
        
        
        this.espece = espece;
        this.longueur = longueur;
        this.poids = poids;
        this.taille = taille;
        this.tauxInfestation = tauxInfestation;
        this.contenus = contenus;
    }

    /**
     * Constructeur pour créer un poisson sans taux d'infestation (valeur manquante)
     *
     * @param espece Espèce du poisson
     * @param longueur Longueur du poisson (cm)
     * @param poids Poids du poisson (g)
     * @param taille Taille du poisson (cm)
     * @param contenu Partie analysée du poisson
     */

    public Poisson(String espece, Double longueur, Double poids, Double taille, List<Contenu> contenus) {
        this(espece, longueur, poids, taille, null, contenus);
    }

     /**
     * Représentation textuelle du poisson
     *
     * @return String avec toutes les informations du poisson
     */

    @Override

    public String toString(){
        return "poisson { Espece: " + espece + ",  Longueur: " + longueur + ",  Poids: " + poids + ",  Taille: " + taille + ",  TauxInfestation: " + tauxInfestation + ",  Contenu: " + contenus + "}" ;
    }

    public static void main(String[] args) {
        List<Contenu> contenus=new ArrayList<>();
        contenus.add(new Contenu("foie",2.0));

        Poisson poisson=new Poisson("Merlu", 30.0, 500.0, 28.0, 1.5, contenus);
        System.out.println(poisson);
    }

}