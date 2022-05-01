package Joueur;

import java.util.Random;

import Global.Configuration;
import Modele.Coup;
import Structures.Sequence;

public class IAAleatoire extends IA {
    
    Random random;

    IAAleatoire() {
        random = new Random();
    }

    @Override
	public Sequence<Coup> joue() {
        Sequence<Coup> sortie = Configuration.instance().nouvelleSequence();
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
        Coup coup = null;

        int mangeX = random.nextInt(jeu.colonnes());
        int mangeY = random.nextInt(jeu.lignes());

        while(!jeu.estCoupJouable(mangeX, mangeY)){
            mangeX = random.nextInt(jeu.colonnes());
            mangeY = random.nextInt(jeu.lignes());
        }

        coup = jeu.creerCoup(mangeX, mangeY);
        sortie.insereTete(coup);
        return sortie;
    }

    @Override
	public void initialise() {
		Configuration.instance().logger().info("Demarrage de l'IA Aleatoire !\n");
	}

    @Override
	public void finalise() {
		Configuration.instance().logger().info("Fin de l'IA Aleatoire !\n");
	}
}
