# mail-server

Servicio de envio de correos.

# Tabla de contenidos
- [mail-server](#mail-server)
- [Tabla de contenidos](#tabla-de-contenidos)
- [Reporte Sonarqube](#reporte-sonarqube)
- [Variables de entorno](#variables-de-entorno)
  - [Variables de entorno obligatorias](#variables-de-entorno-obligatorias)
  - [Variables de entorno opcionales](#variables-de-entorno-opcionales)

---

# Reporte Sonarqube

[![Bugs](http://go.tempestad-online.com:19000/api/project_badges/measure?project=mail-server&metric=bugs)](http://go.tempestad-online.com:19000/dashboard?id=mail-server) [![Code Smells](http://go.tempestad-online.com:19000/api/project_badges/measure?project=mail-server&metric=code_smells)](http://go.tempestad-online.com:19000/dashboard?id=mail-server) [![Coverage](http://go.tempestad-online.com:19000/api/project_badges/measure?project=mail-server&metric=coverage)](http://go.tempestad-online.com:19000/dashboard?id=mail-server) [![Duplicated Lines (%)](http://go.tempestad-online.com:19000/api/project_badges/measure?project=mail-server&metric=duplicated_lines_density)](http://go.tempestad-online.com:19000/dashboard?id=mail-server) [![Lines of Code](http://go.tempestad-online.com:19000/api/project_badges/measure?project=mail-server&metric=ncloc)](http://go.tempestad-online.com:19000/dashboard?id=mail-server) [![Maintainability Rating](http://go.tempestad-online.com:19000/api/project_badges/measure?project=mail-server&metric=sqale_rating)](http://go.tempestad-online.com:19000/dashboard?id=mail-server) [![Quality Gate Status](http://go.tempestad-online.com:19000/api/project_badges/measure?project=mail-server&metric=alert_status)](http://go.tempestad-online.com:19000/dashboard?id=mail-server) [![Reliability Rating](http://go.tempestad-online.com:19000/api/project_badges/measure?project=mail-server&metric=reliability_rating)](http://go.tempestad-online.com:19000/dashboard?id=mail-server) [![Security Rating](http://go.tempestad-online.com:19000/api/project_badges/measure?project=mail-server&metric=security_rating)](http://go.tempestad-online.com:19000/dashboard?id=mail-server) [![Technical Debt](http://go.tempestad-online.com:19000/api/project_badges/measure?project=mail-server&metric=sqale_index)](http://go.tempestad-online.com:19000/dashboard?id=mail-server) [![Vulnerabilities](http://go.tempestad-online.com:19000/api/project_badges/measure?project=mail-server&metric=vulnerabilities)](http://go.tempestad-online.com:19000/dashboard?id=mail-server)

---

# Variables de entorno

Este proyecto hereda todas las configuraciones de variables de entorno de su padre [oreport-parent](../oreport-parent).

## Variables de entorno obligatorias

| Nombre | Detalle |
| - | - |
| `spring.mail.host` | [(spring-var)](https://docs.spring.io/spring-boot/docs/2.0.6.RELEASE/reference/html/boot-features-email.html) Host del servicio de correo electronico, ej: [Outlook](https://support.microsoft.com/en-us/office/pop-imap-and-smtp-settings-8361e398-8af4-4e97-b147-6c6c4ac95353), [Google](https://support.google.com/a/answer/176600?hl=es). |
| `spring.mail.username` | [(spring-var)]([spring-var](https://docs.spring.io/spring-boot/docs/2.0.6.RELEASE/reference/html/boot-features-email.html)) Indica el origen desde el cual se envian los correos. |
| `spring.mail.password` | [(spring-var)]([spring-var](https://docs.spring.io/spring-boot/docs/2.0.6.RELEASE/reference/html/boot-features-email.html)) Clave secreta del `spring.mail.username`. Seguramente el host lo obligue a usar una contraseña de aplicación, ej: [Outlook](https://support.microsoft.com/en-us/account-billing/using-app-passwords-with-apps-that-don-t-support-two-step-verification-5896ed9b-4263-e681-128a-a6f2979a7944), [Google](https://support.google.com/accounts/answer/185833?hl=es). |
| `spring.mail.port` | [(spring-var)]([spring-var](https://docs.spring.io/spring-boot/docs/2.0.6.RELEASE/reference/html/boot-features-email.html)) Puerto en el que el `spring.mail.host` escucha llamados del tipo SMTP. |

## Variables de entorno opcionales

Valores por defecto (y ejemplos) en [application.properties](src/main/resources/application.properties).

| Nombre | Detalle |
| - | - |
| `api-docs.request.mail.description` | Documentacion de OpenApi. |
| `spring.mail.properties.mail.smtp.connecttimeout` | [(spring-var)](https://docs.spring.io/spring-boot/docs/2.0.6.RELEASE/reference/html/boot-features-email.html) Relacionado a tiempo de espera maximo de operacion TCP/SMTP contra `spring.mail.host`. No asignarlo indicaria tiempo infinito. |
| `spring.mail.properties.mail.smtp.timeout` | [(spring-var)](https://docs.spring.io/spring-boot/docs/2.0.6.RELEASE/reference/html/boot-features-email.html) Relacionado a tiempo de espera maximo de operacion TCP/SMTP contra `spring.mail.host`. No asignarlo indicaria tiempo infinito. |
| `spring.mail.properties.mail.smtp.writetimeout` | [(spring-var)](https://docs.spring.io/spring-boot/docs/2.0.6.RELEASE/reference/html/boot-features-email.html) Relacionado a tiempo de espera maximo de operacion TCP/SMTP contra `spring.mail.host`. No asignarlo indicaria tiempo infinito. |
| `spring.mail.properties.mail.smtp.starttls.enable` | [(spring-var)](https://docs.spring.io/spring-boot/docs/2.0.6.RELEASE/reference/html/boot-features-email.html) Inicia la conexion protegida por TLS.  |
| `spring.mail.properties.mail.smpt.auth` | [(spring-var)](https://docs.spring.io/spring-boot/docs/2.0.6.RELEASE/reference/html/boot-features-email.html) Fuerza autenticacion contra `spring.mail.host`. |