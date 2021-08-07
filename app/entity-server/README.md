# entity-server

Servicio CRUD de entidades de OReport.

# Tabla de contenidos
- [entity-server](#entity-server)
- [Tabla de contenidos](#tabla-de-contenidos)
- [Reporte Sonarqube](#reporte-sonarqube)
- [Variables de entorno](#variables-de-entorno)
  - [Variables de entorno obligatorias](#variables-de-entorno-obligatorias)
  - [Variables de entorno opcionales](#variables-de-entorno-opcionales)

---

# Reporte Sonarqube

[![Bugs](http://go.tempestad-online.com:19000/api/project_badges/measure?project=entity-server&metric=bugs)](http://go.tempestad-online.com:19000/dashboard?id=entity-server) [![Code Smells](http://go.tempestad-online.com:19000/api/project_badges/measure?project=entity-server&metric=code_smells)](http://go.tempestad-online.com:19000/dashboard?id=entity-server) [![Coverage](http://go.tempestad-online.com:19000/api/project_badges/measure?project=entity-server&metric=coverage)](http://go.tempestad-online.com:19000/dashboard?id=entity-server) [![Duplicated Lines (%)](http://go.tempestad-online.com:19000/api/project_badges/measure?project=entity-server&metric=duplicated_lines_density)](http://go.tempestad-online.com:19000/dashboard?id=entity-server) [![Lines of Code](http://go.tempestad-online.com:19000/api/project_badges/measure?project=entity-server&metric=ncloc)](http://go.tempestad-online.com:19000/dashboard?id=entity-server) [![Maintainability Rating](http://go.tempestad-online.com:19000/api/project_badges/measure?project=entity-server&metric=sqale_rating)](http://go.tempestad-online.com:19000/dashboard?id=entity-server) [![Quality Gate Status](http://go.tempestad-online.com:19000/api/project_badges/measure?project=entity-server&metric=alert_status)](http://go.tempestad-online.com:19000/dashboard?id=entity-server) [![Reliability Rating](http://go.tempestad-online.com:19000/api/project_badges/measure?project=entity-server&metric=reliability_rating)](http://go.tempestad-online.com:19000/dashboard?id=entity-server) [![Security Rating](http://go.tempestad-online.com:19000/api/project_badges/measure?project=entity-server&metric=security_rating)](http://go.tempestad-online.com:19000/dashboard?id=entity-server) [![Technical Debt](http://go.tempestad-online.com:19000/api/project_badges/measure?project=entity-server&metric=sqale_index)](http://go.tempestad-online.com:19000/dashboard?id=entity-server) [![Vulnerabilities](http://go.tempestad-online.com:19000/api/project_badges/measure?project=entity-server&metric=vulnerabilities)](http://go.tempestad-online.com:19000/dashboard?id=entity-server)

---

# Variables de entorno

Este proyecto hereda todas las configuraciones de variables de entorno de su padre [oreport-parent](../oreport-parent).

## Variables de entorno obligatorias

| Nombre | Detalle |
| - | - |
| `spring.r2dbc.url` | Ejemplo para H2 local: `r2dbc:h2:tcp://localhost:{PUERTO H2}/file./oreport;DB_CLOSE_DELAY=-1`|
| `spring.r2dbc.username` | Usuario de la conexion a la BBDD. |
| `spring.r2dbc.password` | Contraseña del usuario de la conexion a la BBDD. |
| `spring.datasource.driverClassName` | Driver de conexion a la BBDD. Para H2: `org.h2.Driver` |

## Variables de entorno opcionales

Valores por defecto (y ejemplos) en [application.properties](src/main/resources/application.properties).

| Nombre | Detalle |
| - | - |
| `api-docs.request.mail.description` | Documentacion de OpenApi. |