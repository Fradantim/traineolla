# Modelo de Base de Datos

- [Modelo de Base de Datos](#modelo-de-base-de-datos)
  - [DIAGRAMA](#diagrama)
  - [DEFINICIONES](#definiciones)
    - [ACCOUNT](#account)
    - [ACCOUNT_COURSE_SNAPSHOT *pendiente de crear*](#account_course_snapshot-pendiente-de-crear)
    - [ACCOUNT_LEARNING_PATH](#account_learning_path)
    - [COURSE](#course)
    - [COURSE_LEARNING_PATH](#course_learning_path)
    - [LEARNING_PATH](#learning_path)
    - [TECHNOLOGY](#technology)

## DIAGRAMA

```
[TECHNOLOGY]-1-<>-N-[LEARNING_PATH]-1-<>-N-[COURSE_LEARNING_PATH]
          |                     |                       |
 ´-N-<>-1-´            ´-N-<>-1-´              ´-1-<>-N-´
 |                     |                       |
[ACCOUNT]-1-<>-N-[ACCOUNT_LEARNING_PATH]    [COURSE]
   |                                          |
   `-1-<>-N-[ACCOUNT_COURSE_SNAPSHOT]-N-<>-1--´
```

## DEFINICIONES

### ACCOUNT

| Columna | Tipo de dato | Constraints | Referencias |
| - | - | - | - | 
| ID | BIGINT | PK | - |
| ENTERPRISE_ID | VARCHAR(255) | UNIQUE, NON_NULLL | INDEX |
| EMAIL | VARCHAR(255) | - | - |
| PEOPLE_LEAD_EID | VARCHAR(255) | - | - |
| PEOPLE_LEAD_EMAIL | VARCHAR(255) | - | - |
| LEVEL | INT | - | INDEX |
| TECHNOLOGY_ID | BIGINT | - | INDEX, TECHNOLOGY -> ID |

### ACCOUNT_COURSE_SNAPSHOT *pendiente de crear*

| Columna | Tipo de dato | Constraints | Referencias |
| - | - | - | - | 
| ACCOUNT_ID | BIGINT | - | PK |
| COURSE_ID | BIGINT | - | PK |
| SNAPSHOT_DATETIME | DATETIME | - | PK |
| COURSE_DURATION | INT | - | - |
| COMPLETION_RATIO | INT | - | - |
| NUM_VIDEO_CONSUMED_MINUTES | INT | - | - |
| COURSE_ENROLL_DATE | DATETIME | - | - |
| COURSE_START_DATE | DATETIME | - | - |
| COURSE_FIRST_COMPLETION_DATE | DATETIME | - | - |
| COURSE_COMPLETION_DATE | DATETIME | - | - |

### ACCOUNT_LEARNING_PATH

| Columna | Tipo de dato | Constraints | Referencias |
| - | - | - | - | 
| ACCOUNT_ID | BIGINT | PK | ACCOUNT -> ID |
| LEARNING_PATH_ID | BIGINT | PK | LEARNING_PATH -> ID |

### COURSE

| Columna | Tipo de dato | Constraints | Referencias |
| - | - | - | - | 
| ID | BIGINT | PK | - |
| UDEMY_ID | BIGINT | UNIQUE, NON_NULL | INDEX |
| TITLE | VARCHAR(255) | - | INDEX |
| DESCRIPTION | CLOB | - | - |
| ESTIMATED_CONTENT_LENGTH | INT | - | - |
| NUM_LECTURES | INT | - | - |
| NUM_QUIZZES | INT | - | - |
| NUM_PRACTICE_TESTS | INT | - | - |

### COURSE_LEARNING_PATH

| Columna | Tipo de dato | Constraints | Referencias |
| - | - | - | - | 
| COURSE_ID | BIGINT | PK | COURSE -> ID |
| LEARNING_PATH_ID | BIGINT | PK | LEARNING_PATH -> ID |

### LEARNING_PATH

| Columna | Tipo de dato | Constraints | Referencias |
| - | - | - | - | 
| ID | BIGINT | PK | - |
| NAME | VARCHAR(255) | UNIQUE, NON_NULL | INDEX |
| DESCRIPTION | CLOB | - | - |
| LEVEL | INT | - | INDEX |
| TECHNOLOGY_ID | BIGINT | - | INDEX, TECHNOLOGY -> ID |

### TECHNOLOGY

| Columna | Tipo de dato | Constraints | Referencias |
| - | - | - | - | 
| ID | BIGINT | PK | - |
| NAME | VARCHAR(255) | UNIQUE, NON_NULL | INDEX |