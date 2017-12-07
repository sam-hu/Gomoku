package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.Board;
import model.Game;
import model.GameListener;
import model.Line;
import model.Player;
import model.Board.State;

public class GUIBoard extends JFrame implements GameListener {
	protected static boolean humanX;
	protected static boolean humanO;
	private Line wins;

	/** 
	 * Create a new board that displays the state of the game board and
	 * can be interacted with if one or more players is a human.
	 */
	public GUIBoard(boolean X, boolean Y) {
		humanX = X;
		humanO = Y;
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	/**
	 * Displays a new board.
	 */
	public void printGUIBoard(Board b, Game g) {
		JPanel center = new JPanel();
		center.setLayout(new GridLayout(Board.NUM_ROWS, Board.NUM_COLS));
		for (int row = 0; row < Board.NUM_ROWS; row++)
			for (int col = 0; col < Board.NUM_COLS; col++) {
				Player mark = b.get(row, col);
				Space n = new Space(row, col, mark, g);
				if (humanX || humanO)
					n.clickable = true;
				if (mark != null)
					n.selected = true;
				if (wins != null && wins.contains(row, col))
					n.winningline = true;
				center.add(n);
			}
		add(center, BorderLayout.CENTER);
		setVisible(true);
		if (g.getBoard().getState() == State.HAS_WINNER)
			JOptionPane.showMessageDialog(
					null, g.getBoard().getWinner().winner + " wins!");
		else if (g.getBoard().getState() == State.DRAW)
			JOptionPane.showMessageDialog(null, "It's a draw!");
		pack();
	}

	/** 
	 * Checks for a winner and prints a new board every time the game is changed.
	 */
	@Override
	public void gameChanged(Game g) {
		wins = (g.getBoard().getState() == State.HAS_WINNER)
				? g.getBoard().getWinner().line : null;
		printGUIBoard(g.getBoard(), g);
	}
}
