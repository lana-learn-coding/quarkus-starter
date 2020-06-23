# Quarkus starter
This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running And Packaging

You may need:
- Java 11
- Graalvm 20

Run:
```
./mvnw quarkus:dev
```

Packaging:
```
./mvnw package
```

Build native executable:
```
./mvnw package -Pnative
```
Or container friendly
```
./gradlew build -Dquarkus.package.type=native -Dquarkus.native.container-build=true
```
## Progress:
- Phase 1: A simple crud server + test, with a minimal fake-DB
- Phase 2: Quarkus CDI: Inject fake-DB into
- Phase 3: Hibernate orm + h2-DB
- Phase 4: Vertx reactive route integration + test