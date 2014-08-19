@echo off
echo Start the WebServer
java -jar target/gamerestserver-0.0.1-SNAPSHOT.jar com.webcontext.apps.grs.application.services.Server port=8888 stopkey=stop dbembedded=true dbport=27017
pause
 