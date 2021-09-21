@echo off


call install.bat
SET RETVAL=%ERRORLEVEL%
IF %RETVAL% NEQ 0 (
    Echo "Error!!"
    cd %SCRIPT_PATH%
    exit /B %RETVAL%
)

call start.bat