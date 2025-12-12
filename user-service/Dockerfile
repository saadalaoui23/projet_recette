# Étape 1 : Construction de l'application (Utilisation de Maven pour construire le JAR)
FROM maven:3.9.5-amazoncorretto-17 AS build
WORKDIR /app

# Copier les fichiers de construction (pom.xml) et télécharger les dépendances pour mise en cache
COPY pom.xml .
RUN mvn dependency:go-offline

# Copier le reste du code source
COPY src ./src

# Construire le JAR exécutable (le -DskipTests est crucial pour la rapidité)
RUN mvn clean package -DskipTests

# Étape 2 : Création de l'image d'exécution légère
FROM amazoncorretto:17-alpine-jdk
WORKDIR /app

# Copier le JAR du Service Utilisateur depuis l'étape de construction
COPY --from=build /app/target/*.jar app.jar

# Définir le port exposé
EXPOSE 8083

# Commande pour lancer l'application
# (Les variables d'environnement JWT_SECRET_KEY, SPRING_DATASOURCE_URL, etc.,
# seront passées manuellement lors de la commande 'docker run'.)
ENTRYPOINT ["java", "-jar", "app.jar"]