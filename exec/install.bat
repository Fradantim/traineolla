@echo off

SET MAVEN_OPTS=-DskipTests=true
SET SCRIPT_PATH=%CD%

cd ..\app\oreport-parent
call mvnw clean install %MAVEN_OPTS%
SET RETVAL=%ERRORLEVEL%
IF %RETVAL% NEQ 0 (
    Echo "Error!!"
    cd %SCRIPT_PATH%
    exit /B %RETVAL%
)

cd ..

for /f %%f in ('dir /b') do (
    rem echo %%f
    if "%%f" NEQ "oreport-parent" (
        rem echo %%f
        cd %%f
        call mvnw clean package %MAVEN_OPTS%
        SET RETVAL=%ERRORLEVEL%
        IF %RETVAL% NEQ 0 (
            Echo "Error!!"
            cd %SCRIPT_PATH%
            exit /B %RETVAL%
        )
        cd ..
    )    
)

cd %SCRIPT_PATH%
exit /B 0