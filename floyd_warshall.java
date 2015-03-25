package ueb11;

public class Floyd_Warshall {

	private static final int inf = Integer.MAX_VALUE;
	private int[][] graph;
	private int n;
	private int[][] length; // D_k
	private int[][] paths; // S_k

	Floyd_Warshall(int[][] matrix, int n) {
		this.graph = matrix;
		this.n = n;
		this.length = this.graph;
		this.paths = new int[n][n];
		// fill S_k = paths.
		for (int j = 0; j < n; j++) {
			for (int i = 0; i < n; i++) {
				this.paths[i][j] = j + 1;
			}
			this.paths[j][j] = 0;
		}
	}

	public int min(int x, int y) {
		return x <= y ? x : y;
	}

	private void floyd_warshall() {
		int d, old_value, new_value = 0;
		for (int k = 1; k < n+1; k++) {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					if (k - 1 != i && k - 1 != j) {
						old_value = length[i][j];
						new_value = (length[i][k - 1] == inf)
								|| (length[k - 1][j] == inf) ? inf
								: Math.abs(length[i][k - 1] + length[k - 1][j]);
						d = min(old_value, new_value);
						// adjust paths.
						if (d != old_value) {
							length[i][j] = d;
							paths[i][j] = k;
						}
					}
				}
			}
		}
	}

	static String printArray(int[] array) {
		String res = "[";
		for (int i = 0; i < array.length - 1; i++) {
			res += array[i] != inf ? array[i] : "-";
			res += " ,";
		}
		res += array[array.length - 1] != inf ? array[array.length - 1]
				: "-";
		res += "]";
		return res;
	}

	static void printMatrix(int[][] matrix) {
		for (int i = 0; i < matrix.length; i++)
			System.out.println(printArray(matrix[i]));
		System.out.println();
	}

	public int[][] getLengths() {
		return length;
	}

	public int[][] getPaths() {
		return paths;
	}

	public static void main(String args[]) {
		int n = 4;
		int[][] graph = new int[n][n];
		for (int i = 0; i < n; i++)
			graph[i][i] = 0;
		graph[0][1] = graph[1][0] = 2;
		graph[0][2] = graph[2][0] = 4;
		graph[0][3] = graph[3][0] = inf;
		graph[1][2] = graph[2][1] = 1;
		graph[1][3] = graph[3][1] = 5;
		graph[2][3] = graph[3][2] = 3;
		Floyd_Warshall undirected_shortestPath = new Floyd_Warshall(graph, n);
		System.out.println("Adjacency matrix for undirected graph");
		printMatrix(undirected_shortestPath.getLengths());

		undirected_shortestPath.floyd_warshall();
		System.out.println("Length of paths between nodes (undir. Graph)");
		printMatrix(undirected_shortestPath.getLengths());
		System.out.println("Paths between nodes (undir. Graph):");
		printMatrix(undirected_shortestPath.getPaths());
		int[][] graph2 = {
                {0,10,inf,5,inf}, 
                {inf,0,1,5,inf}, 
                {inf,inf,0,inf,6},
                {inf,3,inf,0,2}, 
                {7,inf,4,inf,0}};
		
		Floyd_Warshall directed_shortestPath = new Floyd_Warshall(graph2, 5);
		System.out.println("Adjazenzmatrix zum gerichteten Graph");
		printMatrix(directed_shortestPath.getLengths());

		directed_shortestPath.floyd_warshall();
		System.out.println("Length of paths between nodes (ger. Graph)");
		directed_shortestPath.getLengths();
		
		printMatrix(directed_shortestPath.getLengths());
		System.out.println("Paths between nodes (ger. Graph):");
		printMatrix(directed_shortestPath.getPaths());

	}
}
