package Global;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import Structures.Sequence;
import Structures.SequenceListe;
import Structures.SequenceTableau;
import Structures.SequenceTableauCirculaire;

public class Configuration {

    static Configuration instance = null;
    Properties properties;
	Logger logger;

    public static Configuration instance() {
		if (instance == null)
			instance = new Configuration();
		return instance;
	}

    protected Configuration() {
        properties = new Properties();
        try {
            InputStream propertiesInputStream = charge("configGaufre.cfg");
            properties.load(propertiesInputStream);
            
            String home = System.getProperty("user.home");
		
	    File f = new File(home + File.separator + ".gaufreEmpoisonnee");
            if(!f.isFile()){ 
                f.createNewFile();
            }

            FileInputStream fileInputStream = new FileInputStream(home + File.separator + ".gaufreEmpoisonnee");
            properties = new Properties(properties);
            properties.load(fileInputStream);
        } catch (Exception e) {
            Configuration.instance().logger().warning("Erreur du chargement de la configuration : " + e + "\n");
        }
	}

    public static InputStream charge(String nom) {
        return ClassLoader.getSystemClassLoader().getResourceAsStream(nom);
    }

    public String lis(String cle) {
        String resultat = properties.getProperty(cle);
        if (resultat == null) {
            throw new NoSuchElementException("Propriete " + cle + " non definie dans 'configGaufre.cfg'");
        }
        return resultat;
    }

    public <E> Sequence<E> nouvelleSequence() {
        String type = lis("Sequence");
        switch (type) {
            case "TableauCirculaire":
                return new SequenceTableauCirculaire<>();
            case "Tableau":
                return new SequenceTableau<>();
            case "Liste":
                return new SequenceListe<>();
            default:
                throw new RuntimeException("Type de sequence inconnu : " + type);
        }
    }
    
    public Logger logger() {
		if (logger == null) {
			System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s : %5$s%n");
			logger = Logger.getLogger("Sokoban.Logger");
            logger.setLevel(Level.parse(lis("LogLevel")));
		}
		return logger;
	}
}
