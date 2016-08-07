# GlobalChatManager
GlobalChatManager is a very configurable chat management plugin with the goal of being able to modify and change chat in any way you like.

**Current Functions**
- Custom Chat Rooms: *GlobalChatManager contains a system for creating and managing private chat rooms.*
- Global Chat Formatting: *It also contains a system for formatting chat in any way including colors and PermissionsEX groups.*
- Chat Censoring: *It also contains a working but limited chat censoring system that can be toggled for each individual player.*

**DEPENDENCIES**
- PermissionsEX: *PermissionsEX is a hardcore permission system used by many servers.*
- Essentials: *Essentials is one of the most used, most loved and most essential bukkit plugins.*

**Commands**
The main command of GlobalChatManager is `/gcm`
The syntax is as following:

- `/gcm list` *Shows a list of all currently loaded chat rooms.*
- `/gcm censor` *Toggles chat censoring for the command sender.*
- `/gcm me` *Shows a list of your player name in different forms.*
- `/gcm players` *Changes options for chat rooms* (Requires permission *gcm.players*)
- `/gcm players hasPerms <Player> <ChatRoom>` *Returns true if player has permissions for said chat room.*
- `/gcm players add <Player> <ChatRoom>` *Gives player access to a specified Chat Room.* (Requires permission *gcm.players.add*)
- `/gcm players remove <Player> <ChatRoom>` *Forbids player from using a specified Chat Room.* (Requires permission *gcm.players.remove*)
- `/gcm <ChatRoom>` *Toggles chatting in a specific chat room.*
- `/gcm <ChatRoom> <Message>` *Sends a message in specified chat room.*


**Default Configuration**
```
\#This should always stay as false.
resetconfig: false

censor:
    \#This is the message that will send in chat when toggling chat censoring.
    message: '&bChat Censoring is %bool% &bactive.'
    \#This message will only send when activating chat censoring.
    disclaimer: '&7Note: Chat Censoring is not perfect and we cannot guarantee all words will be correctly censored.'
    \#These are the words that will be censored.
    words:
    \- 'fuck'
    \- 'shit'
    \- 'hell'
    \- '...'

chat:
    \#This is how the chat will be formatted, %player% will output the players name, %nickname% will output their essentials \#nickname, %prefix% will output their PermissionsEX prefix and %suffix% will output their suffix.
    formatting: '&f<%prefix% &b%nickname% &f%suffix%&f> %message%'


\#THESE ARE THE DEFAULT CHAT ROOMS

builder:
    \#These are the players that will be allowed to use the chat room, these can be changed in game using "/gcm players add" \#and "/gcm players remove". 
    allowedplayers:
    \-
    \#This is how the chat room will be formatted, %player% will output the players name, %nickname% will output their \#essentials nickname, %prefix% will output their PermissionsEX prefix and %suffix% will output their suffix.
    formatting: '&1[&9B %player%&1] &f%message%'
    
developer:
    \#These are the players that will be allowed to use the chat room, these can be changed in game using "/gcm players add" \#and "/gcm players remove". 
    allowedplayers:
    \-
    \#This is how the chat room will be formatted, %player% will output the players name, %nickname% will output their \#essentials nickname, %prefix% will output their PermissionsEX prefix and %suffix% will output their suffix.
    formatting: '&6[%prefix% &eD %player%&6] &8<&c%message%&8>'
```
