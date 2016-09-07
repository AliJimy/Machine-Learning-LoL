package main;

import elements.Cell;
import elements.Parameters;
import learn.Chain;
import learn.Machine;

public class Multi {
	public static void main(String[] args) {
		int row = Parameters.ROW;
		int col = Parameters.COL;

		Machine m = new Machine("mac");
		Machine x = new Machine("xsher");

		// Start Learning
		for (int i = 0; i < 1000; i++) {
			m.setUpCells();
			x.setUpCells();
			System.out.println("i = " + i);
			// Cell startM = new Cell("EMPTY", 1 + new Random().nextInt(ROW *
			// COL));
			// Cell startX = new Cell("EMPTY", 1 + new Random().nextInt(ROW *
			// COL));
			Cell startM = new Cell("EMPTY", 8);
			Cell startX = new Cell("EMPTY", 18);
			m.setCell(startM);
			m.setGoal(startX.getX(), startX.getY());
			int xM = m.getCell().getX();
			int yM = m.getCell().getY();

			x.setCell(startX);
			x.setGoal(startM.getX(), startM.getY());
			int xX = x.getCell().getX();
			int yX = x.getCell().getY();

			while (true) {
				Cell bestM = m.getCells()[yM][xM].getBestAction(m.getCells());
				Cell bestX = x.getCells()[yX][xX].getBestAction(x.getCells());

				xX = bestX.getX();
				yX = bestX.getY();
				x.upgradeCell(x.getCell(), bestX, m);

				xM = bestM.getX();
				yM = bestM.getY();
				m.upgradeCell(m.getCell(), bestM, x);

				x.getCells()[yX][xX].calculatePoint(x.getCells());
				m.getCells()[yM][xM].calculatePoint(m.getCells());

//				x.showMultiMachine(m);
//				m.showMultiMachine(x);

				if (m.hasReachedToGoal() || x.hasReachedToGoal())
					break;
			}

		}
	}
}
