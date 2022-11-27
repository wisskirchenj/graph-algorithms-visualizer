# IDEA EDU Course ...

A visualizer for graph-algorithms
Implemented in the Java Core Track of hyperskill.org's JetBrain Academy.

## Technology / External Libraries

- POJO Java 19,
- javax.swing frontend
- with Lombok annotation processors, 
- Apache Log4j SLF4J API binding to Log4j 2 logging and 
- Junit 5 with 
- Mockito (mockito-inline) testing.

## Repository Contents

The sources of main project tasks (7 stages) and unit, mockito testing.

## Program description

An interesting sounding project with Swing UI to create graphs and visualize graph algorithms like traversals,
spanning trees, and shortest pathfinding.

Have fun!

## Project completion

[//]: # (Project was completed on 21.05.22.)

## Progress

11.11.22 Project started and Stage 1 completed, drawing of a black JPanel graph with 4 vertices in the edges 
(Vertex consists of Jpanel with Jlabel in it and overwritten paint(g) method to draw a circle around label).

12.11.22 Stage 2 completed. Vertices can be added by mouse click in the graph canvas. In this case
an input dialog pops up and asks for a 1 char vertex label - can be cancelled...

19.11.22 Stage 3 completed. Mode Menu added to a Menu bar, that switches between, "Add Vertex", "Add Edge" and "None".
In the Add Edge mode, the first click on a vertex selects this one (and highlights it in color) and a click on a second
vertex then lets the user input a weight via modal dialog. Then an edge is drawn between these two vertices and a label 
is placed near the center of the edge. -> interesting stage with a lot of paint, Graphics and JComponent-Methods used.

21.11.22 Stage 4 completed. File Menu (New and Exit) added. Also added the modes "Remove a Vertex" and "Remove an Edge"
to the Mode Menu. For that to work, we introduced a GraphModel, that models the Vertex-Edge Connections of the
user-created graph and also groups the otherwise not well associated components (label, edge, reverse) of an edge.
To remove a vertex just click on it with the mode selected (all associated edges are removed too). And to remove
an edge click on the center of the edge (i.e. near midpoint of the drawn line).

26.11.22 Stage 5 completed. Algorithm Menu (Depth First Search and Bread First Search) added. 
When an algorithm is selected, the user is asked to click on a start vertex for the graph traversal. After that, the
traversal algorithm runs and stores its orders into a queue, which is played ba a swing timer graphical showing the
traversal by highlighting each traversed edge and vertex in slow motion.

27.11.22 Stage 6 completed. Algorithm Menu got new item "Dijkstra's Algorithm", which if chosen asks the user to click on
a start vertex (as all algorithm) and then performs a Dijkstra search to calculate the shortest paths to all other vertices
based on the edge weights given. The result is displyed in the status bar.