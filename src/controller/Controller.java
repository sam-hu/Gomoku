package controller;

import model.Board.State;
import model.Game;
import model.GameListener;
import model.Location;
import model.Player;

/**
 * Controllers represent different strategies for choosing the next move when
 * it is a player's turn.  For example, there are controllers that ask the user
 * to input moves, and other controllers that use various algorithms to make
 * their moves.
 */
public abstract class Controller implements GameListener {

	protected final Player me;

	/**
	 * Return the Location of the next move this player should make in
	 * game g, or null if the player cannot play.
	 * 
	 * Precondition, it is this player's turn.
	 */
	protected abstract Location nextMove(Game g);
	
	/** Make a move for game g if possible. */
	public @Override void gameChanged(Game g) {
		if (g.getBoard().getState() == State.NOT_OVER && g.nextTurn() == me) {
			Location move = nextMove(g);
			if (move != null)
				g.submitMove(me, move);
		}
	}

	/** A Controller that plays for me */
	protected Controller(Player me) {
		this.me = me;
	}
	
	/** Do nothing for a short time. */
	protected void delay() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
	}
}
