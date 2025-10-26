@echo off

if "%~1"=="" (
    echo Usage: build-year.bat YEAR
    exit /b 1
)

set MODULE=adventofcode-%~1

mvn -am -pl %MODULE% clean package
IF %ERRORLEVEL% NEQ 0 exit /b %ERRORLEVEL%
