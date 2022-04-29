package Modele;

import Patterns.Commande;
import Structures.Sequence;
import Global.Configuration;

import java.awt.Point;

public class Coup extends Commande {

    Sequence<Point> bouchee;
    Gaufre gaufre;

    Coup() {
        bouchee = Configuration.instance().nouvelleSequence();
    }

    void fixerGaufre(Gaufre g) {
        gaufre = g;
    }

    void mange(int coupX, int coupY) {
        bouchee.insereQueue(new Point(coupX, coupY));
    }

    Sequence<Point> bouchees() {
        return bouchee;
    }

    void X() {
        // Iterateur<Point> iterateur = bouchee.iterateur();
        // while (iterateur.aProchain()) {
            // Point pointcourant = (Point) iterateur.prochain();
            // pointcourant.x =
            // pointcourant.y =
        // }
    }

    @Override
    public void execute() {
        X();
    }

    @Override
    public void desexecute() {
        X();
    }
    
}
