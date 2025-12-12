# Étape 1 : Création de l'image d'exécution légère
# Utilisation d'une image Java alpine optimisée pour la petite taille
FROM amazoncorretto:17-alpine-jdk

WORKDIR /app

# ✅ COPIE DIRECTE DU JAR LOCAL
# Ce fichier doit être compilé avec 'mvn clean package' juste avant le docker build.
# Assurez-vous que le nom correspond bien à ce qui est généré dans le dossier 'target'.
COPY target/api-gateway-0.0.1-SNAPSHOT.jar app.jar

# Le port de la Gateway (tel que configuré dans application.yml)
EXPOSE 8000

# Commande pour lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]