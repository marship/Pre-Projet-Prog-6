package Modele;

import Global.Configuration;
import Patterns.Observable;

public class Jeu extends Observable {

    int joueurGagnant;
    int joueurCourant;
    int largeurPrevisualisation;
    int hauteurPrevisualisation;
    int previsualisationX;
    int previsualisationY;
    Gaufre gaufre;
    boolean coupZoneDejaMangee = false;

    public Jeu() {
        int nbLignesGaufre = Integer.parseInt(Configuration.instance().lis("Ligne"));
        int nbColonnesGaufre = Integer.parseInt(Configuration.instance().lis("Colonne"));
        gaufre = new Gaufre(nbLignesGaufre, nbColonnesGaufre);
    }

    public Gaufre gaufre() {
        return gaufre;
    }

    public void modifierTailleGauffre(int modifLigne, int modifColonne) {
        if ((gaufre.lignes() == 1 && modifLigne == -1) || (gaufre.colonnes() == 1 && modifColonne == -1)) {
            Configuration.instance().logger().warning("Reduction de la grille impossible\n");
        } else {
            gaufre.initialisation(gaufre.lignes() + modifLigne, gaufre.colonnes() + modifColonne);
        }
    }

    boolean estCoupJouable(int coupX, int coupY) {
        return gaufre.estCoupJouable(coupX, coupY);
    }

    public boolean estTermine() {
        return gaufre.estTermine();
    }

    public void jouerCoup(int coupX, int coupY) {
        coupZoneDejaMangee = false;
        if ((gaufre.grilleGaufre[coupY][coupX] != -1) && !gaufre.estTermine() && estCoupJouable(coupX, coupY)) {
            gaufre.jouerCoup(coupX, coupY);
            miseAJour();
            verificationJoueurGagnant();
        } else if (gaufre.estTermine()) {
            afficherJoueurGagnant();
        } else if (gaufre.grilleGaufre[coupY][coupX] == -1) {
            Configuration.instance().logger().info("La gaufre est deja mangee !\n");
            coupZoneDejaMangee = true;
            miseAJour();
        }
    }

    public void verificationJoueurGagnant() {
        if (gaufre.estTermine()) {
            // Inversion resultat car changement de joueur s'effectue apres le dernier coup joué
            joueurGagnant = gaufre.joueurCourant() ? 1 : 2;
            afficherJoueurGagnant();
        }
    }

    public boolean estCoupZoneDejaMangee() {
        return coupZoneDejaMangee;
    }

    public int getJoueurCourant() {
        return joueurCourant = gaufre.joueurCourant() ? 1 : 2;
    }

    public void afficherJoueurGagnant() {
        Configuration.instance().logger().info("La partie est termine ! Joueur " + joueurGagnant + " a gagne !\n");
    }

    public Coup annule() {
        Coup coup = gaufre.annuler();
        miseAJour();
        return coup;
    }

    public Coup refaire() {
        Coup coup = gaufre.refaire();
        miseAJour();
        return coup;
    }

    public int largeurPrevisualisation() {
        return largeurPrevisualisation;
    }

    public int hauteurPrevisualisation() {
        return hauteurPrevisualisation;
    }
    public int previsualisationX() {
        return previsualisationX;
    }

    public int previsualisationY() {
        return previsualisationY;
    }

    public void setLargeurPrevisualisation(int valeurlargeur) {
        largeurPrevisualisation = valeurlargeur;
    }

    public void setHauteurPrevisualisation(int valeurhauteur) {
        hauteurPrevisualisation = valeurhauteur;
    }

    public void setPrevisualisationX(int coupX) {
        previsualisationX = coupX;
    }

    public void setPrevisualisationY(int coupY) {
        previsualisationY = coupY;
    }
}
