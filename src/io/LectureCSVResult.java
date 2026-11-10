package io;
import java.util.List; 

public class LectureCSVResult {
    private DetectedSchema schema;
    private List<String[]> lignes ;

    public LectureCSVResult(DetectedSchema schema , List<String[]> lignes){
        this.schema =schema;
        this.lignes=lignes;
    }

    public DetectedSchema getSchema(){
    return schema;
    }

    public List<String[]> getLignes(){
        return lignes;
    }
}


