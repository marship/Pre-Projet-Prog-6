package Controleur;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import Global.Configuration;
import Joueur.IA;
import Modele.Coup;
import Modele.Jeu;
import Modele.Position;
import Structures.Iterateur;
import Structures.Sequence;
import Vue.CollecteurEvenements;
import Vue.InterfaceGraphique;

public class ControleurMediateur implements CollecteurEvenements {

    final static double TEMPS_ATTENTE_COUP_IA = 0.5;

    // ============ Jeu ===============
    InterfaceGraphique interfaceGraphique;
    Jeu jeu;

    // ============ IA ================
    IA joueurAutomatique;
    boolean iaActive = false;
    Sequence<Coup> enAttente;

    public ControleurMediateur(Jeu j) {
        jeu = j;
    }

    // ============ Clic Souris ================
    @Override
    public void clicSouris(int coupX, int coupY) {
        if (jeu.estTermine()) {
            interfaceGraphique.afficherMasquerInfoJoueur(3, true);
            Configuration.instance().logger().info("Fin de partie !\n");
        } else {
            if (estPositionSourisCorrect(coupX, coupY)) {
                if (jeu.estDejaMangee(conversionCoordonneeVersCases(coupX, true), conversionCoordonneeVersCases(coupY, false))) {
                    interfaceGraphique.afficherMasquerInfoJoueur(2, true);
                    Configuration.instance().logger().info("Morceau deja mange !\n");
                } else {
                    manger(conversionCoordonneeVersCases(coupX, true), conversionCoordonneeVersCases(coupY, false));
                    miseAJourIHM();
                    gestionIA();
                }
            } else {
                interfaceGraphique.afficherMasquerInfoJoueur(1, true);
                Configuration.instance().logger().info("Coup hors gaufre !\n");
            }
        }
    }

    // ============ Mouvement Souris ================
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

    // TO DO
    private void gestionIA() {
        if(iaActive){
            attendreAvantJouer(TEMPS_ATTENTE_COUP_IA);
            faireJouerIA();
        }
    }

    // TO DO
    private void miseAJourIHM() {
        interfaceGraphique.majJoueurCourant();
        jeu.nbCoupPlus();
        interfaceGraphique.majNbCoup();
    }

    boolean estPositionSourisCorrect(int coupX, int coupY) {
        return (coupX <= jeu.gaufre().colonnes() * interfaceGraphique.gaufreGraphique.largeurCase()) && (coupY <= jeu.gaufre().lignes() * interfaceGraphique.gaufreGraphique.hauteurCase());
    }

    int conversionCoordonneeVersCases(int coup, Boolean estAxeAbcisses) {
        if (estAxeAbcisses) {
            return coup = (coup / interfaceGraphique.gaufreGraphique.largeurCase());
        } else {
            return coup = (coup / interfaceGraphique.gaufreGraphique.hauteurCase());
        }
    }

    void gestionPrevisualisationCoup(int coupX, int coupY, boolean reinitialisation) {
        int valeurLargeurPrevisualisation = 0;
        int valeurHauteurPrevisualisation = 0;
        if (reinitialisation) {
            coupX = 0;
            coupY = 0;
        } else {
            valeurLargeurPrevisualisation = jeu.gaufre().colonnes() - coupX;
            valeurHauteurPrevisualisation = jeu.gaufre().lignes() - coupY;
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
                interfaceGraphique.majDejaMangee(coupX, coupY);
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
        jeu.nbCoupMoins();
        interfaceGraphique.majNbCoup();
    }

    void refaire() {
        jeu.refaire();
        jeu.nbCoupPlus();
        interfaceGraphique.majNbCoup();
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
            case "j1":
                jeu.init_joueurCourant(true);
                break;
            case "j2":
                jeu.init_joueurCourant(false);
                break;
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
                jeu.nbCoupZero();
                interfaceGraphique.majNbCoup();
                break;
            case "ia":
                activationDesactivationIA();
                break;
            case "save":
                jeu.sauvegarder();
                break;
            case "load":
                charge();
                interfaceGraphique.majTexteTailleGaufre();
                interfaceGraphique.majScore();
                interfaceGraphique.majJoueurCourant();
                interfaceGraphique.majNbCoup();
                break;
            case "histoire":
                parcours();
            default:
                return false;
        }
        return true;
    }

    private void parcours() {
        int destination = interfaceGraphique.destination();
        int depart = jeu.nbCoup();
        if(destination < depart){
            while(depart != destination){
                annule();
                depart--;
            }
        }
        else{
            while(depart != destination){
                refaire();
                depart++;
            }
        }
    }

    private void viderHistorique() {
        jeu.viderHistorique();
    }

    @Override
    public void fixerInterfaceGraphique(InterfaceGraphique iGraphique) {
        interfaceGraphique = iGraphique;
    }

    public void activationDesactivationIA() {
        iaActive = !iaActive;
    }

    private void faireJouerIA() {
        if (iaActive && !jeu.estTermine()) {
            utilisationIA();
            interfaceGraphique.majJoueurCourant();
            jeu.nbCoupPlus();
            interfaceGraphique.majNbCoup();
        }
    }

    public void utilisationIA() {
        
        if (joueurAutomatique == null) {
            joueurAutomatique = IA.nouvelle(jeu);
            joueurAutomatique.activeIA();
        }

        if ((enAttente == null) || enAttente.estVide()) {
            enAttente = joueurAutomatique.elaboreCoups();
        }

        if ((enAttente == null) || enAttente.estVide()) {
            Configuration.instance().logger().severe("Bug : l'IA n'a joue aucun coup");
        } else {
            jouerCoup(enAttente.extraitTete());
        }

        if (jeu.estTermine()) {
            joueurAutomatique.finalise();
        }
    }

    private void X() {
        int coupX = 0;
        int coupY = 0;

        Coup coup = null;
        coup = enAttente.extraitTete();

        Iterateur<Position> iterateur = coup.positionBouchee.iterateur();
        while (iterateur.aProchain()) {
            Position position = (Position) iterateur.prochain();
            coupX = position.positionX;
            coupY = position.positionY;
        }
        
        System.out.println("Mange en (" + coupX + ", " + coupY +")");
        jouerCoup(coup);
        // manger(coupX, coupY);
    }

    private void attendreAvantJouer(Double secondes) {
        try {
            Thread.sleep((long) (secondes * 1000));
        } catch (InterruptedException e) {
            Configuration.instance().logger().severe("Bug Timer : " + e + "\n");
            e.printStackTrace();
        }
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
