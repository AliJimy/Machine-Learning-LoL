package main;

import java.util.Random;

import elements.Cell;
import elements.Parameters;
import learn.Machine;

public class Multi {
	public static void main(String[] args) {
		int row = Parameters.ROW;
		int col = Parameters.COL;
		
		Random random = new Random();

		Machine m = new Machine("mac", null);
		Machine x = new Machine("xsher", null);

		m.setOpponent(x);
		x.setOpponent(m);

		// Start Learning
		for (int i = 0; i < 1000; i++) {
			m.setUpCells();
			x.setUpCells();
			System.out.println("i = " + i);
			
			while(m.getGoal() == null) {
				Cell cell = new Cell("EMPTY", random.nextInt(row * col) + 1, x);
				if(x.getCells()[cell.getY()][cell.getX()].isEmpty()){
					x.setCell(cell);
					m.setGoal(x.getCell());
				}
			}
			while(x.getGoal() == null){
				Cell cell = new Cell("EMPTY", random.nextInt(row * col) + 1, m);
				if(m.getCells()[cell.getY()][cell.getX()].isEmpty()){
					m.setCell(cell);
					x.setGoal(m.getCell());
				}
			}

			while (true) {
				Cell bestM = m.getCell().getBestAction();
				Cell thisM = m.getCell();
				Cell bestX = x.getCell().getBestAction();
				Cell thisX = x.getCell();

				x.upgradeCell(thisX, bestX);
				m.upgradeCell(thisM, bestM);

				x.getCell().calculatePoint();
				m.getCell().calculatePoint();
				
				x.showMultiMachine(m);
				m.showMultiMachine(x);

				if(m.hasReachedToGoal(x) || x.hasReachedToGoal(m))
					break;
			}
		}
	}
}
