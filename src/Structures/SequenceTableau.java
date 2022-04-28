package Structures;

public class SequenceTableau<Mama> implements Sequence<Mama> {

    Object[] tableauElements;
    int taille;
    final int CAPACITE = 10;

    public SequenceTableau() {
        tableauElements = new Object[CAPACITE];
    }

    // Insere element en debut de sequence (en premiere position)
    public void insereTete(Mama element) {
        if (taille >= CAPACITE) {
            throw new RuntimeException("Tab Sequence pleine");
        }
        for (int i = taille; i > 0; i--) {
            tableauElements[i] = tableauElements[i - 1];
        }
        tableauElements[0] = element;
        taille++;
    }

    // Insere element en fin de sequence (en derniere position)
    public void insereQueue(Mama element) {
        if (taille >= CAPACITE) {
            throw new RuntimeException("Tab Sequence pleine");
        }
        tableauElements[taille] = element;
        taille++;
    }

    // Extrait + Renvoie la valeur de l'element situe en debut de sequence (en
    // première position)
    @SuppressWarnings("unchecked")
    public Mama extraitTete() {
        if (taille == 0) {
            throw new RuntimeException("Tab Sequence vide");
        }
        Mama resultat = (Mama) tableauElements[0];
        taille--;
        for (int i = 0; i < taille; i++) {
            tableauElements[i] = tableauElements[i + 1];
        }
        return resultat;
    }

    // Renvoie vrai ssi la sequence est vide
    public boolean estVide() {
        return taille == 0;
    }

    public Iterateur<Mama> iterateur() {
        return null;
    }

    public String toString() {
        String resultat = "Sequence tableau : [ ";
        for (int i = 0; i < taille; i++) {
            resultat = resultat + tableauElements[i] + " ";
        }
        resultat = resultat + "]";
        return resultat;
    }
}
