Starting this worklog to keep track of things - Liban 


I was able to build the maven project following the project structure from the mavenValidate professor Auberon highlighted. 

Used AI to learn how to convert the console command into maven 


javac -cp lib/junit-platform-console-standalone-1.12.0.jar src/*.java && java -cp src ImageSummaryApp sampleInput/squares.jpg FFA200 164

TO 

mvn exec:java -Dexec.args="sampleInput/squares.jpg FFA200 164"

