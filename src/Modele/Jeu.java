package Modele;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import Global.Configuration;
import Patterns.Observable;
import Structures.Iterateur;
import Structures.Sequence;

public class Jeu extends Observable {

    Gaufre gaufre;
    int joueurGagnant;
    int joueurCourant;
    boolean dejaMangee = false;

    int largeurPrevisualisation;
    int hauteurPrevisualisation;
    int previsualisationX;
    int previsualisationY;

    int J1, J2;

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

    public boolean estDejaMangee() {
        return dejaMangee;
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

    public void changerJoueurCourant() {
        gaufre.changerJoueur();
    }

    public boolean estAuDebut() {
        return gaufre.estAuDebut();
    }

    public boolean estTermine() {
        return gaufre.estTermine();
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

    public void scoresZero(){
        J1 = J2 = 0;
    }

    public void increJ(int j){
        if(j == 1){
            J1++;
        }
        else{
            J2++;
        }
    }

    public void baisseJ(int j){
        if(j == 1){
            J1--;
        }
        else{
            J2--;
        }
    }

    public int getJ(int j){
        if(j == 1){
            return J1;
        }
        else{
            return J2;
        }
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
                System.out.println("Fichier fait");
            }
            else{
                System.out.println("Sauvegarde existe déjà");
            }

            FileWriter fw = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("" + getJoueurCourant() + "\r\n"); // On note le joueur courant
            bw.write("" + J1 + "\r\n"); // On note le score du J1
            bw.write("" + J2 + "\r\n"); // On note le score du J1

            // Si on veut afficher les linges et colonnes et la grille
            /*
            bw.write("" + lignes() + "\r\n");
            bw.write("" + colonnes() + "\r\n");
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
                Iterateur<Position> iterateur = coup.positionBouchee.iterateur();
                while (iterateur.aProchain()) {
                    Position position = (Position) iterateur.prochain();
                    bw.write("" + position.positionX);
                    bw.write("" + position.positionY);
                }
                bw.write("\r\n");
            }
            bw.close();
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }

    public void charger(String fic){
        
    }
}
