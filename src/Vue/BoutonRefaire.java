package Vue;

import javax.swing.JButton;

import Modele.Jeu;
import Patterns.Observateur;
import java.awt.Component;

public class BoutonRefaire extends JButton implements Observateur {

    Jeu jeu;

    BoutonRefaire(String string, String commande, CollecteurEvenements cEvenements, Jeu j) {
        super(string);
        jeu = j;
        addActionListener(new AdaptateurCommande(cEvenements, commande));
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setFocusable(false);
        jeu.ajouteObservateur(this);
    }

    @Override
    public void majInfoPartie() {
        setEnabled(jeu.gaufre().peutRefaire());
    }
}
