package clui;

import model.*;
import model.Board.State;

/**
 * Helper methods to print out {@link model.Board}s on the console.  This
 * class can also be used as a GameListener; it will cause the board to be
 * printed whenever the board changes.
 */
public class BoardPrinter implements GameListener {

	/** Clear the console. */
	public static void clear() {
		for (int i = 0; i < 80; i++)
			System.out.print("-");
		for (int i = 0; i < 3; i++)
			System.out.println();
	}

	/** Print the board.  For example:
	 * <pre>
	 *      0   1   2   3   4
	 *    +---+---+---+---+---+
	 *  0 |   |   |   |   |   |
	 *    +---+---+---+---+---+
	 *  1 |   | X | O |   |   |
	 *    +---+---+---+---+---+
	 *  2 |   |   | X |   |   |
	 *    +---+---+---+---+---+
	 * </pre>
	 */
	public  static void printBoard(Board b) {	
		// print column headings
		System.out.print("  ");
		for (int col = 0; col < Board.NUM_COLS; col++)
			System.out.print("   " + col);
		System.out.println();
		
		// print first separator (top of table)
		printSep();
		
		Line wins = b.getState() == State.HAS_WINNER ?
			b.getWinner().line : null;

		// print each row
		for (int row = 0; row < Board.NUM_ROWS; row++) {
			System.out.print(" " + row + " ");
			for (int col = 0; col < Board.NUM_COLS; col++) {
				char mark = wins != null && wins.contains(row,col) ? '*' : ' ';
				System.out.print("|" + mark);
				if (b.get(row,col) == null)
					System.out.print(" ");
				else
					System.out.print(b.get(row,col));
				
				System.out.print(mark);
			}
			System.out.println("|");
			printSep();
		}
	}

	/** prints the separator between rows on the board:
	 * <pre>
	 *    +---+---+---+---+
	 * </pre>
	 */
	private static void printSep() {
		System.out.print("   ");
		for (int i = 0; i < Board.NUM_COLS; i++)
			System.out.print("+---");
		System.out.println("+");
	}

	/**
	 * Print the current board and an indication of whether the game is over
     * and, if it is,  whether it is a draw and who the winner is.
     */
	public @Override void gameChanged(Game g) {
		clear();
		System.out.println("Current Board:");
		printBoard(g.getBoard());
		System.out.println();
		switch(g.getBoard().getState()) {
		case HAS_WINNER:
			System.out.println(g.getBoard().getWinner().winner + " wins!");
			break;
		case DRAW:
			System.out.println("Game ended in a draw!");
			break;
		case NOT_OVER:
			System.out.println("It is " + g.nextTurn() + "'s turn");
			break;
		}
	}
	
}
