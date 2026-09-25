Current version: **1.0.2**

# csv-stats-match-persistence

Persistencia separada de `csv-stats-match`.

```text
stats-match.parsed -> csv-stats-match-persistence -> PostgreSQL match_summary
```

Consume `StatsMatchKey` / `StatsMatchValue` desde `stats-match.parsed` y persiste el resumen en PostgreSQL.

La responsabilidad del micro termina en la base de datos: **no publica topics de salida**, no gestiona `file.success` / `file.errors` y no mantiene `csv_status`. Si el `matchId` ya existe, ignora la reentrega.

El PR se fusiona automáticamente a `main` cuando pasan los checks y después elimina la rama origen.
