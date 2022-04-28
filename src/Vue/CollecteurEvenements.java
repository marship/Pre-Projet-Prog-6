package Vue;

public interface CollecteurEvenements {
    
    void clicSouris(int coupX, int coupY);
	boolean commande(String com);
    void fixerInterfaceUtilisateur(InterfaceGraphique interfaceGraphique);
}
