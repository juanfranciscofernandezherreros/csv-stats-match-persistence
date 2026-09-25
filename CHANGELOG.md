# Changelog

## 1.0.6 - 2026-09-25

- [patch] KAN-81 sustituye los schemas locales `StatsMatchKey`/`StatsMatchValue` por `basketball-event-contracts:1.0.2`.
- [patch] Elimina generación Avro local y configura CI con lectura autenticada desde GitHub Packages.
- [patch] Mantiene el contrato Kafka y el modelo de persistencia PostgreSQL sin cambios.

## 1.0.5 - 2026-09-25

- [patch] Refuerza AGENTS.md con lectura obligatoria por tarea, autonomía y prohibición absoluta de escrituras directas en main.

## 1.0.4

- [patch] Estandariza la automatización del repositorio con el flujo autónomo de csv-results-parser.

## 1.0.3 - 2026-09-25

- [patch] Añade eliminación automática de la rama origen después de mergear una Pull Request en `main`.

## 1.0.2 - 2026-09-25

- [patch] Exige confirmar rama y nivel SemVer antes de cualquier cambio.

## 1.0.1 - 2026-09-24
- Simplifica persistence para que solo consuma Kafka y escriba en PostgreSQL.
- Elimina `file.success`, `file.errors`, `csv_status` y `processed_file_event`.
- Simplifica el contrato Avro al resumen que realmente se persiste.

## 1.0.0 - 2026-09-24
- Separa la persistencia de MATCH_SUMMARY.
- Consume `stats-match.parsed`.
- Añade auto-merge tras checks y borrado de rama.
