package main;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;

import elements.Cell;
import elements.Parameters;
import learn.Chain;
import learn.Machine;

public class Multi {
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {

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
		for (int i = 0; i < 100; i++) {
			m.setUpCells();
			x.setUpCells();
			m.writeInFile("i = " + i + "\n");
//			if (i == 28) {
//				System.out.println();
//			}

//			while (m.getGoal() == null) {
//				Cell cell = new Cell("EMPTY", random.nextInt(row * col) + 1, x);
//				if (x.getCells()[cell.getY()][cell.getX()].isEmpty()) {
//					x.setCell(cell);
//					m.setGoal(x.getCell());
//				}
//			}
//			while (x.getGoal() == null) {
//				Cell cell = new Cell("EMPTY", random.nextInt(row * col) + 1, m);
//				if (m.getCells()[cell.getY()][cell.getX()].isEmpty()) {
//					m.setCell(cell);
//					x.setGoal(m.getCell());
//				}
//			}

//			Cell startM = new Cell("EMPTY", random.nextInt(row * col) + 1, m);
//			Cell startX = new Cell("EMPTY", random.nextInt(row * col) + 1, x);

			Cell startM = new Cell("EMPTY", 7, m);
			Cell startX = new Cell("EMPTY", 10, x);

			if (m.getCells()[startM.getY()][startM.getX()].isBarrier() || x.getCells()[startX.getY()][startX.getX()].isBarrier()) {
				continue;
			}

			m.setCell(startM);
			x.setCell(startX);

			m.setGoal(startX);
			x.setGoal(startM);

			m.getChain().addCell(m.getCell());
			x.getChain().addCell(x.getCell());

			while (true) {
				Cell bestM = m.getCell().getBestAction();
				Cell thisM = m.getCell();
				Cell bestX = x.getCell().getBestAction();
				Cell thisX = x.getCell();

				if (!m.isNextCellACorrectChoice(bestM)) {
					bestM = m.getCell().getNextState(bestM);
				}
				if (!x.isNextCellACorrectChoice(bestX)) {
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

			m.reset();

			x.reset();
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
//		showPoints();


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
