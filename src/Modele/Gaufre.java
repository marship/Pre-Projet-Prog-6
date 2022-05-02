package Modele;

import Global.Configuration;
import Structures.Sequence;

public class Gaufre extends Historique<Coup> implements Cloneable {

    int nbLigneGaufre;
    int nbColonnesGaufre;
    public boolean joueurCourant = true;   // True : Joueur 1 | False : Joueur 2

    //  0 : Morceau de gaufre mangeable
    //  1 : Morceau de gaufre empoisonné
    // -1 : Morceau de gaufre déjà mangé
    //  2 : Morceau de gaufre empoisonné mangé
    //  3 : Morceau de gaufre sélectionné
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

    public boolean estDejaMangee(int coupX, int coupY) {
        return ((grilleGaufre[coupY][coupX] == -1) || (grilleGaufre[coupY][coupX] == 2) || (grilleGaufre[coupY][coupX] == 3));
    }

    public boolean estCoupJouable(int coupX, int coupY) {
        return ((grilleGaufre[coupY][coupX] == 0) || (grilleGaufre[coupY][coupX] == 1));
    }
    
    public Coup creerCoup(int coupX, int coupY) {
        Coup resultat = new Coup();
        if (!estTermine() && estCoupJouable(coupX, coupY)) {
            resultat.mange(coupX, coupY);
            return resultat;
        } else {
            return null;
        }
    }

    public void jouerCoup(Coup coup) {
        coup.fixerGaufre(this);
        nouveau(coup);
    }

    public void jouerCoupGaufre(int positionX, int positionY) {
        ajoutPrevisualisation(positionX, positionY);
        if(grilleGaufre[positionY][positionX] == 1) {
            grilleGaufre[positionY][positionX] = 2;
        } else {
            grilleGaufre[positionY][positionX] = 3;
        }
        changerJoueur();
    }

    public void dejouerCoupGaufre(int positionX, int positionY) {
        // Annulation de la case sélectionnée
        if(grilleGaufre[positionY][positionX] == 2) {
            grilleGaufre[positionY][positionX] = 1;
        } else {
            grilleGaufre[positionY][positionX] = 0;
        }

        // Reset des zones mangées sauf les cases sélectionnées
        for (int i = 0; i < grilleGaufre.length; i++) {
            for (int j = 0; j < grilleGaufre[i].length; j++) {
                if(grilleGaufre[i][j] == -1) {
                    grilleGaufre[i][j] = 0;
                }
            }
        }

        // Retracer les prévisualisation précédentes grâce aux cases sélectionnées
        for (int i = 0; i < grilleGaufre.length; i++) {
            for (int j = 0; j < grilleGaufre[i].length; j++) {
                if(grilleGaufre[i][j] == 3) {
                    ajoutPrevisualisation(j, i);
                }
            }
        }
        changerJoueur();
    }

    private void ajoutPrevisualisation(int positionX, int positionY) {
        for (int i = positionY; i < grilleGaufre.length; i++) {
            for (int j = positionX; (j < grilleGaufre[i].length) && (grilleGaufre[i][j] != -1); j++) {
                if(grilleGaufre[i][j] == 0) {
                    grilleGaufre[i][j] = -1;
                }
            }
        }
    }

    public boolean joueurCourant() {
        return joueurCourant;
    }

    void changerJoueur(){
        joueurCourant = !joueurCourant;
    }

    public boolean estAuDebut() {
        boolean estAuDebut = true;
        for (int i = 0; i < grilleGaufre.length; i++) {
            for (int j = 0; j < grilleGaufre[i].length; j++) {
                if((grilleGaufre[i][j] != 0) && (grilleGaufre[i][j] != 1)) {
                    estAuDebut = false;
                    return estAuDebut;
                }
            }
        }
        return estAuDebut;
    }
    
    public void init_joueurCourant(boolean p) {
        joueurCourant = p;
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

    public int tailleHistoire(){
        int res = 0;
        Sequence<Coup> liste = Configuration.instance().nouvelleSequence();
        while( !passe.estVide() ){
            liste.insereQueue(passe.extraitTete());
            res++;
        }
        while( !liste.estVide() ){
            passe.insereQueue(liste.extraitTete());
        }
        
        liste = Configuration.instance().nouvelleSequence();
        while( !futur.estVide() ){
            liste.insereQueue(futur.extraitTete());
            res++;
        } 
        while( !liste.estVide() ){
            futur.insereQueue(liste.extraitTete());
        }
        return res;
    }

    // ================================
    // ============ IA ================
    // ================================

    @Override
	public Gaufre clone() {
		try {
			Gaufre resultat = (Gaufre) super.clone();

			//Copie du tableau de cases
			resultat.grilleGaufre = new int[this.grilleGaufre.length][this.grilleGaufre[0].length];

			int i = 0;
			while(i < this.grilleGaufre.length)
			{
				resultat.grilleGaufre[i] = this.grilleGaufre[i].clone();

				i = i + 1;
			}
            resultat.passe = Configuration.instance().nouvelleSequence();

			return resultat;
		} catch (CloneNotSupportedException e) {

		}
		return null;
	}

    public String hash(){
        String res = "" + joueurCourant;
        int i = 0;
        int j = 0;
        while(i < nbLigneGaufre){
            while(j < nbColonnesGaufre){
                if(grilleGaufre[i][j] == 3){
                    res += -1;
                }
                else{
                    res += grilleGaufre[i][j];
                }
                j++;
            }
            j = 0;
            i++;
        }
        
        return res;
    }



}
