# Build Your Own World Design Document

**Partner 1:**
Nathan Pak
**Partner 2:**
Monse Lopez 
## Classes and Data Structures
### OurWorld: 
This class provides static methods that allow us to instantiate the tiles in our world as well as add rooms 

####    Static Variables: 
#####   1. int WIDTH - of ourWorld
#####   2. int HEIGHT - of ourWorld
#####   3. int maxWidth - maximum width for the BIG rooms
#####   4. int maxHeight - maximum height for the BIG rooms
#####   5. int maxSWidth - maximum width for the SMALL rooms
#####   6. int maxSHeight - maximum height for the SMALL rooms
#####   7. int maxTWidth - maximum width for the TINY rooms
#####   8. int maxTHeight - maximum height for the TINY rooms
#####   9. int largestX - The maximum limit of which x value of the bottom left corner of the room can go (in order to prevent the problem of out of bounds)
#####   10. int largestY - The maximum limit of which y value of the bottom left corner of the room can go (in order to prevent the problem of out of bounds):
#####   11. Set<Position> coveredPositions - set tracks the positions that are covered by a tile that is not a NOTHING tile, used to prevent overlapping.  
#####   12. Set<Room> distinctRooms - Set tracks the rooms that were not eliminated to prevent overlapping 
#####   13. ArrayHeapMinPQ<Room> listOfRooms - Priority Queue tracks the rooms generated 
#####   14. ArrayHeapMinPQ<Room> isolatedRooms - Priority Queue tracks the rooms that are not right next to any other rooms in our world. 
#####   15. List<Position> openCoordinates - List of Positions in each room that contains an "opening" in the WALL surrounding - list used to give a path for A* Solver to follow. 
#####   16. UnionFind roomsToConnect - Disjoint Set data structure for keeping track of connecting all the rooms in the world
#####    17. Map<Room, Integer> roomToNumber - Get the number id of the room in UnionFind roomsToConnect for the corresponding room.
#####   18. Map<Integer, Room> numberToRoom - Gets the room from the number id of UnionFind roomsToConnect.
#####   19. Map<Position, Room> openToRoom - Gets the position of the room where there is an opening(a Tileset.NOTHING).
#####   20. Map<Position, Room> wallToRoom - Gets the position of the room where there is a wall.
#####   21. Map<Integer, Integer> verticesToConnect - A map to store the pair of vertices to connect in UnionFind at the end to ensure connectedness.


### Room
This class provides methods that construct the rooms in our world and joins them together.  This class also contains a method that tracks the open positions in our world.   
#### Instance Variables:
#####   1. Position bottomLeft - represents the bottom left tile of each room 
#####   2. int width - width of room 
#####   3. int height - height of room 
#####   4. boolean overlap - boolean set to false by default. 
#####   5. boolean connected - boolean set to false used to check if rooms connect by a hallway.  
#####   6. Random random - creates an instance of a random object 
#####   7. Map<String, Boolean> openSides - sides of a room that contains an opening - represented by a FLOOR tile in 
#####   8. boolean isTopOpen - boolean instance variable used to confirm if there is an open position on the top side of the room 
#####   9. boolean isRightOpen - boolean instance variable used to confirm if there is an open position on the right side of the room 
#####   10. boolean isBottomOpen - boolean instance variable used to confirm if there is an open position on the bottom side of the room 
#####   11. boolean isLeftOpen - boolean instance variable used to confirm if there is an open position on the left side of the room 

### PathGraph 
This class implements AStarGraph that graphs the positions of our tiles into weighted edges. 
This class does not have any instance variables.
### VerticalHallway
This class extends the room class and provides methods that create new hallways 
#####   1. boolean goingUp: checks if hallway is vertical


### Engine: 
This class runs the two methods interactWithKeyBoard and interactWithInputString.  Also in charge if movement for our avatar.  


### Game: 
Generates a new world for our game method and implements the method displayHUD, which is in charge of implementing the HUD of our game.  

####    Instance Variables: 
#####   1. InputSource inputSource - input source instance allows us to call the methods found in the KeyboardInteract class (which inherits the methods of InputSource class)
#####   2. private boolean pressedQ - boolean instance verifies when game has been quit. 
#####   3. private boolean onWall - boolean instance verifies if mouse is hovering over a wall tile. 
#####   4. private boolean onFloor - boolean instance verifies if mouse is hovering over a floor tile. 

### KeyboardInteract: 
This class implements InputSource and contains the methods getNextKey, getKeyWait, and possibleNextInput.  This class allows interactivity between our game and the users movements via their keyboard.  

### Avatar: 

This class keeps track of the position of the avatar in our world and includes the methods: getPosition and updatePosition.  

####    Instance Variable: 
#####   1. Position position - keeps track of tile the Avatar is currently on.  


