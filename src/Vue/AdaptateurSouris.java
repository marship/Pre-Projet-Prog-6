package Vue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdaptateurSouris extends MouseAdapter {
    GaufreGraphique gaufreGraphique;
    CollecteurEvenements collecteurEvenements;

    AdaptateurSouris(GaufreGraphique gGraphique, CollecteurEvenements cEvenements) {
        gaufreGraphique = gGraphique;
        collecteurEvenements = cEvenements;
    }

    @Override
    public void mousePressed(MouseEvent e) {

        int coupX = e.getX();
        int coupY = e.getY();
        collecteurEvenements.clicSouris(coupX, coupY);
    }

}
