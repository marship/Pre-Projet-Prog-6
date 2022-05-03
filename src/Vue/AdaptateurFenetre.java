package Vue;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AdaptateurFenetre extends WindowAdapter {

    InterfaceGraphique interfaceGraphique;

    AdaptateurFenetre(InterfaceGraphique iGraphique) {
        interfaceGraphique = iGraphique;
    }

    @Override
    public void windowClosed(WindowEvent e) {
        interfaceGraphique.frame.setEnabled(true);
    }
}
