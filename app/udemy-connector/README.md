# udemy-connector

Servicio para comunicarse con Udemy.

# Tabla de contenidos
- [udemy-connector](#udemy-connector)
- [Tabla de contenidos](#tabla-de-contenidos)
- [Variables de entorno](#variables-de-entorno)
  - [Variables de entorno obligatorias](#variables-de-entorno-obligatorias)
  - [Variables de entorno opcionales](#variables-de-entorno-opcionales)
- [Respuestas de Udemy modeladas](#respuestas-de-udemy-modeladas)

---

# Variables de entorno

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
| `env.prop.file` | Ruta absoluta a un archivo *.properties* del que se se pueden asignar las variables de entorno. |
| `udemy.default-page-size` | Cantidad elementos por página retornados por cada consulta a Udemy. Udemy indica no superar 100. |
| `udemy.course.url` | Url de la lista de catalogo del portal de aprendizaje de Udemy, ingresando por id de curso. |
| `udemy.course.url.query-params` | Argumentos de busqueda para acotar resultados o especificar atributos a recuperar al consultar contra `udemy.course.url`. |
| `udemy.courses.url` | Url de la lista de catalogo del portal de aprendizaje de Udemy. |
| `udemy.courses.url.query-params` | Argumentos de busqueda para acotar resultados o especificar atributos a recuperar al consultar contra `udemy.courses.url`. |
| `udemy.user-activity.url` | Url para actividad de usuarios del portal de aprendizaje de Udemy. |
| `udemy.user-activity.url.query-params` | Argumentos de busqueda para acotar resultados o especificar atributos a recuperar al consultar contra `udemy.user-activity.url`. |
| `udemy.user-course-activity.url` | Url para actividad de consumo de usuarios por curso del portal de aprendizaje de Udemy. |
| `udemy.user-course-activity.url.query-params` | Argumentos de busqueda para acotar resultados o especificar atributos a recuperar al consultar contra `udemy.user-course-activity.url`. |
| `udemy.user-progress.url` | Url para actividad de progreso de usuarios por curso del portal de aprendizaje de Udemy. |
| `udemy.user-progress.url.query-params` | Argumentos de busqueda para acotar resultados o especificar atributos a recuperar al consultar contra `udemy.user-progress.url`. |

---

# Respuestas de Udemy modeladas

- ✔️ /courses/list
- ✔️ /courses/{course-id}
- ✔️ /analytics/user-activity/
- ✔️ /analytics/user-course-activity/
- ⏲ /analytics/user-progress/
  - ?user_email
  - ?from_date
  - ?to_date