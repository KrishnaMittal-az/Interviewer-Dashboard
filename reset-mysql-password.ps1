# Stop MySQL service
Write-Host "Stopping MySQL service..."
Stop-Service -Name "MySQL93" -Force
Start-Sleep -Seconds 5

# Start MySQL with skip-grant-tables
Write-Host "Starting MySQL with skip-grant-tables..."
$mysqlPath = "C:\Program Files\MySQL\MySQL Server 9.3\bin\mysqld.exe"
$process = Start-Process -FilePath $mysqlPath -ArgumentList "--skip-grant-tables", "--console" -PassThru -WindowStyle Hidden
Start-Sleep -Seconds 10

# Reset the password
Write-Host "Resetting root password..."
$mysqlClient = "C:\Program Files\MySQL\MySQL Server 9.3\bin\mysql.exe"
& $mysqlClient -u root -e "FLUSH PRIVILEGES; ALTER USER 'root'@'localhost' IDENTIFIED BY 'root';"

# Stop MySQL
Write-Host "Stopping MySQL process..."
Stop-Process -Id $process.Id -Force
Start-Sleep -Seconds 5

# Start MySQL service normally
Write-Host "Starting MySQL service..."
Start-Service -Name "MySQL93"
Start-Sleep -Seconds 5

# Create the database
Write-Host "Creating interview_scheduler database..."
& $mysqlClient -u root -proot -e "CREATE DATABASE IF NOT EXISTS interview_scheduler;" 2>$null

Write-Host "`nMySQL Configuration:"
Write-Host "Database: interview_scheduler"
Write-Host "Username: root"
Write-Host "Password: root"
Write-Host "Port: 3306"
Write-Host "`nSetup complete!"
