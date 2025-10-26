@echo off

if "%~1"=="" (
    echo Usage: run-year.bat YEAR ARGS...
    exit /b 1
)

set MODULE=adventofcode-%~1
set JAR=%MODULE%\target\%MODULE%-1.0-SNAPSHOT-jar-with-dependencies.jar

if not exist "%JAR%" (
    echo Jar not found: %JAR%
    echo Please run build-year.bat %~1 first
    exit /b 1
)

set ARGS=
:loop
shift
if "%1"=="" goto done
set ARGS=%ARGS% %1
goto loop
:done
 
java -jar "%JAR%" %ARGS%
