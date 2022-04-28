import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Adapteur extends JButton implements ActionListener {

    MonApplication monApplication;
    JFrame maFenetre;

    Adapteur(MonApplication mApplication, JFrame frame) {
        monApplication = mApplication;
        maFenetre = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        monApplication.grille.remplirGrille();
        monApplication.f.repaint();
    }
    
}
