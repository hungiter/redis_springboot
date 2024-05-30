@echo off
:: Combine with Java Development Kit 17: 
:: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
set "JAVA_HOME=E:\Windows\Java\jdk-17\bin"
:: IntelliJ IDEA Community (where we install IntelliJ)
set "IDEA_HOME=E:\etc\data\IntelliJ IDEA Community"
:: Project's Directory
:: %~dp0:this file directions
set "projectDir=%~dp0consumer" 

echo JAVA_HOME's directory: %JAVA_HOME%
echo Consumer's project directory: %projectDir%


cd %projectDir%
if exist "target" (
echo Clear process ........
"%JAVA_HOME%\java.exe" ^
	"-Dmaven.multiModuleProjectDirectory=%projectDir%" ^
	-Djansi.passthrough=true ^
	"-Dmaven.home=%IDEA_HOME%\plugins\maven\lib\maven3" ^
	"-Dclassworlds.conf=%IDEA_HOME%\plugins\maven\lib\maven3\bin\m2.conf" ^
	"-Dmaven.ext.class.path=%IDEA_HOME%\plugins\maven\lib\maven-event-listener.jar" ^
	"-javaagent:%IDEA_HOME%\lib\idea_rt.jar=63345:%IDEA_HOME%\bin" ^
	-Dfile.encoding=UTF-8 ^
	-classpath "%IDEA_HOME%\plugins\maven\lib\maven3\boot\plexus-classworlds-2.7.0.jar;%IDEA_HOME%\plugins\maven\lib\maven3\boot\plexus-classworlds.license" org.codehaus.classworlds.Launcher ^
	-Didea.version=2024.1 clean > nul
)

cls
echo Build process ........
start /b "" "%JAVA_HOME%\java.exe" ^
			"-Dmaven.multiModuleProjectDirectory=%projectDir%" ^
			-Djansi.passthrough=true ^
			"-Dmaven.home=%IDEA_HOME%\plugins\maven\lib\maven3" ^
			"-Dclassworlds.conf=%IDEA_HOME%\plugins\maven\lib\maven3\bin\m2.conf" ^
			"-Dmaven.ext.class.path=%IDEA_HOME%\plugins\maven\lib\maven-event-listener.jar" ^
			"-javaagent:%IDEA_HOME%\lib\idea_rt.jar=63295:%IDEA_HOME%\bin" ^
			-Dfile.encoding=UTF-8 ^
			-classpath "%IDEA_HOME%\plugins\maven\lib\maven3\boot\plexus-classworlds-2.7.0.jar;%IDEA_HOME%\plugins\maven\lib\maven3\boot\plexus-classworlds.license" ^
		    org.codehaus.classworlds.Launcher ^
			-Didea.version=2024.1 package -DskipTests >nul 2>&1

:: Capture the PID of the last started process
setlocal enabledelayedexpansion			
:: Wait a short time to ensure the process starts and can be captured
timeout /t 2 /nobreak >nul

:: Capture the PID of the last started java.exe process
for /f "tokens=2 delims=," %%i in ('tasklist /FI "IMAGENAME eq java.exe" /FO CSV /NH') do (
    set "pid=%%~i"
)
echo Monitoring process ID: %pid%
endlocal & set pid=%pid%

:WAIT_LOOP
:: Check if the specific java.exe process is still running
tasklist /fi "PID eq %pid%" 2>NUL | find /I /N "%pid%">NUL
if "%ERRORLEVEL%"=="0" (
    echo On processing...
    timeout /t 5 /nobreak >nul
    goto :WAIT_LOOP
)

echo Build success ........


pause
