# DRUGSTORE
Modelowanie i implementacja aplikacji biznesowych

## Testing


    mvnw test


## Docker
```
mvnw package -Pprod verify jib:dockerBuild
```

```
docker-compose -f src/main/docker/app.yml up -d
```
