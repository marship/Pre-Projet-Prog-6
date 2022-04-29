package Structures;

public class SequenceTableauCirculaire<Loulou> implements Sequence<Loulou> {

    Object[] tableauElements;
    int debut;
    int taille;

    public SequenceTableauCirculaire() {
        tableauElements = new Object[1];
    }

    private void redimensionne() {

        if (taille >= tableauElements.length) {
            Object[] nouveau = new Object[tableauElements.length * 2];
            int fin = Math.min(debut + taille, tableauElements.length);
            for (int i = debut; i < fin; i++) {
                nouveau[i] = tableauElements[i];
            }
            fin = (debut + taille) - tableauElements.length;
            for (int i = 0; i < fin; i++) {
                nouveau[i + tableauElements.length] = tableauElements[i];
            }
            tableauElements = nouveau;
        }
    }

    // Insere element en debut de sequence (en premiere position)
    public void insereTete(Loulou element) {

        redimensionne();
        debut--;
        if (debut < 0) {
            debut = debut + tableauElements.length;
        }
        tableauElements[debut] = element;
        taille++;
    }

    // Insere element en fin de sequence (en derniere position)
    public void insereQueue(Loulou element) {

        redimensionne();
        int position = (debut + taille) % tableauElements.length;
        tableauElements[position] = element;
        taille++;
    }

    // Extrait + Renvoie la valeur de l'element situe en debut de sequence (en
    // première position)
    @SuppressWarnings("unchecked")
    public Loulou extraitTete() {

        if (taille == 0) {
            throw new RuntimeException("TabCirc Sequence vide");
        }
        Loulou resultat = (Loulou) tableauElements[debut];
        taille--;
        debut = (debut + 1) % tableauElements.length;
        return resultat;
    }

    // Renvoie vrai ssi la sequence est vide
    public boolean estVide() {
        return taille == 0;
    }

    public Iterateur<Loulou> iterateur() {
        return new IterateurTableauCirculaire<>(this);
    }

    public String toString() {
        String resultat = "Sequence tableau circulaire : [ ";
        int fin = Math.min(debut + taille, tableauElements.length);
        for (int i = debut; i < fin; i++) {
            resultat = resultat + tableauElements[i] + " ";
        }
        fin = (debut + taille) - tableauElements.length;
        for (int i = 0; i < fin; i++) {
            resultat = resultat + tableauElements[i] + " ";
        }
        resultat = resultat + "]";
        return resultat;
    }
}
