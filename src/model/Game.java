package model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

/**
 * A Game object maintains the state of the game: whose turn it is, and what
 * the board looks like.
 *
 * <p>Game objects use the Observer pattern: each Game has an associated list
 * of {@link GameListener} objects.  Whenever the state of the game changes
 * (for example, if a player makes a move), the Game calls the gameChanged
 * method on each of its observers.
 * 
 * <p>This gives a very flexible way for different components of the code to
 * react to changes.  It is easy to create add code to automatically log moves
 * as they are made, for example.  The view and controller classes both observe
 * the model: the view should be updated if the board contains new information,
 * while the controller may wish to prompt the user for input if it has become
 * their turn.
 */
public class Game {
	private List<GameListener> listeners;
	private Player next;
	private Board  board;

	/** Create a game with an empty board; p goes first. */
	public Game(Player p) {
		this.board = Board.EMPTY;
		this.next  = p;
		this.listeners = new ArrayList<GameListener>();
	}
	
	/** Create a game with an empty board; X goes first. */
	public Game() {
		this(Player.X);
	}
	
	/**
	 * Add a listener to this game: causes the listener to be notified now and
	 * at any point in the future when this game's state changes.
	 */
	public void addListener(@NonNull GameListener listener) {
		this.listeners.add(listener);
		listener.gameChanged(this);
	}

	/** Return the player who should play next. */
	public @NonNull Player nextTurn() {
		return this.next;
	}
	
	/**
	 * Update the game in response to player p playing in location loc.
	 * @throws IllegalArgumentException if p is not currently allowed to play
	 *     in location loc (for example if it is not p's turn, or if loc has
	 *     already been played)
	 */
	public void submitMove(@NonNull Player p, @NonNull Location loc)
	     throws IllegalArgumentException
	{
		if (p != next)
			throw new IllegalArgumentException("it is not " + p + "'s turn");
		
		this.board = this.board.update(p, loc);
		this.next  = this.next.opponent();
		
		for (GameListener g : listeners)
			g.gameChanged(this);
	}

	public Board getBoard() {
		return this.board;
	}
	
}
