public class Grille {
    int [][] contenuDeLaGrille; // 0 = Poison, 1 = Gaufre, 2 = Gaufre mangée
    int nbLigneGrille;
    int nbColonneGrille;
    int quiJoue;
    boolean fini;

    Grille(int nbLignes, int nbColonnes) {
        contenuDeLaGrille = new int[nbLignes][nbColonnes];
        nbLigneGrille = nbLignes;
        nbColonneGrille = nbColonnes;
        quiJoue = 1;
        remplirGrille();
    }

    void changerJoueur(){
        if(quiJoue == 1){
            quiJoue = 2;
        }
        else{
            quiJoue = 1;
        }
    }

    void remplirGrille(){
        int i = 0;
        int j = 0;
        while(i < nbLigneGrille){
            j = 0;
            while(j < nbColonneGrille){
                contenuDeLaGrille[i][j] = 1;
                j++;
            }
            i++;
        }
        contenuDeLaGrille[0][0] = 0;
        quiJoue = 1;
        fini = false;
    }

    boolean coupPossible(int l, int c){
        return contenuDeLaGrille[l][c] == 1;
    }

    void maj(int l, int c){
        int i = l;
        int j = c;
        while( i != nbLigneGrille){
            j = c;
            while(j != nbColonneGrille){
                contenuDeLaGrille[i][j] = 2;
                j++;
            }
            i++;
        }
    }

    boolean fini(){
        return fini;
    }

    int contenuDeLaGrille(int l, int c){
        return contenuDeLaGrille[l][c];
    }

    int nbLigneGrille() {
        return nbLigneGrille;
    }

    int nbColonneGrille() {
        return nbColonneGrille;
    }

    int quiJoue(){
        return quiJoue;
    }
}
