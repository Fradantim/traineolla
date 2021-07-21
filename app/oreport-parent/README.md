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

[![Bugs](http://go.tempestad-online.com:19000/api/project_badges/measure?project=oreport-parent&metric=bugs)](http://go.tempestad-online.com:19000/dashboard?id=oreport-parent) [![Code Smells](http://go.tempestad-online.com:19000/api/project_badges/measure?project=oreport-parent&metric=code_smells)](http://go.tempestad-online.com:19000/dashboard?id=oreport-parent) [![Coverage](http://go.tempestad-online.com:19000/api/project_badges/measure?project=oreport-parent&metric=coverage)](http://go.tempestad-online.com:19000/dashboard?id=oreport-parent) [![Duplicated Lines (%)](http://go.tempestad-online.com:19000/api/project_badges/measure?project=oreport-parent&metric=duplicated_lines_density)](http://go.tempestad-online.com:19000/dashboard?id=oreport-parent) [![Lines of Code](http://go.tempestad-online.com:19000/api/project_badges/measure?project=oreport-parent&metric=ncloc)](http://go.tempestad-online.com:19000/dashboard?id=oreport-parent) [![Maintainability Rating](http://go.tempestad-online.com:19000/api/project_badges/measure?project=oreport-parent&metric=sqale_rating)](http://go.tempestad-online.com:19000/dashboard?id=oreport-parent) [![Quality Gate Status](http://go.tempestad-online.com:19000/api/project_badges/measure?project=oreport-parent&metric=alert_status)](http://go.tempestad-online.com:19000/dashboard?id=oreport-parent) [![Reliability Rating](http://go.tempestad-online.com:19000/api/project_badges/measure?project=oreport-parent&metric=reliability_rating)](http://go.tempestad-online.com:19000/dashboard?id=oreport-parent) [![Security Rating](http://go.tempestad-online.com:19000/api/project_badges/measure?project=oreport-parent&metric=security_rating)](http://go.tempestad-online.com:19000/dashboard?id=oreport-parent) [![Technical Debt](http://go.tempestad-online.com:19000/api/project_badges/measure?project=oreport-parent&metric=sqale_index)](http://go.tempestad-online.com:19000/dashboard?id=oreport-parent) [![Vulnerabilities](http://go.tempestad-online.com:19000/api/project_badges/measure?project=oreport-parent&metric=vulnerabilities)](http://go.tempestad-online.com:19000/dashboard?id=oreport-parent)

---

# Variables de entorno

## Variables de entorno obligatorias

| Nombre | Detalle |
| - | - |
| N/A | N/A |

## Variables de entorno opcionales

Valores por defecto (y ejemplos) en [core.properties](oreport-core/src/main/resources/core.properties).

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
