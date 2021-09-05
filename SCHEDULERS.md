# Schedulers

- [Schedulers](#schedulers)
  - [DIAGRAMA DE SECUENCIA](#diagrama-de-secuencia)
  - [DEFINICIONES](#definiciones)
    - [ACCOUNT_COURSE_ACTIVITY](#account_course_activity)
    - [ACCOUNT_STATUS](#account_status)
    - [COURSE_UPDATE](#course_update)
    - [LEARNING_PATH_UPDATE](#learning_path_update)
    - [RANKING_UPDATE](#ranking_update)
    - [UNKNOWN_ACCOUNTS](#unknown_accounts)

## DIAGRAMA DE SECUENCIA
```
⏰ -> [ACCOUNT_STATUS]

⏰ -> [ACCOUNT_COURSE_ACTIVITY] -> [UNKNOWN_ACCOUNTS]
       |
       `-> [COURSE_UPDATE] -> [LEARNING_PATH_UPDATE] -> [RANKING_UPDATE]
```

## DEFINICIONES

### ACCOUNT_COURSE_ACTIVITY

Actualiza la actividad de los usuarios en relación a los cursos.

- **PERIODICIDAD**
  - Determinado por **INPUT**
- **INPUT** from_date (DATE), to_date (DATE)
  - (TODAY - 1, TODAY) correr 3 veces por día
  - (TODAY - 1, TODAY) correr diariamente
  - (TODAY - 7, TODAY) correr semanalmente **(ideal)**
  - (PRIMERO DEL MES PASADO, PRIMERO DEL MES ACTUAL) correr mensualmente
- **OPERACION**
  - Query Udemy User Course Activity con args from_date, to_date
- **OUTPUT**
  - Por cada resultado de Udemy:
    - `SELECT * FROM ACCOUNT_COURSE_SNAPSHOT WHERE ACCOUNT_EMAIL/ACCOUNT_EID = {} AND COURSE_UDEMY_ID = {} AND SNAPSHOT_DATE = (SELECT MAX(SNAPSHOT_DATE) FROM ACCOUNT_COURSE_SNAPSHOT WHERE ACCOUNT_EMAIL/ACCOUNT_EID = {} AND COURSE_UDEMY_ID = {})` *(consulta indexada)*
  - ¿Existe el resultado de la consulta?
    - SI -> ¿El dato de Udemy tiene info distinta al resultado de la consulta?
      - SI -> Agrego nuevo elemento
      - NO -> `no-op`
    - NO -> Agrego nuevo elemento
- **NOTAS**
  - `UDEMY_PAGE_SIZE` = 100 (o lo máximo que permita Udemy, actualmente 100)
  - `TRANSACTION_SIZE` = `UDEMY_PAGE_SIZE` * 10
  - **NECESITA CAMBIOS EN MODELO DE BBDD**
      - ❓ ¿Agregar a `ACCOUNT_COURSE_SNAPSHOT` columna `COURSE_UDEMY_ID` hacerlo transitivo al ID de la entidad de negocio?
      - ❓ ¿Agregar a `ACCOUNT_COURSE_SNAPSHOT` columna `ACCOUNT_EMAIL` o `ACCOUNT_EID`?
  - ⚠️ Para dar una buena visibilidad del pasado necesita una consulta inicial "desde el principio de los tiempos", caso contrario de aca no se recupera cuantos minutos dedicó la cuenta al curso.

### ACCOUNT_STATUS

Informa a las cuentas del avance de sus capacitaciones.

- **PERIODICIDAD**
  - Determinado por **INPUT**
- **INPUT** from_date (DATE), to_date (DATE)
  - (TODAY - 7, TODAY) correr una vez por semana
  - (PRIMERO DEL MES PASADO, PRIMERO DEL MES ACTUAL) correr mensualmente **(ideal)**
  - (PRIMERO DEL MES, HACE 2 MESES, PRIMERO DEL MES ACTUAL) correr bimestralmente
  - (PRIMERO DEL MES, HACE 3 MESES, PRIMERO DEL MES ACTUAL) correr trimestralmente
- **INPUT**
  - `SELECT * FROM ACCOUNT` 
- **OPERACION**
  - Con la información de la cuenta, el from_date y to_date recuperar
  - Cantidad de cursos finalizados en el intervalo y en total
  - Avance del learning path en el intervalo y en general.
  - Flag de advertencia en el correo y detalle correspondiente en el caso de que no se haya finalizado/avanzado ningún curso en el último mes.
  -	Ranking de entrenamiento de la persona según el avance de toda la comunidad.
- **OUTPUT**
  - ¿Ya conozco el curso con dicho UDEMY_ID?
    - SI -> `no-op`
    - NO -> `INSERT INTO COURSE VALUES (...)`
- **NOTAS**
  - Pendiente de la pregunta 
    - ¿Agregar a `ACCOUNT_COURSE_SNAPSHOT` columna `COURSE_UDEMY_ID` hacerlo transitivo al ID de la entidad de negocio?

### COURSE_UPDATE

Renueva la información remota de los cursos.

- **PERIODICIDAD**
  - Despues de [ACCOUNT_COURSE_ACTIVITY](#account_course_activity)
- **INPUT** (una de estas)
  - `SELECT DISTINCT COURSE_UDEMY_ID FROM ACCOUNT_COURSE_SNAPSHOT` 
    - Idealmente esta, dado que nos puede alimentar de nuevos cursos no conocidos que se están consumiendo.
  - `SELECT UDEMY_ID FROM COURSE WHERE ID IN (SELECT DISTINCT COURSE_ID FROM ACCOUNT_COURSE_SNAPSHOT)`
    - Nos alimenta solamente de cursos conocidos que están siendo consumidos.
  - `SELECT UDEMY_ID FROM COURSE`
    - Nos alimenta solamente de cursos conocidos.
- **OPERACION**
  - Calcular score del curso
  - ¿Ya conozco el curso con dicho UDEMY_ID?
    - `SELECT * FROM COURSE WHERE UDEMY_ID = {}` *(consulta indexada)*
    - SI -> `UPDATE COURSE SET (...) WHERE UDEMY_ID = {}`
    - NO -> `no-op`
- **OUTPUT**
  - Calcular score del curso
    - ¿Se puede conservar el resultado de la operacion anterior?
  - ¿Ya conozco el curso con dicho UDEMY_ID?
    - `SELECT * FROM COURSE WHERE UDEMY_ID = {}` *(consulta indexada)*
      - ¿Se puede conservar el resultado de la consulta anterior?
    - SI -> `no-op`
    - NO -> `INSERT INTO COURSE VALUES (...)`
- **NOTAS**
  - Pendiente de la pregunta 
    - ¿Agregar a `ACCOUNT_COURSE_SNAPSHOT` columna `COURSE_UDEMY_ID` hacerlo transitivo al ID de la entidad de negocio?

### LEARNING_PATH_UPDATE

Actualiza la información de los learning path en base a las actualizaciones de los cursos

- **PERIODICIDAD**
  - Despues de [COURSE_UPDATE](#course_update)
- **INPUT**
  - `SELECT * FROM LEARNING_PATH`
- **OPERACION**
  ``` sql
  UPDATE LEARNING_PATH SET SCORE = (
    SELECT SUM(SCORE) from COURSE WHERE ID IN (
      SELECT COURSE_ID FROM COURSE_LEARNING_PATH WHERE LEARNING_PATH_ID = {}
    )
  )
  WHERE LEARNING_PATH_ID = {}
  ```
- **OUTPUT**
  - N/A
- **NOTAS**
  - **NECESITA CAMBIOS EN MODELO DE BBDD**
    - Agregar a tabla `LEARNING_PATH` columna `SCORE`

### RANKING_UPDATE

- **PERIODICIDAD**
  - Despues de [LEARNING_PATH_UPDATE](#learning_path_update)
- **BEFORE**
  - Iniciar transacción
  - `DELETE FROM RANKING` 
- **INPUT**
  - `SELECT * FROM ACCOUNT` 
- **OPERACION**
  - Recupero (e intento cachear) el learning path que le corresponde a la cuenta segun su level
    - `SELECT * FROM LEARNING_PATH WHERE LEVEL = {}` 
  - Recupero el snapshot más reciente de cada curso para ese usuario y learning path (ACCOUNT_COURSE_SNAPSHOT puede tener info de cursos que no están en el LP actual)
  ``` sql
  SELECT * FROM ACCOUNT_COURSE_SNAPSHOT acs 
  WHERE ACCOUNT_EMAIL/ACCOUNT_EID = {}
  AND COURSE_ID IN (
    SELECT COURSE_ID FROM COURSE_LEARNING_PATH WHERE LEARNING_PATH_ID = {}
  )
  AND SNAPSHOT_DATE = (
    SELECT MAX(SNAPSHOT_DATE) FROM ACCOUNT_COURSE_SNAPSHOT sub
    WHERE sub.ACCOUNT_EMAIL/ACCOUNT_EID = acs.ACCOUNT_EMAIL/ACCOUNT_EID
    AND sub.COURSE_ID = acs.COURSE_ID
  )
  -- consultas indexadas
  -- ACCOUNT_EMAIL/ACCOUNT_EID -> COURSE_ID
  ``` 
  - Calcular score y % de completado del LP.
- **OUTPUT**
  - `INSERT INTO RANKING VALUES (...)`
- **AFTER**
  - Commitear transacción
- **NOTAS**
  - **NECESITA CAMBIOS EN MODELO DE BBDD**
      - ❓ ¿Agregar a tabla `RANKING`?

### UNKNOWN_ACCOUNTS

Indica cuentas no conocidas que estén utilizando las utilidades de Udemy.

- **PERIODICIDAD**
  - Despues de [ACCOUNT_COURSE_ACTIVITY](#account_course_activity)
- **INPUT**
  - N/A
- **OPERACION**
  - `SELECT ACCOUNT_EMAIL/ACCOUNT_EID FROM ACCOUNT_COURSE_SNAPSHOT WHERE ACCOUNT_EMAIL NOT IN (SELECT EMAIL/ENTERPISE_ID FROM ACCOUNT)` 
- **OUTPUT**
  - Colleccionar los resultados y enviar correo con la información.
    - El correo puede ilustrarse más con la información de qué actividad tuvieron esos usuarios.
  - Esta info también la deberiamos disponibilizar en el front, con la posibilidad de:
    - Consultar casos de cuentas desconocidas.
    - Eliminar la info de cuentas desconocidas (para que deje de aparecer en correos).
- **NOTAS**
  - ⚠️ Requiere conocer todos las cuentas del dominio.
  - Pendiente de la pregunta 
    - ¿Agregar a `ACCOUNT_COURSE_SNAPSHOT` columna `ACCOUNT_EMAIL` o `ACCOUNT_EID`?