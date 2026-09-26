![version](https://img.shields.io/badge/version-1.2.1-blue)
# csv-stats-match-persistence

Persistencia separada de `csv-stats-match`.

```text
stats-match.parsed -> csv-stats-match-persistence -> PostgreSQL match_summary
```

Consume `StatsMatchKey` / `StatsMatchValue` desde `stats-match.parsed` y persiste el resumen en PostgreSQL.

La responsabilidad del micro termina en la base de datos: **no publica topics de salida**, no gestiona `file.success` / `file.errors` y no mantiene `csv_status`.

El PR se fusiona automáticamente a `main` cuando pasan los checks y después elimina la rama origen.

## Política de idempotencia y reimportación

KAN-46 y KAN-47 hacen explícita la política de MATCH definida por KAN-19.

La identidad de negocio es:

```text
match_id
```

La tabla mantiene una constraint `UNIQUE` sobre `match_id`. La política ya no es first-write-wins implícita: una reimportación del mismo partido **actualiza el resumen materializado actual**.

La escritura usa una única operación atómica:

```sql
INSERT INTO match_summary (...)
VALUES (...)
ON CONFLICT (match_id)
DO UPDATE SET ...;
```

Esto elimina la carrera `existsByMatchId() -> save()` y evita descartar silenciosamente una corrección.

Comportamiento definido:

- primera importación → inserta;
- redelivery idéntico → mantiene una única fila;
- reimportación corregida → actualiza el resumen existente;
- redelivery concurrente del mismo evento → converge en una única fila.

El contrato `StatsMatchValue` sí contiene `sourceEventId`, pero la tabla `match_summary` todavía no lo persiste. Esa trazabilidad está separada en KAN-48 y no se mezcla con KAN-46/KAN-47 para no ampliar el alcance de estos tickets.

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

## Decisión de rendimiento para MATCH

KAN-152 evalúa el batching de KAN-22 para este flujo y mantiene deliberadamente el consumo **registro a registro**.

La razón es la cardinalidad del dominio: cada fichero MATCH genera un único `StatsMatchValue` con el resumen del partido. Por tanto, el tamaño efectivo del lote por fichero es 1. Cambiar el listener a batch y usar `JdbcTemplate.batchUpdate(...)` no eliminaría ninguna sentencia ni ningún round-trip PostgreSQL para ese fichero; solo añadiría complejidad al listener y al manejo de errores.

La decisión es:

- mantener `ConsumerRecord<StatsMatchKey, StatsMatchValue>` en el listener;
- mantener un único `INSERT ... ON CONFLICT (match_id) DO UPDATE` por resumen;
- no activar `spring.kafka.listener.type=batch` ni `reWriteBatchedInserts` únicamente por uniformidad;
- conservar sin cambios el orden, la idempotencia current-state de KAN-46/KAN-47 y la estrategia retry/DLT de KAN-107.

Esta decisión es específica de MATCH. Los flujos que producen múltiples registros por fichero sí usan batching cuando reduce round-trips.

## Validación

Suite rápida:

```bash
mvn -B test
```

Suite de integración con PostgreSQL real:

```bash
mvn -B verify -Pintegration
```

La suite de integración cubre primera importación, reimportación corregida y redelivery concurrente.
