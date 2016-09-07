package main;

import java.util.Random;

import learn.Machine;
import elements.Cell;
import elements.Parameters;

public class Main {
	public static void main(String[] args) {
		int row = Parameters.ROW;
		int col = Parameters.COL;
		
		Cell[][] cells = new Cell[row][col];
		int number = 1;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				cells[i][j] = new Cell("EMPTY", number++);
			}
		}

		cells[0][1].setState("BARRIER");
		cells[1][1].setState("BARRIER");
		cells[2][1].setState("BARRIER");
		cells[3][2].setState("BARRIER");
		cells[1][3].setState("BARRIER");

		cells[0][2].setState("GOAL");

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				cells[i][j].setSurroundingCells(cells);
			}
		}

//		Machine m = new Machine(cells, "mac");

		// Start Learning
		for (int i = 0; i < 1000; i++) {
			System.out.println("i = " + i);
			int xRandom = (new Random().nextInt(col));
			int yRandom = (new Random().nextInt(row));

			for (int j = 0; j < 1000; j++) {
				System.out.println(j + "\t" + xRandom + "\t" + yRandom);
				Cell bestCellToGo = cells[yRandom][xRandom]
						.getBestAction(cells);
				cells[yRandom][xRandom].calculatePoint(cells);
				xRandom = bestCellToGo.getX();
				yRandom = bestCellToGo.getY();
			}
		}

		double[][][] pointsTable = new double[row][col][4];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				for (int k = 0; k < 4; k++) {
					pointsTable[i][j][k] = cells[i][j].getActions()[k]
							.getPoint();
				}
			}
		}
		for (int k = 0; k < 4; k++) {
			for (int i = 0; i < row; i++) {
				for (int j = 0; j < col; j++) {
					System.out.printf("%f\t", pointsTable[i][j][k]);
				}
				System.out.println();
			}
		}

		// Moving the Machine
//		showBestPath(m, cells[0][0]);
	}

	private static void showBestPath(Machine machine, Cell startCell) {
		machine.setCell(startCell);
		/* char[][] path = */machine.findBestChain();
	}
}
