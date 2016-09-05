import java.util.Random;

/**
 * Created by Future on 8/26/2016.
 */
public class Multi {
	public static final int ROW = 5;
	public static final int COL = 5;

	public static void main(String[] args) {

		Machine m = new Machine("mac");
		Machine x = new Machine("xsher");
		double[][][] points = new double[ROW][COL][4];

		// Start Learning
		for (int i = 0; i < 1000; i++) {
			Chain chainM = new Chain();
			Chain chainX = new Chain();
			m.setUpCells();
			x.setUpCells();
			System.out.println("i = " + i);
//			Cell startM = new Cell("EMPTY", 1 + new Random().nextInt(ROW * COL));
//			Cell startX = new Cell("EMPTY", 1 + new Random().nextInt(ROW * COL));
			Cell startM = new Cell("EMPTY", 8);
			Cell startX = new Cell("EMPTY", 12);
			m.setCell(startM);
			m.getCells()[startX.getY()][startX.getX()].setState("GOAL");
			int xM = m.getCell().getX();
			int yM = m.getCell().getY();

			x.setCell(startX);
			x.getCells()[startM.getY()][startM.getX()].setState("GOAL");
			int xX = x.getCell().getX();
			int yX = x.getCell().getY();

//			boolean flag = (m.getDistanceFrom(x.getCell()) > 1.1 * ROW);

			while (true) {
				Cell bestM = m.getCells()[yM][xM].getBestAction(m.getCells());
				Cell bestX = x.getCells()[yX][xX].getBestAction(x.getCells());

				if (chainM.isNextCellACorrectChoice(bestM)) {
					xM = bestM.getX();
					yM = bestM.getY();
					m.upgradeCell(m.getCell(), bestM, x);
				} else {
					bestM = m.getCells()[yM][xM].getNextState(bestM);
					if (chainM.isNextCellACorrectChoice(bestM)) {
						xM = bestM.getX();
						yM = bestM.getY();
						m.upgradeCell(m.getCell(), bestM, x);
					}
				}


				m.getCells()[yM][xM].calculatePoint(m.getCells());
				x.getCells()[yX][xX].calculatePoint(x.getCells());
//				if(flag)
					x.showMultiMachine(m);
				

				if (m.hasReachedToGoal())
					break;
				
				xX = bestX.getX();
				yX = bestX.getY();
				x.upgradeCell(x.getCell(), bestX, m);
				if (x.hasReachedToGoal())
					break;
				
			}
			
		}


		for(int i = 0;i < ROW;i++) {
			for(int j = 0;j < COL;j++) {
				for(int k = 0;k < 4;k++) {

				}
			}
		}

		for(int k = 0;k < 4;k++) {
			System.out.printf("k: %d\n", k);
			for (int i = 0; i < ROW; i++) {
				for (int j = 0; j < COL; j++) {
					System.out.print(points[i][j][k] + "\t");
				}
				System.out.println();
			}
		}


		// Moving the Machine
//		m.setCell(startM);
	}
}
