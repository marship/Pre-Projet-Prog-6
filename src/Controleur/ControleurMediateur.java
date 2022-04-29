package Controleur;

import Global.Configuration;
import Joueur.IA;
import Modele.Jeu;
import Vue.CollecteurEvenements;
import Vue.InterfaceGraphique;

public class ControleurMediateur implements CollecteurEvenements {

    InterfaceGraphique interfaceGraphique;
    Configuration configuration;
    Jeu jeu;
    IA joueurAutomatique;

    public ControleurMediateur(Jeu j) {
        configuration = Configuration.instance();
        jeu = j;
    }

    @Override
    public void clicSouris(int coupX, int coupY) {
        if ((coupX <= jeu.gaufre().colonnes() * interfaceGraphique.gaufreGraphique.largeurCase()) && (coupY <= jeu.gaufre().lignes() * interfaceGraphique.gaufreGraphique.hauteurCase())) {
                coupX = (coupX / interfaceGraphique.gaufreGraphique.largeurCase());
                coupY = (coupY / interfaceGraphique.gaufreGraphique.hauteurCase());
                jouerCoup(coupX, coupY);
        } else {
            Configuration.instance().logger().info("Coup hors zone !\n");
        }
    }

    void jouerCoup(int coupX, int coupY) {
        jeu.jouerCoup(coupX, coupY);
    }

    void annule() {
        jeu.annule();
    }

    void refaire() {
        jeu.refaire();
    }

    @Override
    public boolean commande(String commande) {
        switch (commande) {
            case "up":
                jeu.modifierTailleGauffre(1, 0);
                interfaceGraphique.metAJour();
                break;
            case "down":
                jeu.modifierTailleGauffre(-1, 0);
                interfaceGraphique.metAJour();
                break;
            case "left":
                jeu.modifierTailleGauffre(0, -1);
                interfaceGraphique.metAJour();
                break;
            case "right":
                jeu.modifierTailleGauffre(0, 1);
                interfaceGraphique.metAJour();
                break;
            case "quit":
                System.exit(0);
                break;
            case "annule":
                annule();
                break;
            case "refaire":
                refaire();
                break;
            case "fullscreen":
                interfaceGraphique.basculePleinEcran();
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void fixerInterfaceUtilisateur(InterfaceGraphique iGraphique) {
        interfaceGraphique = iGraphique;
    }
}
