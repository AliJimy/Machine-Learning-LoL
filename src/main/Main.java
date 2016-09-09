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
		
		// Start Learning
		for (int i = 0; i < 1000; i++) {
			System.out.println("i = " + i);
			m.setUpCells();
//			m.setBarrier(4, 3);
//			m.setBarrier(3, 3);
//			m.setBarrier(2, 3);
			m.setCell(new Cell("EMPTY", random.nextInt(row * col) + 1, m));
			m.setGoal(new Cell("GOAL", random.nextInt(row * col) + 1, m));


			
//			while (m.getCell().isOut()) {
//				Cell cell = new Cell("EMPTY", random.nextInt(row * col) + 1, m);
//				if (m.getCells()[cell.getY()][cell.getX()].isEmpty()) {
//					m.setCell(cell);
//				}
//			}
//
//			while (m.getGoal().isOut()) {
//				Cell cell = new Cell("GOAL", random.nextInt(row * col) + 1, m);
//				if (m.getCells()[cell.getY()][cell.getX()].isEmpty()) {
//					m.setGoal(cell);
//				}
//			}

			while (m.hasReachedToGoal()) {
				// System.out.println(j + "\t" + xRandom + "\t" + yRandom);
				Cell bestCellToGo = m.getCell().getBestAction();
				m.getCell().calculatePoint();
				m.setCell(bestCellToGo);
				m.showPath();
			}
		}

		// Moving the Machine
		showBestPath(m, m.getCells()[0][0]);
	}

	private static void showBestPath(Machine machine, Cell startCell) {
		machine.resetGoal(new Cell("GOAL", 25, machine));
		machine.setCell(startCell);
		machine.findBestChain();
	}
}
