/**
 * Created by Future on 8/26/2016.
 */
public class Multi {
	public static final int ROW = 5;
	public static final int COL = 5;

	public static void main(String[] args) {

		Machine m = new Machine("mac");
		Cell startM = new Cell("EMPTY", 3);
		Machine x = new Machine("xsher");
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
				Cell bestM = m.getCells()[yM][xM].getBestAction(m.getCells());
				Cell bestX = x.getCells()[yX][xX].getBestAction(x.getCells());
				
				m.getCells()[yM][xM].calculatePoint(m.getCells());
				x.getCells()[yX][xX].calculatePoint(x.getCells());
				x.showMultiMachine(m);
				
				xM = bestM.getX();
				yM = bestM.getY();
				m.upgradeCell(m.getCell(), bestM, x);
				if (m.hasReachedToGoal())
					break;
				
				xX = bestX.getX();
				yX = bestX.getY();
				x.upgradeCell(x.getCell(), bestX, m);
				if (x.hasReachedToGoal())
					break;
				
			}
			
		}

		// Moving the Machine
		m.setCell(startM);
	}
}
