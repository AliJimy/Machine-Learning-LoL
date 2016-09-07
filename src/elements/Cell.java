package elements;

import java.util.ArrayList;
import java.util.Random;

import learn.Action;

public class Cell {
	private String state;

	private Cell up;
	private Cell left;
	private Cell right;
	private Cell down;

	private int number;
	private int x;
	private int y;
	private Action[] actions; // 0: Up, 1: Right, 2: Down, 3: Left

	public Cell(String state, int number) {
		this.actions = new Action[4];
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

	public void setSurroundingCells(Cell[][] cells) {
		Cell out = new Cell("OUT", -1);

		this.left = (x == 0) ? out : (cells[y][x - 1]);
		this.right = (x == Parameters.COL - 1) ? out : (cells[y][x + 1]);
		this.up = (y == 0) ? out : (cells[y - 1][x]);
		this.down = (y == Parameters.ROW - 1) ? out : (cells[y + 1][x]);
		this.setActions();
	}

	public Cell getBestAction(Cell[][] cells) {
		double maxPoint = -1000000.0;
		for (int i = 0; i < this.actions.length; i++) {
			if (maxPoint < this.actions[i].getPoint()
					&& this.actions[i].getFinalCell() != null
					&& !this.actions[i].getFinalCell().getState().equals("OUT")
					&& !this.actions[i].getFinalCell().getState()
							.equals("BARRIER")) {
				maxPoint = this.actions[i].getPoint();
			}
		}

		Cell[] cls = new Cell[4];
		int num = 0;
		for (int i = 0; i < this.actions.length; i++) {
			if (this.actions[i].getPoint() == maxPoint
					&& this.actions[i].getFinalCell() != null
					&& !this.actions[i].getFinalCell().getState().equals("OUT")
					&& !this.actions[i].getFinalCell().getState()
							.equals("BARRIER")) {
				cls[num++] = this.actions[i].getFinalCell();
			}
		}
		return this.chooseNearestCellToGoal(cls, cells);
	}

	public Cell getNewState(double bestAction) {
		for (int i = 0; i < this.actions.length; i++) {
			if (this.actions[i].getPoint() == bestAction
					&& this.actions[i].getFinalCell() != null
					&& !this.actions[i].getFinalCell().getState().equals("OUT")
					&& !this.actions[i].getFinalCell().getState()
							.equals("BARRIER")
					&& !this.actions[i].getFinalCell().equals(this)) {
				return this.actions[i].getFinalCell();
			}
		}
		return null;
	}

	public double getReward() {
		double reward;
		if (this.state.equals("BARRIER")) {
			reward = -100;
		} else if (this.state.equals("EMPTY")) {
			reward = -1;
		} else if (this.state.equals("OUT")) {
			reward = -100;
		} else if (this.state.equals("GOAL")) {
			reward = 100; // this is for Goal
		} else if (this.state.equals("PASSED")) {
			reward = -10;
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

	public void calculatePoint(Cell[][] cells) {
		Cell bestAcion = this.getBestAction(cells);
		for (int i = 0; i < this.actions.length; i++) {
			if (actions[i].getFinalCell().equals(bestAcion)
					&& !actions[i].getFinalCell().getState().equals("BARRIER")
					&& !actions[i].getFinalCell().getState().equals("OUT")) {
				this.actions[i].calculatePoint(cells);
				return;
			}
		}
	}

	@Override
	public boolean equals(Object object) {
		Cell cell = (Cell) object;
		if (this.getX() == cell.getX() && this.getY() == cell.getY()) {
			return true;
		}
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
					&& !actions[i].getFinalCell().getState().equals("BARRIER")) {
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

	private Cell chooseNearestCellToGoal(Cell[] suggestedCells, Cell[][] cells) {
		Cell goal = null;

		for (int i = 0; i < Parameters.ROW; i++) {
			for (int j = 0; j < Parameters.COL; j++) {
				if (cells[i][j].getState().equals("GOAL")) {
					goal = cells[i][j];
					break;
				}
			}
		}
		double[] distances = new double[suggestedCells.length];
		for (int i = 0; i < suggestedCells.length; i++) {
			if (suggestedCells[i] == null) {
				distances[i] = 1000;
				continue;
			}
			distances[i] = Math.sqrt(Math.pow(goal.x - suggestedCells[i].x, 2)
					+ Math.pow(goal.y - suggestedCells[i].y, 2));
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
		return bestCells.get(new Random().nextInt(bestCells.size()));
	}
}
