package elements;

import java.util.ArrayList;
import java.util.Random;

import learn.Action;
import learn.Machine;

public class Cell {
	private String state;

	private Machine machine;

	private Cell up;
	private Cell left;
	private Cell right;
	private Cell down;

	private int number;
	private int x;
	private int y;
	private Action[] actions; // 0: Up, 1: Right, 2: Down, 3: Left

	public Cell(String state, int number, Machine machine) {
		this.actions = new Action[4];
		this.machine = machine;
		this.state = state;
		this.number = number;
		if (number < 0) {
			return;
		} else {
			this.x = (this.number - 1) % Parameters.COL;
			this.y = (this.number - 1) / Parameters.COL;
		}
	}

	private void setActions() {
		actions[0] = new Action(this, this.up, "UP");
		actions[1] = new Action(this, this.right, "RIGHT");
		actions[2] = new Action(this, this.down, "DOWN");
		actions[3] = new Action(this, this.left, "LEFT");
		if (number < 0) {
			for (int i = 0; i < this.actions.length; i++) {
				this.actions[i].setPoint(-10000);
			}
		}
	}

	public void setSurroundingCells() {
		Cell[][] cells = machine.getCells();
		Cell out = new Cell("OUT", -1, machine);

		this.left = (x == 0) ? out : (cells[y][x - 1]);
		this.right = (x == Parameters.COL - 1) ? out : (cells[y][x + 1]);
		this.up = (y == 0) ? out : (cells[y - 1][x]);
		this.down = (y == Parameters.ROW - 1) ? out : (cells[y + 1][x]);
		this.setActions();
	}

	public Cell getBestAction() {
		double maxPoint = -1000000.0;
		for (int i = 0; i < this.actions.length; i++) {
			if (maxPoint < this.actions[i].getPoint()
					&& this.actions[i].getFinalCell() != null
					&& !this.actions[i].getFinalCell().isOut()
					&& !this.actions[i].getFinalCell().isBarrier()) {
				maxPoint = this.actions[i].getPoint();
			}
		}

		Cell[] cls = new Cell[4];
		int num = 0;
		for (int i = 0; i < this.actions.length; i++) {
			if (this.actions[i].getPoint() == maxPoint
					&& this.actions[i].getFinalCell() != null
					&& !this.actions[i].getFinalCell().isOut()
					&& !this.actions[i].getFinalCell().isBarrier()) {
				cls[num++] = this.actions[i].getFinalCell();
			}
		}
		return this.chooseNearestCellToGoal(cls);
	}

	public Cell guessOpponentNextAction() {
		Cell opponentCell = machine.getOpponent().getCell();
		double maxPoint = -1000000.0;
		for (int i = 0; i < opponentCell.actions.length; i++) {
			if (maxPoint < opponentCell.actions[i].getPoint()
					&& opponentCell.actions[i].getFinalCell() != null
					&& !opponentCell.actions[i].getFinalCell().isOut()
					&& !opponentCell.actions[i].getFinalCell().isBarrier()) {
				maxPoint = opponentCell.actions[i].getPoint();
			}
		}
		Cell[] cls = new Cell[4];
		int num = 0;
		for (int i = 0; i < opponentCell.actions.length; i++) {
			if (opponentCell.actions[i].getPoint() == maxPoint
					&& opponentCell.actions[i].getFinalCell() != null
					&& !opponentCell.actions[i].getFinalCell().isOut()
					&& !opponentCell.actions[i].getFinalCell().isBarrier()) {
				cls[num++] = opponentCell.actions[i].getFinalCell();
			}
		}
		ArrayList<Cell> nearestCellsToThis = this
				.chooseNearestCellTo(this, cls);
		if (nearestCellsToThis.size() == 1) {
			return nearestCellsToThis.get(0);
		} else{
			nearestCellsToThis = this.chooseFurtherestCellTo(this.machine.getChain().getLastCell(), nearestCellsToThis);
			for(int i = 0;i < nearestCellsToThis.size();i++) {
				if (!opponentCell.machine.getChain().isRepeatedCell(nearestCellsToThis.get(i))) {
					return nearestCellsToThis.get(i);
				}
			}
			return nearestCellsToThis.get(0);
		}
	}

	public Cell getNewState(double bestAction) {
		for (int i = 0; i < this.actions.length; i++) {
			if (this.actions[i].getPoint() == bestAction
					&& this.actions[i].getFinalCell() != null
					&& !this.actions[i].getFinalCell().isOut()
					&& !this.actions[i].getFinalCell().isBarrier()
					&& !this.actions[i].getFinalCell().equals(this)) {
				return this.actions[i].getFinalCell();
			}
		}
		return null;
	}

