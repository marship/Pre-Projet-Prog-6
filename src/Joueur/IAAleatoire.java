package Joueur;

import java.util.Random;

import Controleur.ControleurMediateur;
import Global.Configuration;
import Modele.Coup;
import Structures.Sequence;

public class IAAleatoire extends IA {
    
    Random random;
    int quiDoitGagner;

    IAAleatoire(ControleurMediateur controleurMediateur) {
        super(controleurMediateur);
        random = new Random();
    }

    @Override
	public Sequence<Coup> joue() {
        Sequence<Coup> sortie = Configuration.instance().nouvelleSequence();
        Coup coup;
        int mangeX = random.nextInt(jeu.colonnes()) + 1;
        int mangeY = random.nextInt(jeu.lignes()) + 1;

        while(!jeu.estCoupJouable(mangeX, mangeY)){
            mangeX = random.nextInt(jeu.colonnes()) + 1;
            mangeY = random.nextInt(jeu.lignes()) + 1;
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
