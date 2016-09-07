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
			
//			while(m.getGoal() == null) {
//				Cell cell = new Cell("EMPTY", random.nextInt(row * col) + 1, x);
//				if(x.getCells()[cell.getY()][cell.getX()].isEmpty()){
//					x.setCell(cell);
//					m.setGoal(x.getCell());
//				}
//			}
//			while(x.getGoal() == null){
//				Cell cell = new Cell("EMPTY", random.nextInt(row * col) + 1, m);
//				if(m.getCells()[cell.getY()][cell.getX()].isEmpty()){
//					m.setCell(cell);
//					x.setGoal(m.getCell());
//				}
//			}

			Cell startM = new Cell("EMPTY", 8, m);
			Cell startX = new Cell("EMPTY", 18, x);
			m.setCell(startM);
			x.setCell(startX);

			int xM = m.getCell().getX();
			int yM = m.getCell().getY();
			
			int xX = x.getCell().getX();
			int yX = x.getCell().getY();

			while (true) {
				Cell bestM = m.getCells()[yM][xM].getBestAction();
				Cell bestX = x.getCells()[yX][xX].getBestAction();

				xX = bestX.getX();
				yX = bestX.getY();
				x.upgradeCell(bestX, m);

				xM = bestM.getX();
				yM = bestM.getY();
				m.upgradeCell(bestM, x);

				x.getCells()[yX][xX].calculatePoint();
				m.getCells()[yM][xM].calculatePoint();

				if (m.hasReachedToGoal() || x.hasReachedToGoal())
					break;
				
				if(m.hasReachedToGoal(x) || x.hasReachedToGoal(m))
					break;
			}
			
			x.showMultiMachine(m);
			m.showMultiMachine(x);
		}
	}
}
