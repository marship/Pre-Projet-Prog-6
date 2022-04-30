### Pré-Projet de Prog 6

# Objectifs

    Tester le projet sur une petite échelle.
    Réfléchir à l’architecture logicielle de votre jeu.
    Appréhender le travail au sein de votre groupe.

# Mise en œuvre

    3 jours de travail.
    Revue de la part des enseignants le 3 mai (en présentiel).

# Non noté

### Contraintes de réalisation

## IHM

    Claire : état du jeu, tour, score, fonctionnalités
    Ergonomique : organisation, utilisation

## Niveaux pour l’IA

    Aléatoire
    Coup gagnant/perdant
    Arbre et/ou

## Fonctionnalités

    Jeu à deux joueurs sur une même fenêtre
    Historique : annuler / refaire sans limite
    Sauvegarder / charger avec historique
    Nouvelle partie
    Coup aléatoire parmi les meilleurs coups

### Membres du groupe

BALSA Raphaël
CASAGRANDE Dorian
FAURE-JEUNOT Paul
FAURIE Alban
GLEMBA Adrien
LAMBERT Mathis

### La gaufre empoisonnée

C'est un jeu à 2 joueurs. Le but du jeu est d'obliger l'autre joueur à jouer le coup perdant. 

Déroulement de la partie :

    L'un des deux joueurs commence.
    Ensuite, chacun joue à tour de rôle et est obligé de jouer (passer son tour est interdit) jusqu'à ce qu'un des joueurs joue le coup perdant.La partie est alors terminée, ce dernier joueur est déclaré perdant et l'autre gagnant.

### Structure de l'espace de travail

L'espace de travail contient trois dossiers :

- `src` : le dossier pour maintenir le code source.
- `res` : le dossier pour maintenir les ressources.
- `lib` : le dossier pour maintenir les dépendances.

Les fichiers de sortie compilés seront générés dans le dossier `bin`.

Le fichier `.gitignore` permet de ne pas polluer le dépôt Git.