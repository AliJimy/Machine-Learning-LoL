package learn;

import elements.Cell;

public class Action {
	private double point;
	private int numberOfCalls;
	private String kind;
	private Cell sourceCell;
	private Cell finalCell;
	private final double alpha = 0.5;
	private final double gamma = 0.5;

	public Action(Cell sourceCell, Cell finalCell, String kind) {
		this.sourceCell = sourceCell;
		this.finalCell = finalCell;
		this.point = 0;
		this.numberOfCalls = 0;
		this.kind = kind;
	}

	public void calculatePoint(Machine machine) {
		this.setPoint(this.point
				+ alpha
				* (finalCell.getReward()
						+ (gamma * sourceCell.getBestAction().getReward()) - this.point));
		this.numberOfCalls++;
	}

	@Override
	public boolean equals(Object object) {
		Action action = (Action) object;
		if (action.getSourceCell().equals(this.sourceCell)
				&& action.getFinalCell().equals(this.finalCell)) {
			return true;
		}

		return false;
	}

	public double getPoint() {
		return point;
	}

	public void setPoint(double point) {
		this.point = point;
	}

	public int getNumberOfCalls() {
		return numberOfCalls;
	}

	public void setNumberOfCalls(int numberOfCalls) {
		this.numberOfCalls = numberOfCalls;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public Cell getFinalCell() {
		return finalCell;
	}

	public void setFinalCell(Cell finalCell) {
		this.finalCell = finalCell;
	}

	public Cell getSourceCell() {
		return sourceCell;
	}

	public void setSourceCell(Cell sourceCell) {
		this.sourceCell = sourceCell;
	}
}
