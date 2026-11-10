package io;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsable de la detection automatique d un fichier csv
 * 
 * elle permet de :
 * - detecter  le separateur utilise
 * - identifier les colonnes importantes grace a un systeme d alias 
 * -valider les types des donnees 
 * - determiner le types globale du fichier selon sa structure 
 * 
 * cette classe est centrale car elle permet d adapter le programme a differents formats de fichiers csv
 */
public class SchemaDetector {
    
    /**
     * méthode principale qui détecte le schéma d’un fichier CSV.
     * 
     * etapes: 
     * 1- detecter le separateur 
     * 2- analyse du header
     * 3- trouver les colonnes via lea alias
     * 4- valider les colonnes 
     * 5- determiner le type du fichier 
     * 
     * @param headerLine premiere ligne du ficher csv
     * @param lignesAValider quelques lignes du fichier pour validation
     * @return shema detecté
     */

    public DetectedSchema detecterSchema(String headerLine, List <String> lignesAValider){

        // detection du separateur 
        String separateur = detecterSeparateur(headerLine);

        // creation d un wrapper pour manipuler les colonnes
        HeaderWrapper header= new HeaderWrapper(headerLine, separateur);


        // recherche des colonnes importantes a partir des alias 
        Integer indexEspece= trouverIndexParAlias(header, "espece");
        Integer indexTaille= trouverIndexParAlias(header, "taille");
        Integer indexTaux= trouverIndexParAlias(header, "taux");
        Integer indexContenu= trouverIndexParAlias(header, "contenu");
        Integer indexPoids= trouverIndexParAlias(header, "poids");
        Integer indexParametre = trouverIndexParAlias(header, "parametre");


        //detection du type global du fichier
        String typeFichier= detecterTypeParStructure(headerLine, separateur, header);

        //Validation des colonnes candidats

        if(!validerColonneTexte(lignesAValider, separateur, indexEspece)){
            indexEspece=null;
        }
        if(!validerColonneNumerique(lignesAValider, separateur, indexTaille) && indexTaille !=null ){
            indexTaille=null;
        }
        if(!validerColonneNumerique(lignesAValider, separateur, indexTaux) && indexTaux!=null){
            indexTaux=null;
        }// si la colonne n est pas valide in l annule
        if(!validerColonneNumerique(lignesAValider, separateur, indexPoids) && indexPoids!=null){
            indexPoids=null;
        }

        //creaton du shema detecte 
        return new DetectedSchema(typeFichier, separateur, indexEspece, indexTaille, indexTaux, indexContenu, indexPoids, indexParametre);
    }


    /**
     * Detecte le separateur utilisé dans le csv
     * @param headerLine premiere ligne du fichier
     * @return separateur detecté
     */
    private String detecterSeparateur(String headerLine){
        if(headerLine.contains(";")){
            return ";";
        }
        return ",";
    }

    /**
     * Rechrche l'index d'une colonne a partir d'une liste d'alias
     * @param header header analysé
     * @param typeAlias liste d'aliase possibles
     * @return index trouvé ou null
     */
    private Integer trouverIndexParAlias(HeaderWrapper header, String typeAlias){
        List<String> alias=ColumnAliases.ALIASES.get(typeAlias);
        if(alias == null){
            return null;
        }

        for(String a: alias){ // parcours des alias possibles
            Integer index =header.getIndexNormalise(a);
            if(index != null){
                return index;
            }
        }
        return null;
    }

    /**
     * Valide qu'une colonne contient majoritairement du texte 
     * @param lignes lignes a analyser
     * @param separateur separateur du fichier
     * @param index index de la colonne
     * @return true si la colonne semble textuelle
     */
    private boolean validerColonneTexte(List<String> lignes, String separateur, Integer index){
        if(index==null || lignes==null || lignes.isEmpty()){
            return false;
        }

        int total=0;
        int valides=0;

        for(String ligne: lignes){
            String[] colonnes = ligne.split(separateur,-1);
            if(index>= colonnes.length){ //verifie que l index existe 
                continue;
            }

            String valeur= colonnes[index].trim();

            // ignore les valeurs vides ou speciales
            if(valeur.isEmpty() || valeur.equalsIgnoreCase("NA") || valeur.equalsIgnoreCase("null") || valeur.equalsIgnoreCase("nan")){
                continue;
            }

            total++;
            // si c est pas un nombre donc texte valide
            if(!estNumerique(valeur)){
                valides++;
            }
        }

        return total>0 && valides>=Math.max(1, total/2);
    }

