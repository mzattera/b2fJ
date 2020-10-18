@echo off
setlocal

@echo.
@echo ===== b2fJ .class to C =====
@echo.

rem home of the b2fJ installation
set "B2FJ_HOME=%~dp0"
if not "%B2FJ_HOME:~-1%"=="\" set "FB_BIN=%B2FJ_HOME%\"
set "B2FJ_HOME=%B2FJ_HOME%.."
@echo   b2fJ Home: "%B2FJ_HOME%"

rem home of the jdk to use for compiling
if ["%JAVA_HOME%"] == [] (
    @echo   Java Home empty; javac must be in your CLASSPATH
    set "JC=java"
) else (
    @echo   Java Home: "%JAVA_HOME%"
    set "JC=%JAVA_HOME%\bin\java"
)
    @echo   Java CLASSPATH: "%CLASSPATH%"

set "LINK_CLASSPATH=.;%CLASSPATH%;%B2FJ_HOME%\redistr\lib\bcel-5.1.jar;%B2FJ_HOME%\redistr\lib\commons-cli-1.0.jar;%B2FJ_HOME%\lib\jtools.jar;%B2FJ_HOME%\lib\classes.jar"

@echo.
@echo Linking classes - creating C bytecode array:
@echo.
@echo on

"%JC%" -classpath "%LINK_CLASSPATH%" js.tinyvm.TinyVM --writeorder LE --classpath "%LINK_CLASSPATH%" -o java_code.h "%~n1"
@echo off
if ERRORLEVEL 1 goto end

MOVE /Y java_code.h "%B2FJ_HOME%"\src\platform\c64

:end
@echo off
@echo.
@echo ============================

