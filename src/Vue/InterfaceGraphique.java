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
import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class InterfaceGraphique implements Runnable, Observateur {

    Jeu jeu;
    CollecteurEvenements collecteurEvenements;
    boolean estMaximise;
    JFrame frame;
    public GaufreGraphique gaufreGraphique;
    JLabel infoJoueurCourant, infoFin, scores, J1L, J2L;
    JButton annuler, refaire, nouvellePartie, suite;
    int J1, J2;

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
        barreLaterale.add(createLabel(" Gaufre Empoisonnée "));
        barreLaterale.add(Box.createGlue());
        infoJoueurCourant = createLabel("Joueur " + jeu.getJoueurCourant() + " doit jouer");
        barreLaterale.add(infoJoueurCourant);
        barreLaterale.add(Box.createGlue());
        infoFin = createLabel(" Partie en cours ... ");
        barreLaterale.add(infoFin);
        scores = createLabel("Scores");
        barreLaterale.add(scores);
        Box barreScores = Box.createHorizontalBox();
        J1L = createLabel("J1 : " + J1);
        barreScores.add(J1L);
        J2L = createLabel("   J2 : " + J2);
        barreScores.add(J2L);
        barreLaterale.add(barreScores);
        suite = creerBouton("Abandon", "suite");
        barreLaterale.add(suite);
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
        nouvellePartie = creerBouton("Nouvelle Partie", "Nouvelle");
        barreLaterale.add(nouvellePartie);
        barreLaterale.add(Box.createGlue());
        frame.add(barreLaterale, BorderLayout.LINE_END);

        Box barreInferieure = Box.createHorizontalBox();
        barreInferieure.add(createLabel("Copyright Groupe 5 - Projet Prog6 - 2022"));
        frame.add(barreInferieure, BorderLayout.SOUTH);

        ((Component) gaufreGraphique).addMouseMotionListener(new AdaptateurSourisMouvement(gaufreGraphique, collecteurEvenements));
        ((Component) gaufreGraphique).addMouseListener(new AdaptateurSouris(gaufreGraphique, collecteurEvenements));
        frame.addKeyListener(new AdaptateurClavier(collecteurEvenements));

        collecteurEvenements.fixerInterfaceUtilisateur(this);

        // Un clic sur le bouton de fermeture clos l'application
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // On fixe la taille et on demarre
        frame.setSize(760, 500);
        frame.setVisible(true);
    }

    public void majScore(){
        if(J1 > J2){
            J1L.setForeground(new Color(104, 186, 118));
            J2L.setForeground(new Color(255,0,0));
        }
        else{
            if(J2 > J1){
                J2L.setForeground(new Color(104, 186, 118));
                J1L.setForeground(new Color(255,0,0));
            }
            else{
                J2L.setForeground(new Color(0,0,0));
                J1L.setForeground(new Color(0,0,0));
            }
        }
        J1L.setText("J1 : " + J1);
        J2L.setText("    J2 : " + J2);
    }

    public void previsualisation(int joueurCourant, int coupX, int coupY, int largeurPreselection, int hauteurPreselection) {
        gaufreGraphique.tracerRectangle(joueurCourant, coupX, coupY, largeurPreselection, hauteurPreselection);
        ((Component) gaufreGraphique).repaint();
    }

    @Override
    public void metAJour() {
        if (jeu.estTermine()) {
            infoFin.setText("Fin de partie !");
            infoJoueurCourant.setText("Joueur " + jeu.getJoueurCourant() + " gagne !");
            incrementeScore();
            majScore();
            suite.setText("Manche Suivante");
        } else {
            suite.setText("Abandon");
            infoFin.setText(" Partie en cours ... ");
            if (jeu.estCoupZoneDejaMangee()) {
                infoJoueurCourant.setText("Joueur " + jeu.getJoueurCourant() + " doit rejouer, case deja mangee !");
            } else {
                infoJoueurCourant.setText("Joueur " + jeu.getJoueurCourant() + " doit jouer");
            }
        }
        annuler.setEnabled(jeu.gaufre().peutAnnuler());
        ((Component) gaufreGraphique).repaint();
    }

    public void nouvelle(){
        J1 = 0;
        J2 = 0;
        majScore();
    }

    public void incrementeScore(){
        if(jeu.getJoueurCourant() == 1){
            J1++;
        }
        else{
            J2++;
        }
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
