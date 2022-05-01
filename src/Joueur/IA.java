package Joueur;

import Controleur.ControleurMediateur;
import Global.Configuration;
import Modele.Coup;
import Modele.Gaufre;
import Modele.Jeu;
import Structures.Sequence;

public class IA {
    
    ControleurMediateur controleurMediateur;
    Jeu jeu;

    IA (ControleurMediateur c) {
        controleurMediateur = c;
    }

    /*
    IA instance = null;
        String name = Configuration.instance().lis("IA");
        try {
            instance = (IA) ClassLoader.getSystemClassLoader().loadClass(name).newInstance();
            instance.jeu = j;
        } catch (Exception e) {
            Configuration.instance().logger().severe("Impossible de trouver l'IA : " + name);
        }
        return instance;
    */

    final Sequence<Coup> elaboreCoups() {
        return joue();
    }

    final void activeIA() {
        initialise();
    }

    void initialise() {
    }

    Sequence<Coup> joue() {
        return null;
    }

    void finalise() {
    }
}
