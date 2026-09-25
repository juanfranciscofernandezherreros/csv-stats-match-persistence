![version](https://img.shields.io/badge/version-1.0.6-blue)
# csv-stats-match-persistence

Persistencia separada de `csv-stats-match`.

```text
stats-match.parsed -> csv-stats-match-persistence -> PostgreSQL match_summary
```

Consume `StatsMatchKey` / `StatsMatchValue` desde `stats-match.parsed` y persiste el resumen en PostgreSQL.

La responsabilidad del micro termina en la base de datos: **no publica topics de salida**, no gestiona `file.success` / `file.errors` y no mantiene `csv_status`. Si el `matchId` ya existe, ignora la reentrega.

El PR se fusiona automáticamente a `main` cuando pasan los checks y después elimina la rama origen.


## Contratos Avro compartidos

`StatsMatchKey` y `StatsMatchValue` se consumen desde `com.fernandez.basketball:basketball-event-contracts:1.0.2`. Este repositorio ya no mantiene copias locales de los schemas ni genera las clases Avro durante su propia build.

Fuera de GitHub Actions, Maven necesita credenciales con `read:packages` para resolver el artefacto desde GitHub Packages.
