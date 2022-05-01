package Joueur;

import java.util.Random;

import Controleur.ControleurMediateur;
import Global.Configuration;
import Modele.Coup;
import Structures.Sequence;

public class IAAleatoire extends IA {
    
    Random random;

    IAAleatoire(ControleurMediateur controleurMediateur) {
        super(controleurMediateur);
        random = new Random();
    }

    @Override
	public Sequence<Coup> joue() {
        // int mangeX = random.nextInt(jeu.colonnes());
        // int mangeY = random.nextInt(jeu.lignes());

        // controleurMediateur.jouerCoup(mangeX, mangeY);
        
        return null;
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
