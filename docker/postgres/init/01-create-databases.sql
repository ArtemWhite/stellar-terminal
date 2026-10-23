-- База `catalog` создаётся entrypoint'ом postgres из POSTGRES_DB.
-- Здесь — две оставшиеся базы для satellite-service и user-service.
-- `user` зарезервировано в PostgreSQL, поэтому берётся в кавычки.
CREATE DATABASE satellite;
CREATE DATABASE "user";
