package Controleur;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import Global.Configuration;
import Joueur.IA;
import Modele.Coup;
import Modele.Jeu;
import Vue.CollecteurEvenements;
import Vue.InterfaceGraphique;

public class ControleurMediateur implements CollecteurEvenements {

    InterfaceGraphique interfaceGraphique;
    IA joueurAutomatique;
    Jeu jeu;
    
    public ControleurMediateur(Jeu j) {
        jeu = j;
    }

    @Override
    public void clicSouris(int coupX, int coupY) {
        if (!jeu.estTermine()) {
            if (estPositionSourisCorrect(coupX, coupY)) {
                if (jeu.estDejaMangee(conversionCoordonneeVersCases(coupX, true), conversionCoordonneeVersCases(coupY, false))) {
                    Configuration.instance().logger().info("Morceau deja mange !\n");
                    interfaceGraphique.majDejaMangee();
                } else {
                    manger(conversionCoordonneeVersCases(coupX, true), conversionCoordonneeVersCases(coupY, false));
                    interfaceGraphique.majJoueurCourant();
                }
            } else {
                Configuration.instance().logger().info("Coup hors gaufre !\n");
                interfaceGraphique.majHorsGaufre();
            }
        } else {
            Configuration.instance().logger().info("Fin de partie !\n");
                interfaceGraphique.majFinPartie();
        }
    }

    @Override
    public void traqueSouris(int coupX, int coupY) {
        
        if (jeu.estTermine()) {
            // Désactiver Prévisualisation
            gestionPrevisualisationCoup(coupX, coupY, true);
        } else {
            if (estPositionSourisCorrect(coupX, coupY)) {
                coupX = conversionCoordonneeVersCases(coupX, true);
                coupY = conversionCoordonneeVersCases(coupY, false);
                // Activer Prévisualisation
                gestionPrevisualisationCoup(coupX, coupY, false);
            } else {
                // Configuration.instance().logger().info("Curseur hors zone !\n");
            }
        }
    }

    boolean estPositionSourisCorrect(int coupX, int coupY) {
        return (coupX <= jeu.gaufre().colonnes() * interfaceGraphique.gaufreGraphique.largeurCase()) && (coupY <= jeu.gaufre().lignes() * interfaceGraphique.gaufreGraphique.hauteurCase());
    }

    int conversionCoordonneeVersCases(int coup, Boolean X) {
        if (X) {
            return coup = (coup / interfaceGraphique.gaufreGraphique.largeurCase());
        } else {
            return coup = (coup / interfaceGraphique.gaufreGraphique.hauteurCase());
        }
    }

    void gestionPrevisualisationCoup(int coupX, int coupY, boolean reset) {
        int valeurLargeurPrevisualisation = 0;
        int valeurHauteurPrevisualisation = 0;
        if (!reset) {
            valeurLargeurPrevisualisation = jeu.gaufre().colonnes() - coupX;
            valeurHauteurPrevisualisation = jeu.gaufre().lignes() - coupY;
        } else {
            coupX = 0;
            coupY = 0;
        }
        setPrevisualisationCoup(valeurLargeurPrevisualisation, valeurHauteurPrevisualisation, coupX, coupY);
        previsualisationCoup(coupX, coupY);
    }

    void setPrevisualisationCoup(int valeurLargeurPrevisualisation, int valeurHauteurPrevisualisation, int coupX, int coupY) {
        jeu.setLargeurPrevisualisation(valeurLargeurPrevisualisation);
        jeu.setHauteurPrevisualisation(valeurHauteurPrevisualisation);
        jeu.setPrevisualisationX(coupX);
        jeu.setPrevisualisationY(coupY);
    }

    void previsualisationCoup(int coupX, int coupY) {
        interfaceGraphique.previsualisation(jeu.getJoueurCourant(), coupX, coupY, jeu.largeurPrevisualisation(), jeu.hauteurPrevisualisation());
    }

    void manger(int coupX, int coupY) {
        Coup coup = jeu.creerCoup(coupX, coupY);
        if (coup != null) {
            if (jeu.estTermine()) {
                interfaceGraphique.majInfoPartie();
                jeu.afficherJoueurGagnant();
            } else if (!jeu.estCoupJouable(coupX, coupY)) {
                interfaceGraphique.majInfoPartie();
            }
            jouerCoup(coup);
            jeu.verificationJoueurGagnant();
        }
    }

    void jouerCoup(Coup coup) {
        jeu.jouerCoup(coup);
    }

    void annule() {
        jeu.annule();
        interfaceGraphique.majScore();
        interfaceGraphique.majAnnule();
    }

    void refaire() {
        jeu.refaire();
    }

    void modificationTailleGaufre(int nbLigne, int nbColonne) {
        gestionMajTailleGaufre(nbLigne, nbColonne);
        interfaceGraphique.majTexteTailleGaufre();
    }

    void gestionMajTailleGaufre(int nbLigne, int nbColonne) {
        jeu.modifierTailleGauffre(nbLigne, nbColonne);
        viderHistorique();
        interfaceGraphique.majInfoPartie();
    }

    void majPointScore() {
        jeu.increJ(jeu.getJoueurCourant());
        interfaceGraphique.majScore();
    }

    @Override
    public boolean commande(String commande) {
        switch (commande) {
            case "down":
                modificationTailleGaufre(1, 0);
                break;
            case "up":
                modificationTailleGaufre(-1, 0);
                break;
            case "left":
                modificationTailleGaufre(0, -1);
                break;
            case "right":
                modificationTailleGaufre(0, 1);
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
                gestionMajTailleGaufre(0, 0);
                interfaceGraphique.nouvelle();
                break;
            case "suite":
                if(jeu.estTermine()) {
                    gestionMajTailleGaufre(0, 0);
                } else {
                    jeu.changerJoueurCourant();
                    majPointScore();
                    gestionMajTailleGaufre(0, 0);
                }
                break;
            case "save":
                jeu.sauvegarder();
                break;
            case "load":
                charge();
                interfaceGraphique.majTexteTailleGaufre();
                interfaceGraphique.majScore();
                interfaceGraphique.majJoueurCourant();
                break;
            default:
                return false;
        }
        return true;
    }

    private void viderHistorique() {
        jeu.viderHistorique();
    }

    @Override
    public void fixerInterfaceGraphique(InterfaceGraphique iGraphique) {
        interfaceGraphique = iGraphique;
    }

    public void charge() {
        JFileChooser chooser = new JFileChooser(System.getProperty("user.dir") + File.separator + "res" + File.separator + "Sauvegardes");
        int returnVal = chooser.showOpenDialog(interfaceGraphique.frame);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(null,"Vous n'avez rien selectionne");
            return;
        }
        jeu.charger(chooser.getSelectedFile().getPath());
    }

    public void sauvegarder() {
        jeu.sauvegarder();
    }
}
