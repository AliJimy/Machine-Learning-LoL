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
		m.setUpCells();
		m.setGoal(4, 4);
		m.setBarrier(3, 3);
//		m.setBarrier(4, 3);

		// Start Learning
		for (int i = 0; i < 1000; i++) {
			System.out.println("i = " + i);
			int xRandom = random.nextInt(col);
			int yRandom = random.nextInt(row);

			for (int j = 0; j < 1000; j++) {
//				System.out.println(j + "\t" + xRandom + "\t" + yRandom);
				Cell bestCellToGo = m.getCells()[yRandom][xRandom].getBestAction();
				m.getCells()[yRandom][xRandom].calculatePoint();
				xRandom = bestCellToGo.getX();
				yRandom = bestCellToGo.getY();
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

//		 Moving the Machine
		showBestPath(m, m.getCells()[0][0]);
	}

	private static void showBestPath(Machine machine, Cell startCell) {
		machine.setCell(startCell);
		machine.findBestChain();
	}
}
