package Vue;

public interface GaufreGraphique {

    int largeur();
    int hauteur();

    int largeurCase();
    int hauteurCase();

    void tracerImage(ImageGaufre imageGaufre, int x, int y, int largeurCase, int hauteurCase);
    void tracerTrait(int x, int y, int largeurCase, int hauteurCase);
    void tracerRectangle(int joueurCourant, int coupX, int coupY, int largeurPreselection, int hauteurPreselection);
}
