package processing;


import model.Population;

public class CompleteurMoyenne implements Completeur{
    
    @Override
    public void completer(Population population){
        if(population!=null){
            population.completerDonneesManquantesParEspece();
        }
    }
}
