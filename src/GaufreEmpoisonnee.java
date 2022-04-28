import Controleur.ControleurMediateur;
import Modele.Jeu;
import Vue.InterfaceGraphique;

public class GaufreEmpoisonnee {
    public static void main(String[] args) throws Exception {
        
        try {
            Jeu jeu = new Jeu();
            ControleurMediateur controleurMediateur = new ControleurMediateur(jeu);
            
            InterfaceGraphique.demarrer(jeu, controleurMediateur);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
