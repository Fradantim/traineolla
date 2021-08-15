# udemy-connector

Servicio para comunicarse con Udemy.

# Tabla de contenidos
- [udemy-connector](#udemy-connector)
- [Tabla de contenidos](#tabla-de-contenidos)
- [Reporte Sonarqube](#reporte-sonarqube)
- [Variables de entorno](#variables-de-entorno)
  - [Variables de entorno obligatorias](#variables-de-entorno-obligatorias)
  - [Variables de entorno opcionales](#variables-de-entorno-opcionales)
- [Cuestiones de recursos](#cuestiones-de-recursos)

---

# Reporte Sonarqube

[![Bugs](http://go.tempestad-online.com:19000/api/project_badges/measure?project=udemy-connector&metric=bugs)](http://go.tempestad-online.com:19000/dashboard?id=udemy-connector) [![Code Smells](http://go.tempestad-online.com:19000/api/project_badges/measure?project=udemy-connector&metric=code_smells)](http://go.tempestad-online.com:19000/dashboard?id=udemy-connector) [![Coverage](http://go.tempestad-online.com:19000/api/project_badges/measure?project=udemy-connector&metric=coverage)](http://go.tempestad-online.com:19000/dashboard?id=udemy-connector) [![Duplicated Lines (%)](http://go.tempestad-online.com:19000/api/project_badges/measure?project=udemy-connector&metric=duplicated_lines_density)](http://go.tempestad-online.com:19000/dashboard?id=udemy-connector) [![Lines of Code](http://go.tempestad-online.com:19000/api/project_badges/measure?project=udemy-connector&metric=ncloc)](http://go.tempestad-online.com:19000/dashboard?id=udemy-connector) [![Maintainability Rating](http://go.tempestad-online.com:19000/api/project_badges/measure?project=udemy-connector&metric=sqale_rating)](http://go.tempestad-online.com:19000/dashboard?id=udemy-connector) [![Quality Gate Status](http://go.tempestad-online.com:19000/api/project_badges/measure?project=udemy-connector&metric=alert_status)](http://go.tempestad-online.com:19000/dashboard?id=udemy-connector) [![Reliability Rating](http://go.tempestad-online.com:19000/api/project_badges/measure?project=udemy-connector&metric=reliability_rating)](http://go.tempestad-online.com:19000/dashboard?id=udemy-connector) [![Security Rating](http://go.tempestad-online.com:19000/api/project_badges/measure?project=udemy-connector&metric=security_rating)](http://go.tempestad-online.com:19000/dashboard?id=udemy-connector) [![Technical Debt](http://go.tempestad-online.com:19000/api/project_badges/measure?project=udemy-connector&metric=sqale_index)](http://go.tempestad-online.com:19000/dashboard?id=udemy-connector) [![Vulnerabilities](http://go.tempestad-online.com:19000/api/project_badges/measure?project=udemy-connector&metric=vulnerabilities)](http://go.tempestad-online.com:19000/dashboard?id=udemy-connector)

---

# Variables de entorno

Este proyecto hereda todas las configuraciones de variables de entorno de su padre [oreport-parent](../oreport-parent).

## Variables de entorno obligatorias

| Nombre | Detalle |
| - | - |
| `udemy.organization.name` | Nombre del portal de Udemy. Ej: `accenture-sandbox` |
| `udemy.organization.id` | Id del portal de Udemy. Ej: `161730` |
| `udemy.client.auth-header` | Header HTTP de autorización que se envia con las consultas a Udemy. Si se poosee puede omitir cargar `udemy.client.id` y `udemy.client.secret`. |
| `udemy.client.id` | Id del cliente utilizado para autenticar contra Udemy. |
| `udemy.client.secret` | Secreto del cliente utilizado para autenticar contra Udemy. |

## Variables de entorno opcionales

Valores por defecto (y ejemplos) en [application.properties](src/main/resources/application.properties).

| Nombre | Detalle |
| - | - |
| `api-docs.courses.description` | Documentacion de OpenApi. |
| `api-docs.courses.id.description` | Documentacion de OpenApi. |
| `api-docs.request.udemy.params.description` | Documentacion de OpenApi. |
| `api-docs.user-course-activity.description` | Documentacion de OpenApi. |
| `request.udemy.params.identifier` | Los request-param que empiecen con este valor serán incluidos directamente en la consulta a Udemy. |
| `udemy.default-page-size` | Cantidad elementos por página retornados por cada consulta a Udemy. Udemy indica no superar 100 y recomienda utilizar 20. <br /> **IMPORTANTE**: Ver [Cuestiones de recursos](#cuestiones-de-recursos) |
| `udemy.course.url` | Url de la lista de catalogo del portal de aprendizaje de Udemy, ingresando por id de curso. |
| `udemy.course.url.query-params` | Argumentos de busqueda para acotar resultados o especificar atributos a recuperar al consultar contra `udemy.course.url`. |
| `udemy.courses.url` | Url de la lista de catalogo del portal de aprendizaje de Udemy. |
| `udemy.courses.url.query-params` | Argumentos de busqueda para acotar resultados o especificar atributos a recuperar al consultar contra `udemy.courses.url`. <br /> **IMPORTANTE**: Ver [Cuestiones de recursos](#cuestiones-de-recursos) |
| `udemy.user-activity.url` | **EN DESUSO** Url para actividad de usuarios del portal de aprendizaje de Udemy. |
| `udemy.user-activity.url.query-params` | **EN DESUSO** Argumentos de busqueda para acotar resultados o especificar atributos a recuperar al consultar contra `udemy.user-activity.url`. |
| `udemy.user-course-activity.url` | Url para actividad de consumo de usuarios por curso del portal de aprendizaje de Udemy. |
| `udemy.user-course-activity.url.query-params` | Argumentos de busqueda para acotar resultados o especificar atributos a recuperar al consultar contra `udemy.user-course-activity.url`. |
| `udemy.user-progress.url` | **EN DESUSO** Url para actividad de progreso de usuarios por curso del portal de aprendizaje de Udemy. |
| `udemy.user-progress.url.query-params` | **EN DESUSO** Argumentos de busqueda para acotar resultados o especificar atributos a recuperar al consultar contra `udemy.user-progress.url`. |

---

# Cuestiones de recursos

Si se consulta una pagina muy grande, supongamos de 100 elementos, y estos elementos traen atributos grandes, como la descripcion de un curso, es muy facil que el tamaño de la respuesta supere el permitido por el cliente http reactivo de Spring, WebClient (256KB). Existen dos alternativas en forma de variables de entorno para controlar esto:

| Alternativa | Detalle |
| - | - |
| `spring.codec.max-in-memory-size=512KB` | *A ojo*, en 512KB entran 100 elementos y su descripcion. |
| `spring.codec.max-in-memory-size=-1` | Desactivar el limite . |
