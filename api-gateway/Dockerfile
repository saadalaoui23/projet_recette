# =================================================================
# ÉTAPE 1 : BUILDER (Compilation du projet Spring Boot)
# Objectif : Compiler le code Java et générer le fichier JAR.
# =================================================================
# Utilise Maven et Java 17 (Eclipse Temurin est une bonne base stable)
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Définir le répertoire de travail pour la compilation
WORKDIR /app

# Copier le fichier de dépendances (pom.xml) et télécharger les dépendances.
# Cette étape est cruciale pour le cache Docker (si pom.xml ne change pas, on ne re-télécharge pas).
COPY pom.xml .
RUN mvn dependency:go-offline

# Copier le reste du code source
COPY src ./src

# Compiler et créer le JAR exécutable dans target/ (on saute les tests pour la rapidité)
RUN mvn clean package -DskipTests

# =================================================================
# ÉTAPE 2 : RUNNER (Création de l'image finale légère)
# Objectif : Créer une image d'exécution minimale contenant uniquement le JRE et le JAR.
# =================================================================

# Utiliser une image JRE légère basée sur Amazon Corretto 17 Alpine pour la petite taille.
FROM amazoncorretto:17-alpine-jdk

# Définir le répertoire de travail dans l'image finale
WORKDIR /app

# Copier le JAR créé à l'étape 'builder' vers l'image finale.
# Le caractère générique '*' permet de s'assurer que le nom du fichier JAR est capturé correctement.
COPY --from=builder /app/target/*.jar app.jar

# Définir le port que l'application écoute (tel que configuré dans votre application.yml)
EXPOSE 8000

# Commande par défaut pour lancer l'application Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]