## Algorithms
### OurWorld
#####   1. main: this main method instantiates all the tiles in our world and generates random rooms with three different max dimensions. Renders the frame for our world. 
#####   2. addRoom: the addRoom method takes in a room, the world it belongs to and checks if it overlaps with any other rooms.  If the room does not overlap, then the room will be added to distinctRooms and listOfRooms and the WALL & FLOOR Tiles will be set. 
#####   3. addOpenings: this method takes in a list of the rooms in our world and creates openings in the rooms by taking a list of "open" wall coordinates and reassigning these tiles to floor tiles. 
#####   4. generateHallways: This method takes in TETile[][] world and turns the path created by AStarSolver into hallways that connect the rooms of our world.
#####   5. generatePaths: This method takes in a path from one hole in one room to another hole in the other. 
#####   6. sharedCoordinates: Determines if two open coordinates(which are the holes to connect) are part of the same room.
#####   7. roomsConnected: Determines if two rooms are connected using a disjoint set data structure roomsToConnect.
#####   8. deleteWallCoordinates: removes the wall position from the room in the wallToRoom hashmap and coveredWallPositions list.
#####   9. addOpenCoordinates: This method adds open coordinates to the list of all the open coordinates in the world after generating paths that have possibly led to openings of rooms. 
#####   10. connectRooms: Assumes that the world is not connected. Connects the open coordinate of one room to another. It does so until all the unconnected rooms that were not connected from generateHallways have been connected by using a disjoint set data structure roomsToConnect. Then, it adds the change to the disjoint set data structure roomsToConnect.
#####   11. checkSurroundings: Checks if there are any covered positions(either floor or wall) surrounding the given position(diagonally, horizontally, and vertically) not used up by the world. If one of the position is Tileset.nothing, add a wall tile. Otherwise, do not do anything. This method only applies to the coordinates of solution vertices given by A* solver
#####   12. checkSurroundingsX: Checks if there are any covered positions(either floor or wall) from left and right. If a left or right position has no covered positions, place a wall tile. This applies to the coordinates where paths are being generated from one solution vertex to another vertically.
####    13. checkSurroundingsY: Checks if there are any covered positions(either floor or wall) from top and bottom. If a top or bottom position has no covered positions, place a wall tile. This applies to the coordinates where paths are being generated from one solution vertex to another horizontally.
####    
### Room
#####   1. Room: room constructor takes in a position, height, width & a random value and instantiates private variables 
#####   2. getWallCoordinates: method creates a list of coordinates in a particular room whose tile is a WALL.  This method checks for overlapping rooms, keeps track of covered positions, and determines what side of the tile's wall will contain an opening.  
#####   3. getFloorCoordinates: method creates a list of coordinates in a particular room whose tile is FLOOR. This method checks for overlapping rooms, and keeps track of covered positions. 
#####   4. overlap: method takes in parameters x, y, Set<Position> positions and returns a boolean value determining whether Positions of a room have been covered or not. If the creation of a room causes overlap, the room will not be added to the world.
#####   5. getOpenCoordinates: method that gets a list of Positions that represent an opening in all the worlds in our world which is them passed through a pathfinder in order to construct the hallways of our world.  
#####   6. openBottom - this method takes in the list of open coordinates and generates a random position that will represent an opening in the bottom side of a room. New Position  generated is added to the list of open coordinates that will pass through the path finder.
#####   7. openTop - this method takes in the list of open coordinates and generates a random position that will represent an opening in the top side of a room. New Position  generated is added to the list of open coordinates that will pass through the path finder.
#####   8. openLeft - this method takes in the list of open coordinates and generates a random position that will represent an opening in the left side of a room. New Position  generated is added to the list of open coordinates that will pass through the path finder.
#####   9. openRight - this method takes in the list of open coordinates and generates a random position that will represent an opening in the right side of a room. New Position  generated is added to the list of open coordinates that will pass through the path finder.
#####   10. openHole - Opens the room from a random wall coordinate.

### PathGraph:
#####   1. neighbors - method takes in a position p and generates a list of four neighbors surrounding each Position (top, bottom, right, left)
#####   2. estimatedDistanceToGoal - overridden method of AStarGraph

### Engine:
####    interactWithKeyboard: method creates the starting screen for our game, as well as allows the player to start a new game, load an old game, or quit a game.  This method also allows the avatar to move around the generated world.  

### Game: 
####    1. Game: constructor instantiates variables 
####    2. isPressedQ: boolean method checks to see if user quit game. 
####    3. pressedQ: method sets boolean to true after user hits q on their keyboard. 
####    4. generateNewWorld: Generates a new world given that the user inputs N + seed + S. 
####    5. displayHud: method takes in a world and displays what kind of tile the user is hovering over with their mouse. 
####    6. isHoverWall: method takes in a world and determines whether user is hovering over a wall tile.  
####    7. isHoverFloor: method takes in a world and determines whether user is hovering over a floor tile.  

## Persistence

### GameMap: 

This class constructs a hashmap that allows us to add and get a particular game by tracking the seed, the game created using that seed when running our game.  
####    Instance Variable: 
#####   1. Map<String, Game> gameMap - hashmap used to track games played.  


### TheGame:
This class provides methods that allow us to save and load a particular game, as well as generate a file for each new game created.  

####    Instance Variables: 
#####   1. GameMap gameMap - creates an instance of a gameMap that allows us to store the games created when running our program
#####   2. File SavedGames - Creates a file that will allow us to store all text files created for each world. 
#####   2. File mapFile - creates a new file for each new game started. 

