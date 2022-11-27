@echo off
cd /d %~dp0

@echo.
@echo =============================
@echo ### Agent Re-Starting...
@echo =============================
@echo.
@echo.

call stop_agent_manual.bat
@echo.
@echo.
timeout /t 1 /nobreak >NUL
@echo.
@echo.
call start_agent_manual.bat
exit /B
