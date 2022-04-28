import java.awt.Component;

import javax.swing.*;

public class MonApplication {

    Grille grille;
    static VueGrille vueGrille;
    JFrame f;

    public void demarre(JFrame fenetreDuJeu, int nbLignes, int nbColonnes) {
        
        grille = new Grille(nbLignes, nbColonnes);
        vueGrille = new VueGrille(grille);
        vueGrille.addMouseListener(new AdapteurSouris(grille, vueGrille));
        f = fenetreDuJeu;
        fenetreDuJeu.add((Component) vueGrille);

    }

}
