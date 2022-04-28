package Modele;

import Global.Configuration;
import Patterns.Observable;

public class Jeu extends Observable {

    Gaufre gaufre;

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
            Configuration.instance().logger().info("Reduction de la grille impossible\n");
        } else {
            gaufre.initialisation(gaufre.lignes() + modifLigne, gaufre.colonnes() + modifColonne);
        }
    }

    boolean estCoupJouable(int coupX, int coupY) {
        return gaufre.estCoupJouable(coupX, coupY);
    }

    public void jouerCoup(int coupX, int coupY) {
        if ((gaufre.grilleGaufre[coupY][coupX] != -1) && !gaufre.estTermine() && estCoupJouable(coupX, coupY)) {
            gaufre.jouerCoup(coupX, coupY);
            miseAJour();
        } else if (gaufre.estTermine()) {
            int joueurGagnant = gaufre.joueurCourant() ? 2 : 1;
            Configuration.instance().logger().info("Le jeu est fini, Joueur " + joueurGagnant + " a gagne !\n");
        } else if (gaufre.grilleGaufre[coupY][coupX] == -1) {
            Configuration.instance().logger().info("La gaufre est deja mangee !\n");
        }
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
}
