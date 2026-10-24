@echo off
cd /d "%~dp0"

echo Starting Ticket System (the first run can take a few minutes)...
docker compose up -d --build
if errorlevel 1 (
    echo.
    echo Docker failed. Is Docker Desktop running?
    pause
    exit /b 1
)

echo Waiting for the backend...
set tries=0
:wait
curl.exe -s -f -o nul http://localhost:8080/tickets/check-available && goto ready
set /a tries+=1
if %tries% geq 90 (
    echo.
    echo The backend did not start. Check the logs with: docker compose logs backend
    pause
    exit /b 1
)
timeout /t 2 /nobreak >nul
goto wait

:ready
start http://localhost:5500
echo.
echo Ready.
echo   Frontend: http://localhost:5500
echo   Backend:  http://localhost:8080
echo Use stop.bat to stop everything.
timeout /t 5 >nul
