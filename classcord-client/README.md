# Projet Semaine Intensive SLAM - BTS SIO 2024

## Nom du projet : ClassCord - Client de messagerie interopérable

### Public concerné
Étudiants option SLAM (Solutions Logicielles et Applications Métiers)

---

## ✨ Contexte du projet
Vous intégrez une équipe de développeurs chargée de créer une application cliente pour une plateforme de messagerie instantanée utilisée en réseau local dans un établissement. Baptisée **ClassCord**, cette solution a pour objectif de permettre aux utilisateurs (professeurs, étudiants) de communiquer en temps réel, de manière fluide et sécurisée.

Le serveur de tchat, déjà en place et géré par les étudiants SISR, fonctionne sur le réseau local. Chaque utilisateur se connecte à l'adresse IP de son choix pour interagir avec le serveur d'un camarade ou avec le serveur central de la salle. Votre formateur vous proposera dès le début du projet son propre serveur opérationnel afin que vous ayez toujours un serveur sur lequel vous pourrez vous connecter.

---

## 📆 Objectif pédagogique de la semaine
Réaliser un client Java Swing complet capable de se connecter à un serveur de tchat et d'offrir à l'utilisateur une interface fonctionnelle, réactive et évolutive.

Compétences mobilisées : modélisation, architecture logicielle, programmation réseau, interface graphique, documentation technique. Méthodologie de projet itérative.

---

## 🚀 Démarrage via GitHub
### Étapes :
1. Forkez le dépôt
2. Clonez votre fork :
```bash
git clone https://github.com/votre-identifiant/classcord-client.git
cd classcord-client
```
3. Enregistrez régulièrement :
```bash
git add .
git commit -m "ex: ajout interface de login"
git push origin main
```

### Contraintes GitHub pour la validation
- Travail sur votre fork uniquement
- Au moins 1 commit clair par jour
- Fichier `README.md` avec :
  - nom/prénom
  - fonctionnalités développées
  - instructions de lancement

---

## 📊 Cahier des charges fonctionnel
L'application cliente doit permettre :
- Connexion à un serveur (IP + port)
- Connexion invité (pseudo temporaire)
- Connexion avec compte (login/mot de passe)
- Affichage/envoi messages global
- Liste utilisateurs connectés
- Envoi messages privés
- Affichage des statuts (disponible/absent/invisible)
- Changement de statut personnel
- Interface Swing fluide

---

## 🪨 Cahier des charges technique
- **Java 11+**, **Swing**
- IDE : **VSCode** + **Maven**
- Dépendance : `org.json:json:20231013`
- Architecture : **MVC**
- Réception dans un thread séparé
- Socket TCP
- JSON avec `
` comme délimiteur

### Exemples JSON :
```json
{ "type": "message", "subtype": "global", "to": "global", "content": "Bonjour à tous !" }
{ "type": "message", "subtype": "private", "to": "bob", "content": "Salut Bob, dispo ?" }
{ "type": "login", "username": "alice", "password": "1234" }
{ "type": "status", "state": "invisible" }
```

---

## 📕 Jour 1 - Mise en place du projet et modélisation
Objectifs :
- Projet Maven fonctionnel
- Dépendance JSON
- Packages `model`, `network`, `ui`, `app`
- Classes `User`, `Message`
- Compilation sans erreur
- `Main.java` minimal

---

## 📖 Jour 2 - Connexion en mode invité
Objectifs :
- Connexion au serveur (IP/port)
- Envoi/réception messages JSON
- Affichage console ou UI simple
- Thread de réception indépendant

---

## 📗 Jour 3 - Authentification
Objectifs :
- Interface de login (Swing)
- Envoi JSON pour login/register
- Réception de réponse (succès/échec)
- Passage fluide à l'interface principale

---

## 📘 Jour 4 - Messages privés et liste utilisateurs
Objectifs :
- Affichage de la liste connectée (JList)
- Statuts via messages `status`
- Envoi de MP
- Affichage différencié MP vs Global

---

## 📙 Jour 5 - Statuts et finalisation
Objectifs :
- Choix de statut personnel (disponible/absent/invisible)
- Mise à jour dynamique dans la liste utilisateurs
- Finalisation graphique Swing
- Tests et débogage finaux
- Rédaction documentation (README, captures)

---

## 🌟 Démonstration finale
1. Lancer serveur
2. Lancer deux clients
3. Connexion, tchat global, MP
4. Changement de statut visible
5. Liste des connectés dynamique
6. Aucun crash à la déconnexion

---

© Projet pédagogique BTS SIO SLAM 2024
