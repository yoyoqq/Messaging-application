


How to run: 
    javac Server/ServerHandler/ChatServer.java
    javac Server/ClientHandler/ChatClient.java

    java Server/ServerHandler/ChatServer 1234
    java User/ClientHandler/ChatClient localhost 1234
    java Server/ClientHandler/ChatClient localhost 1234

    java User/ClientHandler/ChatClient 127.16.0.1 1234
    java User/ClientHandler/ChatClient 127.16.0.1 1234

API:
    GET -> READ
    POST -> CREATE
    PUT -> UPDATE
    DELETE -> DELETE



    ex:
        POST/URL/authorization/JSON(data)
                name: "yagol"
                id: "50"
                groutchat: "1"
                message: "heyy"

    http:
        200 -> successfull
        400 -> something wrong with request
        500 -> soething wrong at server level

    // get
    status code/ headers(info about the server)/ body(data in json)



design
long pooling -> ask for data from time to time, will not work (http)
TCP -> transmission connection protocol (faster) uses websockets (peer to peer communication)

message -> 
	"to"
	"from"

send
deliver
read

// CLEAN WAY OF WRITING SOCKET CONNECTION IN CLIENT
inputStreamReader = new InputStreamReader(socket.getInputStream());
outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
bufferedReader = new BufferedReader(inputStreamReader);
bufferedWriter = new BufferedWriter(outputStreamWriter);