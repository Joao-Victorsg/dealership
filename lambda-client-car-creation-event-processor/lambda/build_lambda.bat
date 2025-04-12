@echo off
SETLOCAL ENABLEDELAYEDEXPANSION

REM Set variables
SET ZIP_NAME=lambda.zip
SET SRC_DIR=src
SET BUILD_DIR=build

REM Clean previous builds
IF EXIST %ZIP_NAME% del /f %ZIP_NAME%
IF EXIST %BUILD_DIR% rmdir /s /q %BUILD_DIR%

REM Create build directory
mkdir %BUILD_DIR%\python

REM Install dependencies
echo Installing dependencies...
pip install -r requirements.txt -t %BUILD_DIR%\python

REM Copy source files
xcopy /E /I /Y %SRC_DIR% %BUILD_DIR%\python\

REM Create zip
echo Creating zip package...
cd %BUILD_DIR%\python
powershell -command "Compress-Archive -Path * -DestinationPath ..\..\%ZIP_NAME%"
cd ..\..

echo Lambda package created: %ZIP_NAME%