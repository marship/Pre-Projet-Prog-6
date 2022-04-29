package Controleur;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import Global.Configuration;
import Joueur.IA;
import Modele.Jeu;
import Vue.CollecteurEvenements;
import Vue.InterfaceGraphique;

public class ControleurMediateur implements CollecteurEvenements {

    InterfaceGraphique interfaceGraphique;
    Configuration configuration;
    Jeu jeu;
    IA joueurAutomatique;

    public ControleurMediateur(Jeu j) {
        configuration = Configuration.instance();
        jeu = j;
    }

    @Override
    public void clicSouris(int coupX, int coupY) {
        if ((coupX <= jeu.gaufre().colonnes() * interfaceGraphique.gaufreGraphique.largeurCase()) && (coupY <= jeu.gaufre().lignes() * interfaceGraphique.gaufreGraphique.hauteurCase())) {
                coupX = (coupX / interfaceGraphique.gaufreGraphique.largeurCase());
                coupY = (coupY / interfaceGraphique.gaufreGraphique.hauteurCase());
                jouerCoup(coupX, coupY);
                interfaceGraphique.majJoueurCourant();
        } else {
            Configuration.instance().logger().info("Coup hors zone !\n");
        }
    }

    @Override
    public void traqueSouris(int coupX, int coupY) {
        
        if (!jeu.estTermine()) {
            if ((coupX <= jeu.gaufre().colonnes() * interfaceGraphique.gaufreGraphique.largeurCase()) && (coupY <= jeu.gaufre().lignes() * interfaceGraphique.gaufreGraphique.hauteurCase())) {
                coupX = (coupX / interfaceGraphique.gaufreGraphique.largeurCase());
                coupY = (coupY / interfaceGraphique.gaufreGraphique.hauteurCase());
                
                jeu.setLargeurPrevisualisation(jeu.gaufre().colonnes() - coupX);
                jeu.setHauteurPrevisualisation(jeu.gaufre().lignes() - coupY);
                jeu.setPrevisualisationX(coupX);
                jeu.setPrevisualisationY(coupY);
    
                interfaceGraphique.previsualisation(jeu.getJoueurCourant(), coupX, coupY, jeu.largeurPrevisualisation(), jeu.hauteurPrevisualisation());
            } else {
                Configuration.instance().logger().info("Curseur hors zone !\n");
            }
        } else {
            // jeu.modifierTailleGauffre(0, 0); // ADRIEN VA MODIF CA !!!
        }
    }

    void jouerCoup(int coupX, int coupY) {
        jeu.jouerCoup(coupX, coupY);
    }

    void annule() {
        jeu.annule();
    }

    void refaire() {
        jeu.refaire();
    }

    @Override
    public boolean commande(String commande) {
        switch (commande) {
            case "down":
                jeu.modifierTailleGauffre(1, 0);
                interfaceGraphique.majTaille();
                interfaceGraphique.metAJour();
                break;
            case "up":
                jeu.modifierTailleGauffre(-1, 0);
                interfaceGraphique.majTaille();
                interfaceGraphique.metAJour();
                break;
            case "left":
                jeu.modifierTailleGauffre(0, -1);
                interfaceGraphique.majTaille();
                interfaceGraphique.metAJour();
                break;
            case "right":
                jeu.modifierTailleGauffre(0, 1);
                interfaceGraphique.majTaille();
                interfaceGraphique.metAJour();
                break;
            case "quit":
                System.exit(0);
                break;
            case "annule":
                annule();
                break;
            case "refaire":
                refaire();
                break;
            case "fullscreen":
                interfaceGraphique.basculePleinEcran();
                break;
            case "Nouvelle":
                jeu.modifierTailleGauffre(0, 0);
                interfaceGraphique.nouvelle();
                interfaceGraphique.metAJour();
                break;
            case "suite":
                if(jeu.estTermine()){
                    jeu.modifierTailleGauffre(0, 0);
                    interfaceGraphique.metAJour();
                }
                else{
                    jeu.gaufre().joueurCourant = !jeu.gaufre().joueurCourant();
                    interfaceGraphique.incrementeScore();
                    interfaceGraphique.majScore();
                    jeu.modifierTailleGauffre(0, 0);
                    interfaceGraphique.metAJour();
                }
                break;
            case "save":
                sauvegarder();
                break;
            case "load":
                charge();
                break;
                
            default:
                return false;
        }
        return true;
    }

    @Override
    public void fixerInterfaceUtilisateur(InterfaceGraphique iGraphique) {
        interfaceGraphique = iGraphique;
    }

    public void charge() {
        System.out.println(System.getProperty("user.dir") + File.separator + "res" + File.separator + "Sauvegardes");
        JFileChooser chooser = new JFileChooser(System.getProperty("user.dir") + File.separator + "res" + File.separator + "Sauvegardes");
        int returnVal = chooser.showOpenDialog(interfaceGraphique.frame);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(null,"Vous n'avez rien sélectionné.");
            return;
        }
        try {
            File myObj = new File(chooser.getSelectedFile().getPath());
            Scanner myReader = new Scanner(myObj);
            int kijou = Integer.parseInt(myReader.nextLine());
            int score1 = Integer.parseInt(myReader.nextLine());
            int score2 = Integer.parseInt(myReader.nextLine());
            while (myReader.hasNextLine()) {
                // Jouer et Ajouter les coups
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        
    }

    public void sauvegarder() {

    }
}
