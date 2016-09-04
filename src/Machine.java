/**
 * Created by Future on 8/26/2016.
 */
public class Machine extends Thread {
	private final String name;

	private Cell cell;
	private Cell[][] cells;

	private int row;
	private int col;

	public Machine(Cell[][] cls, String name) {
		this.name = name;
		this.row = Main.ROW;
		this.col = Main.COL;
		this.cells = cls;
	}

	public void run() {
		while (!hasReachedToGoal()) {
			this.findBestChain();
		}
	}

	public Action getActionTo(Cell cell) {
		for (int i = 0; i < this.cell.getActions().length; i++) {
			if (cell.equals(this.cell.getActions()[i].getFinalCell())) {
				return this.cell.getActions()[i];
			}
		}

		return null;
	}

	public String getMachineName(){
		return this.name;
	}
	
	public void setCells(Cell[][] cells) {
		this.cells = cells;
	}
	
	public Cell[][] getCells(){
		return this.cells;
	}

	public void setCell(Cell cell) {
		this.cell = cell;
	}
	
	public Cell getCell(){
		return this.cell;
	}

	public boolean hasReachedToGoal() {
		if (this.cell.getState().equals("GOAL")) {
			return true;
		}

		return false;
	}

	public char[][] findBestChain() {
		char[][] path = new char[row][col];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				path[i][j] = cells[i][j].getState().charAt(0);
			}
		}

		showPath(path);
		Chain chain = new Chain();
		while (!hasReachedToGoal()) {
			if (!chain.hasRepeatedCell()) {
				chain.addCell(this.cell);
				path[this.cell.getY()][this.cell.getX()] = name.charAt(0);
			}

			Cell nextCell = this.cell.getBestAction(this.cells);

			if (nextCell.getState().equals("GOAL")
					|| chain.isNextCellACorrectChoice(nextCell)) {
				this.cell = nextCell;
			} else {
				nextCell = this.cell.getNextState(nextCell);
				if (chain.isNextCellACorrectChoice(nextCell)) {
					this.cell = nextCell;
					showPath(path);
				}
				continue;
			}

			showPath(path);
		}
		return path;
	}

	public void showPath(char[][] path) {
		for (int i = 0; i < col; i++) {
			System.out.printf("\t%d", i);
		}
		System.out.println("\n");

		for (int i = 0; i < row; i++) {
			System.out.printf("%d |\t", i);
			for (int j = 0; j < col; j++) {
				System.out.print(path[i][j] + "\t");
			}
			System.out.println("\n");
		}
		System.out.println();
	}
	
	public void upgradeCell(Cell prevCell, Cell nextCell, Machine machine){
		cells[prevCell.getY()][prevCell.getY()].setState("PASSED");
		this.cell = nextCell;
		machine.getCells()[prevCell.getY()][prevCell.getX()].setState("FOUND");
		machine.getCells()[nextCell.getY()][nextCell.getX()].setState("GOAL");
	}
	
	public void showMultiMachine(Machine machine){
		char[][] show = new char[this.row][this.col];
		
		for(int i = 0; i < row; i++){
			for(int j = 0; j < col; j++){
				if(cells[i][j].getState().equals("PASSED"))
					show[i][j] = name.charAt(0);
				else if(machine.getCells()[i][j].getState().equals("PASSED"))
					show[i][j] = machine.getMachineName().charAt(0);
				else if(machine.getCells()[i][j].getState().equals("GOAL"))
					show[i][j] = 'G';
				else
					show[i][j] = cells[i][j].getState().charAt(0);
			}
		}
		
		showPath(show);
	}
}
