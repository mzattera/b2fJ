@echo off
setlocal

@echo.
@echo ===== b2fJ .java to .class =====
@echo.

rem home of the b2fJ installation
set "B2FJ_HOME=%~dp0.."
@echo   b2fJ Home: "%B2FJ_HOME%"

rem home of the jdk to use for compiling
if [%JAVA_HOME%] == [] (
    @echo   Java Home empty; javac must be in your CLASSPATH
    set "JC=javac"
) else (
    @echo   Java Home: "%JAVA_HOME%"
    set "JC=%JAVA_HOME%\bin\javac"
)
    @echo   Java CLASSPATH: "%CLASSPATH%"

@echo.
@echo Launching compilaton:
@echo.
@echo on

%JC% -bootclasspath "%B2FJ_HOME%\lib\classes.jar" -classpath ".;%CLASSPATH%" %1 %2 %3 %4 %5 %6 %7 %8 %9

@echo.
@echo ============================
