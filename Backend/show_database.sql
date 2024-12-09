USE ticketing_system;
SHOW TABLES;
SELECT table_name, column_name, data_type 
FROM information_schema.columns 
WHERE table_schema = 'ticketing_system' 
ORDER BY table_name, ordinal_position;
