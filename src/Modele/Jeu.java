package Modele;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import Global.Configuration;
import Patterns.Observable;
import Structures.Iterateur;
import Structures.Sequence;

public class Jeu extends Observable {

    Gaufre gaufre;
    int joueurGagnant = 0;
    int joueurCourant;
    boolean dejaMangee = false;

    int largeurPrevisualisation;
    int hauteurPrevisualisation;
    int previsualisationX;
    int previsualisationY;

    int scoreJ1, scoreJ2, nbCoup;

    public Jeu() {
        int nbLignesGaufre = Integer.parseInt(Configuration.instance().lis("Ligne"));
        int nbColonnesGaufre = Integer.parseInt(Configuration.instance().lis("Colonne"));
        gaufre = new Gaufre(nbLignesGaufre, nbColonnesGaufre);
    }

    public Gaufre gaufre() {
        return gaufre;
    }

    public void modifierTailleGauffre(int modifLigne, int modifColonne) {            
        if (lignes() == 1 && modifLigne == -1) {
            Configuration.instance().logger().warning("Reduction de la gaufre sur une ligne impossible\n");
        } else if (colonnes() == 1 && modifColonne == -1) {
            Configuration.instance().logger().warning("Reduction de la gaufre sur une colonne impossible\n");
        } else {
            gaufre.initialisation(lignes() + modifLigne, colonnes() + modifColonne);
        }
    }

    public boolean estCoupJouable(int coupX, int coupY) {
        return gaufre.estCoupJouable(coupX, coupY);
    }

    public boolean estDejaMangee(int coupX, int coupY) {
        return gaufre.estDejaMangee(coupX, coupY);
    }

    public Coup creerCoup(int coupX, int coupY) {
        return gaufre.creerCoup(coupX, coupY);
    }

    public void jouerCoup(Coup coup) {
        dejaMangee = false;
        if (coup == null) {
            dejaMangee();
        } else {
            gaufre.jouerCoup(coup);
            miseAJour(); // Mise à jour de l'historique des coups
        }
    }

    public void dejaMangee() {
        dejaMangee = true;
        Configuration.instance().logger().info("Ce morceau de gaufre a deja ete mangee !\n");
    }

    public int getJoueurCourant() {
        // True = Joueur 1 | False = Joueur 2
        return joueurCourant = gaufre.joueurCourant() ? 1 : 2;
    }

    public int getJoueurGagnant() {
        // 0 = Aucun | 1 = Joueur 1 | 2 = Joueur 2
        return joueurGagnant;
    }

    public void changerJoueurCourant() {
        gaufre.changerJoueur();
    }

    public boolean estAuDebut() {
        return gaufre.estAuDebut();
    }

    public boolean estTermine() {
        return gaufre.estTermine();
    }

    public void init_joueurCourant(boolean p) {
        gaufre.init_joueurCourant(p);
    } 
    
    public void verificationJoueurGagnant() {
        if (estTermine()) {
            joueurGagnant = getJoueurCourant();
            afficherJoueurGagnant();
        }
    }

    public void afficherJoueurGagnant() {
        Configuration.instance().logger().info("La partie est termine ! Joueur " + joueurGagnant + " a gagne !\n");
    }

    public int lignes() {
        return gaufre.lignes();
    }

    public int colonnes() {
        return gaufre.colonnes();
    }

    public void reinitialiserScore(){
        scoreJ1 = scoreJ2 = 0;
    }

    public void incrementerScoreJoueur(int numJoueur){
        if(numJoueur == 1){
            scoreJ1 ++;
        }
        else{
            scoreJ2 ++;
        }
    }

    public void baisseJ(int j){
        if(j == 1){
            scoreJ1 --;
        }
        else{
            scoreJ2 --;
        }
    }

    public int getScoreJoueur(int numJoueur){
        if(numJoueur == 1){
            return scoreJ1;
        }
        else{
            return scoreJ2;
        }
    }

    public int nbCoup(){
        return nbCoup;
    }

    public void nbCoupPlus() {
        nbCoup ++;
    }

    public void nbCoupMoins() {
        nbCoup --;
    }

    public void miseAZeroNbCoup() {
        nbCoup = 0;
    }

    // ================================
    // ======= PREVISUALISATION =======
    // ================================

    public void setLargeurPrevisualisation(int valeurlargeur) {
        largeurPrevisualisation = valeurlargeur;
    }

