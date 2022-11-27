@echo off
cd /d %~dp0

:set_config
set AGENT_NAME=StatAgent
set AGENT_PORT=9090
set VM_ARGS=-Xms256m -Xmx4096M
set waitcnt=0
set retrycnt=0

title %AGENT_NAME%

:process_check
set waitcnt=0
set /a retrycnt += 1
wmic process where name="javaw.exe" get commandline 2>NUL|find "%AGENT_NAME%" >NUL
if %ERRORLEVEL%%==0 goto startup_wait
rem 일단 무한대로 프로세스 시작을 체크한다. / 화면에 표출되고 있으니 문제없음
rem if %retrycnt% GEQ 10 (
rem @echo %AGENT_NAME% not started >> log\launcher.log
rem date /T&time /T >> log\launcher.log
rem exit /B 1
rem )
@echo.
@echo ===========================
@echo Starting [%AGENT_NAME%] ...
@echo ===========================
@echo.
goto agent_start



:startup_wait
set /a waitcnt += 1
netstat -ano|find "LISTEN"|find ":%AGENT_PORT%" >NUL
if %ERRORLEVEL%==0 goto agent_started

if %waitcnt% GEQ 10 goto process_check
@echo starting... wait[%waitcnt%]
timeout /t 1 >NUL
goto startup_wait



:agent_start
start "%AGENT_NAME%" javaw -classpath %cd%\libs\*;%cd%\%AGENT_NAME%.jar %VM_ARGS% -Dlogging.config=log4j2.xml com.aiear.AgentMain

@echo startup called
timeout /t 2 >NUL
goto startup_wait


:agent_started
@echo.
@echo ===========================
@echo [%AGENT_NAME%] started!!!
@echo ===========================
@echo.
goto eof

:eof
timeout /t 5 >NUL
exit /B 0