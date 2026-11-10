package model;

import java.util.ArrayList;
import java.util.List;

import io.DetectedSchema;

/**
 * Represente une populaton de poissons 
 * 
 * cette classe permet de manipuler un ensemble de poissons et de raliser differentes
 *  operations sur ces donnes
 * 
 * Fonctionnalites principales :
 * -Ajouter des poissons
 * - rechercher des poissons par espece 
 * - construire une population a partir d un fichier 
 * -completer automatiquement les donnees manquantes 
 */

public class Population {

    //liste contenant tous les possons de la populaton 
    private List<Poisson> poissons;

    /**
     * constructeur par defaut 
     * Initialise une populaton vide
     */
    public Population(){
        this.poissons=new ArrayList<>();
    }

    /**
     * costructeur avec une liste de poissons existante 
     * 
     * @param poissons liste initiale de poissons 
     */

    public Population(List<Poisson> poissons){
        this.poissons=poissons;
    }


    /**
     * ajoute un poisson a la population
     * @param poisson poisson a ajouter
     */

    public void ajouterPoisson(Poisson poisson){
        if(poisson!=null){
            poissons.add(poisson);
        }
    }

    /**
     * retourne la liste de poissons
     * @return liste de poissons 
     */

    public List<Poisson> getPoissons(){
        return poissons;
    }

    /**
     * remplace la liste des poissons 
     * @param poissons nouvelle liste de poissons 
     */

    public void setPoissons(List<Poisson> poissons){
        this.poissons=poissons;
    }

    /**
     * Retourne le nombre total de poissons.
     *
     * @return taille de la population
    */
    public int taillePopulation(){
        return poissons.size();
    }

    /**
     * retourne tout les poissons appartenant a une espece donnee
     * @param espece espece recherchee
     * @return liste des poissons correspondant 
     */
    public List<Poisson> getPoissonsParEspece(String espece){
        
        List<Poisson> resultat=new ArrayList<>();

        if (espece == null){
            return resultat;
        }

        // Parcours de tout les poissons
        for (Poisson p : poissons){
            if (p.getEspece() !=null && p.getEspece().equalsIgnoreCase(espece)){
                resultat.add(p); //si on trouve pas un poisson on cree un
            }
        }
        return resultat;
    }

    /**
     * rechercher le premier poisson correspondant a une espece donnee
     * @param espece espece recherchee
     * @return poisson trouve sinon null
     */
    private Poisson chercherPoissonParEspece( String espece){
        if (espece == null){
            return null;
        }

        for (Poisson p : poissons ){
            if (p.getEspece() != null && p.getEspece().equalsIgnoreCase(espece)){
                return p ;
            }
        }

        return null;
    }

    /**
     * construit une population de poissons a partir de lignes de lignes csv classiques 
     * utilise le shema dectecte pour extraire les informations :
     * espece , taille ' poids , taix d infestation et contenu
     * 
     * @param lignes liste des lignes du fichiers
     * @param schema schema detecte contenant les index des colonnes 
     */



    public void construireDepuisLignesClassiques(List<String[]> lignes , DetectedSchema schema){
        if (lignes== null || schema == null ){
            return ;
        }

        for(String[] colonnes : lignes ){
            String espece = lireText(colonnes , schema.getIndexEspece());
            Double taille = lireDouble(colonnes , schema.getIndexTaille());
            Double poids = lireDouble(colonnes , schema.getIndexPoids());
            Double taux = lireDouble(colonnes , schema.getIndexTauxInfestation());
            String contenuNom = lireText(colonnes , schema.getIndexContenu());

            List<Contenu> contenus = new ArrayList<>();
            if(contenuNom!=null){
                contenus.add(new Contenu(contenuNom , taux));
            }

            Poisson poisson = new Poisson(espece , null , poids , taille , contenus );

            ajouterPoisson(poisson);
        }
    }

