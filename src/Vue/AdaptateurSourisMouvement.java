package Vue;

import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

public class AdaptateurSourisMouvement implements MouseMotionListener {

    GaufreGraphique gaufreGraphique;
    CollecteurEvenements collecteurEvenements;

    public AdaptateurSourisMouvement(GaufreGraphique gGraphique, CollecteurEvenements cEvenements) {
        gaufreGraphique = gGraphique;
        collecteurEvenements = cEvenements;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // Pas Nécéssaire
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int coupX = e.getX();
        int coupY = e.getY();
        collecteurEvenements.traqueSouris(coupX, coupY);
    }
    
}
