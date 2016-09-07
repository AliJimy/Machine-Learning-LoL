package learn;

import elements.Cell;
import elements.Parameters;

public class Machine extends Thread {
	private final String name;
	private Machine opponent;

	private Cell cell;
	private Cell[][] cells;

	private int row;
	private int col;

	public Machine(String name, Machine opponent) {
		this.opponent = opponent;
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

	public boolean hasReachedToGoal() {
		if (this.cell.isGoal()) {
			return true;
		}

		return false;
	}

	public boolean hasReachedToGoal(Machine machine){
		if(this.cell.equals(machine.getCell()))
			return true;
		
		return false;
	}
	
	public void findBestChain() {
		Chain chain = new Chain();
		
		System.out.printf("First map:\n");
		showPath();
		
		while (!hasReachedToGoal()) {
			if (!chain.hasRepeatedCell()) {
				chain.addCell(this.cell);
			}

			Cell nextCell = this.cell.getBestAction();

			if (!nextCell.isGoal() && !chain.isNextCellACorrectChoice(nextCell)) {
				nextCell = this.cell.getNextState(nextCell);
			}
			
			upgradeCell(this.cell, nextCell);
			showPath();
		}
	}

	public void showPath() {
		char[][] path = new char[row][col];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if(cells[i][j].equals(cell)){
					path[i][j] = getMachineName().toUpperCase().charAt(0);
				}
				else
					path[i][j] = cells[i][j].getState().charAt(0);
			}
		}
		
		showPath(path);
	}
	
	public void upgradeCell(Cell prevCell, Cell nextCell){
		setEmpty(prevCell);
		opponent.setEmpty(prevCell);
		
		this.cell = nextCell;
		
		opponent.setGoal(nextCell);
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

	public void setUpCells() {
		cells = new Cell[row][col];
		int number = 0;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				cells[i][j] = new Cell("EMPTY", ++number, this);
			}
		}

		setBarrier(1, 2);
		setBarrier(2, 2);
		setBarrier(3, 2);

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				cells[i][j].setSurroundingCells();
			}
		}
	}
	
	public void setBarrier(int x, int y){
		this.cells[y][x].setState("BARRIER");
	}
	
	public void setGoal(int x, int y){
		this.cells[y][x].setState("GOAL");
	}
	
	public void setGoal(Cell cell){
		this.cells[cell.getY()][cell.getX()].setState("GOAL");
	}
	
	public void setEmpty(Cell cell){
		cells[cell.getY()][cell.getX()].setState("EMPTY");
	}
	
	public void setEmpty(int x, int y){
		cells[y][x].setState("EMPTY");
	}

	public double getDistanceFrom(Cell cell){
		return Math.sqrt(Math.pow(this.cell.getX() - cell.getX(), 2) + Math.pow(this.cell.getY() - cell.getY(), 2));
	}

	public int getRow() {
		return row;
	}
	
	public void setRow(int row){
		this.row = row;
	}
	
	public int getCol(){
		return col;
	}
	
	public void setCol(int col){
		this.col = col;
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
		this.cell = this.cells[cell.getY()][cell.getX()];

	}
	
	public Cell getCell(){
		return this.cell;
	}
	
	public Cell getGoal(){
		for(int i = 0; i < row; i++){
			for(int j = 0; j < col; j++){
				if(cells[i][j].isGoal())
					return cells[i][j];
			}
		}
		
		return null;
	}

	public void setOpponent(Machine opponent) {
		this.opponent = opponent;
	}

	public Machine getOpponent() {
		return opponent;
	}
}
