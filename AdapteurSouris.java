import java.awt.event.*;

public class AdapteurSouris extends MouseAdapter {

    Grille grille;
    VueGrille vueGrille;

    public AdapteurSouris(Grille g, VueGrille vGrille) {
        grille = g;
        vueGrille = vGrille;
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(grille.fini()){
            System.out.println("Le jeu est fini, J" + grille.quiJoue() + " a gagné !");
            return;
        }
        if(e.getX() > vueGrille.largeurCase() * grille.nbColonneGrille()){
            System.out.println("Clic hors de la zone de jeu !\n");
            return;
        }
        int colonne = e.getX() / vueGrille.largeurCase();
        int ligne = e.getY() / vueGrille.largeurCase();
        if(grille.contenuDeLaGrille(ligne, colonne) == 2){
            System.out.println("La gaufre est déjà mangée !\n");
            return;
        }
        if(grille.contenuDeLaGrille(ligne, colonne) == 1) {
            grille.maj(ligne, colonne);
            grille.changerJoueur();
            vueGrille.repaint();
            return;
        }
        if(grille.contenuDeLaGrille(ligne, colonne) == 0){
            grille.fini = true;
            grille.changerJoueur();
            System.out.println("Le jeu est fini, vous avez perdu. J" + grille.quiJoue() + " a gagné !\n");
            return;
        }
    }
}
