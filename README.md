# Quarkus starter
This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running And Packaging

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

## Progress:
- Phase 1: A simple crud server + test, with a minimal fake-DB
- Phase 2: Quarkus CDI: Inject fake-DB into
- Phase 3: Hibernate orm + h2-DB