@echo on>sources.txt
@echo off
setlocal enabledelayedexpansion
set "parentfolder=%CD%"
for /r . %%g in (*.java) do (
  set "var=%%g"
  set var=!var:%parentfolder%=!
  echo .!var! >> sources.txt
)
javac -classpath ".\lib\jade.jar;.\lib\twitter4j-core-4.0.7.jar" -d .\bin  @sources.txt
java -cp ".\lib\jade.jar;.\lib\twitter4j-core-4.0.7.jar;bin" jade.Boot -name AgentLauncher -agents Launcher:com.tweetcrawl.agents.AgentLauncher
pause