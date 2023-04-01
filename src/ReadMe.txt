The app is a messaging application designed to send and 
receive text messages between two or more users. To 
design and implement the messaging application Java 
and SQL was used. Additionally, public and private 
messages can be sent to any connected or non-connected 
user. There is a coordinator that manages the group chat
by adding or removing the user from it. On the other 
hand, the database is a stateful system that maintains 
information about all the members and information used 
by the application. This includes information such as 
user profiles, available groups, chat history, and message 
delivery status.
documentation

The Diagram folder shows how the overall system was implemented

Client side -> ClientServer folder
Server side -> Server folder

DEPENDENCIES
    SQLITE 3
    JUNIT 

TO RUN THE PROJECT
Use the "Runner" folder
1. Run the RunServer.java
2. Run ONE function on the main class for each user
3. Use the commands below

COMMANDS FOR THE USER
 * GET commands
 * /get/id -> own ID
 * /get/users -> all the users
 * /get/connectedUsers -> all connected users
 * /get/coordinator/groupChat_ID -> coordinator of a groupchat
 * /get/userInGroups/user_ID -> see in which groups a user is in
 * /get/groupMembers/groupChat_ID -> members of a group chat
 * /get/name/user_ID -> get the name of the ID
 * /get/messages/groupChat_ID -> get messages from a GC
 * 
 * PUT commands
 * /put/newGroupChat -> create groupchat
 * /put/userInGroup/groupChatID/user_ID -> add user into groupchat
 * /put/message/groupChat_ID -> add a message in a groupchat
 *
 * /kickUser/gropuChat/userID
 * 
 * /quit
 *
 * /help