@echo off
setlocal

@echo.
@echo ===== Build b2fJ JVM =====
@echo.

rem home of the leJOS installation
set "B2FJ_HOME=%~dp0.."
@echo   b2fJ Home: "%B2FJ_HOME%"

@echo.
@echo Cleaning up:
@echo.

if exist *.prg del /S /Q /F javavm\*.o

rem moves into C source folder, this saves us from issues with spaces in folder names and cc65
set "CURRENT_FOLDER=%CD%"
cd "%B2FJ_HOME%\src"
set "BUILD_HOME=_tmp_build"

rem Clears temp folder where we will build the JVM
rmdir /S /Q %BUILD_HOME%
mkdir %BUILD_HOME%

rem Remove old .o files
if exist javavm\*.o del /S /Q /F javavm\*.o
if exist platform\c64\*.o del /S /Q /F platform\c64\*.o
if exist util\*.o del /S /Q /F util\*.o

@echo.
@echo Compiling JVM:
@echo.

set "CC=..\redistr\cc65\bin\cl65"
set "CC_PARAMS=-c -t c64 -I .\javavm -I .\platform\c64 -I .\util -Oi "
set "CC_CLI=%CC% %CC_PARAMS%"

echo on

%CC_CLI% javavm\exceptions.c
if ERRORLEVEL 1 goto end
%CC_CLI% javavm\interpreter.c
if ERRORLEVEL 1 goto end
%CC_CLI% javavm\language.c
if ERRORLEVEL 1 goto end
%CC_CLI% javavm\threads.c
if ERRORLEVEL 1 goto end
%CC_CLI% javavm\memory.c
if ERRORLEVEL 1 goto end
%CC_CLI% javavm\debug.c
if ERRORLEVEL 1 goto end
%CC_CLI% javavm\nativeemul.c
if ERRORLEVEL 1 goto end
%CC_CLI% javavm\tvmemul.c
if ERRORLEVEL 1 goto end

%CC_CLI% platform\c64\load.c
if ERRORLEVEL 1 goto end
%CC_CLI% platform\c64\platform_native.c
if ERRORLEVEL 1 goto end
%CC_CLI% platform\c64\main.c
if ERRORLEVEL 1 goto end

move /Y javavm\*.o %BUILD_HOME%
move /Y platform\c64\*.o %BUILD_HOME%
move /Y util\*.o %BUILD_HOME%

@echo.
@echo Linking JVM:
@echo.

cd "%BUILD_HOME%"

set "CL=..\..\redistr\cc65\bin\ld65"
set "CL_PARAMS="
set "CL_CLI=%CL% %CL_PARAMS%"

@echo Linking btfJ.prg
%CL_CLI% -t c64 -o b2fJ.prg exceptions.o interpreter.o language.o load.o main.o memory.o debug.o nativeemul.o platform_native.o threads.o tvmemul.o c64.lib
@if not exist b2fJ.prg (
	echo Some compilation error happened.
	goto end
)	

rem moves back all prg files into original folder and goes back there
@echo Copying binaries
move /Y *.prg "%CURRENT_FOLDER%"
cd ..
rmdir /S /Q "%BUILD_HOME%\"
cd "%CURRENT_FOLDER%""

@echo.
@echo ============================

:end
