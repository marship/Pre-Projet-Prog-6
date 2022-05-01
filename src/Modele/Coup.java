package Modele;

import Patterns.Commande;
import Structures.Iterateur;
import Structures.Sequence;
import Global.Configuration;

public class Coup extends Commande {

    Sequence<Position> positionBouchee;
    Gaufre gaufre;

    Coup() {
        positionBouchee = Configuration.instance().nouvelleSequence();
    }

    void fixerGaufre(Gaufre g) {
        gaufre = g;
    }

    void mange(int coupX, int coupY) {
        positionBouchee.insereQueue(new Position(coupX, coupY));
    }

    Sequence<Position> listeBouchees() {
        return positionBouchee;
    }

    @Override
    public void execute() {
        manger(0, 1);
    }

    @Override
    public void desexecute() {
        recracher(1, 0);
    }  

    // Utilisation pour Historique de coups (executer)
    void manger(int X, int Y) {
        Iterateur<Position> iterateur = positionBouchee.iterateur();
        while (iterateur.aProchain()) {
            Position position = (Position) iterateur.prochain();
            gaufre.jouerCoupGaufre(position.positionX, position.positionY);
        }
    }

    // Utilisation pour Historique de coups (desexecute)
    void recracher(int X, int Y) {
        Iterateur<Position> iterateur = positionBouchee.iterateur();
        while (iterateur.aProchain()) {
            Position position = (Position) iterateur.prochain();
            gaufre.dejouerCoupGaufre(position.positionX, position.positionY);
        }
    }  
}
