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
                interfaceGraphique.majJoueurCourant();
        } else {
            Configuration.instance().logger().info("Coup hors zone !\n");
        }
    }

    @Override
    public void traqueSouris(int coupX, int coupY) {
        
        if (!jeu.estTermine()) {
            if ((coupX <= jeu.gaufre().colonnes() * interfaceGraphique.gaufreGraphique.largeurCase()) && (coupY <= jeu.gaufre().lignes() * interfaceGraphique.gaufreGraphique.hauteurCase())) {
                coupX = (coupX / interfaceGraphique.gaufreGraphique.largeurCase());
                coupY = (coupY / interfaceGraphique.gaufreGraphique.hauteurCase());
                
                jeu.setLargeurPrevisualisation(jeu.gaufre().colonnes() - coupX);
                jeu.setHauteurPrevisualisation(jeu.gaufre().lignes() - coupY);
                jeu.setPrevisualisationX(coupX);
                jeu.setPrevisualisationY(coupY);
    
                interfaceGraphique.previsualisation(jeu.getJoueurCourant(), coupX, coupY, jeu.largeurPrevisualisation(), jeu.hauteurPrevisualisation());
            } else {
                Configuration.instance().logger().info("Curseur hors zone !\n");
            }
        } else {
            // jeu.modifierTailleGauffre(0, 0); // ADRIEN VA MODIF CA !!!
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
            case "Nouvelle":
                jeu.modifierTailleGauffre(0, 0);
                interfaceGraphique.nouvelle();
                interfaceGraphique.metAJour();
                break;
            case "suite":
                if(jeu.estTermine()){
                    jeu.modifierTailleGauffre(0, 0);
                    interfaceGraphique.metAJour();
                }
                else{
                    interfaceGraphique.incrementeScore();
                    interfaceGraphique.majScore();
                    jeu.modifierTailleGauffre(0, 0);
                    interfaceGraphique.metAJour();
                }
                
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
