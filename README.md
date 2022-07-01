# Prova Finale Ingegneria del Software 2022
## Gruppo AM24

- ###   10670135    Bonfanti Stefano ([@BonfantiStefano](https://github.com/BonfantiStefano)) <br>stefano4.bonfanti@mail.polimi.it
- ###   10698406    Marco Baratto ([@MarcoBaratto](https://github.com/MarcoBaratto)) <br>marco.baratto@mail.polimi.it
- ###   10682277    Chyzheuskaya Hanna ([@hannachyzheuskaya](https://github.com/hannachyzheuskaya)) <br>hanna.chyzheuskaya@mail.polimi.it

# Functionalities 

| Functionality  | State |
|:---------------|:-----:|
| Basic Rules    |  🟢   |
| Expert Mode    |  🟢   |
| Socket         |  🟢   |
| GUI            |  🟢   |
| CLI            |  🟢   |
| Multiple games |  🟢   |
| Persistence    |  🔴   |
| 12 Characters  |  🟢   |
| 4 Players      |  🔴   |
| Resilience     |  🟢   |
<!-- 🔴 🟢 🟡 -->
# Coverage

|  Element   |    Class %     |        Method %         |                    Line %                    |
|:----------:|:--------------:|:-----------------------:|:--------------------------------------------:|
|   Model    |  100% (32/32)  |      99% (207/209)      |                98% (792/802)                 |
| Controller |   100% (9/9)   |       96% (72/75)       |                90% (478/802)                 |

# License

The project has been developed for the Software Engineering final examination project at Politecnico of Milan; all rights to the Eriantys game are owned by Cranio Games, which provided the graphical resources used for educational purposes only.

# Starting the game

The game consists of a single jar file by the name <code>AM24-0.1-jar-with-dependencies.jar</code>. It can be found in the deliveries directory.

This file holds both the Server, the CLI and the GUI applications, one of which can be selected when booting.

To run the jar file, use the command

<code>java -jar AM24-0.1-jar-with-dependencies.jar</code>

from the command line in the jar's folder.


## Server

The machine running the server must be reachable from the clients in order to play the game. To start the server, select the <code>0</code> option when booting.

When launching this command, the server asks if you want to use the default port, that is <code>1234</code>, to listen for incoming messages from the clients, otherwise is possible to choose a port between
<code>1024</code> and <code>65535</code>.

## Client

The CLI and GUI versions of the client can be run by selecting the <code>1</code> option when booting, then is asked which interface the client wants to use,
<code>0</code> in order to use the CLI or <code>1</code> in order to use the GUI.

## Command Line Arguments

The command utilized to run the application <code>java -jar AM24-0.1-jar-with-dependencies.jar</code> can be followed by these arguments:
- **-s**: followed by the desired port number between 1024 and 65535 as argument, that represents the port
on which the server will be launched;
- **-c**: followed by the ip address of the server and the port on which the server is listening, to start a client using the CLI;
- **-g**: to start a client using the GUI (no further info needed).

# Windows

<!--For windows are available <code>.bat</code> file in order to launch faster the application
-->