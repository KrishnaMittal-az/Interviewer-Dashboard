# Interview Scheduler - Quick Start Script (PowerShell)

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Interview Scheduler - Quick Start" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check Java
Write-Host "Checking Java version..." -ForegroundColor Yellow
try {
    $javaVersion = java -version 2>&1
    Write-Host "Java found: $($javaVersion[0])" -ForegroundColor Green
} catch {
    Write-Host "ERROR: Java is not installed or not in PATH" -ForegroundColor Red
    Write-Host "Please install Java 17 or higher" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

# Check Maven
Write-Host ""
Write-Host "Checking Maven..." -ForegroundColor Yellow
try {
    $mavenVersion = mvn -v 2>&1 | Select-Object -First 1
    Write-Host "Maven found: $mavenVersion" -ForegroundColor Green
} catch {
    Write-Host "ERROR: Maven is not installed or not in PATH" -ForegroundColor Red
    Write-Host "Please install Maven" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

# Build
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Building project..." -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
mvn clean install -DskipTests
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Build failed" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

# Run
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Starting application..." -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Application will be available at:" -ForegroundColor Green
Write-Host "  - Swagger UI: http://localhost:8080/swagger-ui.html" -ForegroundColor Yellow
Write-Host "  - Basic UI: http://localhost:8080/" -ForegroundColor Yellow
Write-Host ""
Write-Host "Press Ctrl+C to stop the application" -ForegroundColor Cyan
Write-Host ""

mvn spring-boot:run

