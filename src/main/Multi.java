package main;

import java.util.Random;

import elements.Cell;
import elements.Parameters;
import learn.Chain;
import learn.Machine;

public class Multi {
	public static void main(String[] args) {
		int row = Parameters.ROW;
		int col = Parameters.COL;

		Random random = new Random();

		Machine m = new Machine("mac", null);
		Machine x = new Machine("xsher", null);
		
		Parameters.setPointsZero();

		m.setOpponent(x);
		x.setOpponent(m);

		m.setOpponent(x);
		x.setOpponent(m);

		// Start Learning
		initializePoints();
		for (int i = 0; i < 1000; i++) {
			Chain chainM = new Chain();
			Chain chainX = new Chain();
			m.setUpCells();
			x.setUpCells();
			System.out.println("i = " + i);

			while (m.getGoal() == null) {
				Cell cell = new Cell("EMPTY", random.nextInt(row * col) + 1, x);
				if (x.getCells()[cell.getY()][cell.getX()].isEmpty()) {
					x.setCell(cell);
					m.setGoal(x.getCell());
				}
			}
			while (x.getGoal() == null) {
				Cell cell = new Cell("EMPTY", random.nextInt(row * col) + 1, m);
				if (m.getCells()[cell.getY()][cell.getX()].isEmpty()) {
					m.setCell(cell);
					x.setGoal(m.getCell());
				}
			}

			chainM.addCell(m.getCell());
			chainX.addCell(x.getCell());

			while (true) {
				Cell bestM = m.getCell().getBestAction();
				Cell thisM = m.getCell();
				Cell bestX = x.getCell().getBestAction();
				Cell thisX = x.getCell();

				if (!chainM.isNextCellACorrectChoice(bestM)) {
					bestM = m.getCell().getNextState(bestM);
				}
				if (!chainX.isNextCellACorrectChoice(bestX)) {
					bestX = x.getCell().getNextState(bestX);
				}
				x.getCell().calculatePoint();

				if (x.upgradeCell(thisX, bestX)) {
					MergePoints(m, x);
					break;
				}
				x.showMultiMachine(m);
				m.getCell().calculatePoint();

				if (m.upgradeCell(thisM, bestM)) {
					MergePoints(m, x);
					break;
				}
				m.showMultiMachine(x);

				if (x.hasReachedToGoal(m))
					break;
			}

			x.showMultiMachine(m);
			m.showMultiMachine(x);
		}
		showPoints();
	}


	public static void initializePoints(){
		Parameters.points = new double[Parameters.ROW][Parameters.COL][4];
		for(int index = 0; index < Parameters.ROW; index++) {
			for(int j = 0;j < Parameters.COL;j++) {
				for(int k = 0;k < 4;k++) {
					Parameters.points[index][j][k] = 0;
				}
			}
		}
	}


	public static void MergePoints(Machine m, Machine x) {
		for(int index = 0; index < Parameters.ROW; index++) {
            for(int j = 0;j < Parameters.COL;j++) {
                for(int k = 0;k < 4;k++) {
                    Parameters.points[index][j][k] += (x.getCells()[index][j].getActions()[k].getPoint() + m.getCells()[index][j].getActions()[k].getPoint()) / 2;
                }
            }
        }
	}


	public static void showPoints(){
		for(int k = 0;k < 4;k++) {
			for(int i = 0; i < Parameters.ROW; i++) {
				for(int j = 0;j < Parameters.COL;j++) {
					System.out.print(Parameters.points[i][j][k] + "\t");
				}
				System.out.println();
			}
			System.out.println("---------------------------------------------------------");
		}
	}
}
