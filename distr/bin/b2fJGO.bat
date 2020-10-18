@echo off
setlocal

rem home of the leJOS installation
set "B2FJ_HOME=%~dp0"
if not "%B2FJ_HOME:~-1%"=="\" set "FB_BIN=%B2FJ_HOME%\"
set "B2FJ_HOME=%B2FJ_HOME%.."

call "%B2FJ_HOME%\bin\b2fJc" %1
if ERRORLEVEL 1 goto end
call "%B2FJ_HOME%\bin\b2fJl" %1
if ERRORLEVEL 1 goto end
call "%B2FJ_HOME%\bin\makeJVM"
if ERRORLEVEL 1 goto end
"%B2FJ_HOME%\redistr\WinVICE-3.1-x86\x64" "%CD%\b2fJ.prg"

:end
