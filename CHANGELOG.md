# Changelog

## 1.0.1 - 2026-09-24
- Simplifica persistence para que solo consuma Kafka y escriba en PostgreSQL.
- Elimina `file.success`, `file.errors`, `csv_status` y `processed_file_event`.
- Simplifica el contrato Avro al resumen que realmente se persiste.

## 1.0.0 - 2026-09-24
- Separa la persistencia de MATCH_SUMMARY.
- Consume `stats-match.parsed`.
- Añade auto-merge tras checks y borrado de rama.
