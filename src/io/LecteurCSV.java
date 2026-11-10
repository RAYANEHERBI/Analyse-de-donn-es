package io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LecteurCSV {
    public LectureCSVResult lireFichier(String chemin){
        List<String[]> lignes = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(chemin))){
            String headerLine = reader.readLine();
            if(headerLine == null || headerLine.trim().isEmpty()){
                return new LectureCSVResult(null, lignes);
            }
            SchemaDetector detector = new SchemaDetector();
            List<String> echantillon = detector.extrairePremieresLignes(reader, 5);
            DetectedSchema schema= detector.detecterSchema(headerLine, echantillon);

            try (BufferedReader reader2=new BufferedReader(new FileReader(chemin))){
                reader2.readLine();

                String ligne;
                while((ligne = reader2.readLine()) !=null){
                    if(ligne.trim().isEmpty()){
                        continue;
                    }

                    lignes.add(ligne.split(schema.getSeparateur()));
                }
            }
            return new LectureCSVResult(schema, lignes);
        }
        catch(IOException e){
            System.out.println("erreuer lors de la lecture du premier fichier: "+e.getMessage());
            return new LectureCSVResult(null, lignes);
        }
    }
}