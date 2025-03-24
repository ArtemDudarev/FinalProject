@echo off

set JAR_FILE=D:\1.SENLA_FINAL\FinalProject-0.0.1-SNAPSHOT.jar
set JAVA_OPTS=-Xms512m -Xmx1024m

"C:\Users\user\.jdks\openjdk-20.0.1\bin\java" %JAVA_OPTS% -jar %JAR_FILE%

cmd /k