java -jar ./server/target/server.jar

java -jar ./manager/target/manager-jar-with-dependencies.jar

java -Xbootclasspath/p:./client/target/xboot.jar -jar ./target/osrsbot-jar-with-dependencies.jar -dev