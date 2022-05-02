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
                if(jeu.estCoupJouable(i, j)){
                    verif = true;
                    if(calculJA(gaufre)){
                        coup = jeu.creerCoup(i, j);
                        tmp.insereTete(coup);
                        nbCoupsPossibles++;
                    }
                }
                j++;
            }
            j = 0;
            i++;
        }

        if(!verif){
            return null;
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
            if(quiDoitGagner == joueurCourant){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            int i = 0;
            int j = 0;
            boolean sortie = false;
            while(i < gaufre.lignes()){
                while (j < gaufre.colonnes()){
                    if(gaufre.estCoupJouable(i, j)){
                        coup = jeu.creerCoup(i, j);
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
                return true;
            }
            else{
                return false;
            }
        }
        else{
            int i = 0;
            int j = 0;
            boolean sortie = true;
            while(i < gaufre.lignes()){
                while (j < gaufre.colonnes()){
                    if(gaufre.estCoupJouable(i, j)){
                        coup = jeu.creerCoup(i, j);
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
        quiDoitGagner = jeu.getJoueurCourant();
        hashTable = new Hashtable<String, Gaufre>();
	}

    @Override
	public void finalise() {
		Configuration.instance().logger().info("Fin de l'IA Et Ou !\n");
	}
}
