# Documentation du Projet School Management

## 1. Vue d'ensemble
Ce projet est une application de gestion scolaire développée avec Spring Boot. Elle permet de gérer les étudiants, les enseignants, les cours, les classes, les notes, et les emplois du temps. L'application offre une API REST sécurisée ainsi qu'une interface web utilisateur utilisant Thymeleaf.

## 2. Technologies Utilisées
-   **Java** 21
-   **Spring Boot** 3.2.1
-   **Spring Data JPA** (Hibernate)
-   **Spring Security** (avec JWT pour l'API)
-   **Thymeleaf** (Moteur de template pour le frontend)
-   **MySQL** (Base de données)
-   **Lombok** (Réduction du code boilerplate)
-   **iText** (Génération de PDF)

## 3. Architecture du Projet
Le projet suit une architecture en couches classique :
-   `com.example.SpringProject.controller`: Contrôleurs Web (Thymeleaf) et API (REST).
-   `com.example.SpringProject.service`: Logique métier.
-   `com.example.SpringProject.repository`: Accès aux données (JPA Repository).
-   `com.example.SpringProject.entity`: Modèles de données (JPA Entities).
-   `com.example.SpringProject.security`: Configuration de la sécurité et filtre JWT.
-   `com.example.SpringProject.dto`: Objets de transfert de données.

## 4. Prérequis et Installation
### Prérequis
-   JDK 21 installé.
-   Maven installé.
-   Serveur MySQL en cours d'exécution.

### Configuration de la Base de Données
Le fichier `src/main/resources/application.properties` contient la configuration. Assurez-vous que votre serveur MySQL est configuré pour correspondre, ou modifiez ce fichier :
```properties
spring.datasource.url=jdbc:mysql://localhost:3307/school_management
spring.datasource.username=root
spring.datasource.password=
server.port=8081
```
Assurez-vous de créer une base de données nommée `school_management` si nécessaire (le paramètre `ddl-auto=update` tentera de mettre à jour le schéma).

### Lancer l'application
Pour démarrer l'application, utilisez la commande Maven suivante à la racine du projet :
```bash
mvn spring-boot:run
```
L'application sera accessible sur `http://localhost:8081`.

## 5. Fonctionnalités Principales
-   **Authentification**: Système de connexion sécurisé pour les différents rôles.
-   **Gestion des Utilisateurs**: Administration des comptes Administrateurs, Enseignants, et Étudiants.
-   **Gestion Scolaire**: Création et gestion des Classes, Cours, Groupes d'étudiants (TD/TP).
-   **Pédagogie**: Saisie et consultation des notes, Génération de bulletins, Gestion de l'emploi du temps.