	public double getReward() {
		double reward;
		if (isBarrier()) {
			reward = -100;
		} else if (isEmpty()) {
			reward = -1;
		} else if (isOut()) {
			reward = -100;
		} else if (isGoal()) {
			reward = 100; // this is for Goal
//		} else if (isPassed()) {
//			reward = -10;
		} else if (this.state.equals("FOUND")) {
			reward = 10;
		} else {
			reward = -1;
		}

		return reward;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void calculatePoint() {
		Cell bestAcion = this.getBestAction();
		for (int i = 0; i < this.actions.length; i++) {
			if (actions[i].getFinalCell().equals(bestAcion)
					&& !actions[i].getFinalCell().isBarrier()
					&& !actions[i].getFinalCell().isOut()) {
				this.actions[i].calculatePoint(machine);
				return;
			}
		}
	}

	@Override
	public boolean equals(Object object) {
		Cell cell = (Cell) object;
		if (this.getX() == cell.getX() && this.getY() == cell.getY())
			return true;

		return false;
	}

	public String getState() {
		return state;
	}

	public Action[] getActions() {
		return actions;
	}

	public Action findActionByCell(Cell cell) {
		if (up.equals(cell)) {
			return actions[0];
		} else if (right.equals(cell)) {
			return actions[1];
		} else if (down.equals(cell)) {
			return actions[2];
		} else if (left.equals(cell)) {
			return actions[3];
		}
		return null;
	}

	public void setActions(Action[] actions) {
		this.actions = actions;
	}

	public Cell getNextState(Cell nextCell) {
		Action action = this.findActionByCell(nextCell);
		double actionPoint = action.getPoint();
		for (int i = 0; i < this.actions.length; i++) {
			if (actions[i].getPoint() <= actionPoint
					&& !actions[i].equals(action)
					&& actions[i].getFinalCell() != null
					&& !actions[i].getFinalCell().isBarrier()) {
				return actions[i].getFinalCell();
			}
		}
		return null;
	}

	public Cell[] getAllPossibleChoices() {
		Cell[] cells = new Cell[4];
		cells[0] = this.up;
		cells[1] = this.right;
		cells[2] = this.down;
		cells[3] = this.left;
		return cells;
	}

	private Cell chooseNearestCellToGoal(Cell[] suggestedCells) {
		Cell goal = machine.getGoal();
		if (this.machine.getOpponent() == null) {
			ArrayList<Cell> nearestCellsToGoal = this.chooseNearestCellTo(goal,
					suggestedCells);
			return nearestCellsToGoal.get(new Random().nextInt(nearestCellsToGoal.size()));
		}
		Cell opponentNextCell = this.guessOpponentNextAction();
		if (goal == null) {
			machine.showPath();
			throw new NullPointerException();
		}
		ArrayList<Cell> nearestCellsToGoal = this.chooseNearestCellTo(goal,
				suggestedCells);
		ArrayList<Cell> nearestCellsToOpponnetNextCell = this
				.chooseNearestCellTo(opponentNextCell, suggestedCells);
		for (int i = 0; i < nearestCellsToGoal.size(); i++) {
			for (int j = 0; j < nearestCellsToOpponnetNextCell.size(); j++) {
				if (nearestCellsToGoal.get(i).equals(
						nearestCellsToOpponnetNextCell.get(j))) {
					return nearestCellsToGoal.get(i);
				}
			}
		}
		return null;

	}

	private ArrayList<Cell> chooseNearestCellTo(Cell cell, Cell[] suggestedCells) {
		double[] distances = new double[suggestedCells.length];
		for (int i = 0; i < suggestedCells.length; i++) {
			if (suggestedCells[i] == null) {
				distances[i] = 1000;
				continue;
			}
			distances[i] = Math.sqrt(Math.pow(cell.x - suggestedCells[i].x, 2)
					+ Math.pow(cell.y - suggestedCells[i].y, 2));
		}
		double min = 1000;
		for (int i = 0; i < distances.length; i++) {
			if (min > distances[i]) {
				min = distances[i];
			}
		}
		ArrayList<Cell> bestCells = new ArrayList<>();
		for (int i = 0; i < distances.length; i++) {
			if (min == distances[i]) {
				bestCells.add(suggestedCells[i]);
			}
		}
		return bestCells;
	}

	public ArrayList<Cell> chooseFurtherestCellTo(Cell cell, ArrayList<Cell> suggestedCells) {
		double[] distances = new double[suggestedCells.size()];
		for (int i = 0; i < suggestedCells.size(); i++) {
			distances[i] = Math.sqrt(Math.pow(cell.x - suggestedCells.get(i).x, 2)
					+ Math.pow(cell.y - suggestedCells.get(i).y, 2));
		}
		double maximumDistance = -1000;
		for (int i = 0; i < distances.length; i++) {
			if (maximumDistance < distances[i]) {
				maximumDistance = distances[i];
			}
		}
		ArrayList<Cell> bestCells = new ArrayList<>();
		for (int i = 0; i < distances.length; i++) {
			if (maximumDistance == distances[i]) {
				bestCells.add(suggestedCells.get(i));
			}
		}
		return bestCells;
	}

	public boolean isGoal() {
		if (getState().equals("GOAL"))
			return true;

		return false;
	}

	public boolean isBarrier() {
		if (getState().equals("BARRIER"))
			return true;

		return false;
	}

	public boolean isEmpty() {
		if (getState().equals("EMPTY"))
			return true;

		return false;
	}

	public boolean isOut() {
		if (getState().equals("OUT"))
			return true;

		return false;
	}
	
	public boolean isPassed() {
		if (getState().equals("PASSED"))
			return true;
		
		return false;
	}
}
