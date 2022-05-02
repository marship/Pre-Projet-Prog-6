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
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class InterfaceGraphique implements Runnable, Observateur {

    private final static int LARGEUR_ESPACE = 0;
    private final static int HAUTEUR_ESPACE = 2;
    private final static Color VERT = new Color(104, 186, 118);
    private final static Color ROUGE = new Color(255, 0, 0);
    private final static Color NOIR = new Color(0, 0, 0);

    // ============ Jeu ===============
    CollecteurEvenements collecteurEvenements;
    Jeu jeu;

    // =========== Frame ==============
    public JFrame frame;
    boolean estMaximise;
    
    // ========= JComponent ===========
    JLabel joueurCourantCouleur, joueurCourantAction, etatPartie, titreScore, scoreJ1, scoreJ2, taille, information, nbCoup, copyright;
    JButton annuler, refaire, nouvellePartie, abandonMancheSuivante, save, load, historiqueBouton, joueur_un, joueur_deux, aide;
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

    @Override
    public void run() {
        // Creation de la fenêtre
        frame = new JFrame("Gaufre Empoisonnée");

        // Création de la Gaufre et du Copyright
        gaufreGraphique = new GaufreGraphiqueSwing(jeu);
        copyright = createLabel("Copyright Groupe 5 - Projet Prog6 - 2022", true);

        // Ajout des éléments
        frame.add((Component) gaufreGraphique);
        frame.add(copyright, BorderLayout.SOUTH);

        /*
        // Création Pop up (Choix joueur qui débute)
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
        */

        // Création Box menu lateral droit
        Box menuLateralDroit = creerBox(true, false);
            taille = createLabel("Lignes : " + jeu.lignes() + "    Colonnes : " + jeu.colonnes(), false);
            Box joueurCourant = creerBox(false, false);
                joueurCourantCouleur = createLabel("Joueur " + jeu.getJoueurCourant(), false);
                joueurCourantAction = createLabel(" doit jouer", false);
            information = createLabel("Coup hors zone effectue", false);
            etatPartie = createLabel(" Partie en cours ... ", false);
            titreScore = createLabel("Scores", false);
            Box tableauScore = creerBox(false, false);
                scoreJ1 = createLabel("J1 : " + jeu.getScoreJoueur(1), false);
                scoreJ2 = createLabel("   J2 : " + jeu.getScoreJoueur(2), false);
            abandonMancheSuivante = creerBouton("Abandon", "suite", true);
            nbCoup = createLabel("Nombres de joués : " + jeu.nbCoup() + " sur 0", false);
            Box annulerRefaire = creerBox(false, false);
                annuler = creerBouton("<", "annule", false);
                refaire = creerBouton(">", "refaire", false);
            Box historique = creerBox(false, false);
                listeEtapes = creerComboBox();
                historiqueBouton = creerBouton("GO !", "histoire", true);
            Box sauvegardeCharge = creerBox(false, false);
                save = creerBouton("Sauvegarder", "save", true);
                load = creerBouton("Charger", "load", true);
            aide = creerBouton("Aide", "aide", true);
            nouvellePartie = creerBouton("Nouvelle Partie", "Nouvelle", true);
        

        // Ajout des éléments à la Box menu lateral
        menuLateralDroit.add(createLabel(" *** Gaufre Empoisonnée *** ", false));
        menuLateralDroit.add(Box.createRigidArea(new Dimension(LARGEUR_ESPACE, HAUTEUR_ESPACE)));
        menuLateralDroit.add(taille);
        menuLateralDroit.add(Box.createGlue());

        joueurCourant.add(joueurCourantCouleur);
        joueurCourant.add(joueurCourantAction);
        menuLateralDroit.add(joueurCourant);
        menuLateralDroit.add(Box.createGlue());

        menuLateralDroit.add(information);
        menuLateralDroit.add(Box.createGlue());

        menuLateralDroit.add(etatPartie);
        menuLateralDroit.add(Box.createGlue());

        menuLateralDroit.add(titreScore);
        menuLateralDroit.add(Box.createRigidArea(new Dimension(LARGEUR_ESPACE, HAUTEUR_ESPACE)));
        tableauScore.add(scoreJ1);
        tableauScore.add(scoreJ2);
        menuLateralDroit.add(tableauScore);
        menuLateralDroit.add(Box.createGlue());

        menuLateralDroit.add(abandonMancheSuivante);
        menuLateralDroit.add(Box.createGlue());

        menuLateralDroit.add(nbCoup);
        menuLateralDroit.add(Box.createGlue());

        annulerRefaire.add(annuler);
        annulerRefaire.add(refaire);
        menuLateralDroit.add(annulerRefaire);
        menuLateralDroit.add(Box.createGlue());

        historique.add(listeEtapes);
        historique.add(historiqueBouton);
        menuLateralDroit.add(historique);
        menuLateralDroit.add(Box.createGlue());

        sauvegardeCharge.add(save);
        sauvegardeCharge.add(load);
        menuLateralDroit.add(sauvegardeCharge);
        menuLateralDroit.add(Box.createGlue());

        menuLateralDroit.add(aide);
        menuLateralDroit.add(Box.createGlue());

        menuLateralDroit.add(nouvellePartie);
        menuLateralDroit.add(Box.createGlue());

        // Ajout de la Box menu lateral à la fenêtre
        frame.add(menuLateralDroit, BorderLayout.LINE_END);

        // Initialisation
        initialisationAffichage();

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

    private void initialisationAffichage() {
        miseAJourCouleurJoueurCourant(0, true);
        afficherMasquerInfoJoueur(0, false);
    }

    // ???
    public int destination(){
        return (int) listeEtapes.getSelectedItem();
    }

    @Override
    public void majInfoPartie() {
        afficherMasquerInfoJoueur(0, false);
        if (jeu.estTermine()) {
            etatPartie.setText("Fin de partie !");
            miseAJourCouleurJoueurCourant(3, true);
            incrementerScore();
            miseAJourTableauScore();
            abandonMancheSuivante.setText("Manche Suivante");
        } else {
            miseAJourCouleurJoueurCourant(1, true);
            abandonMancheSuivante.setText("Abandon");
            etatPartie.setText(" Partie en cours ... ");
        }
        miseAJourCouleurJoueurCourant(0, true);
        annuler.setEnabled(jeu.gaufre().peutAnnuler());
        refaire.setEnabled(jeu.gaufre().peutRefaire());
        ((Component) gaufreGraphique).repaint();
        frame.repaint();
    }

    // ================================
    // ============ Score =============
    // ================================

    public void incrementerScore() {
        jeu.incrementerScoreJoueur(jeu.getJoueurCourant());
    }

    private void setCouleurScore(Color couleurScoreJ1, Color couleurScoreJ2) {
        scoreJ1.setForeground(couleurScoreJ1);
        scoreJ2.setForeground(couleurScoreJ2);
    }

    public void miseAJourTableauScore() {
        if(jeu.getScoreJoueur(1) > jeu.getScoreJoueur(2)) {
            setCouleurScore(VERT, ROUGE);
        } else if(jeu.getScoreJoueur(1) < jeu.getScoreJoueur(2)) {
            setCouleurScore(ROUGE, VERT);
        } else {
            setCouleurScore(NOIR, NOIR);
        }
        scoreJ1.setText("J1 : " + jeu.getScoreJoueur(1));
        scoreJ2.setText("    J2 : " + jeu.getScoreJoueur(2));
    }

    // ================================
    // ====== Previsualisation ========
    // ================================

    public void previsualisation(int joueurCourant, int coupX, int coupY, int largeurPreselection, int hauteurPreselection) {
        gaufreGraphique.tracerRectangle(joueurCourant, coupX, coupY, largeurPreselection, hauteurPreselection);
        ((Component) gaufreGraphique).repaint();
    }

    // ================================
    // ====== Fonction Menu Droit =====
    // ================================

    public void nouvellePartie() {
        jeu.reinitialiserScore();
        jeu.miseAZeroNbCoup();
        miseAJourTableauScore();
        miseAJourNbCoup();
    }

    // ================================
    // ===== MàJ Info Menu Droit ======
    // ================================

    public void miseAJourInfoTailleGaufre() {
        taille.setText("Lignes : " + jeu.lignes() + "    Colonnes : " + jeu.colonnes());
    }

    public void miseAJourNbCoup() {
        nbCoup.setText("Nombres de coups joues : " + jeu.nbCoup() + " sur " + jeu.tailleHistoirique());
        listeEtapes.removeAllItems();
        for (int i = 0; i <= jeu.tailleHistoirique(); i++) {
            listeEtapes.addItem(i);
        }
    }

    public void afficherMasquerInfoJoueur(int option, boolean estVisible) {
        switch (option) {
            case 0:
                // Changer visibilité uniquement
                break;
            case 1:
                // Hors gaufre
                information.setText("Coup hors gaufre !");
                break;
            case 2:
                // Morceau deja mange
                information.setText("Morceau deja mange !");
                break;
            default:
                // Option inconu
                Configuration.instance().logger().info("Option pour 'afficherMasquerInfoJoueur' inconu !\n");
                break;
        }
        information.setVisible(estVisible);
        frame.repaint();
    }

    public void miseAJourCouleurJoueurCourant(int option, boolean estVisible) {
        switch (option) {
            case -1:
                // Changer visibilité uniquement
                break;
            case 0:
                // Changer couleur joueur courant
                if (jeu.getJoueurCourant() == 1) {
                    joueurCourantCouleur.setForeground(Color.MAGENTA);
                } else {
                    joueurCourantCouleur.setForeground(Color.BLUE);
                }
                break;
            case 1:
                // Jouer | Annuler
                joueurCourantCouleur.setText("Joueur " + jeu.getJoueurCourant());
                joueurCourantAction.setText(" doit jouer");
                break;
            case 2:
                // Morceau deja mange
                joueurCourantCouleur.setText("Joueur " + jeu.getJoueurCourant());
                joueurCourantAction.setText(" doit rejouer, case deja mangee !");
                break;
            case 3:
                // Gagne
                joueurCourantCouleur.setText("Joueur " + jeu.getJoueurCourant());
                joueurCourantAction.setText(" gagne !");
                break;
            default:
                Configuration.instance().logger().info("Option pour 'miseAJourCouleurJoueurCourant' inconu !\n");
                break;
        }
        afficherMasquerInfoJoueur(0, estVisible);
        frame.repaint(); // TO DO
    }

    // ================================
    // ============ Frame =============
    // ================================

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

    // ================================
    // ====== Création Components =====
    // ================================

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
    private JButton creerBouton(String string, String commande, boolean estClicable) {
        JButton bouton = new JButton(string);
        bouton.addActionListener(new AdaptateurCommande(collecteurEvenements, commande));
        bouton.setAlignmentX(Component.CENTER_ALIGNMENT);
        bouton.setEnabled(estClicable);
        bouton.setFocusable(false);
        return bouton;
    }

    // Création JComboBox
    private JComboBox<Integer> creerComboBox() {
        JComboBox<Integer> comboBox = new JComboBox<>();
        comboBox.addItem(0);
        comboBox.addActionListener(new AdaptateurCommande(collecteurEvenements, comboBox.getSelectedItem().toString()));
        comboBox.setFocusable(false);
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
}
