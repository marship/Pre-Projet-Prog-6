package Vue;

import javax.swing.JComponent;

import Modele.Jeu;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.BasicStroke;

public class GaufreGraphiqueSwing extends JComponent implements GaufreGraphique {

    private static final int EPAISSEUR_BORDURE = 5;
    int largeur, hauteur;
    Graphics2D drawable;
    VueGaufre vueGaufre;

    GaufreGraphiqueSwing(Jeu jeu) {
        vueGaufre = new VueGaufre(jeu, this);
    }

    @Override
    public void paintComponent(Graphics g) {

        // Graphics 2D est le vrai type de l'objet passé en paramètre
        // Le cast permet d'avoir acces a un peu plus de primitives de dessin
        drawable = (Graphics2D) g;

        // On reccupere quelques infos provenant de la partie JComponent
        largeur = getSize().width;
        hauteur = getSize().height;

        // On efface tout
        drawable.clearRect(0, 0, largeur, hauteur);

        vueGaufre.tracerGaufre();
        vueGaufre.tracerPrevisualisation();
    }

    @Override
    public int largeur() {
        return largeur;
    }

    @Override
    public int hauteur() {
        return hauteur;
    }

    @Override
    public int largeurCase() {
        return vueGaufre.largeurCase();
    }

    @Override
    public int hauteurCase() {
        return vueGaufre.hauteurCase();
    }

    @Override
    public void tracerImage(ImageGaufre imageGaufre, int x, int y, int largeurCase, int hauteurCase) {
        drawable.drawImage(imageGaufre.image(), x, y, largeurCase, hauteurCase, null);
    }

    public void tracerTrait(int x, int y, int largeurCase, int hauteurCase) {
        drawable.setColor(new Color(0,0,0));
        drawable.setStroke(new BasicStroke(EPAISSEUR_BORDURE));
        drawable.drawLine(x, y, largeurCase, hauteurCase);
    }

    @Override
    public void tracerRectangle(int joueurCourant, int x, int y, int largeurCase, int hauteurCase) {
        drawable.setStroke(new BasicStroke(EPAISSEUR_BORDURE));
        if (joueurCourant == 1) {
            drawable.setColor(Color.MAGENTA);
        } else {
            drawable.setColor(Color.BLUE);
        }
        drawable.drawRect(x * largeurCase(), y * hauteurCase(), largeurCase * largeurCase(), hauteurCase * hauteurCase());
    }
}
