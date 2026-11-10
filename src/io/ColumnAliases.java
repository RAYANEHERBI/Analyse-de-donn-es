package io;

import java.util.List;
import java.util.Map;

/**
 * Classes utilitaire contenant les alias possibles des colonnes dans les fichiers csv
 * 
 * permet de gerer les differeces de  nommage entre les fichiers 
 * 
 * elle est utilisee pour faire correspondre les colonnes du csv avec les champs metiers du projet
 */
public class ColumnAliases {

    /**
     * Dicionnaire des alias possibles pour chaque  champ logique
     * 
     * cle: nom du champ metier 
     * valeur : lise des noms possibles
     */
    
    // Alias pour l'espèce du poisson
    public static final Map<String, List <String>> ALIASES= Map.of(

        // Alias pour l'espèce du poisson
        "espece", List.of(
            "truehost",
            "host",
            "scientific",
            "species",
            "espece",
            "fish",
            "dataspecies"
        ),

        // Alias pour la taille du poisson
        "taille", List.of(
            "standard lengthcalc",
            "length cm",
            "standardlngth",
            "m size mm",
            "size",
            "length",
            "length sl cm",
            "length tl cm",
            "length fl cm",
            "length fl cm mean",
            "longueur moyenne",
            "longueur"

        ),

        //Alias pour le poids
        "poids", List.of(
            "weight",
            "poids",
            "poids moyen",
            "weight kg",
            "weight mean"

        ),

        // Alias pour le taux d'infestation
        "taux", List.of(
            "standard abundance",
            "abundance mean per fish",
            "number of anisakis sp 13",
            "nparasitestotal",
            "total number of parasites",
            "total",
            "abundance",
            "prevalnce",
            "prevalece (%)",
            "intensite moyenne",
            "intensity mean",
            "abondance moyenne"
        ),

        // Alias pour les parties du poisson (contenu)
        "contenu", List.of(
            "portion of body",
            "partie",
            "body part",
            "organ",
            "portion"
        ),

        //Alias pour parametre
        "parametre", List.of(
            "parametre",
            "paramètre",
            "parameter"

        )
    );



    /**
     * constructure privé pour empecher l'instanciation de la classe
     * 
     * cette classe est une classe utilitaire contenant uniquement des constantes statiques 
     */

    private ColumnAliases(){

    }
}
