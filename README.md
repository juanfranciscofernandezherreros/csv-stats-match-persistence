![version](https://img.shields.io/badge/version-1.1.1-blue)
# csv-stats-match-persistence

Persistencia separada de `csv-stats-match`.

```text
stats-match.parsed -> csv-stats-match-persistence -> PostgreSQL match_summary
```

Consume `StatsMatchKey` / `StatsMatchValue` desde `stats-match.parsed` y persiste el resumen en PostgreSQL.

La responsabilidad del micro termina en la base de datos: **no publica topics de salida**, no gestiona `file.success` / `file.errors` y no mantiene `csv_status`. Si el `matchId` ya existe, ignora la reentrega.

El PR se fusiona automáticamente a `main` cuando pasan los checks y después elimina la rama origen.


## Contratos Avro compartidos

`StatsMatchKey` y `StatsMatchValue` se consumen desde `com.fernandez.basketball:basketball-event-contracts:1.0.2`. Este repositorio ya no mantiene copias locales de esos schemas.


## Estrategia de errores Kafka

KAN-107 aplica la política de KAN-18 al consumo de `stats-match.parsed`.

- errores de datos o integridad: non-retryable;
- fallos transitorios de PostgreSQL: retryable;
- mensajes agotados: `stats-match.parsed.DLT`;
- `KAFKA_RETRY_MAX_ATTEMPTS`: intentos totales, default `3`;
- `KAFKA_RETRY_BACKOFF_MS`: backoff fijo, default `1000`;
- `KAFKA_MATCH_PERSISTENCE_DLT_TOPIC`: topic DLT configurable.

Spring Kafka publica el registro original en DLT con headers de excepción y contexto.


### Deserialización y DLT

Los deserializadores Avro están envueltos con `ErrorHandlingDeserializer`, por lo que un payload corrupto o incompatible entra en el flujo normal de recuperación. La DLT `stats-match.parsed.DLT` admite objetos Avro y `byte[]` originales, conserva los headers de diagnóstico, deja que Kafka seleccione una partición válida y propaga cualquier fallo de publicación.
