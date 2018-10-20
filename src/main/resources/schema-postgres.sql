/*
  Script for initializing database.
 */

DROP TABLE IF EXISTS node;
CREATE TABLE node(id SERIAL PRIMARY KEY, name VARCHAR(100), path TEXT);
CREATE INDEX path_brin_index ON node USING BRIN(path) WITH (pages_per_range = 128);