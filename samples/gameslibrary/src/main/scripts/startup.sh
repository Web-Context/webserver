#!/bin/bash
echo Start the WebServer
java -jar ${project.artifactId}-${project.version}.jar ${mainClass} port=8888 stopkey=stop dbembedded=true dbport=27017 