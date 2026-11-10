package main;

import io.LecteurCSV;
import io.LectureCSVResult;
import model.Population;
import processing.Completeur;
import processing.CompleteurMoyenne;

public class Main {

public static void main(String[] args) {

testerFichier("data/Exemples Fichier études combinées-20260311/ParasitesPeru2021.csv");
testerFichier("data/Données collectées DRYAD-20260311/processed_data_anisakis.csv");
testerFichier("data/Données campagne-20260311/mackerel.97442.csv");
}

public static void testerFichier(String chemin) {
System.out.println("\n======================================");
System.out.println("TEST DU FICHIER : " + chemin);
System.out.println("======================================");

LecteurCSV lecteur = new LecteurCSV();
LectureCSVResult resultat = lecteur.lireFichier(chemin);

if (resultat == null) {
System.out.println("LectureCSVResult est null");
return;
}

if (resultat.getSchema() == null) {
System.out.println("Aucun schéma détecté");
return;
}

System.out.println("Type détecté : " + resultat.getSchema().getTypeFichier());
System.out.println("Séparateur : " + resultat.getSchema().getSeparateur());
System.out.println("Index espèce : " + resultat.getSchema().getIndexEspece());
System.out.println("Index taille : " + resultat.getSchema().getIndexTaille());
System.out.println("Index poids : " + resultat.getSchema().getIndexPoids());
System.out.println("Index taux : " + resultat.getSchema().getIndexTauxInfestation());
System.out.println("Index contenu : " + resultat.getSchema().getIndexContenu());
System.out.println("Index paramètre : " + resultat.getSchema().getIndexParametre());

System.out.println("Nombre de lignes lues : " + resultat.getLignes().size());

Population population = new Population();

String type = resultat.getSchema().getTypeFichier();

if ("peru".equals(type)) {
population.construireDepuisLignesPeru(resultat.getLignes());
} else {
population.construireDepuisLignesClassiques(resultat.getLignes(), resultat.getSchema());
}

System.out.println("Nombre de poissons avant complétion : " + population.taillePopulation());

Completeur completeur = new CompleteurMoyenne();
completeur.completer(population);

System.out.println("Nombre de poissons après complétion : " + population.taillePopulation());

int limite = Math.min(5, population.getPoissons().size());
for (int i = 0; i < limite; i++) {
System.out.println(population.getPoissons().get(i));
}
}
}