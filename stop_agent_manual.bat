@echo off
cd /d %~dp0

:set_config
set AGENT_NAME=StatAgent
set AGENT_PORT=9090
set waitcnt=0
set stopped_flag=0

title %AGENT_NAME%
@echo.
@echo =============================
@echo Shutting down [%AGENT_NAME%]
@echo =============================
@echo.

java -classpath %cd%\libs\*;%cd%\%AGENT_NAME%.jar com.aiear.ShutdownMain

@echo shutdown called

:stop_wait
set /a waitcnt+=1
set /a stopped_flag=0
netstat -ano|find "LISTEN"|find ":%AGENT_PORT%" >NUL
if not %ERRORLEVEL%==0 set /a stopped_flag+=1
wmic process where name="javaw.exe" get commandline 2>NUL|find "%AGENT_NAME%" >NUL
if not %ERRORLEVEL%==0 set /a stopped_flag+=1
if %stopped_flag%==2 goto eof

if %waitcnt% GEQ 30 goto kill_process
@echo stopping... wait[%waitcnt%]
timeout /t 1 >NUL
goto stop_wait


:kill_process
echo Killing Process Force!!!


:eof
@echo.
@echo ===========================
@echo [%AGENT_NAME%] stopped!!!
@echo ===========================
@echo.
exit /B
