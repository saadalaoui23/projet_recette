# =================================================================
# ÉTAPE 1 : BUILDER (Compilation du projet Spring Boot)
# =================================================================
# Utilise Maven et Java 17 pour compiler
FROM maven:3.9.6-eclipse-temurin-17 AS planner-builder

# Définir le répertoire de travail
WORKDIR /app

# Copier les fichiers de construction (pom.xml)
COPY pom.xml .

# Télécharger toutes les dépendances Maven (pour le cache)
RUN mvn dependency:go-offline

# Copier le reste du code source
COPY src ./src

# Compiler et créer le JAR exécutable. L'étape 'test' est sautée (-DskipTests)
RUN mvn clean package -DskipTests

# =================================================================
# ÉTAPE 2 : RUNNER (Création de l'image finale légère)
# =================================================================

# Utiliser une image JRE minimale basée sur Alpine pour la légèreté
FROM eclipse-temurin:17-jre-alpine

# AJOUT DE L'UTILISATEUR SÉCURISÉ (évite l'exécution en root)
RUN addgroup -S spring && adduser -S spring -G spring -u 10001
USER 10001

# Définir le répertoire de travail
WORKDIR /app

# Copier le JAR créé à l'étape 'builder' vers l'image finale.
# Le JAR a le nom par défaut de Maven.
COPY --from=planner-builder /app/target/*.jar app.jar

# Définir la variable d'environnement du port
ENV SERVER_PORT=8082

# Exposer le port de l'application
EXPOSE 8082

# Commande par défaut pour exécuter l'application
ENTRYPOINT ["java", "-jar", "app.jar"]