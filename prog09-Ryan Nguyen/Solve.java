import java.util.ArrayList;

public class Solve {

	public static void main(String[] args) {
	
		String filename;
		String algorithm;

		if (args.length != 2) {	
			System.out.print("Expecting 2 arguments ");
			System.out.println("(e.g. java Solve maze1.txt bfs)");
			return;
			
		} else {
			filename = args[0];
			algorithm = args[1];
			
			if (!(algorithm.equals("bfs") || algorithm.equals("dfs"))) {
				System.out.println("Algorithm must be string bfs or dfs.");
				return;
			}
		}
	
		System.out.println("__________________________________");
		try {
			testSolve(filename,algorithm);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void testSolve(String fname, String queue) {

		System.out.println("Solving "+fname+" using "+queue);

		// Get the maze initialized based on specified file
		Maze maze = new Maze();
		MazeNode start = maze.load(fname);
		
		// let's see how it looks
		maze.print();

		// ready to find the exit to the maze!
		ArrayList<MazeNode> path = maze.solve(queue);
		String pathString = "";
		// Have to reverse the path so it is from start to exit
		for (MazeNode n: path) {
			pathString = n.value() + " "+pathString;
		}
		
		System.out.println("PATH: "+ pathString);
		System.out.println("TOTAL COUNT: "+maze.total_count());	
		System.out.println("MAX COUNT: " + maze.max_count());	
	} // end main
} // end class Main