    /**
     * construit la population a partir de lignes d un fichier de type Peru
     * 
     * Format attendu :
     * Espece, Parametre, Total,
     * 
     * cette methode regroupe doc les information dans un meme objet poisson
     * 
     * @param lignes liste de lignes deja decoupees en colonnes 
     */
    public void construireDepuisLignesPeru(List<String[]> lignes ) {
        if (lignes == null || lignes.isEmpty()){
            return;
        }

        //parcours des lignes d ficher 
        for(String[] colonnes : lignes ){
            // verifie qu il y a assez de colonnes 
            if (colonnes.length < 3 ){
                continue;
            }

            //extractions de donnees
            String espece = nettoyerTexte(colonnes[0]);
            String parametre = nettoyerTexte(colonnes[1]);
            Double valeur =extraireNombre(colonnes[2]);

            if (espece == null || parametre == null){
                continue;
            }

            // recherche d un poisson existant 
            Poisson poisson =chercherPoissonParEspece(espece);

            // si aucun trouve -> creation 
            if(poisson == null){
                poisson = new Poisson ( espece , null , null, null, null , new ArrayList<>());
                ajouterPoisson(poisson);
            }
            // remplissage des donnees
            remplirPoissonDepuisParametre(poisson , parametre , valeur);
        }
    }

    /**
     * complete un poisson en fonction du parametre lu dans le fichier 
     * @param poisson poisson a modifier 
     * @param parametre nom du parametre 
     * @param valueur valeur associee
     */


    private void remplirPoissonDepuisParametre(Poisson poisson , String parametre , Double valeur ){
        if(poisson == null || parametre == null || valeur == null){
            return ;
        }

        String p = parametre.toLowerCase();

        if ((p.contains("longueur"))  || p.contains("length") || p.contains("size")){
            poisson.setTaille(valeur);
        }
        else if (p.contains("poids")|| p.contains("weight")){
            poisson.setPoids(valeur);
        }
        else if (p.contains("prevalence") || p.contains("prévalence")){
            poisson.setPoids(valeur);
        }
        else if (p.contains("abondance") || p.contains("abundance")) {
            poisson.setTauxInfestation(valeur);
        }
        else if (p.contains("intensite") || p.contains("intensité")) {
            poisson.setTauxInfestation(valeur);
        }
    }

    /**
     * Lit une valeur texte dans une ligne CSV.
     *
     * @param colonnes ligne du CSV
     * @param index position de la colonne
     * @return texte ou null si invalide 
     */

    private String lireText(String[] colonnes , Integer index){
        if( index == null || index <0 || index >= colonnes.length){
            return null;
        }

        String valeur = colonnes[index].trim();

        if (valeur.isEmpty() || valeur.equalsIgnoreCase("NA") || valeur.equalsIgnoreCase("null")){
            return null;
        }

        return valeur;
    }

    /**
     * Lit une valeur numerique (Double) dans une ligne CSV.
     *
     * @param colonnes ligne du CSV
     * @param index position de la colonne
     * @return nombre ou null si invalide ou non convertible
     */

    private Double lireDouble( String[] colonnes , Integer index){
        if( index == null || index <0 || index >= colonnes.length){
            return null;
        }

        String valeur = colonnes[index].trim();

        if (valeur.isEmpty() || valeur.equalsIgnoreCase("NA") || valeur.equalsIgnoreCase("null")){
            return null;
        }

        valeur = valeur.replace(",", ".");

        try{
            return Double.parseDouble(valeur);
        }
        catch(NumberFormatException e){
            return null;
        }

    }



    /**
     * Complete automatiquement les donnees manquantes
     * en utilisant la moyenne des poissons de la meme espece.
     */
    public void completerDonneesManquantesParEspece() {

        for (Poisson poisson : poissons) {

            String espece = poisson.getEspece();

            // Completer le poids
            if (poisson.getPoids() == null) {
                Double moyennePoids = calculerMoyennePoidsParEspece(espece);
                poisson.setPoids(moyennePoids);
            }

            // Completer la taille
            if (poisson.getTaille() == null) {
                Double moyenneTaille = calculerMoyenneTailleParEspece(espece);
                poisson.setTaille(moyenneTaille);
            }

            // Completer le taux
            if (poisson.getTauxInfestation() == null) {
                Double moyenneTaux = calculerMoyenneTauxParEspece(espece);
                poisson.setTauxInfestation(moyenneTaux);
            }
        }
    }

