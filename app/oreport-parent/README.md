# oreport-parent

Proyecto pom padre que provee administracion de dependencias y plugins para proyectos construidos con Maven.

# Tabla de contenidos
- [oreport-parent](#oreport-parent)
- [Tabla de contenidos](#tabla-de-contenidos)
- [Reporte Sonarqube](#reporte-sonarqube)
- [Variables de entorno](#variables-de-entorno)
  - [Variables de entorno obligatorias](#variables-de-entorno-obligatorias)
  - [Variables de entorno opcionales](#variables-de-entorno-opcionales)

---

# Reporte Sonarqube

(pendiente)

---

# Variables de entorno

## Variables de entorno obligatorias

| Nombre | Detalle |
| - | - |
| N/A | N/A |

## Variables de entorno opcionales

Valores por defecto (y ejemplos) en [core.properties](oreport-core/src/main/resources/application.properties).

En `env.prop.file` se puede asignar una ruta absoluta a un archivo *.properties* del que se se pueden asignar / reemplazar todas las variables de entorno.

| Nombre | Detalle |
| - | - |
| `springdoc.api-docs.enabled` | (OpenApi) Exponer o no el endpoint de api-docs. |
| `springdoc.api-docs.ext-docs` | (OpenApi) Información de documentos externos. |
| `springdoc.api-docs.license` | (OpenApi) Información de la licencia. |
| `springdoc.api-docs.path` | (OpenApi) Endpoint de api-docs. |
| `springdoc.api-docs.title` | (OpenApi) Titulo de los api-docs. |
| `springdoc.swagger-ui.enabled` | (OpenApi) Exponer o no el endpoint de Swagger-ui. |
| `springdoc.swagger-ui.path` | (OpenApi) Endpoint de Swagger-ui. |
