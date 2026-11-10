package io;

/**
 * Repreente  le schema detectee d'un fichier csv
 * 
 * cette classe contient les informations necessaires pour lire correctement un fichier csv:
 * - type du fichier 
 * - separateur utilise 
 * - les positions des colonnes importantes 
 * 
 * elle est utilisee pour adapter automatiquement la lecture du fichier meme si les colonnes changent d ordre
 * 
 */

public class DetectedSchema {

    private String typeFichier; //type du fichier
    private String separateur; // separateur utilise

    private Integer indexEspece;
    private Integer indexTaille;
    private Integer indexTauxInfestation;
    private Integer indexContenu;
    private Integer indexPoids;
    private Integer indexParametre;

    /**
     * Construit un schema detecte complet
     * @param typeFichier type de fichier detecte
     * @param separateur separateur du csv
     * @param indexEspece index de de l'espece
     * @param indexTaille index de la taille
     * @param indexTauxInfestation index du taux d'infestation
     * @param indexContenu index de contenu
     * @param indexParametre index de parametre
     */
    public DetectedSchema(String typeFichier, String separateur,Integer indexEspece,Integer indexTaille,Integer indexTauxInfestation,Integer indexContenu, Integer indexPoids, Integer indexParametre){
        this.typeFichier=typeFichier;
        this.separateur=separateur;
        this.indexEspece=indexEspece;
        this.indexTaille=indexTaille;
        this.indexTauxInfestation=indexTauxInfestation;
        this.indexContenu=indexContenu;
        this.indexPoids= indexPoids;
        this.indexParametre=indexParametre;
    }
    
    public String getTypeFichier(){
        return typeFichier;
    }

    public String getSeparateur(){
        return separateur;
    }

    public Integer getIndexEspece(){
        return indexEspece;
    }

    public Integer getIndexTaille(){
        return indexTaille;
    }

    public Integer getIndexTauxInfestation(){
        return indexTauxInfestation;
    }

    public Integer getIndexContenu(){
        return indexContenu;
    }

    public Integer getIndexPoids(){
        return indexPoids;
    }

    public Integer getIndexParametre(){
        return indexParametre;
    }

    /**
     * representaton textuelle du shema detecte
     */

    @Override
    public String toString(){
        return "DetectedSchema{" + 
            "typeFichier='" + typeFichier + '\'' +
            ", separateur='"+separateur + '\'' +
            ", indexEspece=" + indexEspece +
                ", indexTaille=" + indexTaille +
                ", indexTauxInfestation=" + indexTauxInfestation +
                ", indexContenu=" + indexContenu +
                ", indexParametre=" + indexParametre +
                '}';
    }
}

