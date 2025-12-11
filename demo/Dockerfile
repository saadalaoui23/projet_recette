# =================================================================
# ÉTAPE 1 : BUILDER (Compilation du projet Spring Boot)
# =================================================================
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Définir le répertoire de travail à l'intérieur du conteneur
WORKDIR /app

# Copier le fichier de définition de dépendances (pom.xml)
# Cela permet à Docker de mettre en cache les dépendances si le pom.xml ne change pas
COPY pom.xml .

# Télécharger toutes les dépendances. Cette étape est mise en cache pour accélérer les builds futurs.
RUN mvn dependency:go-offline

# Copier le reste du code source
COPY src ./src

# Compiler et créer le JAR exécutable dans target/
RUN mvn clean package -DskipTests

# =================================================================
# ÉTAPE 2 : RUNNER (Création de l'image finale légère)
# =================================================================

# Utiliser une image minimale de JRE (Java Runtime Environment) pour exécuter l'application.
# eclipse-temurin est une excellente base stable et légère.
FROM eclipse-temurin:17-jre-alpine

RUN addgroup -S spring && adduser -S spring -G spring -u 10001

# Définir l'utilisateur d'exécution sur cet utilisateur nouvellement créé
USER 10001

# Définir le répertoire de travail
WORKDIR /app

# Copier le JAR créé à l'étape 'builder' vers l'image finale.
# Le JAR est nommé 'demo-0.0.1-SNAPSHOT.jar' par défaut par Maven.
# Si votre nom de fichier est différent, ajustez-le ici.
COPY --from=builder /app/target/*.jar app.jar

# Définir la variable d'environnement du port (pour Kubernetes)
ENV SERVER_PORT=8081

# Exposer le port sur lequel l'application s'exécute (pas obligatoire, mais bonne pratique)
EXPOSE 8081

# Commande par défaut pour exécuter l'application (l'entrée du conteneur)
# Java lance l'application Spring Boot via le JAR exécutable.
ENTRYPOINT ["java", "-jar", "app.jar"]