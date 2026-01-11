@echo off
echo ========================================
echo Interview Scheduler - Quick Start
echo ========================================
echo.

echo Checking Java version...
java -version
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 17 or higher
    pause
    exit /b 1
)

echo.
echo Checking Maven...
mvn -v
if %errorlevel% neq 0 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven
    pause
    exit /b 1
)

echo.
echo ========================================
echo Building project...
echo ========================================
call mvn clean install -DskipTests
if %errorlevel% neq 0 (
    echo ERROR: Build failed
    pause
    exit /b 1
)

echo.
echo ========================================
echo Starting application...
echo ========================================
echo Application will be available at:
echo   - Swagger UI: http://localhost:8080/swagger-ui.html
echo   - Basic UI: http://localhost:8080/
echo.
echo Press Ctrl+C to stop the application
echo.

call mvn spring-boot:run

pause

