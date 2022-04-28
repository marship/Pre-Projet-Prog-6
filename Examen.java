import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.*;

public class Examen implements Runnable {

    MonApplication monApplication;
    JButton nouvellePartie;
    JFrame frame;

    public void run() {

        frame = new JFrame("Puissance 4");
        monApplication = new MonApplication();
        monApplication.demarre(frame, 6, 7);

        Box barreHorizontale = Box.createHorizontalBox();
        nouvellePartie = creerBouton("Nouvelle Partie");
        barreHorizontale.add(nouvellePartie);
        frame.add(barreHorizontale, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(550, 510);
        frame.setVisible(true);
    }

    public JButton creerBouton(String nomBouton) {
        JButton bouton = new JButton(nomBouton);
        bouton.addActionListener(new Adapteur(monApplication, frame));
        bouton.setAlignmentX(Component.CENTER_ALIGNMENT);
        bouton.setFocusable(false);
        return bouton;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Examen());
    }

}
