package main;

import java.util.Random;

import learn.Machine;
import elements.Cell;
import elements.Parameters;

public class Main {
	public static void main(String[] args) {
		int row = Parameters.ROW;
		int col = Parameters.COL;

		Random random = new Random();
		Machine m = new Machine("mac", null);
		
		Parameters.setPointsZero();
		m.setUpCells();
		m.setBarrier(3, 3);
		m.setBarrier(2, 3);
		
		
		
		// Start Learning
		for (int i = 0; i < 1000; i++) {
			System.out.println("i = " + i);
			m.setUpCells();
			m.setBarrier(3, 3);
			m.setBarrier(2, 3);
			
			while (m.getCell() == null) {
				Cell cell = new Cell("EMPTY", random.nextInt(row * col) + 1, m);
				if (m.getCells()[cell.getY()][cell.getX()].isEmpty()) {
					m.setCell(cell);
				}
			}

			while (m.getGoal() == null) {
				Cell cell = new Cell("EMPTY", random.nextInt(row * col) + 1, m);
				if (m.getCells()[cell.getY()][cell.getX()].isEmpty()) {
					m.setGoal(cell);
				}
			}

			while (m.hasReachedToGoal()) {
				// System.out.println(j + "\t" + xRandom + "\t" + yRandom);
				Cell bestCellToGo = m.getCell().getBestAction();
				m.getCell().calculatePoint();
				m.setCell(bestCellToGo);
				m.showPath();
			}
		}

		double[][][] pointsTable = new double[row][col][4];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				for (int k = 0; k < 4; k++) {
					pointsTable[i][j][k] = m.getCells()[i][j].getActions()[k]
							.getPoint();
				}
			}
		}
		for (int k = 0; k < 4; k++) {
			for (int i = 0; i < row; i++) {
				for (int j = 0; j < col; j++) {
					System.out.printf("%f\t", pointsTable[i][j][k]);
				}
				System.out.println();
			}
		}

		// Moving the Machine
		showBestPath(m, m.getCells()[0][0]);
	}

	private static void showBestPath(Machine machine, Cell startCell) {
		machine.setCell(startCell);
		machine.findBestChain();
	}
}
