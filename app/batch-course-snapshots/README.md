# batch-course-snapshots

Batch que actualiza informacion de los cursos consumidos.

# Tabla de contenidos
- [batch-course-snapshots](#batch-course-snapshots)
- [Tabla de contenidos](#tabla-de-contenidos)
- [Reporte Sonarqube](#reporte-sonarqube)
- [Diagrama de contexto](#diagrama-de-contexto)
- [Variables de entorno](#variables-de-entorno)
  - [Variables de entorno obligatorias](#variables-de-entorno-obligatorias)
  - [Variables de entorno opcionales](#variables-de-entorno-opcionales)

---

# Reporte Sonarqube

*TODO...*

---

# Diagrama de contexto

```
[batch-course-snapshots] ------------------> [udemy-connector]
   | INSERT               HTTP GET ND_JSON
   | ACCOUNT_COURSE_SNAPSHOT
  \ /
[BBDD] 
```

# Variables de entorno

Este proyecto hereda todas las configuraciones de variables de entorno de su padre [oreport-parent](../oreport-parent).

## Variables de entorno obligatorias

| Nombre | Detalle |
| - | - |
| `spring.datasource.url` | Url de la BBDD. Ejemplo para H2 local: `jdbc:h2:tcp://localhost:{PUERTO H2}/file./oreport;DB_CLOSE_DELAY=-1`|
| `spring.datasource.username` | Usuario de la conexion a la BBDD. |
| `spring.datasource.password` | Contraseña del usuario de la conexion a la BBDD. |
| `spring.datasource.driverClassName` | Driver de conexion a la BBDD. Para H2: `org.h2.Driver` |
| `udemy-connector.user-account-activity.url` | Url desde la cual conseguir la información de las actividades de Udemy. Ej para local: `http://localhost:8082/user-course-activity` |

## Variables de entorno opcionales

Valores por defecto (y ejemplos) en [application.properties](src/main/resources/application.properties).

| Nombre | Detalle |
| - | - |
| `transaction.size` | Cantidad de elementos por transacción hacia la bbdd. |
| `udemy-connector.query.page_size` | Cantidad de elementos por pagina a enviar al servicio [udemy-connector](../udemy-connector). |
| `udemy-connector.query-params` | Parametros de request tipicos que se envian al servicio [udemy-connector](../udemy-connector). |