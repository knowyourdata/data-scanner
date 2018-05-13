@echo off

set PROG_HOME=..

set CLASSPATH=%PROG_HOME%\lib\*
set CLASSPATH=%CLASSPATH%;%PROG_HOME%\config\
set CLASSPATH=%CLASSPATH%;%PROG_HOME%\data\

echo classpath: %CLASSPATH%

java at.fwd.data_scanner.ScannerCommand

pause
