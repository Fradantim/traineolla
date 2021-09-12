# Modelo de Base de Datos

- [Modelo de Base de Datos](#modelo-de-base-de-datos)
- [DIAGRAMA](#diagrama)
- [DEFINICIONES](#definiciones)
  - [ACCOUNT](#account)
    - [TABLA](#tabla)
    - [INDICES](#indices)
  - [ACCOUNT_COURSE_SNAPSHOT](#account_course_snapshot)
    - [INDICES](#indices-1)
  - [ACCOUNT_LEARNING_PATH](#account_learning_path)
  - [COURSE](#course)
    - [INDICES](#indices-2)
  - [COURSE_LEARNING_PATH](#course_learning_path)
  - [LEARNING_PATH](#learning_path)
  - [RANKING](#ranking)
    - [INDICES](#indices-3)
  - [TECHNOLOGY](#technology)

# DIAGRAMA

```
[TECHNOLOGY]-1-<>-N-[LEARNING_PATH]-1-<>-N-[COURSE_LEARNING_PATH]
          |                     |                       |
 ´-N-<>-1-´            ´-N-<>-1-´              ´-1-<>-N-´
 |                     |                       |
[ACCOUNT]-1-<>-N-[ACCOUNT_LEARNING_PATH]    [COURSE]
   |                                          |
   `-1-<>-N-[ACCOUNT_COURSE_SNAPSHOT]-N-<>-1--´
```

# DEFINICIONES

## ACCOUNT

Tabla que refiere a las cuentas que consumen los cursos.

### TABLA

| Columna | Tipo de dato | Constraints | Referencias |
| - | - | - | - | 
| ID | BIGINT | PK | - |
| ENTERPRISE_ID | VARCHAR(255) | UNIQUE, NON_NULLL | ACCOUNT_IDX_EID |
| EMAIL | VARCHAR(255) | - | - |
| PEOPLE_LEAD_EID | VARCHAR(255) | - | - |
| PEOPLE_LEAD_EMAIL | VARCHAR(255) | - | - |
| LEVEL | INT | - | ACCOUNT_IDX_LEVEL |
| TECHNOLOGY_ID | BIGINT | - | ACCOUNT_IDX_TECHNOLOGY, FK: TECHNOLOGY->ID |

### INDICES

- ACCOUNT_IDX_EID
  - COLUMNAS: ENTERPRISE_ID
  - UNIQUE: TRUE
- ACCOUNT_IDX_TECHNOLOGY
  - COLUMNAS: TECHNOLOGY_ID
  - UNIQUE: FALSE

## ACCOUNT_COURSE_SNAPSHOT

| Columna | Tipo de dato | Constraints | Referencias |
| - | - | - | - | 
| ID | BIGINT | PK | - |
| ACCOUNT_EID | VARCHAR(255) | NON_NULLL | ACC_COURSE_SNAP_IDX_ACC_COURSE_DATE |
| UDEMY_COURSE_ID | BIGINT | NON_NULLL | ACC_COURSE_SNAP_IDX_ACC_COURSE_DATE, ACC_COURSE_SNAP_IDX_COURSE_DATE |
| SNAPSHOT_DATETIME | DATETIME | NON_NULLL | ACC_COURSE_SNAP_IDX_ACC_COURSE_DATE, ACC_COURSE_SNAP_IDX_COURSE_DATE |
| UDEMY_ACCOUNT | VARCHAR(255) | - | - |
| COURSE_DURATION | INT | - | - |
| COMPLETION_RATIO | INT | - | - |
| NUM_VIDEO_CONSUMED_MINUTES | INT | - | - |
| COURSE_ENROLL_DATE | DATETIME | - | - |
| COURSE_START_DATE | DATETIME | - | - |
| COURSE_FIRST_COMPLETION_DATE | DATETIME | - | - |
| COURSE_COMPLETION_DATE | DATETIME | - | - |

### INDICES

- ACC_COURSE_SNAP_IDX_ACC_COURSE_DATE
  - COLUMNAS: ACCOUNT_EID -> UDEMY_COURSE_ID -> SNAPSHOT_DATETIME
  - UNIQUE: TRUE
- ACC_COURSE_SNAP_IDX_COURSE_DATE
  - COLUMNAS: UDEMY_COURSE_ID -> SNAPSHOT_DATETIME
  - UNIQUE: FALSE
- ACCOUNT_IDX_TECHNOLOGY
  - COLUMNAS: TECHNOLOGY_ID
  - UNIQUE: FALSE

## ACCOUNT_LEARNING_PATH

| Columna | Tipo de dato | Constraints | Referencias |
| - | - | - | - | 
| ACCOUNT_ID | BIGINT | PK | FK: ACCOUNT->ID |
| LEARNING_PATH_ID | BIGINT | PK | FK: LEARNING_PATH->ID |

## COURSE

| Columna | Tipo de dato | Constraints | Referencias |
| - | - | - | - | 
| ID | BIGINT | PK | - |
| UDEMY_ID | BIGINT | UNIQUE, NON_NULL | COURSE_IDX_UDEMY_ID |
| TITLE | VARCHAR(255) | - | - |
| DESCRIPTION | VARCHAR(1023) | - | - |
| ESTIMATED_CONTENT_LENGTH | INT | - | - |
| NUM_LECTURES | INT | - | - |
| NUM_QUIZZES | INT | - | - |
| NUM_PRACTICE_TESTS | INT | - | - |
| EXPECTED_SCORE | INT | - | - |

### INDICES

- COURSE_IDX_UDEMY_ID
  - COLUMNAS: UDEMY_ID
  - UNIQUE: TRUE

## COURSE_LEARNING_PATH

| Columna | Tipo de dato | Constraints | Referencias |
| - | - | - | - | 
| COURSE_ID | BIGINT | PK | FK: COURSE->ID |
| LEARNING_PATH_ID | BIGINT | PK | FK: LEARNING_PATH->ID |

## LEARNING_PATH

| Columna | Tipo de dato | Constraints | Referencias |
| - | - | - | - | 
| ID | BIGINT | PK | - |
| NAME | VARCHAR(255) | UNIQUE, NON_NULL | - |
| DESCRIPTION | VARCHAR(1023) | - | - |
| LEVEL | INT | - | - |
| TECHNOLOGY_ID | BIGINT | - | FK: TECHNOLOGY->ID |
| EXPECTED_SCORE | INT | - | - |

## RANKING

| Columna | Tipo de dato | Constraints | Referencias |
| - | - | - | - | 
| ID | BIGINT | PK | - |
| ACCOUNT_EID | VARCHAR(255) | UNIQUE, NON_NULL | FK: ACCOUNT->ID |
| SCORE | INT | - | - |
| COMPLETION_RATIO | INT | - | - |
| SCORE_RANK | INT | - | - |
| COMPLETION_RANK | INT | - | - |

### INDICES

- RANKING_IDX_SCORE_RANK
  - COLUMNAS: SCORE_RANK
  - UNIQUE: TRUE
- RANKING_IDX_COMPLETION_RANK
  - COLUMNAS: COMPLETION_RANK
  - UNIQUE: TRUE

## TECHNOLOGY

| Columna | Tipo de dato | Constraints | Referencias |
| - | - | - | - | 
| ID | BIGINT | PK | - |
| NAME | VARCHAR(255) | UNIQUE, NON_NULL | - |