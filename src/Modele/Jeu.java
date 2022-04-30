package Modele;

import Global.Configuration;
import Patterns.Observable;

public class Jeu extends Observable {

    Gaufre gaufre;
    int joueurGagnant;
    int joueurCourant;
    boolean dejaMangee = false;

    int largeurPrevisualisation;
    int hauteurPrevisualisation;
    int previsualisationX;
    int previsualisationY;

    public Jeu() {
        int nbLignesGaufre = Integer.parseInt(Configuration.instance().lis("Ligne"));
        int nbColonnesGaufre = Integer.parseInt(Configuration.instance().lis("Colonne"));
        gaufre = new Gaufre(nbLignesGaufre, nbColonnesGaufre);
    }

    public Gaufre gaufre() {
        return gaufre;
    }

    public void modifierTailleGauffre(int modifLigne, int modifColonne) {            
        if (lignes() == 1 && modifLigne == -1) {
            Configuration.instance().logger().warning("Reduction de la gaufre sur une ligne impossible\n");
        } else if (colonnes() == 1 && modifColonne == -1) {
            Configuration.instance().logger().warning("Reduction de la gaufre sur une colonne impossible\n");
        } else {
            gaufre.initialisation(lignes() + modifLigne, colonnes() + modifColonne);
        }
    }

    boolean estCoupJouable(int coupX, int coupY) {
        return gaufre.estCoupJouable(coupX, coupY);
    }

    public boolean estDejaMangee() {
        return dejaMangee;
    }

    public void jouerCoup(int coupX, int coupY) {
        dejaMangee = false;
        if (!estTermine() && estCoupJouable(coupX, coupY)) {
            gaufre.jouerCoup(coupX, coupY);
            miseAJour(); // Mise à jour de l'historique des coups
            verificationJoueurGagnant();
        } else if (gaufre.estTermine()) {
            afficherJoueurGagnant();
        } else if (!estCoupJouable(coupX, coupY)) {
            dejaMangee = true;
            Configuration.instance().logger().info("Ce morceau de gaufre a deja ete mangee !\n");
        }
    }

    public int getJoueurCourant() {
        // True = Joueur 1 | False = Joueur 2
        return joueurCourant = gaufre.joueurCourant() ? 1 : 2;
    }

    public void changerJoueurCourant() {
        gaufre.changerJoueur();
    }

    public boolean estTermine() {
        return gaufre.estTermine();
    }

    public void verificationJoueurGagnant() {
        if (estTermine()) {
            joueurGagnant = getJoueurCourant();
            afficherJoueurGagnant();
        }
    }

    public void afficherJoueurGagnant() {
        Configuration.instance().logger().info("La partie est termine ! Joueur " + joueurGagnant + " a gagne !\n");
    }

    public int lignes() {
        return gaufre.lignes();
    }

    public int colonnes() {
        return gaufre.colonnes();
    }

    // ================================
    // ======= PREVISUALISATION =======
    // ================================

    public void setLargeurPrevisualisation(int valeurlargeur) {
        largeurPrevisualisation = valeurlargeur;
    }

    public int largeurPrevisualisation() {
        return largeurPrevisualisation;
    }

    public void setHauteurPrevisualisation(int valeurhauteur) {
        hauteurPrevisualisation = valeurhauteur;
    }

    public int hauteurPrevisualisation() {
        return hauteurPrevisualisation;
    }

    public void setPrevisualisationX(int coupX) {
        previsualisationX = coupX;
    }

    public int previsualisationX() {
        return previsualisationX;
    }

    public void setPrevisualisationY(int coupY) {
        previsualisationY = coupY;
    }

    public int previsualisationY() {
        return previsualisationY;
    }

    // ================================
    // ========== HISTORIQUE ==========
    // ================================

    // TO DO (faire fonctionner)
    public Coup annule() {
        Coup coup = gaufre.annuler();
        miseAJour();
        return coup;
    }

    // TO DO (faire fonctionner)
    public Coup refaire() {
        Coup coup = gaufre.refaire();
        miseAJour();
        return coup;
    }
}
