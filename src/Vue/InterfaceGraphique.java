package Vue;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import Global.Configuration;
import Modele.Jeu;
import Patterns.Observateur;

import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class InterfaceGraphique implements Runnable, Observateur {

    // ============ Jeu ===============
    CollecteurEvenements collecteurEvenements;
    Jeu jeu;

    // =========== Frame ==============
    public JFrame frame;
    boolean estMaximise;
    
    // ========= JComponent ===========
    JLabel infoJoueurCourantCouleur, infoJoueurCourant, infoFin, scores, J1L, J2L, taille, infoJoueur, nbCoup, copyright;
    JButton annuler, refaire, nouvellePartie, suite, save, load, histoire, joueur_un, joueur_deux, aide;
    public GaufreGraphique gaufreGraphique;
    JComboBox<Integer> listeEtapes;

    InterfaceGraphique(Jeu j, CollecteurEvenements cEvenements) {
        jeu = j;
        jeu.ajouteObservateur(this);
        collecteurEvenements = cEvenements;
    }

    public static void demarrer(Jeu j, CollecteurEvenements cEvenements) {
        SwingUtilities.invokeLater(new InterfaceGraphique(j, cEvenements));
    }

    // Création JLabel
    private JLabel createLabel(String nomDuLabel, boolean estOpaque) {
        JLabel label = new JLabel(nomDuLabel);
        if (estOpaque) {
            label.setOpaque(true);
            label.setBackground(Color.WHITE);
        }
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    // Création JButton
    private JButton creerBouton(String string, String commande) {
        JButton bouton = new JButton(string);
        bouton.addActionListener(new AdaptateurCommande(collecteurEvenements, commande));
        bouton.setAlignmentX(Component.CENTER_ALIGNMENT);
        bouton.setFocusable(false);
        return bouton;
    }

    // Création JComboBox
    private JComboBox<String> creerComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.addActionListener(new AdaptateurCommande(collecteurEvenements, comboBox.getSelectedItem().toString()));
        return comboBox;
    }

    // Création Box
    private Box creerBox(boolean estVerticale, boolean estOpaque) {
        Box box;
        if (estVerticale) {
            box = Box.createVerticalBox();
        } else {
            box = Box.createHorizontalBox();
        }

        if (estOpaque) {
            box.setOpaque(true);
            box.setBackground(Color.WHITE);
        }
        return box;
    }

    @Override
    public void run() {
        // Creation de la fenêtre
        frame = new JFrame("Gaufre Empoisonnée");

        // Création de la Gaufre et du Copyright
        gaufreGraphique = new GaufreGraphiqueSwing(jeu);
        copyright = createLabel("Copyright Groupe 5 - Projet Prog6 - 2022", true);

        // Création de Box (Gaufre et Copyright)
        Box gaufreCredit = creerBox(true, true);
        gaufreCredit.add((Component) gaufreGraphique);
        gaufreCredit.add(copyright);
        frame.add(gaufreCredit, BorderLayout.SOUTH);

        // Création Pop up (choix de joueur au début de la partie)
        JDialog player_settings = new JDialog(frame);
        player_settings.setBounds(500, 300, 400, 300);

        //choix du 2e joueur (joueur ou IA)
        Box choix_adversaire = Box.createVerticalBox();
        choix_adversaire.add(createLabel("Choisissez votre adversaire !", false));
        choix_adversaire.add(Box.createGlue());

        String[] deuxieme_joueur = {"Joueur 2", "IAAleatoire", "IAEtOu", "IAGP"};
        JComboBox<String> adversaire = creerComboBox(deuxieme_joueur);
        adversaire.setBounds(80, 50, 140, 20);
        choix_adversaire.add(adversaire);

        //choix joueur qui commence
        Box choix_joueur = Box.createVerticalBox();
        choix_joueur.add(createLabel("Qui commence ?", false));
        choix_joueur.add(Box.createGlue());
        Box bouton_choix_joueur = Box.createHorizontalBox();
        joueur_un = creerBouton("joueur 1", "j1");
        bouton_choix_joueur.add(joueur_un);
        joueur_deux = creerBouton("joueur 2", "j2");
        bouton_choix_joueur.add(joueur_deux);
        choix_joueur.add(bouton_choix_joueur);

        player_settings.add(choix_adversaire, BorderLayout.NORTH);
        player_settings.add(choix_joueur, BorderLayout.SOUTH);
        player_settings.setVisible(true);

        // Ajout de notre composant de dessin dans la fenetre
        gaufreGraphique = new GaufreGraphiqueSwing(jeu);
        frame.add((Component) gaufreGraphique);

        // Box
        Box barreLaterale = Box.createVerticalBox();
        barreLaterale.add(createLabel(" Gaufre Empoisonnée ", false));
        taille = createLabel("Lignes : " + jeu.gaufre().lignes() + "    Colonnes : " + jeu.gaufre().colonnes(), false);
        barreLaterale.add(taille);
        barreLaterale.add(Box.createGlue());
        Box barreJoueur = Box.createHorizontalBox();
        infoJoueurCourantCouleur = createLabel("Joueur " + jeu.getJoueurCourant(), false);
        infoJoueurCourant = createLabel(" doit jouer", false);

        infoJoueur = createLabel("Coup hors zone effectue", false);
        afficherMasquerInfoJoueur(0, false);

        majJoueurCourant();
        barreJoueur.add(infoJoueurCourantCouleur);
        barreJoueur.add(infoJoueurCourant);
        barreLaterale.add(barreJoueur);
        barreLaterale.add(Box.createGlue());
        barreLaterale.add(infoJoueur);
        barreLaterale.add(Box.createGlue());
        infoFin = createLabel(" Partie en cours ... ", false);
        scores = createLabel("Scores", false);
        barreLaterale.add(scores);
        Box barreScores = Box.createHorizontalBox();
        J1L = createLabel("J1 : " + jeu.getJ(1), false);
        barreScores.add(J1L);
        J2L = createLabel("   J2 : " + jeu.getJ(2), false);
        barreScores.add(J2L);
        barreLaterale.add(barreScores);
        suite = creerBouton("Abandon", "suite");
        barreLaterale.add(suite);
        barreLaterale.add(Box.createGlue());

        // Annuler / Refaire
        nbCoup = createLabel("Nombres de joués : " + jeu.nbCoup() + " sur 0", false);
        barreLaterale.add(nbCoup);
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

        Box historique = Box.createHorizontalBox();
        listeEtapes = new JComboBox<>();
        listeEtapes.addItem(0);
        listeEtapes.setFocusable(false);
        histoire = creerBouton("GO !", "histoire");
        historique.add(listeEtapes);
        historique.add(histoire);
        barreLaterale.add(historique);
        barreLaterale.add(Box.createGlue());

        // Save et Load
        Box sauvegardeCharge = Box.createHorizontalBox();
        save = creerBouton("Sauvegarder", "save");
        load = creerBouton("Charger", "load");
        sauvegardeCharge.add(save);
        sauvegardeCharge.add(load);
        barreLaterale.add(sauvegardeCharge);
        barreLaterale.add(Box.createGlue());

	aide = creerBouton("Aide", "aide");
        barreLaterale.add(aide);
        nouvellePartie = creerBouton("Nouvelle Partie", "Nouvelle");
        barreLaterale.add(nouvellePartie);
        barreLaterale.add(Box.createGlue());
        frame.add(barreLaterale, BorderLayout.LINE_END);

        // Mise en place des Listeners
        ((Component) gaufreGraphique).addMouseMotionListener(new AdaptateurSourisMouvement(gaufreGraphique, collecteurEvenements));
        ((Component) gaufreGraphique).addMouseListener(new AdaptateurSouris(gaufreGraphique, collecteurEvenements));
        frame.addKeyListener(new AdaptateurClavier(collecteurEvenements));

        // Garde à jour l'interface graphique du controleur
        collecteurEvenements.fixerInterfaceGraphique(this);

        // Paramètre de la fenêtre
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setVisible(true);
    }

    public void afficherMasquerInfoJoueur(int option, boolean estVisible) {
        switch (option) {
            case 0:
                // Changer visibilité uniquement
                break;
            case 1:
                // Hors gaufre
                infoJoueur.setText("Coup hors gaufre !");
                break;
            case 2:
                // Morceau deja mange
                infoJoueur.setText("Morceau deja mange !");
                break;
            case 3:
                // Fin de partie
                infoJoueur.setText("Fin de partie !");
                break;
            default:
                // Option inconu
                Configuration.instance().logger().info("Option pour 'afficherMasquerInfoJoueur' inconu !\n");
                break;
        }
        infoJoueur.setVisible(estVisible);
        frame.repaint();
    }

    public int destination(){
        return (int) listeEtapes.getSelectedItem();
    }
    public void majNbCoup(){
        nbCoup.setText("Nombres de coups joués : " + jeu.nbCoup() + " sur " + jeu.gaufre().tailleHistoire());
        listeEtapes.removeAllItems();
        int i = 0;
        while(i <= jeu.gaufre().tailleHistoire()){
            listeEtapes.addItem(i);
            i++;
        }
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
        afficherMasquerInfoJoueur(0, false);
        if (jeu.estTermine()) {
            infoFin.setText("Fin de partie !");
            infoJoueurCourantCouleur.setText("Joueur " + jeu.getJoueurCourant());
            infoJoueurCourant.setText(" gagne !");
            incrementeScore();
            majScore();
            suite.setText("Manche Suivante");
        } else {
            infoJoueurCourantCouleur.setText("Joueur " + jeu.getJoueurCourant());
            infoJoueurCourant = createLabel(" doit jouer", false);
            suite.setText("Abandon");
            infoFin.setText(" Partie en cours ... ");
        }
        majJoueurCourant();
        annuler.setEnabled(jeu.gaufre().peutAnnuler());
        refaire.setEnabled(jeu.gaufre().peutRefaire());
        ((Component) gaufreGraphique).repaint();
        frame.repaint();
    }

    public void majDejaMangee(int coupX, int coupY) {
        if (jeu.estDejaMangee(coupX, coupY)) {
            infoJoueurCourantCouleur.setText("Joueur " + jeu.getJoueurCourant());
            infoJoueurCourant.setText(" doit rejouer, case deja mangee !");
        } else {
            infoJoueurCourantCouleur.setText("Joueur " + jeu.getJoueurCourant());
            infoJoueurCourant.setText(" doit jouer");
        }
    }

    public void nouvelle(){
        jeu.scoresZero();
        jeu.nbCoupZero();
        majScore();
        majNbCoup();
    }

    public void incrementeScore(){
        jeu.increJ(jeu.getJoueurCourant());
    }

    public void majAnnule() {
        infoJoueurCourantCouleur.setText("Joueur " + jeu.getJoueurCourant());
        afficherMasquerInfoJoueur(0, true);
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
