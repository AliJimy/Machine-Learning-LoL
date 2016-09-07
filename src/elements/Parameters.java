package elements;

public class Parameters {
	public static double[][][] points;
	public static int ROW = 5;
	public static int COL = 5;
	public static final double ALPHA = 0.5;
	public static final double GAMMA = 0.5;
	
	public static void setPointsZero(){
		points = new double[ROW][COL][4];
		for(int i = 0; i < ROW; i++)
			for(int j = 0; j < COL; j++)
				for(int k = 0; k < 4; k++)
					Parameters.points[i][j][k] = 0.0;
	}
	public static void showPoints(){
		for (int k = 0; k < 4; k++) {
			for (int i = 0; i < ROW; i++) {
				for (int j = 0; j < COL; j++) {
					System.out.printf("%f\t", points[i][j][k]);
				}
				System.out.println();
			}
		}
	}
}