    public int largeurPrevisualisation() {
        return largeurPrevisualisation;
    }

    public void setHauteurPrevisualisation(int valeurhauteur) {
        hauteurPrevisualisation = valeurhauteur;
    }

    public int hauteurPrevisualisation() {
        return hauteurPrevisualisation;
    }

    public void setPrevisualisationX(int coupX) {
        previsualisationX = coupX;
    }

    public int previsualisationX() {
        return previsualisationX;
    }

    public void setPrevisualisationY(int coupY) {
        previsualisationY = coupY;
    }

    public int previsualisationY() {
        return previsualisationY;
    }

    // ================================
    // ========== HISTORIQUE ==========
    // ================================

    public Coup annule() {
        if(estTermine()){
            baisseJ(getJoueurCourant());
            joueurGagnant = 0;
        }
        Coup coup = gaufre.annuler();
        miseAJour();
        return coup;
    }

    public Coup refaire() {
        Coup coup = gaufre.refaire();
        miseAJour();
        return coup;
    }

    public void viderHistorique() {
        gaufre.viderHistorique();
    }

    // ================================
    // ==== SAUVEGARDE CHARGEMENT =====
    // ================================

    public void sauvegarder(){
        try {
            // On récupère la date pour le nom de la sauvegarde
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
            String fic = System.getProperty("user.dir") + File.separator + "res" + File.separator + "Sauvegardes" + File.separator + dtf.format(LocalDateTime.now()) + ".txt";
            File f = new File(fic);

            // On fait le fichier
            if (!f.isFile()){
                f.createNewFile();
                Configuration.instance().logger().info("Fichier Creer !");
            }
            else{
                Configuration.instance().logger().info("Sauvegarde existe deja !");
            }

            FileWriter fw = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("" + getJoueurCourant() + "\n"); // On note le joueur courant

            bw.write("" + scoreJ1 + "\n"); // On note le score du J1
            bw.write("" + scoreJ2 + "\n"); // On note le score du J1

            bw.write("" + lignes() + "\n"); // On note les lignes
            bw.write("" + colonnes() + "\n"); // On note les colonnes

            // Si on veut afficher la grille
            /*
            int i = 0;
            int j = 0;
            while(i < lignes()){
                while(j < colonnes()){
                    bw.write("" + gaufre.grilleGaufre[i][j]);
                    j++;
                }
                bw.write("\r\n");
                i++;
                j = 0;
            }
            */

            // Sauvegarde des coups
            Sequence<Coup> liste = Configuration.instance().nouvelleSequence();
            Coup coup = null;
            while( !gaufre().passe.estVide() ){
                coup = gaufre().passe.extraitTete();
                liste.insereTete(coup);
            }
            coup = null;
            while( !liste.estVide() ){
                coup = liste.extraitTete();
                gaufre().passe.insereTete(coup);
                Iterateur<Position> iterateur = coup.positionBouchee.iterateur();
                while (iterateur.aProchain()) {
                    Position position = (Position) iterateur.prochain();
                    bw.write("" + position.positionX + "\n");
                    bw.write("" + position.positionY + "\n");
                }
            }
            bw.close();
            Configuration.instance().logger().info("Fichier Sauvegarde !");
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }

    public void charger(String fic){
        try{
            Scanner scan = new Scanner(new File(fic));

            int j = Integer.parseInt(scan.nextLine()); // Lecture du joueur courant

            int SJ1 = Integer.parseInt(scan.nextLine()); // Lecture des scores
            int SJ2 = Integer.parseInt(scan.nextLine());

            int ligne = Integer.parseInt(scan.nextLine()); // Lecture de la taille de la grille
            int colonne = Integer.parseInt(scan.nextLine());

            gaufre().initialisation(ligne, colonne); // On crée la gauffre

            // On lit et joue tous les coups sauvegardés
            while (scan.hasNext()){
                int x = Integer.parseInt(scan.nextLine());
                int y = Integer.parseInt(scan.nextLine());
                Coup coup = creerCoup(x, y);
                jouerCoup(coup);
                nbCoup++;
            }

            // On met à jour tout ce qui doit être à jour
            scoreJ1 = SJ1;
            scoreJ2 = SJ2;
            if(j == 1){
                gaufre().joueurCourant = true;
            }
            else{
                gaufre().joueurCourant = false;
            }
            
            scan.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
    }

    public int tailleHistoirique() {
        return gaufre.tailleHistoire();
    }
}
