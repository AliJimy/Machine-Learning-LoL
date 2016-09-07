import java.util.Random;

/**
 * Created by Future on 8/26/2016.
 */
public class Main {
	public static final int ROW = 5;
	public static final int COL = 5;

	public static void main(String[] args) {
		Cell[][] cells = new Cell[ROW][COL];
		int number = 1;
		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COL; j++) {
				cells[i][j] = new Cell("EMPTY", number++);
			}
		}

		cells[0][1].setState("BARRIER");
		cells[1][1].setState("BARRIER");
		cells[2][1].setState("BARRIER");
		cells[3][2].setState("BARRIER");
		cells[1][3].setState("BARRIER");

		cells[0][2].setState("GOAL");

		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COL; j++) {
				cells[i][j].setSurroundingCells(cells);
			}
		}

//		Machine m = new Machine(cells, "mac");

		// Start Learning
		for (int i = 0; i < 1000; i++) {
			System.out.println("i = " + i);
			int xRandom = (new Random().nextInt(COL));
			int yRandom = (new Random().nextInt(ROW));

			for (int j = 0; j < 1000; j++) {
				System.out.println(j + "\t" + xRandom + "\t" + yRandom);
				Cell bestCellToGo = cells[yRandom][xRandom]
						.getBestAction(cells);
				cells[yRandom][xRandom].calculatePoint(cells);
				xRandom = bestCellToGo.getX();
				yRandom = bestCellToGo.getY();
			}
		}

		double[][][] pointsTable = new double[ROW][COL][4];
		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COL; j++) {
				for (int k = 0; k < 4; k++) {
					pointsTable[i][j][k] = cells[i][j].getActions()[k]
							.getPoint();
				}
			}
		}
		for (int k = 0; k < 4; k++) {
			for (int i = 0; i < ROW; i++) {
				for (int j = 0; j < COL; j++) {
					System.out.printf("%f\t", pointsTable[i][j][k]);
				}
				System.out.println();
			}
		}

		// Moving the Machine
//		showBestPath(m, cells[0][0]);
	}

	private static void showBestPath(Machine machine, Cell startCell) {
		machine.setCell(startCell);
		/* char[][] path = */machine.findBestChain();
	}
}
