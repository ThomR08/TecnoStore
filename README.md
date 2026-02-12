# 📱 TecnoStore

Sistema de gestión de ventas para una tienda de celulares desarrollado en **Java**, utilizando arquitectura en capas y patrón DAO para el acceso a datos.

---

## 📌 Descripción

**TecnoStore** es una aplicación de consola que permite administrar:

- 📦 Celulares
    
- 👤 Clientes
    
- 🏷️ Marcas
    
- 🧾 Ventas y Detalles de Venta
    
- 📊 Reportes (TXT y CSV)
    

El sistema está estructurado siguiendo buenas prácticas de separación de responsabilidades:

- `model` → Entidades del negocio
    
- `dao` → Acceso a base de datos
    
- `controller` → Lógica del negocio
    
- `view` → Menús y presentación
    
- `dto` → Objetos de transferencia de datos
    
- `utils` → Utilidades generales
    

---

## 🏗️ Arquitectura del Proyecto

`com.mycompany.tecnostore │ ├── controller    → Lógica del negocio ├── dao           → Acceso a base de datos (Patrón DAO) ├── database      → Scripts SQL ├── dto           → Data Transfer Objects ├── model         → Entidades del sistema ├── utils         → Clases auxiliares ├── view          → Menús del sistema └── TecnoStore.java → Clase principal`

---

## 🧠 Funcionalidades

### 📱 Gestión de Celulares

- Registrar celular
    
- Listar celulares
    
- Modificar información
    
- Control de stock
    

### 👤 Gestión de Clientes

- Registrar cliente
    
- Listar clientes
    
- Buscar por documento
    

### 🧾 Gestión de Ventas

- Registrar venta con múltiples detalles
    
- Cálculo automático de:
    
    - Subtotal
        
    - IVA
        
    - Total
        
- Asociación con cliente
    

### 📊 Reportes

- Generar archivo `reporte_ventas.txt`
    
- Generar archivo `reporte_ventas.csv`
    
- Reportes usando DTO:
    
    - Celulares más vendidos
        
    - Ventas mensuales
        

---

## 🗄️ Base de Datos

El proyecto incluye scripts SQL en:

`database/DataBaseTecnoStore.sql`

Tablas principales:

- `cliente`
    
- `celular`
    
- `marca`
    
- `venta`
    
- `detalle_venta`
    

Relaciones:

- Una venta pertenece a un cliente
    
- Una venta tiene múltiples detalles
    
- Un detalle pertenece a un celular
    

---

## 🛠️ Tecnologías Utilizadas

- ☕ Java
    
- 🗄️ MySQL
    
- 📂 JDBC
    
- 🧱 Patrón DAO
    
- 📑 Arquitectura en capas
    
- 📊 CSV (compatible con Excel)
    

---

## 🚀 Cómo Ejecutar el Proyecto

1. Crear la base de datos en MySQL.
    
2. Ejecutar el script:
    
    `database/DataBaseTecnoStore.sql`
    
3. Configurar credenciales en:
    
    `dao/DBConnection.java`
    
4. Ejecutar:
    
    `TecnoStore.java`
    

---

## 📄 Reportes Generados

Los reportes pueden guardarse en la ubicación seleccionada por el usuario.

Formatos disponibles:

- `.txt`
    
- `.csv` (compatible con Excel y herramientas de análisis)
    

---

## 📚 Conceptos Aplicados

- Programación Orientada a Objetos
    
- Encapsulamiento
    
- Relaciones uno a muchos
    
- DTO (Data Transfer Object)
    
- Patrón DAO
    
- Manejo de archivos (FileWriter / BufferedWriter)
    
- Manejo de excepciones
    
- Arquitectura modular
    

---

## 🎯 Objetivo Académico

Este proyecto fue desarrollado como práctica para:

- Aplicar arquitectura en capas
    
- Implementar acceso a base de datos con JDBC
    
- Modelar correctamente relaciones con claves foráneas
    
- Generar reportes estructurados
    
- Simular un sistema real de ventas
    

---

## 👨‍💻 Autor

Thomas Ramirez  
Proyecto académico – Java + MySQL
