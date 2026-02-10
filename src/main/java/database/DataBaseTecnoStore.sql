-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `TecnoStore` DEFAULT CHARACTER SET utf8 ;
USE `TecnoStore` ;

-- -----------------------------------------------------
-- Table `mydb`.`Marca`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TecnoStore`.`Marca` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `nombre_UNIQUE` (`nombre` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Celular`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TecnoStore`.`Celular` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `marca` INT UNSIGNED NOT NULL,
  `modelo` VARCHAR(45) NOT NULL,
  `precio` DECIMAL(12,0) UNSIGNED NOT NULL,
  `stock` INT UNSIGNED NOT NULL DEFAULT 0,
  `sistema_operativo` ENUM('ANDROID', 'IOS', 'HARMONY_OS') NOT NULL,
  `gama` ENUM('BAJA', 'MEDIA', 'ALTA') NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Celular_1_idx` (`marca` ASC) VISIBLE,
  UNIQUE INDEX `marca_modelo_UNIQUE` (`marca`, `modelo` ASC) VISIBLE,
  CONSTRAINT `fk_Celular_1`
    FOREIGN KEY (`marca`)
    REFERENCES `mydb`.`Marca` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TecnoStore`.`Cliente` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `documento` VARCHAR(45) NOT NULL,
  `tipo_documento` ENUM('CC', 'CE', 'TI', 'PASAPORTE') NOT NULL DEFAULT 'CC',
  `correo` VARCHAR(45) NOT NULL,
  `telefono` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `correo_UNIQUE` (`correo` ASC) VISIBLE,
  UNIQUE INDEX `documento_tipo_UNIQUE` (`documento`,`tipo_documento` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Venta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TecnoStore`.`Venta` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `cliente_id` INT UNSIGNED NOT NULL,
  `fecha` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `subtotal` DECIMAL(12,0) UNSIGNED NOT NULL,
  `iva` DECIMAL(12,0) UNSIGNED NOT NULL,
  `total` DECIMAL(12,0) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Venta_1_idx` (`cliente_id` ASC) VISIBLE,
  CONSTRAINT `fk_Venta_1`
    FOREIGN KEY (`cliente_id`)
    REFERENCES `mydb`.`Cliente` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`DetalleDeVenta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TecnoStore`.`DetalleDeVenta` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `venta_id` INT UNSIGNED NOT NULL,
  `celular_id` INT UNSIGNED NOT NULL,
  `cantidad` INT UNSIGNED NOT NULL DEFAULT 1,
  `precio` DECIMAL(12,0) UNSIGNED NOT NULL,
  `subtotal` DECIMAL(12,0) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_DetalleDeVenta_1_idx` (`venta_id` ASC) VISIBLE,
  INDEX `fk_DetalleDeVenta_2_idx` (`celular_id` ASC) VISIBLE,
  CONSTRAINT `fk_DetalleDeVenta_1`
    FOREIGN KEY (`venta_id`)
    REFERENCES `mydb`.`Venta` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_DetalleDeVenta_2`
    FOREIGN KEY (`celular_id`)
    REFERENCES `mydb`.`Celular` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;