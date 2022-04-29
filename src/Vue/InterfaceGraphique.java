package Vue;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import Modele.Jeu;
import Patterns.Observateur;

import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class InterfaceGraphique implements Runnable, Observateur {

    Jeu jeu;
    CollecteurEvenements collecteurEvenements;
    boolean estMaximise;
    JFrame frame;
    public GaufreGraphique gaufreGraphique;
    JLabel nbPas, nbPoussees;
    JButton annuler, refaire;

    InterfaceGraphique(Jeu j, CollecteurEvenements cEvenements) {
        jeu = j;
        jeu.ajouteObservateur(this);
        collecteurEvenements = cEvenements;
    }

    public static void demarrer(Jeu j, CollecteurEvenements cEvenements) {
        SwingUtilities.invokeLater(new InterfaceGraphique(j, cEvenements));
    }

    private JLabel createLabel(String nomDuLabel) {
        JLabel label = new JLabel(nomDuLabel);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JButton creerBouton(String string, String commande) {
        JButton bouton = new JButton(string);
        bouton.addActionListener(new AdaptateurCommande(collecteurEvenements, commande));
        bouton.setAlignmentX(Component.CENTER_ALIGNMENT);
        bouton.setFocusable(false);
        return bouton;
    }

    @Override
    public void run() {
        // Creation d'une fenetre
        frame = new JFrame("Gaufre Empoisonnée");

        // Ajout de notre composant de dessin dans la fenetre
        gaufreGraphique = new GaufreGraphiqueSwing(jeu);
        frame.add((Component) gaufreGraphique);

        // Box
        Box barreLaterale = Box.createVerticalBox();
        barreLaterale.add(createLabel("Gaufre Empoisonnée"));
        barreLaterale.add(Box.createGlue());

        // Annuler / Refaire
        Box annulerRefaire = Box.createHorizontalBox();
        annuler = creerBouton("<", "annule");
        annuler.setEnabled(false);
        refaire = new BoutonRefaire(">", "refaire", collecteurEvenements, jeu);
        refaire.setEnabled(false);
        annulerRefaire.add(annuler);
        annulerRefaire.add(refaire);
        barreLaterale.add(annulerRefaire);

        barreLaterale.add(Box.createGlue());
        barreLaterale.add(createLabel("Copyright Groupe 5 - Projet Prog6 - 2022"));
        frame.add(barreLaterale, BorderLayout.LINE_END);

        ((Component) gaufreGraphique).addMouseListener(new AdaptateurSouris(gaufreGraphique, collecteurEvenements));
        frame.addKeyListener(new AdaptateurClavier(collecteurEvenements));

        collecteurEvenements.fixerInterfaceUtilisateur(this);

        // Un clic sur le bouton de fermeture clos l'application
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // On fixe la taille et on demarre
        frame.setSize(800, 500);
        frame.setVisible(true);
    }

    @Override
    public void metAJour() {
        annuler.setEnabled(jeu.gaufre().peutAnnuler());
        ((Component) gaufreGraphique).repaint();
    }

    public void basculePleinEcran() {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = env.getDefaultScreenDevice();
		if (estMaximise) {
			device.setFullScreenWindow(null);
			estMaximise = false;
		} else {
			device.setFullScreenWindow(frame);
			estMaximise = true;
		}
        frame.repaint();
	}
    
}
