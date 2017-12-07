package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import model.Board.State;
import model.Game;
import model.Location;
import model.Player;

public class Space extends JPanel implements MouseListener {
	private final int row, col;
	private Location loc;
	protected boolean selected;
	private Game game;
	private Player player;
	protected boolean winningline;
	private boolean mouseIn;
	protected boolean clickable;

	/**
	 * Creates a new grid tile that can be interacted with if one or more
	 * players is a human
	 */
	public Space(int i, int j, Player mark, Game g) {
		this.row = i;
		this.col = j;
		loc = new Location(row, col);
		selected = false;
		game = g;
		player = mark;
		winningline = false;
		mouseIn = false;
		clickable = false;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenHeight = (int) screenSize.getHeight();
		setPreferredSize(new Dimension(screenHeight / 10, screenHeight / 10));
		addMouseListener(this);
	}

	/**
	 * Displays whether a player has made a move on this Space. If the player
	 * who moves next is a human, displays whose move it is and which Space the
	 * cursor is over. If game is over, displays whether this space is part of
	 * the winning line.
	 */
	public @Override void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
		g2.setColor(Color.BLACK);
		g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		g2.setStroke(new BasicStroke(10));
		if (game.getBoard().get(loc) != null) {
			if (winningline)
				g2.setColor(Color.RED);
			drawPlayer(game.getBoard().get(loc), g2);
		} else if (mouseIn && !selected) {
			g.setColor(Color.BLUE);
			drawPlayer(game.nextTurn(), g2);
		}
	}

	/**
	 * Draws an X or an O in the Space
	 */
	public void drawPlayer(Player p, Graphics2D g) {
		if (p == Player.X) {
			g.drawLine(10, 10, getWidth() - 10, getHeight() - 10);
			g.drawLine(getWidth() - 10, 10, 10, getHeight() - 10);
		} else if (p == Player.O)
			g.drawArc(5, 5, getWidth() - 10, getHeight() - 10, 0, 360);
	}

	/**
	 * If the game is not over, the player who goes next is a human, this Space
	 * is empty, and this Space is clicked, submits a move to the location of
	 * this space.
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (clickable && game.getBoard().getState() == State.NOT_OVER)
			if (game.getBoard().get(row, col) == null) {
				selected = true;
				player = game.nextTurn();
				game.submitMove(player, loc);
			}
	}

	/**
	 * If the game is not over, the player who goes next is a human, this Space
	 * is empty, and the cursor is within this Space, display the player who
	 * moves next within this Space.
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) {
		if (clickable && game.getBoard().getState() == State.NOT_OVER)
			if (game.getBoard().get(row, col) == null) {
				mouseIn = true;
				repaint();
			}
	}

	/**
	 * If the game is not over, the player who goes next is a human, this Space
	 * is empty, and the cursor is not within this Space, display an empty
	 * Space.
	 */
	@Override
	public void mouseExited(MouseEvent arg0) {
		if (clickable && game.getBoard().getState() == State.NOT_OVER)
			if (game.getBoard().get(row, col) == null) {
				mouseIn = false;
				repaint();
			}
	}

	/**
	 * Do nothing.
	 */
	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	/**
	 * Do nothing.
	 */
	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}
