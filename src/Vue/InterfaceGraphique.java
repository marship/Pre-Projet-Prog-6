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
    public JFrame frame;
    public GaufreGraphique gaufreGraphique;
    JLabel infoJoueurCourantCouleur, infoJoueurCourant, infoFin, scores, J1L, J2L, taille, infoJoueur;
    JButton annuler, refaire, nouvellePartie, suite, save, load;

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
        taille = createLabel("Lignes : " + jeu.gaufre().lignes() + "    Colonnes : " + jeu.gaufre().colonnes());
        barreLaterale.add(taille);
        barreLaterale.add(Box.createGlue());
        Box barreJoueur = Box.createHorizontalBox();
        infoJoueurCourantCouleur = createLabel("Joueur " + jeu.getJoueurCourant());
        infoJoueurCourant = createLabel(" doit jouer");
        infoJoueur = createLabel("Coup hors zone effectue");
        majJoueurCourant();
        barreJoueur.add(infoJoueurCourantCouleur);
        barreJoueur.add(infoJoueurCourant);
        barreLaterale.add(barreJoueur);
        barreLaterale.add(Box.createGlue());
        barreLaterale.add(infoJoueur);
        infoJoueur.setVisible(false);
        barreLaterale.add(Box.createGlue());
        infoFin = createLabel(" Partie en cours ... ");
        scores = createLabel("Scores");
        barreLaterale.add(scores);
        Box barreScores = Box.createHorizontalBox();
        J1L = createLabel("J1 : " + jeu.getJ(1));
        barreScores.add(J1L);
        J2L = createLabel("   J2 : " + jeu.getJ(2));
        barreScores.add(J2L);
        barreLaterale.add(barreScores);
        suite = creerBouton("Abandon", "suite");
        barreLaterale.add(suite);
        barreLaterale.add(Box.createGlue());

        // Annuler / Refaire
        Box annulerRefaire = Box.createHorizontalBox();
        annuler = creerBouton("<", "annule");
        annuler.setEnabled(false);
        // refaire = new BoutonRefaire(">", "refaire", collecteurEvenements, jeu);
        refaire = creerBouton(">", "refaire");
        refaire.setEnabled(false);
        annulerRefaire.add(annuler);
        annulerRefaire.add(refaire);
        barreLaterale.add(annulerRefaire);
        barreLaterale.add(Box.createGlue());

        // Save et Load
        Box sauvegardeCharge = Box.createHorizontalBox();
        save = creerBouton("Sauvegarder", "save");
        load = creerBouton("Charger", "load");
        sauvegardeCharge.add(save);
        sauvegardeCharge.add(load);
        barreLaterale.add(sauvegardeCharge);
        barreLaterale.add(Box.createGlue());

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

        // Garde à jour l'interface graphique du controleur
        collecteurEvenements.fixerInterfaceGraphique(this);

        // Un clic sur le bouton de fermeture clos l'application
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // On fixe la taille et on demarre
        frame.setSize(760, 500);
        frame.setVisible(true);
    }

    public void majScore(){
        if(jeu.getJ(1) > jeu.getJ(2)){
            J1L.setForeground(new Color(104, 186, 118));
            J2L.setForeground(new Color(255,0,0));
        }
        else{
            if(jeu.getJ(1) < jeu.getJ(2)){
                J2L.setForeground(new Color(104, 186, 118));
                J1L.setForeground(new Color(255,0,0));
            }
            else{
                J2L.setForeground(new Color(0,0,0));
                J1L.setForeground(new Color(0,0,0));
            }
        }
        J1L.setText("J1 : " + jeu.getJ(1));
        J2L.setText("    J2 : " + jeu.getJ(2));
    }

    public void majTexteTailleGaufre(){
        taille.setText("Lignes : " + jeu.gaufre().lignes() + "    Colonnes : " + jeu.gaufre().colonnes());
    }

    public void majJoueurCourant() {
        if (jeu.getJoueurCourant() == 1) {
            infoJoueurCourantCouleur.setForeground(Color.MAGENTA);
        } else {
            infoJoueurCourantCouleur.setForeground(Color.BLUE);
        }
    }

    public void previsualisation(int joueurCourant, int coupX, int coupY, int largeurPreselection, int hauteurPreselection) {
        gaufreGraphique.tracerRectangle(joueurCourant, coupX, coupY, largeurPreselection, hauteurPreselection);
        ((Component) gaufreGraphique).repaint();
    }

    @Override
    public void majInfoPartie() {
        infoJoueur.setVisible(false);
        if (jeu.estTermine()) {
            infoFin.setText("Fin de partie !");
            infoJoueurCourantCouleur.setText("Joueur " + jeu.getJoueurCourant());
            infoJoueurCourant.setText(" gagne !");
            incrementeScore();
            majScore();
            suite.setText("Manche Suivante");
        } else {
            infoJoueurCourantCouleur.setText("Joueur " + jeu.getJoueurCourant());
            infoJoueurCourant = createLabel(" doit jouer");
            suite.setText("Abandon");
            infoFin.setText(" Partie en cours ... ");
        }
        majJoueurCourant();
        System.out.println(jeu.getJoueurCourant());
        annuler.setEnabled(jeu.gaufre().peutAnnuler());
        refaire.setEnabled(jeu.gaufre().peutRefaire());
        ((Component) gaufreGraphique).repaint();
        frame.repaint();
    }

    public void nouvelle(){
        jeu.scoresZero();
        majScore();
    }

    public void incrementeScore(){
        jeu.increJ(jeu.getJoueurCourant());
    }

    public void majAnnule() {
        infoJoueurCourantCouleur.setText("Joueur " + jeu.getJoueurCourant());
        afficherActualiserInfoJeu();
    }

    public void majHorsGaufre() {
        infoJoueur.setText("Coup hors zone effectue");
        afficherActualiserInfoJeu();
    }

    public void majDejaMangee() {
        infoJoueur.setText("Morceau deja mangee !");
        afficherActualiserInfoJeu();
    }

    public void majFinPartie() {
        infoJoueur.setText("Fin de la partie !");
        afficherActualiserInfoJeu();
    }

    private void afficherActualiserInfoJeu() {
        infoJoueur.setVisible(true);
        frame.repaint();
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
