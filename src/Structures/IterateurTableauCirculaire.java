package Structures;

public class IterateurTableauCirculaire<Fifi> extends Iterateur<Fifi> {

    int position;
    SequenceTableauCirculaire<Fifi> sequenceEnTableauCirculaire;

    IterateurTableauCirculaire(SequenceTableauCirculaire<Fifi> sequenceTableauCirculaire) {
        sequenceEnTableauCirculaire = sequenceTableauCirculaire;
    }

    @Override
    public boolean aProchain() {
        return position < sequenceEnTableauCirculaire.taille;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Fifi prochain() {
        super.prochain();
        int indice = (sequenceEnTableauCirculaire.debut + position)
                % sequenceEnTableauCirculaire.tableauElements.length;
        position++;
        return (Fifi) sequenceEnTableauCirculaire.tableauElements[indice];
    }

    @Override
    public void supprime() {
        super.prochain();
        assert (position > 0);
        for (int i = position; i < sequenceEnTableauCirculaire.taille; i++) {
            int precedent = (sequenceEnTableauCirculaire.debut + i - 1)
                    % sequenceEnTableauCirculaire.tableauElements.length;
            int suivant = (precedent + 1) % sequenceEnTableauCirculaire.tableauElements.length;
            sequenceEnTableauCirculaire.tableauElements[precedent] = sequenceEnTableauCirculaire.tableauElements[suivant];
        }
        sequenceEnTableauCirculaire.taille--;
        position--;
    }
}
