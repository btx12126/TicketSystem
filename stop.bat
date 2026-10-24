@echo off
cd /d "%~dp0"
docker compose down
echo.
echo Stopped. The database data is kept. To delete it too, run: docker compose down -v
pause
