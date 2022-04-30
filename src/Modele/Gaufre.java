package Modele;

public class Gaufre extends Historique<Coup> {

    int nbLigneGaufre;
    int nbColonnesGaufre;
    public boolean joueurCourant = true;   // True : Joueur 1 | False : Joueur 2

    //  0 : Morceau de gaufre mangeable
    //  1 : Morceau de gaufre empoisonné
    // -1 : Morceau de gaufre déjà mangé
    public int[][] grilleGaufre;
    
    public Gaufre(int ligne, int colonne) {
        initialisation(ligne, colonne);
    }

    void initialisation(int ligne, int colonne) {
        nbLigneGaufre = ligne;
        nbColonnesGaufre = colonne;
        joueurCourant = true;
        grilleGaufre = new int[nbLigneGaufre][nbColonnesGaufre];

        for (int i = 0; i < lignes(); i++) {
            for (int j = 0; j < colonnes(); j++) {
                    grilleGaufre[i][j] = 0;
            }
        }
        grilleGaufre[0][0] = 1;
    }

    public boolean estCoupJouable(int coupX, int coupY) {
        return grilleGaufre[coupY][coupX] != -1;
    }

    public void jouerCoup(int coupX, int coupY) {
        for (int i = coupY; i < grilleGaufre.length; i++) {
            for (int j = coupX; (j < grilleGaufre[i].length) && (grilleGaufre[i][j] != -1); j++) {
                grilleGaufre[i][j] = -1;
            }
        }
        changerJoueur();
    }

    public boolean joueurCourant() {
        return joueurCourant;
    }

    void changerJoueur(){
        joueurCourant = !joueurCourant;
    }

    public boolean estTermine() {
        return grilleGaufre[0][0] != 1;
    }

    public int lignes() {
        return nbLigneGaufre;
    }

    public int colonnes() {
        return nbColonnesGaufre;
    }

    public void afficherGaufre() {
        for (int i = 0; i < grilleGaufre.length; i++) {
            for (int j = 0; j < grilleGaufre[i].length; j++) {
                System.out.print(grilleGaufre[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    // ================================
    // ========== HISTORIQUE ==========
    // ================================

    void ajoutHistorique(Coup coup) {
        coup.fixerGaufre(this);
        nouveau(coup);
    }
}
