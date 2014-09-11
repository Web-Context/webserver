@echo off
echo Start the WebServer
java -jar ${project.artifactId}-${project.version}-full-jar-with-dependencies.jar \
${mainClass} \
port=8888 stopkey=stop dbembedded=true dbport=27017

pause
 