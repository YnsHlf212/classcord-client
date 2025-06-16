Projet ClassCord - Client Java

**Nom :** ADIDI
**Prénom :** Yanis

---

## Fonctionnalités développées

- Création d’un projet Java fonctionnel avec Maven sous Visual Studio Code.
- Intégration de la dépendance `org.json` dans le fichier `pom.xml`.
- Organisation du projet selon le modèle MVC avec les packages suivants :
  - `fr.classcord.model` : classes métier `User` et `Message`.
  - `fr.classcord.network` : package prévu pour la gestion réseau.
  - `fr.classcord.ui` : package prévu pour l’interface utilisateur.
  - `fr.classcord.app` : classe principale de lancement.
- Implémentation des classes métier `User` et `Message` avec leurs attributs, constructeurs, getters/setters.
- Test de compilation réussi avec Maven (`mvn compile`).
- Classe principale `App` simple affichant un message de bienvenue.

---

## Instructions pour lancer le projet

1. Cloner ou télécharger le projet.

2. Ouvrir le dossier du projet dans Visual Studio Code.

3. Vérifier que Maven est installé sur votre machine avec la commande :

   ```bash
   mvn -v