    /**
     * Calcule la moyenne des poids pour une espece donnee.
     */
    private Double calculerMoyennePoidsParEspece(String espece) {

        double somme = 0.0;
        int compteur = 0;

        for (Poisson poisson : poissons) {
            if (poisson.getEspece() != null &&
                poisson.getEspece().equalsIgnoreCase(espece) &&
                poisson.getPoids() != null) {

                somme += poisson.getPoids();
                compteur++;
            }
        }

        return (compteur == 0) ? null : somme / compteur;
    }

     /**
     * Calcule la moyenne des tailles pour une espece donnee.
     */
    private Double calculerMoyenneTailleParEspece(String espece) {

        double somme = 0.0;
        int compteur = 0;

        for (Poisson poisson : poissons) {
            if (poisson.getEspece() != null &&
                poisson.getEspece().equalsIgnoreCase(espece) &&
                poisson.getTaille() != null) {

                somme += poisson.getTaille();
                compteur++;
            }
        }

        return (compteur == 0) ? null : somme / compteur;
    }

    /**
     * Calcule la moyenne des taux d infestation pour une espece donnee.
     */
    private Double calculerMoyenneTauxParEspece(String espece) {

        double somme = 0.0;
        int compteur = 0;

        for (Poisson poisson : poissons) {
            if (poisson.getEspece() != null &&
                poisson.getEspece().equalsIgnoreCase(espece) &&
                poisson.getTauxInfestation() != null) {

                somme += poisson.getTauxInfestation();
                compteur++;
            }
        }

        return (compteur == 0) ? null : somme / compteur;
    }

    /**
     * Nettoie une chaine de caracteres 
     */
    private String nettoyerTexte(String texte) {

        if (texte == null) {
            return null;
        }

        texte = texte.trim();

        return texte.isEmpty() ? null : texte;
    }

   
    /**
     * extrait un nombre depuis une chaine complexe 
     * @param texte
     * @return
     */

    private Double extraireNombre(String texte) {

        if (texte == null) {
            return null;
        }

        texte = texte.trim();

        if (texte.isEmpty()) {
            return null;
        }

        texte = texte.replace(",", ".");

        StringBuilder nombre = new StringBuilder();
        boolean pointTrouve = false;
        boolean chiffreTrouve = false;

        for (int i = 0; i < texte.length(); i++) {

            char c = texte.charAt(i);

            if (Character.isDigit(c)) {
                nombre.append(c);
                chiffreTrouve = true;

            } else if (c == '.' && !pointTrouve) {
                nombre.append(c);
                pointTrouve = true;

            } else if (c == '-' && !chiffreTrouve && nombre.length() == 0) {
                nombre.append(c);

            } else {
                if (chiffreTrouve) {
                    break;
                }
            }
        }

        if (nombre.length() == 0 || nombre.toString().equals("-") || nombre.toString().equals(".")) {
            return null;
        }

        try {
            return Double.parseDouble(nombre.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }


    @Override
    public String toString(){
        return "Population {"+
                "poissons="+poissons+
                '}';
    }

    public static void main(String[] args) {
        Population population=new Population();

        Poisson p=new Poisson("merlu", null, null, 20.0, 1.0, new ArrayList<>());

        population.ajouterPoisson(p);

        population.ajouterPoisson(new Poisson("Marckel", null, null, 18.0, 2.0, new ArrayList<>()));
        population.ajouterPoisson(new Poisson("merlu", null, null, 25.0, 1.5, new ArrayList<>()));

        List<Poisson> merlus=population.getPoissonsParEspece("merlu");

        System.out.println(population);
        System.out.println(merlus);
    }
}
