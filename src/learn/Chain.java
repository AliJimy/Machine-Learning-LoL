package learn;

import java.util.ArrayList ;

import elements.Cell;
import elements.Parameters;

/**
 * Created by Future on 8/31/16.
 */
public class Chain {
	private ArrayList<Cell> cells = new ArrayList<Cell>();
	private double totalPoint;
	private double rate;

	// Constructors
	public Chain() {
		this.cells = new ArrayList<Cell>();
		this.totalPoint = 0;
		this.rate = 0;
	}

	// Functions
	private void calculateTotalPoint() {
		this.totalPoint = 0;
		for (int i = 0; i < Parameters.ROW - 1; i++) {
			// double point = cells.get(i).getRewardForNextCell(cells.get(i +
			// 1));
			// this.totalPoint += point;
		}
		this.rate = this.totalPoint / this.cells.size();
	}

	public boolean hasRepeatedCell() {
		for (int i = 0; i < this.cells.size(); i++) {
			Cell cell = this.cells.get(i);
			for (int j = 0; j < this.cells.size(); j++) {
				if (i != j && cell.equals(this.cells.get(j))) {
					return true;
				}
			}
		}
		
		return false;
	}

	public boolean isRepeatedCell(Cell cell) {
		return this.cells.contains(cell);
	}

	public void addCell(Cell cell) {
		this.cells.add(cell);
	}

	// Getter && Setters
	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public double getTotalPoint() {
		return totalPoint;
	}

	public void setTotalPoint(double totalPoint) {
		this.totalPoint = totalPoint;
	}

	public ArrayList<Cell> getCells() {
		return cells;
	}

	public void setCells(ArrayList<Cell> cells) {
		this.cells = cells;
		this.calculateTotalPoint();
	}

	public boolean isNextCellACorrectChoice(Cell nextCell) {
		if (this.isRepeatedCell(nextCell)) {
			return false;
		}
		Cell[] newStates = nextCell.getAllPossibleChoices();
		for (int i = 0; i < newStates.length; i++) {
			if (!newStates[i].isBarrier()
					&& !newStates[i].isOut()
					&& !this.isRepeatedCell(newStates[i])) {
				return true;
			}
		}
		return false;
	}
}
