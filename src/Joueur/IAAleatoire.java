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
