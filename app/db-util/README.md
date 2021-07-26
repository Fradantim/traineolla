# db-util

Utilidades para el control de base de datos.

# Tabla de contenidos
- [db-util](#db-util)
- [Tabla de contenidos](#tabla-de-contenidos)
- [Reporte Sonarqube](#reporte-sonarqube)
- [Variables de entorno](#variables-de-entorno)
  - [Variables de entorno obligatorias](#variables-de-entorno-obligatorias)
  - [Variables de entorno opcionales](#variables-de-entorno-opcionales)

---

# Reporte Sonarqube

[![Bugs](http://go.tempestad-online.com:19000/api/project_badges/measure?project=db-util&metric=bugs)](http://go.tempestad-online.com:19000/dashboard?id=db-util) [![Code Smells](http://go.tempestad-online.com:19000/api/project_badges/measure?project=db-util&metric=code_smells)](http://go.tempestad-online.com:19000/dashboard?id=db-util) [![Coverage](http://go.tempestad-online.com:19000/api/project_badges/measure?project=db-util&metric=coverage)](http://go.tempestad-online.com:19000/dashboard?id=db-util) [![Duplicated Lines (%)](http://go.tempestad-online.com:19000/api/project_badges/measure?project=db-util&metric=duplicated_lines_density)](http://go.tempestad-online.com:19000/dashboard?id=db-util) [![Lines of Code](http://go.tempestad-online.com:19000/api/project_badges/measure?project=db-util&metric=ncloc)](http://go.tempestad-online.com:19000/dashboard?id=db-util) [![Maintainability Rating](http://go.tempestad-online.com:19000/api/project_badges/measure?project=db-util&metric=sqale_rating)](http://go.tempestad-online.com:19000/dashboard?id=db-util) [![Quality Gate Status](http://go.tempestad-online.com:19000/api/project_badges/measure?project=db-util&metric=alert_status)](http://go.tempestad-online.com:19000/dashboard?id=db-util) [![Reliability Rating](http://go.tempestad-online.com:19000/api/project_badges/measure?project=db-util&metric=reliability_rating)](http://go.tempestad-online.com:19000/dashboard?id=db-util) [![Security Rating](http://go.tempestad-online.com:19000/api/project_badges/measure?project=db-util&metric=security_rating)](http://go.tempestad-online.com:19000/dashboard?id=db-util) [![Technical Debt](http://go.tempestad-online.com:19000/api/project_badges/measure?project=db-util&metric=sqale_index)](http://go.tempestad-online.com:19000/dashboard?id=db-util) [![Vulnerabilities](http://go.tempestad-online.com:19000/api/project_badges/measure?project=db-util&metric=vulnerabilities)](http://go.tempestad-online.com:19000/dashboard?id=db-util)

---

# Variables de entorno

Este proyecto hereda todas las configuraciones de variables de entorno de su padre [oreport-parent](../oreport-parent).

## Variables de entorno obligatorias

| Nombre | Detalle |
| - | - |
| `spring.datasource.driverClassName` | Driver de conexion a la BBDD. Para H2: `org.h2.Driver` |
| `spring.datasource.url` | String de conexion a la BBDD. Para H2 local: `jdbc:h2:file./oreport;DB_CLOSE_DELAY=-1` |
| `spring.datasource.password` | Contraseña del usuario de BBDD. Para H2 local puede no setearse. |
| `spring.datasource.username` | Usuario de BBDD. |
| `spring.jpa.database-platform` | Dialecto de la BBDD. Para H2: `org.hibernate.dialect.H2Dialect` |

## Variables de entorno opcionales

Valores por defecto (y ejemplos) en [application.properties](src/main/resources/application.properties).

| Nombre | Detalle |
| - | - |
| `h2.enabled` | Indica si el servicio levantará o no una base H2 local. De ser falso la aplicación termina. |
| `h2.console.enabled` | Indica si exponer o no la ruta HTTP `/console` como terminal de BBDD remota. |
| `h2.port` | Puerto en el que se hosteara la BBDD H2. |
| `spring.liquibase.enabled` | Indica si se ejecutara el control de changelog de Liquibase. |