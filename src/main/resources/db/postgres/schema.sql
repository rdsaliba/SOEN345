DROP SCHEMA IF EXISTS petclinic CASCADE;

CREATE SCHEMA IF NOT EXISTS petclinic;

SET SCHEMA 'petclinic';

CREATE TABLE IF NOT EXISTS vets (
  id SERIAL NOT NULL PRIMARY KEY,
  first_name VARCHAR(30),
  last_name VARCHAR(30)
);

CREATE INDEX ON vets (last_name);

CREATE TABLE IF NOT EXISTS specialties (
  id SERIAL NOT NULL PRIMARY KEY,
  name VARCHAR(80)
);

CREATE INDEX ON specialties (name);

CREATE TABLE IF NOT EXISTS vet_specialties (
  vet_id Integer NOT NULL,
  specialty_id Integer NOT NULL,
  FOREIGN KEY (vet_id) REFERENCES vets(id),
  FOREIGN KEY (specialty_id) REFERENCES specialties(id),
  UNIQUE (vet_id,specialty_id)
);

CREATE TABLE IF NOT EXISTS types (
  id SERIAL NOT NULL PRIMARY KEY,
  name VARCHAR(80)
);

CREATE INDEX ON types (name);

CREATE TABLE IF NOT EXISTS owners (
  id SERIAL NOT NULL PRIMARY KEY,
  first_name VARCHAR(30),
  last_name VARCHAR(30),
  address VARCHAR(255),
  city VARCHAR(80),
  telephone VARCHAR(20)
);

CREATE INDEX ON owners (last_name);

CREATE TABLE IF NOT EXISTS pets (
  id SERIAL NOT NULL PRIMARY KEY,
  name VARCHAR(30),
  birth_date DATE,
  type_id Integer NOT NULL,
  owner_id Integer NOT NULL,
  FOREIGN KEY (owner_id) REFERENCES owners(id),
  FOREIGN KEY (type_id) REFERENCES types(id)
);

CREATE INDEX ON pets (name);

CREATE TABLE IF NOT EXISTS visits (
  id SERIAL NOT NULL PRIMARY KEY,
  pet_id Integer NOT NULL,
  visit_date DATE,
  description VARCHAR(255),
  FOREIGN KEY (pet_id) REFERENCES pets(id)
);
