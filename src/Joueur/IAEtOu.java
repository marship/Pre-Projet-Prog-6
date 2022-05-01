package Joueur;

import java.util.Hashtable;
import java.util.Random;

import Controleur.ControleurMediateur;
import Global.Configuration;
import Modele.Coup;
import Modele.Gaufre;
import Structures.Sequence;

public class IAEtOu extends IA {
    
    Random random;
    int quiDoitGagner;
    Hashtable<String, Gaufre> vu;

    IAEtOu(ControleurMediateur controleurMediateur) {
        super(controleurMediateur);
        random = new Random();
    }

    @Override
	public Sequence<Coup> joue() {
        Sequence<Coup> sortie = Configuration.instance().nouvelleSequence();
        Coup coup;
        Gaufre gaufre = jeu.gaufre().clone();

        if(quiDoitGagner == 1){
            sortie = gagne1(gaufre);
        }
        else{
            sortie = gagne2(gaufre);
        }

        if(!jeu.estCoupJouable(0, 1)){
            coup = jeu.creerCoup(1, 0);
        }
        else{
            if(!jeu.estCoupJouable(1, 0)){
                coup = jeu.creerCoup(0, 1);
            }
            else{
                int mangeX = random.nextInt(jeu.colonnes()) + 1;
                int mangeY = random.nextInt(jeu.lignes()) + 1;

                while(!jeu.estCoupJouable(mangeX, mangeY)){
                    mangeX = random.nextInt(jeu.colonnes()) + 1;
                    mangeY = random.nextInt(jeu.lignes()) + 1;
                }
                coup = jeu.creerCoup(mangeX, mangeY);
            }
        }
        
        sortie.insereTete(coup);
        
        return sortie;
    }

    public boolean calcul(Gaufre gaufre){
        if(gaufre.estTermine()){
            int joueurCourant = gaufre.joueurCourant() ? 1 : 2;
            if(joueurCourant == quiDoitGagner){
                
            }
        }
        return false;
    }

    private Sequence<Coup> gagne1(Gaufre gaufre) {
        Sequence<Coup> sortie = Configuration.instance().nouvelleSequence();
        int i = 0;
        int j = 0;
        boolean gagne = false;
        Coup coup;
        while(i < gaufre.lignes()){
            while (j < gaufre.colonnes()){
                gagne = calcul(gaufre);
                if(gagne){
                    coup = jeu.creerCoup(i, j);
                    sortie.insereTete(coup);
                }
                j++;
            }
            i++;
        }

        return sortie;
    }

    private Sequence<Coup> gagne2(Gaufre gaufre) {
        return null;
    }

    @Override
	public void initialise() {
		Configuration.instance().logger().info("Demarrage de l'IA Aleatoire !\n");
        quiDoitGagner = jeu.getJoueurCourant();
        vu = new Hashtable<String, Gaufre>();
	}

    @Override
	public void finalise() {
		Configuration.instance().logger().info("Fin de l'IA Aleatoire !\n");
	}
}
