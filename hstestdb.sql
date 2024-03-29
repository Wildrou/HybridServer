
DROP DATABASE IF EXISTS hstestdb;
CREATE DATABASE hstestdb;
USE hstestdb;

CREATE TABLE HTML (
 uuid CHAR(36) NOT NULL,
 content TEXT NOT NULL,
 PRIMARY KEY (uuid));

CREATE TABLE XML (
 uuid CHAR(36) NOT NULL,
 content TEXT NOT NULL,
  PRIMARY KEY (uuid));

CREATE TABLE XSD (
 uuid CHAR(36) NOT NULL,
 content TEXT NOT NULL,
  PRIMARY KEY (uuid));
  
CREATE TABLE XSLT (
 uuid CHAR(36) NOT NULL,
 content TEXT NOT NULL,
 xsd CHAR(36),
  PRIMARY KEY (uuid));

CREATE USER hsdb@'%' IDENTIFIED BY 'hsdbpass';
GRANT ALL PRIVILEGES ON hstestdb.* TO hsdb@'%';
FLUSH PRIVILEGES;
  