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

    // ============ Jeu ===============
    InterfaceGraphique interfaceGraphique;
    Jeu jeu;

    // ============ IA ================
    IA joueurAutomatique, aide;
    boolean iaActive = false;
    Sequence<Coup> enAttente, aideAttente;
    final static double TEMPS_ATTENTE_COUP_IA = 0.5;
    
    public ControleurMediateur(Jeu j) {
        jeu = j;
    }

    // ============ Clic Souris ================
    @Override
    public void clicSouris(int coupX, int coupY) {
        if (jeu.estTermine()) {
            interfaceGraphique.afficherMasquerInfoJoueur(0, false);
            Configuration.instance().logger().info("Fin de partie !\n");
        } else {
            if (estPositionSourisCorrect(coupX, coupY)) {
                if (jeu.estDejaMangee(conversionCoordonneeVersCases(coupX, true), conversionCoordonneeVersCases(coupY, false))) {
                    interfaceGraphique.afficherMasquerInfoJoueur(2, true);
                    Configuration.instance().logger().info("Morceau deja mange !\n");
                } else {
                    manger(conversionCoordonneeVersCases(coupX, true), conversionCoordonneeVersCases(coupY, false));
                    miseAJourIHM();
                    // TO DO Cas IA Premier
                    gestionIA();
                }
            } else {
                interfaceGraphique.afficherMasquerInfoJoueur(1, true);
                Configuration.instance().logger().info("Coup hors gaufre !\n");
            }
        }
    }

    private void gestionIA() {
        if(iaActive){
            attendreAvantJouer(TEMPS_ATTENTE_COUP_IA);
            faireJouerIA();
        }
    }

    private void miseAJourIHM() {
        interfaceGraphique.miseAJourCouleurJoueurCourant(0, 0, false);
        jeu.nbCoupPlus();
        interfaceGraphique.miseAJourNbCoup();
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
                interfaceGraphique.miseAJourCouleurJoueurCourant(2, 2, true);
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
        interfaceGraphique.miseAJourTableauScore();
        interfaceGraphique.miseAJourCouleurJoueurCourant(1, 0, false);
        jeu.nbCoupMoins();
        interfaceGraphique.miseAJourNbCoup();
    }

    void refaire() {
        jeu.refaire();
        jeu.nbCoupPlus();
        interfaceGraphique.miseAJourNbCoup();
    }

    void modificationTailleGaufre(int nbLigne, int nbColonne) {
        gestionMajTailleGaufre(nbLigne, nbColonne);
        interfaceGraphique.miseAJourInfoTailleGaufre();
    }

    void gestionMajTailleGaufre(int nbLigne, int nbColonne) {
        jeu.modifierTailleGauffre(nbLigne, nbColonne);
        viderHistorique();
        interfaceGraphique.majInfoPartie();
    }

    void majPointScore() {
        jeu.incrementerScoreJoueur(jeu.getJoueurCourant());
        interfaceGraphique.miseAJourTableauScore();
    }

    void init_adversaire(){
        String adversaire_choisi = interfaceGraphique.get_adversaire();
        switch(adversaire_choisi){
            case "Joueur2":
                iaActive = false;
                break;
            case "IAAleatoire":
                joueurAutomatique = IA.nouvelle(jeu, adversaire_choisi);
                joueurAutomatique.activeIA();
                iaActive = true;
                break;
            case "IAEtOu":
                joueurAutomatique = IA.nouvelle(jeu, adversaire_choisi);
                joueurAutomatique.activeIA();
                iaActive = true;
                break;
            case "IAGP":
                joueurAutomatique = IA.nouvelle(jeu, adversaire_choisi);
                joueurAutomatique.activeIA();
                iaActive = true;
                break;
            default :
                joueurAutomatique = IA.nouvelle(jeu);
                joueurAutomatique.activeIA();
                iaActive = true;
                break;
        }
    } 

    @Override
    public boolean commande(String commande) {
        switch (commande) {
            case "commencer":
                init_adversaire();
                jeu.init_joueurCourant(interfaceGraphique.get_joueurCourant());
                gestionMajTailleGaufre(0, 0);
                interfaceGraphique.nouvellePartie();
                interfaceGraphique.fermer_PopUp();
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
                jeu.gaufre().supprimerAide();
                annule();
                break;
            case "refaire":
                jeu.gaufre().supprimerAide();
                refaire();
                break;
            case "fullscreen":
                interfaceGraphique.basculePleinEcran();
                break;
            case "Nouvelle":
                gestionMajTailleGaufre(0, 0);
                interfaceGraphique.nouvellePartie();
                interfaceGraphique.ouvrir_PopUp();
                break;
            case "suite":
                if(jeu.estTermine()) {
                    gestionMajTailleGaufre(0, 0);
                } else {
                    jeu.changerJoueurCourant();
                    majPointScore();
                    gestionMajTailleGaufre(0, 0);
                }
                jeu.miseAZeroNbCoup();
                interfaceGraphique.miseAJourNbCoup();
                break;
            case "ia":
                activationDesactivationIA();
                break;
            case "save":
                jeu.sauvegarder();
                break;
            case "load":
                charge();
                interfaceGraphique.miseAJourInfoTailleGaufre();
                interfaceGraphique.miseAJourTableauScore();
                interfaceGraphique.miseAJourCouleurJoueurCourant(0, 0, false);
                interfaceGraphique.miseAJourNbCoup();
                break;
            case "histoire":
                jeu.gaufre().supprimerAide();
                parcours();
                break;
            case "aide":
                jeu.gaufre().supprimerAide();
                utilisationIA(2);
                break;
            default:
                return false;
        }
        return true;
    }

    private void parcours() {
        int destination = interfaceGraphique.destinationNavigationHistorique();
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
            utilisationIA(1);
            interfaceGraphique.miseAJourCouleurJoueurCourant(0, 0, false);
            jeu.nbCoupPlus();
            interfaceGraphique.miseAJourNbCoup();
        }
    }

    public void utilisationIA(int option) {

        if(option == 1){
        
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
        else{
            Sequence<Coup> s = Configuration.instance().nouvelleSequence();
            while(!jeu.gaufre().futur.estVide()){
                s.insereQueue(jeu.gaufre().futur.extraitTete());
            }
            aide = IA.nouvelle(jeu);
            aide.activeIA();
            if ((aideAttente == null) || aideAttente.estVide()) {
                aideAttente = aide.elaboreCoups();
            }

            if ((aideAttente == null) || aideAttente.estVide()) {
                Configuration.instance().logger().severe("Bug : l'IA n'a joue aucun coup");
            } else {
                Iterateur<Position> iterateur = aideAttente.extraitTete().positionBouchee.iterateur();
                int coupX = 0, coupY = 0;
                while (iterateur.aProchain()) {
                    Position position = (Position) iterateur.prochain();
                    coupX = position.positionX;
                    coupY = position.positionY;
                }
                System.out.println("" + coupX + " " + coupY);
                jeu.gaufre().grilleGaufre[coupY][coupX] = 4;
                interfaceGraphique.frame.repaint();
            }

            if (jeu.estTermine()) {
                aide.finalise();
            }
            while(!s.estVide()){
                jeu.gaufre().futur.insereQueue(s.extraitTete());
            }
        }
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
