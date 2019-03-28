echo on>sources.txt
echo off

find ./src/com/ -name "*.java" > sources.txt

javac -encoding ISO-8859-1 -classpath "./lib/jade.jar":"./lib/twitter4j-core-4.0.7.jar" -d ./bin @sources.txt
java -cp "./lib/jade.jar":"./lib/twitter4j-core-4.0.7.jar":"./bin" jade.Boot -name AgentLauncher -agents launcher:com.tweetcrawl.agents.AgentLauncher
