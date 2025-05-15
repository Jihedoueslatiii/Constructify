package ressources.com.ressources.services;


import ressources.com.ressources.entities.Ressource;

import java.util.List;
import java.util.Map;

public interface IRessourceService {
    Ressource addRessource(Ressource r);

    public List<Ressource> getAllRessources() ;

    Ressource addRessource1(Ressource r);
    Ressource updateRessource(Long id, Ressource r);
    Ressource ajouterRessource(Ressource r);
    void deleteRessource(Long id);
    Ressource getRessourceById(Long id);
    Ressource assignRessourceToProject(Long ressourceId, int projectId ,int nombreRessource);
     void envoyerRapportRessources(String destinataire);
    List<Ressource> findRessourcesByProjectId(int projectId);
    //Map<Integer, Integer> getProjectsForRessource(Long ressourceId);
    public void sendSms(String phoneNumber);
    public void sendSms1(String to, String messageBody);
    Map<Integer, Integer> getProjectsForRessource(Long ressourceId);
    public void sendSms2(String to);
}
