For the demo, 

1. Setup ad run nfd (with the command `nfd-start`)
2. Compile the Server and the Client using
```
javac -cp .:jndn-0.22.jar:json.jar Server.java
javac -cp .:jndn-0.22.jar:json.jar Client.java
```
3. Run the Server followed by the client (on separate terminals
```
java -cp .:jndn-0.22.jar:json.jar Server
java -cp .:jndn-0.22.jar:json.jar Client 
```
