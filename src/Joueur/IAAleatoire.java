package Joueur;

import java.util.Random;
import java.util.logging.Logger;

import Global.Configuration;
import Modele.Coup;
import Structures.Sequence;

public class IAAleatoire extends IA {
    
    Random random;
	Logger logger;

    IAAleatoire() {
        random = new Random();
    }

    @Override
	public Sequence<Coup> joue() {
        return null;
    }

    @Override
	public void initialise() {
		logger = Configuration.instance().logger();
		logger.info("Démarrage de l'IA Aléatoire !");
	}

    @Override
	public void finalise() {
		logger.info("Fin de l'IA Aléatoire !");
	}

}
