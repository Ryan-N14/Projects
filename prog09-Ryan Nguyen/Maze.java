import java.io.FilterInputStream;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;

import java.util.Hashtable;
import java.util.ArrayList;
import java.util.LinkedList;

/** Create a graph representing a maze. */
public class Maze {

	// Create all nodes, placing in hash tables
	private Hashtable<String,MazeNode> nodes = new Hashtable<>();
	
	// Every maze has a start and exit node
	private MazeNode start = null;
	private MazeNode exit = null;
	

	// Track the number of nodes discovered before the exit was found
	private int total_count = 0;
	private int max_count = 0;	// most at any time
	
	/**
	* Default constructor
	*/
	public Maze() {}
		
	/** 
	* Create maze as graph, storing entrance and exit to maze.
	* @param fname file containing maze information
	* @return MazeNode that is start (entrance) of maze 
	*/
	public MazeNode load(String fname) {

		try {
			// open file for reading
			// first pass is to store all nodes
			Scanner myReader = new Scanner(new File(fname));

			// First line has Start, Exit, Max Node Value (a char). Skip for now
			String line = myReader.nextLine();
			
			// Each line starts with the node value, 
			// followed by everything it is connected to (i.e. adjacent to). 
			// Build a hash table (dictionary) of nodes to represent the graph
			String[] values;
			while (myReader.hasNextLine()) {
				// read the next line and extract first string, which is a node 
				line = myReader.nextLine();
				values = line.split(" ");
				nodes.put(values[0], new MazeNode(values[0]));
			} // end while
			myReader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Read the file again, this time adding adjacent nodes
		try {
			Scanner myReader = new Scanner(new File(fname));

			// First line has Start, Exit, Max Node Value (a char)
			String line = myReader.nextLine();
			String[] values = line.split(" ");

			// first line of text file has entrance and exit nodes
			// initialize maze to reflect that
			start = nodes.get(values[0]);
			exit = nodes.get(values[1]);
			exit.setExit(true);
			
			// Add adjacent (children) nodes at each line
			while (myReader.hasNextLine()) {
				// read entire line and split into nodes
				line = myReader.nextLine();
				values = line.split(" ");
				
				// first one is the node -- it is followed by adjacent nodes
				MazeNode current = nodes.get(values[0]);
				for (int i=1; i<values.length; i++) {
					current.addChild(nodes.get(values[i]));
				}
			} // end while
			myReader.close();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		// this is the only entry point into the maze
		return start;
	}
	
	/** Print each of the nodes with their children */
	public void print() {
		for (String n : nodes.keySet()) {
			System.out.println("Node: " + nodes.get(n) + " Their Childerns are: " + nodes.get(n).children());
		}
	}
	
	/**
	* Solve the maze by finding a path from start to exit.
	* @param algorithm refers to the type of traversal: "bfs" or "dfs"
	* @return List of nodes that are the path from start to exit
	*/
	public ArrayList<MazeNode> solve(String algorithm) {
		QueueInterface<MazeNode> solver;

		if(algorithm.equalsIgnoreCase("bfs")){
			//user decides BFS
			solver = new Queue<>();
		} else {
			//user decides DFS
			solver = new Stack<>();
		}

		solver.push(start);
		total_count++;

		
		ArrayList<MazeNode> path = new ArrayList<>();

		while(!solver.isEmpty()){
			
			MazeNode temp = solver.pop();

			for(MazeNode child : temp.children()){
				if(child.isExit()){
					//child is the exit
					child.previous(temp);
					path.add(child);
					//finding the path
					while(child.previous() != null){
						//using previous track back to the start
						path.add(child.previous());
						child = child.previous();
					}
					return path;
				}

				if(child.previous() == null){
					solver.push(child);
					child.previous(temp);
					total_count++;
				}
			}
			max_count = solver.max_count();
		}

		

	
		return null;
	}
	
	public int total_count() {
		return total_count;
	}
	public int max_count() {
		return max_count;
	}

} // end class Mazes
