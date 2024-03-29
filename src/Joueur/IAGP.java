package Joueur;

import java.util.Random;

import Global.Configuration;
import Modele.Coup;
import Structures.Sequence;

public class IAGP extends IA {
    
    Random random;

    IAGP() {
        random = new Random();
    }

    @Override
	public Sequence<Coup> joue() {
        Sequence<Coup> sortie = Configuration.instance().nouvelleSequence();
        Coup coup = null;

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

    @Override
	public void initialise() {
		Configuration.instance().logger().info("Demarrage de l'IA Gangnant/Perdant !\n");
	}

    @Override
	public void finalise() {
		Configuration.instance().logger().info("Fin de l'IA Gangnant/Perdant !\n");
	}
}
