Relay Server
================

TCP Relay Server, helps establishing connection with server behind NAT.
When some server(HTTP/SMTP) is running on machine behind NAT, it is accessible to only local network clients, not to the clients on the Internet.
To solve this case, I created server side Skeleton which contacts Relay and advertise itself(in fact, advertise server on behalf of which it is run).
Clients connect to skeleton through Relay. Skeleton connects to actual server.

Why dont I use port forwarding?
-----------------------
0. Another aim of this project is to mask clients and servers from each other.
0. The NAT of local server might have dynamic public IP,it may connect to internet via different connections. which makes impossible to identify it.
0. Port forwarding or UPnP needs to be enabled/supported by router.

Working
------------------
Relay: Server available on internet, helps establishing connection to server.
client: It is client requesting service from Server behind NAT. Because it cannot contact Server it contacts Relay.
Skeleton: Server Side thread provide access to Server. It maintains connection with Relay.

The Relay listens on port X for clients, Y for Skeletons.

Skeleton Join
-----------
0. The Skeleton connects to port Y on Relay.
0. Skeleton provides its identity to Relay.
0. Relay Server adds it to Set of Available Servers's, which then sent to client on demand.

client Join
--------------
0. The client connects to port X on Relay.
0. The client asks list of available Servers, relay replies with Set of Available Servers.
0. client asks the Relay to establish connection with some Skeleton, providing Server ID.
0. If no any client is using existing connection to Skeleton, then it joins it to client.
0. else it asks Skeleton to create new connection with relay, and returns newer to client.

Authentication
------------------------
Relay has stored list of usernames and passwords.

Public key of Relay Server is available to download from http://example.com/abcd.key
Client connects to relay, relay then sends challenge to client.
Client encrypts challenge with password, and then with public key of relay.
Relay verifies if response is correct.
If its correct then relay provides session key, encrypted with password.
The session key will be used hereafter for symmetric encryption.

Each Skeleton will have pre-shared secret key. Which must be known to client to access server.

