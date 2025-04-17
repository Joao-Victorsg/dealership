@echo off
SETLOCAL ENABLEDELAYEDEXPANSION

REM Set variables
SET ZIP_NAME=lambda.zip
SET SRC_DIR=.
SET BUILD_DIR=build

REM Clean previous builds
IF EXIST ..\%ZIP_NAME% del /f ..\%ZIP_NAME%
IF EXIST %BUILD_DIR% rmdir /s /q %BUILD_DIR%

REM Create build directory
mkdir %BUILD_DIR%

REM Install dependencies
echo Installing dependencies...
pip install -r requirements.txt -t %BUILD_DIR%

REM Copy all source files and directories except unwanted ones
echo Copying source files...
for /d %%D in (%SRC_DIR%\*) do (
    if /I not "%%~nxD"=="%BUILD_DIR%" if /I not "%%~nxD"=="infra" if /I not "%%~nxD"==".venv" (
        xcopy /E /I /Y "%%D" "%BUILD_DIR%\%%~nxD\"
    )
)

for %%F in (%SRC_DIR%\*.py) do (
    if /I not "%%~nxF"==".venv" (
        copy /Y "%%F" "%BUILD_DIR%\"
    )
)

REM Create zip package
echo Creating zip package...
cd %BUILD_DIR%
powershell -command "Compress-Archive -Path * -DestinationPath ..\%ZIP_NAME%"
cd ..

REM Cleanup
rmdir /s /q %BUILD_DIR%

echo Lambda package created: %ZIP_NAME%