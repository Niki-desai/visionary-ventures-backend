@echo off
echo Starting Visionary Ventures Backend...
echo.

REM Try to find Java in common locations
set JAVA_FOUND=0

REM Check if java is in PATH
where java >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    set JAVA_FOUND=1
    echo Java found in PATH
    goto :run
)

REM Check common Java installation paths
for %%J in (
    "C:\Program Files\Java\jdk-25\bin\java.exe"
    "C:\Program Files\Java\jdk-25.0.1\bin\java.exe"
    "C:\Program Files\Java\jdk-21\bin\java.exe"
    "C:\Program Files\Java\jdk-17\bin\java.exe"
    "C:\Program Files (x86)\Java\jdk-25\bin\java.exe"
) do (
    if exist %%J (
        for %%P in ("%%~dpJ..") do set JAVA_HOME=%%~fP
        set PATH=%JAVA_HOME%\bin;%PATH%
        set JAVA_FOUND=1
        echo Java found at: %JAVA_HOME%
        goto :run
    )
)

if %JAVA_FOUND% EQU 0 (
    echo.
    echo ERROR: Java not found!
    echo.
    echo Please do one of the following:
    echo 1. Add Java to your PATH, OR
    echo 2. Set JAVA_HOME environment variable, OR
    echo 3. Run this from Git Bash where Java works: ./mvnw spring-boot:run
    echo.
    pause
    exit /b 1
)

:run
echo.
echo Running Spring Boot application...
echo.
call mvnw.cmd spring-boot:run

pause