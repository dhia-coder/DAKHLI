# Config Server - ST2I Microservices

## Description
Le Config Server centralise toutes les configurations des microservices de l'architecture ST2I. Il utilise le mode natif (fichiers locaux) pour stocker les configurations.

## Architecture
- **Port**: 8888
- **Mode**: Native (fichiers locaux)
- **Découverte**: Eureka Client

## Services configurés

### 1. Eureka Server
- **Fichier**: `config/eureka.properties`
- **Port**: 8761
- **Fonction**: Service de découverte

### 2. Gateway Service
- **Fichier**: `config/gateway.properties`
- **Port**: 8065
- **Fonction**: API Gateway avec routage vers user-service

### 3. User Service
- **Fichier**: `config/user-service.properties`
- **Port**: 8081
- **Fonction**: Gestion des utilisateurs, authentification, autorisation

## Utilisation

### Démarrage local
```bash
cd backEnd/config-server
mvn spring-boot:run
```

### Accès aux configurations
- **Toutes les configurations**: `http://localhost:8888/application/default`
- **Configuration spécifique**: `http://localhost:8888/user-service/default`

## Structure des fichiers de configuration

```
config/
├── eureka.properties              # Configuration Eureka
├── gateway.properties             # Configuration Gateway
└── user-service.properties        # Configuration User Service
```

## Endpoints Actuator
- **Health**: `http://localhost:8888/actuator/health`
- **Info**: `http://localhost:8888/actuator/info`
- **Tous les endpoints**: `http://localhost:8888/actuator`

## Notes importantes
1. Le config-server doit démarrer avant les autres services
2. Les services clients doivent avoir la dépendance `spring-cloud-starter-config`
3. Les configurations locales des services sont minimales et surchargées par le config-server 