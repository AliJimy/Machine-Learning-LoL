package learn;

import elements.Cell;
import elements.Parameters;

public class Action {
	private double point;
	private int numberOfCalls;
	private String kind;
	private Cell sourceCell;
	private Cell finalCell;

	public Action(Cell sourceCell, Cell finalCell, String kind) {
		this.sourceCell = sourceCell;
		this.finalCell = finalCell;
		this.kind = kind;
		this.point = Parameters.points[sourceCell.getY()][sourceCell.getX()][getIndexAction()];
		this.numberOfCalls = 0;
//		this.point = 0;
	}

	public void calculatePoint(Machine machine) {
		this.setPoint(this.point
				+ Parameters.ALPHA * (finalCell.getReward()
				+ (Parameters.GAMMA * sourceCell.getBestAction().getReward()) 
				- this.point));
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

	public int getIndexAction(){
		if(kind.equals("UP"))
			return 0;
		else if(kind.equals("RIGHT"))
			return 1;
		else if(kind.equals("DOWN"))
			return 2;
		else if(kind.equals("LEFT"))
			return 3;
		else
			return -1;
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
