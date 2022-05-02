package Joueur;

import Global.Configuration;
import Modele.Coup;
import Modele.Gaufre;
import Modele.Jeu;
import Structures.Sequence;

public abstract class IA {
    
    Jeu jeu;
    Gaufre gaufre;

    public static IA nouvelle(Jeu j) {
        IA instance = null;
        String name = Configuration.instance().lis("IA");
        try {
            instance = (IA) ClassLoader.getSystemClassLoader().loadClass(name).newInstance();
            instance.jeu = j;
        } catch (Exception e) {
            Configuration.instance().logger().severe("Impossible de trouver l'IA : " + name);
        }
        return instance;
    }

    public final void activeIA() {
        gaufre = jeu.gaufre().clone();
        initialise();
    }

    public final Sequence<Coup> elaboreCoups() {
        gaufre = jeu.gaufre().clone();
        return joue();
    }

    public Sequence<Coup> joue() {
        return null;
    }

    public void initialise() {
    }

    public void finalise() {
    }
}
