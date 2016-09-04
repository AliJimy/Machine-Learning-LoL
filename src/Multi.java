/**
 * Created by Future on 8/26/2016.
 */
public class Multi {
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

		cells[0][2].setState("GOAL");

		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COL; j++) {
				cells[i][j].setSurroundingCells(cells);
			}
		}

		Machine m = new Machine(cells, "mac");
		Cell startM = new Cell("EMPTY", 3);
		Machine x = new Machine(cells, "kos");
		Cell startX = new Cell("EMPTY", 24);

		// Start Learning
		for (int i = 0; i < 1000; i++) {
			System.out.println("i = " + i);
			m.setCell(startM);
			m.getCells()[startX.getY()][startX.getX()].setState("GOAL");
			int xM = m.getCell().getX();
			int yM = m.getCell().getY();
			
			x.setCell(startX);
			x.getCells()[startM.getY()][startM.getX()].setState("GOAL");
			int xX = x.getCell().getX();
			int yX = x.getCell().getY();

			while (true) {
				Cell bestM = cells[yM][xM].getBestAction(m.getCells());
				Cell bestX = cells[yX][xX].getBestAction(x.getCells());
				
				cells[yM][xM].calculatePoint(m.getCells());
				cells[yX][xX].calculatePoint(x.getCells());
				
				xM = bestM.getX();
				yM = bestM.getY();
				if (m.hasReachedToGoal())
					break;
				
				xX = bestX.getX();
				yX = bestX.getY();

				if (x.hasReachedToGoal())
					break;
			}
			
		}

		// Moving the Machine
		m.setCell(startM);
		//Ali J  
	}
}
// Chert o Pert