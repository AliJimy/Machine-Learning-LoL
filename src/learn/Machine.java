package learn;

import elements.Cell;
import elements.Parameters;

/**
 * Created by Future on 8/26/2016.
 */
public class Machine extends Thread {
	private final String name;

	private Cell cell;
	private Cell[][] cells;

	private int row;
	private int col;

	public Machine(String name) {
		this.name = name;
		this.row = Parameters.ROW;
		this.col = Parameters.COL;
		this.setUpCells();
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
		if (this.cell.isGoal()) {
			return true;
		}

		return false;
	}

	public char[][] findBestChain() {
		char[][] path = new char[row][col];
		
		Chain chain = new Chain();
		
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				path[i][j] = cells[i][j].getState().charAt(0);
			}
		}
		
		System.out.printf("First map:\n");
		showPath(path);
		
		while (!hasReachedToGoal()) {
			if (!chain.hasRepeatedCell()) {
				chain.addCell(this.cell);
				path[this.cell.getY()][this.cell.getX()] = name.charAt(0);
			}

			Cell nextCell = this.cell.getBestAction(this.cells);

			if (!nextCell.isGoal() && !chain.isNextCellACorrectChoice(nextCell)) {
				nextCell = this.cell.getNextState(nextCell);
				if (!chain.isNextCellACorrectChoice(nextCell))
					continue;
			}
			
			this.cell = nextCell;
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
		cells[prevCell.getY()][prevCell.getX()].setState("PASSED");
		this.cell = nextCell;
//		machine.getCells()[prevCell.getY()][prevCell.getX()].setState("FOUND");
		machine.getCells()[prevCell.getY()][prevCell.getX()].setState("EMPTY");
		machine.setGoal(nextCell.getX(), nextCell.getY());
	}
	
	public void showMultiMachine(Machine machine){
		char[][] show = new char[this.row][this.col];
		
		System.out.printf("Car %s :\n", name);
		
		for(int i = 0; i < row; i++){
			for(int j = 0; j < col; j++){
				if(cells[i][j].getState().equals("PASSED"))
					show[i][j] = name.charAt(0);
				else if(cell.getX() == j && cell.getY() == i)
					show[i][j] = name.toUpperCase().charAt(0);
				else if(machine.getCell().getX() == j && machine.getCell().getY() == i)
					show[i][j] = machine.getName().toUpperCase().charAt(0);
				else
					show[i][j] = cells[i][j].getState().charAt(0);
			}
		}
		
		showPath(show);
	}

	public void setUpCells() {
		cells = new Cell[row][col];
		int number = 0;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				number++;
				cells[i][j] = new Cell("EMPTY", number);
			}
		}

		setBarrier(1, 2);
		setBarrier(2, 2);
		setBarrier(3, 2);

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				cells[i][j].setSurroundingCells(cells);
			}
		}
	}
	
	public void setBarrier(int x, int y){
		this.cells[y][x].setState("BARRIER");
	}
	
	public void setGoal(int x, int y){
		this.cells[y][x].setState("GOAL");
	}

	public double getDistanceFrom(Cell cell){
		return Math.sqrt(Math.pow(this.cell.getX() - cell.getX(), 2) + Math.pow(this.cell.getY() - cell.getY(), 2));
	}
}