    /**
     * 
     * Valide qu'une colonne contient principalement des nombres 
     * @param lignes lignes a analyser
     * @param separateur separateur du fichier
     * @param index index de la colonne
     * @return true si la colonne semble numerique
     */
    private boolean validerColonneNumerique(List<String> lignes, String separateur,Integer index){
        if(index== null || lignes==null || lignes.isEmpty()){
            return false;
        }

        int total=0; 
        int valides=0;
//
        for(String ligne: lignes){
            String[] colonnes = ligne.split(separateur,-1);
            if(index>= colonnes.length){
                continue;
            }

            String valeur=colonnes[index].trim();
            if(valeur.isEmpty() || valeur.equalsIgnoreCase("NA") || valeur.equalsIgnoreCase("null") || valeur.equalsIgnoreCase("nan")){
                continue;
            }

            total++;

            if(estNumerique(valeur)){
                valides++;
            }
        }

        return total==0 || valides >=Math.max(1, total/2);
    }

    /**
     * determine si une chaine reprsente un nombre
     * @param valeur valeur a tester
     * @return true si la valuer est numerique
     */
    private boolean estNumerique(String valeur){
        if(valeur==null){
            return false;
        }

        valeur= valeur.replace("\"", "").trim(); // nettoyage des guillemets

        try{
            Double.parseDouble(valeur);
            return true;
        }catch(NumberFormatException e){
            return false;
        } 
    }

    /**
     * verifie si au moins un alias d un type est present dans le header
     * 
     * @param header wrapper du header  
     * @param typeAlias type recherche 
     * @return true si au moins un alias est trouve
     */

    private boolean contientAlias(HeaderWrapper header, String typeAlias){
        List<String> alias=ColumnAliases.ALIASES.get(typeAlias);

        if(alias==null){
            return false;
        }

        for (String a: alias){
            if(header.contientNormalise(a)){
                return true;
            }
        }
        return false;
    }

    /**
     * Detecte le type general d fichier  selon sa sctructure 
     * 
     * criteres utilises:
     * -nombre de colonnes 
     * -separateur
     * -presence de mots cles dans le header 
     * 
     * @param headerLine la premiere ligne du ficheier
     * @param separateur separateur detecté
     * @param header wrapper du header
     * @return type de fichier detecte
     */
    private String detecterTypeParStructure(String headerLine, String separateur, HeaderWrapper header){
        String[] colonnes=headerLine.split(separateur,-1);
        int nbColonnes=colonnes.length;

        //header normalise pour faciliter les recherches 
        String h=HeaderWrapper.normaliser(headerLine);

        if(",".equals(separateur) && nbColonnes<40 && contientAlias(header, "espece")&&contientAlias(header, "taille")&&contientAlias(header, "taux")){
            return "processed_data";
        }

        if(",".equals(separateur) && (nbColonnes>=50 || h.contains("abundance mean per fish")|| h.contains("total number of parasites"))){
            return "anisakid_data";
        }

        if(";".equals(separateur) && (nbColonnes>30 || h.contains("nom merlu") || h.contains("anisakis"))){
            return "merlu";
        }

        if(";".equals(separateur) && contientAlias(header, "taille") && contientAlias(header, "taux")){
            return "marckel";
        }

        if(",".equals(separateur) && contientAlias(header, "espece") && contientAlias(header, "parametre") ){
            return "peru";
        }

        if(",".equals(separateur) && nbColonnes<=5 && (contientAlias(header, "espece"))){
            return "host_data";
        }

        return "inconnu";
    }

    /**
     * Lit les premieres lignes non vides d'un fichier pour les utiliser 
     * lors de la validation des colonnes 
     * @param reader lecteur du fichier
     * @param maxLignes nombre max de ligne a lire
     * @return liste de lignes extraites
     * @throws java.io.IOException en cas d'erreur de la lecteur
     */
    public List<String> extrairePremieresLignes(java.io.BufferedReader reader,int maxLignes) throws java.io.IOException{
        List<String> lignes= new ArrayList<>();
        String ligne;

        while((ligne = reader.readLine())!=null && lignes.size() < maxLignes){
            if(!ligne.trim().isEmpty()){
                lignes.add(ligne);
            }
        }
        return lignes;
    }
}
