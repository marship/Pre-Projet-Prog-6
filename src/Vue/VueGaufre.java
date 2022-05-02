package Vue;

import java.io.File;
import java.io.InputStream;

import Global.Configuration;
import Modele.Gaufre;
import Modele.Jeu;

public class VueGaufre {

    Jeu jeu;
    ImageGaufre imageGaufre;
    ImageGaufre imageGaufrePoison;
    ImageGaufre imageSol;
    ImageGaufre imageAide;
    GaufreGraphique gaufreGraphique;
    int largeurCase;
    int hauteurCase;
    Gaufre gaufre;

    public VueGaufre(Jeu j, GaufreGraphique gGraphique) {
        imageSol = chargeImage("sol");
        imageGaufre = chargeImage("gaufre");
        imageGaufrePoison = chargeImage("gaufrePoison");
        imageAide = chargeImage("gaufreAide");
        jeu = j;
        gaufreGraphique = gGraphique;
        gaufre = jeu.gaufre();
    }

    private ImageGaufre chargeImage(String nom) {
        InputStream in = Configuration.charge("Images" + File.separator + nom + ".png");
        return ImageGaufre.getImageGaufre(in);
    }

    public int largeurCase() {
        return largeurCase;
    }

    public int hauteurCase() {
        return hauteurCase;
    }

    public boolean masquerPrevisualisation() {
        return masquerPrevisualisationDebut() || masquerPrevisualisationFin();
    }

    public boolean masquerPrevisualisationDebut() {
        return jeu.estAuDebut();
    }

    public boolean masquerPrevisualisationFin() {
        return jeu.estTermine();
    }

    public void tracerPrevisualisation() {
        gaufreGraphique.tracerRectangle(jeu.getJoueurCourant(), jeu.previsualisationX(), jeu.previsualisationY(), jeu.largeurPrevisualisation(), jeu.hauteurPrevisualisation());
    }

    public void tracerGaufre() {

        largeurCase = gaufreGraphique.largeur() / gaufre.colonnes();
        hauteurCase = gaufreGraphique.hauteur() / gaufre.lignes();

        largeurCase = Math.min(largeurCase, hauteurCase);
        hauteurCase = largeurCase;

        int i = 0;
        int j = 0;
        while( i < gaufre.lignes()){
            j = 0;
            while( j < gaufre.colonnes()){
                switch (gaufre.grilleGaufre[i][j]) {
                    case 0:
                        gaufreGraphique.tracerImage(imageGaufre, j * largeurCase, i * largeurCase, largeurCase, hauteurCase);
                        break;

                    case 1:
                        gaufreGraphique.tracerImage(imageGaufrePoison, j * largeurCase, i * largeurCase, largeurCase, hauteurCase);
                        break;

                    case 3:
                    case 2:
                    case -1:
                        gaufreGraphique.tracerImage(imageSol, j * largeurCase, i * largeurCase, largeurCase, hauteurCase);
                        break;
                    case 4:
                        gaufreGraphique.tracerImage(imageAide, j * largeurCase, i * largeurCase, largeurCase, hauteurCase);
                        break;
                
                    default:
                        break;
                }
                j++;
            }
            i++;
        }
    }
}
