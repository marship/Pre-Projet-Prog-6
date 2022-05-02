package Joueur;

import java.util.Hashtable;
import java.util.Random;

import Global.Configuration;
import Modele.Coup;
import Modele.Gaufre;
import Structures.Sequence;

public class IAEtOu extends IA {
    
    Random random;
    int quiDoitGagner;
    Hashtable<String, Gaufre> hashTable;

    IAEtOu() {
        random = new Random();
    }

    @Override
	public Sequence<Coup> joue() {

        quiDoitGagner = jeu.getJoueurCourant();
        Sequence<Coup> tmp = Configuration.instance().nouvelleSequence();
        Sequence<Coup> sortie = Configuration.instance().nouvelleSequence();
        Coup coup = null;
        Gaufre gaufre = jeu.gaufre().clone();
        boolean verif = false;
        int nbCoupsPossibles = 1;
        int i = 0;
        int j = 0;
        
        while(i < gaufre.lignes()){
            while(j < gaufre.colonnes()){
                if(jeu.estCoupJouable(j, i)){
                    Gaufre g2 = gaufre.clone();
                    g2.jouerCoupGaufre(j, i);
                    if(calculJB(g2)){
                        verif = true;
                        coup = jeu.creerCoup(j, i);
                        tmp.insereTete(coup);
                        System.out.println("" + j + " " + i);
                        nbCoupsPossibles++;
                    }
                }
                j++;
            }
            j = 0;
            i++;
        }

        if(!verif){
            if(!jeu.estCoupJouable(0, 1) && !jeu.estCoupJouable(1, 0)){
                coup = jeu.creerCoup(0, 0);
            }
            else{
                if(!jeu.estCoupJouable(0, 1) && jeu.estCoupJouable(1, 0)){
                    coup = jeu.creerCoup(1, 0);
                }
                else{
                    if(!jeu.estCoupJouable(1, 0) && jeu.estCoupJouable(0, 1)){
                        coup = jeu.creerCoup(0, 1);
                    }
                    else{
                        int mangeX = random.nextInt(jeu.colonnes());
                        int mangeY = random.nextInt(jeu.lignes());
    
                        while( (mangeX == 0 && mangeY == 0) || !jeu.estCoupJouable(mangeX, mangeY)){
                            mangeX = random.nextInt(jeu.colonnes());
                            mangeY = random.nextInt(jeu.lignes());
                        }
                        coup = jeu.creerCoup(mangeX, mangeY);
                    }
                }
            }
            sortie.insereTete(coup);
            return sortie;
        }
        else{
            int alea = random.nextInt(nbCoupsPossibles);
            while(alea != 0){
                coup = tmp.extraitTete();
                alea--;
            }
            sortie.insereTete(coup);
            return sortie;
        }
    }

    public boolean calculJA(Gaufre gaufre){
        Coup coup;
        if(gaufre.estTermine()){
            int joueurCourant = gaufre.joueurCourant() ? 1 : 2;
            if(quiDoitGagner != joueurCourant){
                return false;
            }
            else{
                return true;
            }
        }
        else{
            int i = 0;
            int j = 0;
            boolean sortie = false;
            while(i < gaufre.lignes()){
                while (j < gaufre.colonnes()){
                    if(gaufre.estCoupJouable(j, i)){
                        coup = jeu.creerCoup(j, i);
                        Gaufre suite = gaufre.clone();
                        suite.jouerCoup(coup);
                        sortie = sortie || calculJB(suite);
                    }
                    j++;
                }
                i++;
                j = 0;
            }
            return sortie;
        }
    }

    public boolean calculJB(Gaufre gaufre){
        Coup coup;
        if(gaufre.estTermine()){
            int joueurCourant = gaufre.joueurCourant() ? 1 : 2;
            if(quiDoitGagner != joueurCourant){
                return false;
            }
            else{
                return true;
            }
        }
        else{
            int i = 0;
            int j = 0;
            boolean sortie = true;
            while(i < gaufre.lignes()){
                while (j < gaufre.colonnes()){
                    if(gaufre.estCoupJouable(j, i)){
                        coup = jeu.creerCoup(j, i);
                        Gaufre suite = gaufre.clone();
                        suite.jouerCoup(coup);
                        sortie = sortie && calculJA(suite);
                    }
                    j++;
                }
                i++;
                j = 0;
            }
            return sortie;
        }
    }

    @Override
	public void initialise() {
		Configuration.instance().logger().info("Demarrage de l'IA Et Ou !\n");
        hashTable = new Hashtable<String, Gaufre>();
	}

    @Override
	public void finalise() {
		Configuration.instance().logger().info("Fin de l'IA Et Ou !\n");
	}
}
