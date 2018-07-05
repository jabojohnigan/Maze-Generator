import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/**
 * This is the maze class that builds and generates the maze.
 * @author Jabo Johnigan
 * @version June 1, 2016
 */
public class Maze {
	private char [][] mazeBlocks;
	private boolean[][] visitedBlocks;
	private ArrayList<PathNode> neighbors;
	private int theWidth;
	private int theDepth;
	private boolean debug;
	Random rand;


	public Maze(int width,int depth,boolean debug){
		mazeBlocks = new char[2*width +1][2*depth+1];
		visitedBlocks = new boolean[width][depth];
		this.debug = debug;
		this.theWidth = width;
		this.theDepth = depth;
		initializeMaze();
		Cycle();
		correctPath();
		display();
	}

	private void initializeMaze() { 
		for (int i = 0; i < mazeBlocks.length; i++) { 
			for (int j = 0; j < mazeBlocks[0].length; j++) { 
				if (i % 2 == 0 || j % 2 == 0) { 
					mazeBlocks[i][j] = 'X'; 
				} else { 
					mazeBlocks[i][j] = ' '; 
				} 
			}
		}
		mazeBlocks[0][1] = 'S';

		mazeBlocks[2*theWidth ][2*theDepth -1] = 'E';
		///Start of maze
		mazeBlocks[1][1] = 'O';
		for (int c = 0; c < visitedBlocks.length; c++) { 
			for (int r = 0; r < visitedBlocks[0].length; r++) { 
				visitedBlocks[c][r] = false; 
			} 
		} 

	} 

	private void Cycle(){
		Stack<PathNode> myStack = new Stack<PathNode>();
		rand = new Random();
		int posX = 0;
		int posY = 0;
		visitedBlocks[posX][posY] = true;
		while (!isComplete()) { 
			if (debug) { 
				display();

			} 
			neighbors = addUnvisitedNeighbor(posX, posY); 
			if (!neighbors.isEmpty()) { 

				// randomly select an adjacent unvisited cell 

				PathNode nextLocation = neighbors.get(rand.nextInt(neighbors.size())); 


				// push location of current cell to stack 
				myStack.push(new PathNode(posX, posY)); 


				// remove wall between current cell and new cell 
				if (nextLocation.getX() == posX) { 
					if (nextLocation.getY() > posY) { 
						mazeBlocks[2 * posX + 1][2 * nextLocation.getY()] = ' '; 
					} else { 
						mazeBlocks[2 * posX + 1][2 * posY] = ' '; 
					} 
				} else { 
					if (nextLocation.getX() > posX) { 
						mazeBlocks[2 * nextLocation.getX()][2 * posY + 1] = ' '; 
					} else { 
						mazeBlocks[2 * posX][2 * posY + 1] = ' '; 
					} 
				} 
				// make chosen cell the current cell and mark it as visited 
				posX = nextLocation.getX(); 
				posY = nextLocation.getY(); 
				visitedBlocks[posX][posY] = true;
			} else if (!myStack.isEmpty()) { 
				PathNode nextLocation = myStack.pop(); 
				posX = nextLocation.getX(); 
				posY = nextLocation.getY(); 
			} else { 
				// get unvisited cells 
				ArrayList<PathNode> unvisited = findAllUnvisited(); 
				// pick a random unvisited cell 
				PathNode nextLocation = unvisited.get(rand.nextInt(unvisited.size())); 
				// make chosen cell and mark it as visited 
				posX = nextLocation.getX(); 
				posY = nextLocation.getY(); 
				visitedBlocks[posX][posY] = true; 
			} 
		} 
	}
	public ArrayList<PathNode> addUnvisitedNeighbor(int x, int y){
		ArrayList<PathNode> freePath = new ArrayList<PathNode>();
		if (x + 1 < visitedBlocks.length){
			if (!visitedBlocks[x + 1][y]){
				freePath.add(new PathNode(x+1, y));
			}
		}
		if (x - 1 >= 0){
			if (!visitedBlocks[x-1][y]){
				freePath.add(new PathNode(x - 1, y));
			}
		}
		if (y - 1 >= 0){
			if (!visitedBlocks[x][y-1]){
				freePath.add(new PathNode(x, y - 1));
			}
		}
		if (y + 1 < visitedBlocks[0].length){
			if (!visitedBlocks[x][y+1]) {
				freePath.add(new PathNode(x, y + 1));
			}
		}
		return freePath;	
	}

