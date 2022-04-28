import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class VueGrille extends JComponent {
    
    Graphics2D drawable;
    int largeur;
    int hauteur;

    Grille grille;
    int largeurCase;
    int hauteurCase;
    Random r;

    public VueGrille(Grille g) {
        grille = g;
    }

    @Override
    public void paintComponent(Graphics g) {
        drawable = (Graphics2D) g;
        largeur = getSize().width;
        hauteur = getSize().height;
        drawable.clearRect(0, 0, largeur, hauteur);
        r = new Random();
        tracerGrille();
        tracerJeu();
    }

    public void tracerJeu(){
        int i = 0;
        int j = 0;
        while( i < grille.nbLigneGrille()){
            j = 0;
            while( j < grille.nbColonneGrille()){
                switch (grille.contenuDeLaGrille(i,j)) {
                    case 0:
                        tracerGauffe(j * largeurCase, i * largeurCase, largeurCase, hauteurCase);
                        tracerPoison(j * largeurCase, i * largeurCase, largeurCase, hauteurCase);
                        break;

                    case 1:
                        tracerGauffe(j * largeurCase, i * largeurCase, largeurCase, hauteurCase);
                        break;

                    case 2:
                        tracerVide(j * largeurCase, i * largeurCase, largeurCase, hauteurCase);
                        break;
                
                    default:
                        break;
                }
                j++;
            }
            i++;
        }
    }

    public void tracerGrille() {
        largeurCase = largeur / grille.nbColonneGrille();
        hauteurCase = hauteur / grille.nbLigneGrille();

        largeurCase = Math.min(largeurCase, hauteurCase);
        hauteurCase = largeurCase;

        int i, j;
        int x = 0;
        int y = 0;
        int tailleColonnes = hauteurCase * grille.nbLigneGrille();
        int tailleLignes = largeurCase * grille.nbColonneGrille();
        
        for (i = 0; i <= grille.nbColonneGrille(); i++) {
            x = i * largeurCase;
            drawable.drawLine(x, 0, x, tailleColonnes);
        }
        x = 0;
        for (j = 0; j <= grille.nbLigneGrille(); j++) {
            y = j * hauteurCase;
            drawable.drawLine(0, y, tailleLignes, y);
        }
    }

    public void tracerGauffe(int x, int y, int largeurCase, int hauteurCase){
        drawable.setColor(new Color(189,108,51));
        drawable.fillRect(x, y, largeurCase, hauteurCase);
        drawable.setColor(new Color(0,0,0));
        drawable.drawRect(x, y, largeurCase, hauteurCase);
    }

    public void tracerPoison(int x, int y, int largeurCase, int hauteurCase){
        drawable.setColor(new Color(0,255,0));
        drawable.fillOval(x, y, largeurCase, hauteurCase);
    }

    public void tracerVide(int x, int y, int largeurCase, int hauteurCase){
        drawable.setColor(new Color(255,255,255));
        drawable.fillRect(x, y, largeurCase, hauteurCase);
    }

    public int largeurCase() {
        return largeurCase;
    }

    public int hauteurCase() {
        return hauteurCase;
    }

    public int largeur() {
        return largeur;
    }

    public int hauteur() {
        return hauteur;
    }

}
