Current version: **1.0.0**

# csv-stats-match-persistence

Persistencia separada de `csv-stats-match`.

```text
stats-match.parsed -> PostgreSQL + file.success/file.errors
```

Consume Avro `PARSED` / `FAILED`. Conserva las tablas `match_summary`, `csv_status` y `processed_file_event`, la idempotencia por `eventId` y el comportamiento de ignorar un `matchId` ya existente.

Cuando un resumen nuevo se guarda correctamente publica `file.success`; un mensaje `FAILED` registra `ERROR` y publica `file.errors`.

El PR se fusiona automáticamente a `main` cuando pasan los checks y después elimina la rama origen.