	public ArrayList<PathNode>checkPath(int x, int y){
		ArrayList<PathNode> openPath = new ArrayList<PathNode>(); 
		if (x - 1 >= 0) { 
			if (!visitedBlocks[x - 1][y]) { 
				if (mazeBlocks[2 * x][2 * y + 1] != 'X') { 
					openPath.add(new PathNode(x - 1, y)); 
				} 
			} 
		} 
		if (y - 1 >= 0) { 
			if (!visitedBlocks[x][y - 1]) { 
				if (mazeBlocks[2 * x + 1][2 * y] != 'X') { 
					openPath.add(new PathNode(x, y - 1)); 
				} 
			} 
		} 
		if (x + 1 < visitedBlocks.length) { 
			if (!visitedBlocks[x + 1][y]) { 
				if (mazeBlocks[2 * x + 2][2 * y + 1] != 'X') { 
					openPath.add(new PathNode(x + 1, y)); 
				} 
			} 
		} 
		if (y + 1 < visitedBlocks[0].length) { 
			if (!visitedBlocks[x][y + 1]) { 
				if (mazeBlocks[2 * x+1][2 * (y+1)] != 'X') { 
					openPath.add(new PathNode(x, y + 1)); 
				} 
			} 
		} 
		return openPath;  
	}

	public boolean isComplete(){
		for (int i = 0; i < visitedBlocks.length; i++){
			for (int j = 0; j < visitedBlocks[0].length;j++){
				if (!visitedBlocks[i][j]){
					return false;
				}
			}
		}
		return true;	
	}

	public ArrayList<PathNode> findAllUnvisited(){
		ArrayList<PathNode> allUnvisitedList = new ArrayList<PathNode>(); 
		for (int i = 0; i < visitedBlocks.length; i++) { 
			for (int j = 0; j < visitedBlocks[0].length; j++) { 
				if (!visitedBlocks[i][j]) { 
					allUnvisitedList.add(new PathNode(i, j)); 
				} 
			} 
		} 
		return allUnvisitedList; 	


	}

	private void resetVisited() { 
		for (int i = 0; i < visitedBlocks.length; i++) { 
			for (int j = 0; j < visitedBlocks[0].length; j++) { 
				visitedBlocks[i][j] = false; 
			} 
		} 
	} 

	public void correctPath() { 

		Stack<PathNode> stack = new Stack<PathNode>(); 
		ArrayList<PathNode> neighbors = new ArrayList<PathNode>(); 
		int posX = 0;
		int posY = 0; 
		resetVisited(); 
		visitedBlocks[posX][posY] = true; 
		while (!(posX == theWidth - 1 && posY == theDepth - 1)) { 
			neighbors = checkPath(posX, posY); 
			if (!neighbors.isEmpty()) { 
				stack.push(new PathNode(posX, posY)); 
				PathNode currentLocation = neighbors.get(0); 
				stack.push(currentLocation); 
				posX = currentLocation.getX(); 
				posY = currentLocation.getY(); 
				visitedBlocks[posX][posY] = true;
				//mazeBlocks[posX][posY] = 'O';
			} else { 
				PathNode currentLocation = stack.pop(); 
				posX = currentLocation.getX(); 
				posY = currentLocation.getY(); 
			} 
		} 


		while (!stack.isEmpty()) { 
			PathNode currentLocation = stack.pop(); 
			mazeBlocks[2 * currentLocation.getX() + 1][2 * currentLocation.getY() + 1] = 'O'; 

		} 
	} 
	public void display(){
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < mazeBlocks.length; i++) { 
			for (int j = 0; j < mazeBlocks[0].length; j++) { 
				builder.append(mazeBlocks[i][j]);
				builder.append(mazeBlocks[i][j]);
			} 
			builder.append("\n");
		} 
		System.out.println(builder.toString());
	} 
}

/**
 * Path node class
 * @author Jabo Johnigan
 * @version June 1, 2016
 */
class PathNode {
	private int x;
	private int y;

	public PathNode(int x, int y){
		this.setX(x);
		this.setY(y);
	}
	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}
}





