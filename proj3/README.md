# Build Your Own World Design Document

**Partner 1:**
Nathan Pak
**Partner 2:**
Monse Lopez 
## Classes and Data Structures
### OurWorld: 
This class provides static methods that allow us to instantiate the tiles in our world as well as add rooms 

####    Instance Variables: 
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
### Room
This class provides methods that construct the rooms/hallways in our world and joins them together.  This class also contains a method that tracks the open positions in our world.   

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

###PathGraph 
This class implements AStarGraph that graphs the positions of our tiles into weighted edges. 
This class does not have any instance variables.
### VerticalHallway
This class extends the room class and provides methods that create new hallways 
#####   1. boolean goingUp: checks if hallway is vertical
## Algorithms
### OurWorld
#####   1. main: this main method instantiates all the tiles in our world and generates random rooms with three different max dimensions. Renders the frame for our world. 
#####   2. addRoom: the addRoom method takes in a room, the world it belongs to and checks if it overlaps with any other rooms.  If the room does not overlap, then the room will be added to distinctRooms and listOfRooms and the WALL & FLOOR Tiles will be set. 
#####   3. addOpenings: this method takes in a priority queue containing a list of the rooms in our world and creates openings in the rooms by taking a list of "open" wall coordinates and reassigning these tiles to floor tiles. 
#####   4. generateHallways: 
### Room
#####   1. a) Room: room constructor takes in a position, height, width & a random value and instantiates private variables 
#####   b)Room(for hallways): constructor overloads room constructor.  Takes in position, height, & width and instantiates the following variables: bottomLeft, width, height, overlap, connected 
#####   2. getWallCoordinates: method creates a list of coordinates in a particular room whose tile is a WALL.  This method checks for overlapping rooms, keeps track of covered positions, and determines what side of the tile's wall will contain an opening.  
#####   3. getFloorCoordinates: method creates a list of coordinates in a particular room whose tile is FLOOR. This method checks for overlapping rooms, and keeps track of covered positions. 
#####   4. overlap: method takes in parameters x, y, Set<Position> positions and returns a boolean value determining whether Positions of a room have been covered or not. If the creation of a room causes overlap, the room will not be added to the world.
#####   5. getOpenCoordinates: method that gets a list of Positions that represent an opening in all the worlds in our world which is them passed through a pathfinder in order to construct the hallways of our world.  
#####   6. openBottom - this method takes in the list of open coordinates and generates a random position that will represent an opening in the bottom side of a room. New Position  generated is added to the list of open coordinates that will pass through the path finder.
#####   7. openTop - this method takes in the list of open coordinates and generates a random position that will represent an opening in the top side of a room. New Position  generated is added to the list of open coordinates that will pass through the path finder.
#####   8. openLeft - this method takes in the list of open coordinates and generates a random position that will represent an opening in the left side of a room. New Position  generated is added to the list of open coordinates that will pass through the path finder.
#####   9. openRight - this method takes in the list of open coordinates and generates a random position that will represent an opening in the right side of a room. New Position  generated is added to the list of open coordinates that will pass through the path finder.
#####   10. main - 

### PathGraph
#####   1. neighbors - method takes in a position p and generates a list of four neighbors surrounding each Position (top, bottom, right, left)
#####   2. estimatedDistanceToGoal - overridden method of AStarGraph
### verticalHallway
#####   1. VerticalHallway
#####   2. getWallCoordinates
#####   3. downwardWall
#####   4. getFloorCoordinates
#####   5. downwardFloor
## Persistence

