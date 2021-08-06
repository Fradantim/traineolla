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

(Próximamente)

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
| N/A | N/A |