CREATE TABLE IF NOT EXISTS match_summary (
 id BIGSERIAL PRIMARY KEY, match_id VARCHAR(255) NOT NULL UNIQUE,
 date VARCHAR(255), home_name VARCHAR(255), home_image VARCHAR(255), away_name VARCHAR(255), away_image VARCHAR(255),
 result_home VARCHAR(255), result_away VARCHAR(255), total_local VARCHAR(255), first_local VARCHAR(255), second_local VARCHAR(255),
 third_local VARCHAR(255), fourth_local VARCHAR(255), extra_local VARCHAR(255), total_away VARCHAR(255), first_away VARCHAR(255),
 second_away VARCHAR(255), third_away VARCHAR(255), fourth_away VARCHAR(255), extra_away VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS csv_status (
 id BIGSERIAL PRIMARY KEY, match_id VARCHAR(255), file_name VARCHAR(255), file_path VARCHAR(255), file_type VARCHAR(255),
 status VARCHAR(32), timestamp TIMESTAMP WITH TIME ZONE
);
CREATE TABLE IF NOT EXISTS processed_file_event (
 event_id VARCHAR(255) PRIMARY KEY, processed_at TIMESTAMP WITH TIME ZONE NOT NULL
);
