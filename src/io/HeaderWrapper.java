package io;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

/**
 * classe permet de gerer le header d un fichier csv 
 * Elle associe chaque nom de colonne a son index dans le fichier 
 * la normalisaton permet de gerer : les majuscules /miniscules , les accents , les caracteres speciaux
 */

public class HeaderWrapper {
    /**
     * Map associant un nom de colonne normalise a son ndex
     */
    
    private Map<String, Integer> colonnesNormalisees;

    /**
     * Construit un wrapper de header a partir d'une ligne CSV
     * @param headerLine premiere ligne du fichier csv
     * @param sepatateur separateur utilisé dan le fichier
     */
    
    public HeaderWrapper(String headerLine, String sepatateur){
        colonnesNormalisees=new HashMap<>();
        String[] colonnes=headerLine.split(sepatateur,-1); //decoupe la ligne en colonnes

        for(int i=0;i< colonnes.length;i++){ // parcours haque colonne 
            String nomNormalise=normaliser(colonnes[i]); // normalise chaque colonne 
            colonnesNormalisees.put(nomNormalise, i);  // association nom->index
        }
    }

    /**
     * Retoutne l'index d'une colonne normalisée a partir de son nom
     * @param nom nom de la colonne recherchee
     * @return index de la colonne ou null si absente
     */    
    public Integer getIndexNormalise(String nom){
        return colonnesNormalisees.get(normaliser(nom));
    }

    /**
     * Verifie si une colonne exists dans le header
     * @param nom nom de colonne recherche
     * @return true si la colonne existe false sinon
     */
    public boolean contientNormalise(String nom){
        return colonnesNormalisees.containsKey(normaliser(nom));
    }

    /**
     * Normalise un text:
     * -miniscule
     * -suppression des accents
     * -remplacement des caracteres speciaux
     * @param texte text a normaliser
     * @return texte normalisé
     */
    public static String normaliser(String texte){
        if(texte == null){
            return "";
        }
        String resultat=texte.trim().toLowerCase(); //minuscule +trim
        
        //supression des accents 
        resultat=Normalizer.normalize(resultat, Normalizer.Form.NFD);
        resultat=resultat.replaceAll("\\p{M}","");

        //supression des caracteres speciaux
        resultat=resultat.replaceAll("[_./()\\-]+", " ");

        // suppression des espaces multiples
        resultat=resultat.replaceAll("\\s+", " ").trim();

        return resultat;
    }

}
