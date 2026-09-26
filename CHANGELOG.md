# Changelog

## 1.2.0 - 2026-09-26

- [minor] KAN-46 sustituye el first-write-wins implícito por una política current-state explícita para reimportaciones de MATCH.
- [minor] KAN-47 elimina la carrera `existsByMatchId() -> saveAndFlush()` mediante `INSERT ... ON CONFLICT (match_id) DO UPDATE`.
- [minor] Mantiene la constraint `UNIQUE(match_id)` como garantía de identidad en PostgreSQL.
- [minor] Añade tests de integración para primera importación, reimportación corregida y redelivery concurrente.

## 1.1.1 - 2026-09-26

- [patch] KAN-107 captura errores de deserialización Avro mediante `ErrorHandlingDeserializer`.
- [patch] Permite publicar en DLT objetos Avro y bytes crudos mediante `DelegatingByTypeSerializer`.
- [patch] Deja que Kafka elija la partición DLT y hace visible cualquier fallo de publicación en la DLT.
- [patch] Añade cobertura de deserialización fallida → DLT conservando los bytes originales.


## 1.1.0 - 2026-09-26

- [minor] KAN-107 aplica la estrategia común de errores Kafka de KAN-18.
- [minor] Clasifica errores de datos/integridad como non-retryable y fallos transitorios de PostgreSQL como retryable.
- [minor] Configura retries/backoff y DLT `stats-match.parsed.DLT`.
- [minor] Añade tests de error permanente y transitorio.


## 1.0.6 - 2026-09-25

- [patch] KAN-81 sustituye StatsMatchKey/StatsMatchValue locales por `basketball-event-contracts:1.0.2`.
- [patch] Elimina generación Avro local y autentica GitHub Packages en CI.
- [patch] Mantiene sin cambios la persistencia PostgreSQL y la deduplicación existente.


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
