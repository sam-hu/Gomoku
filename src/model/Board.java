package model;

import java.util.ArrayList;

import org.eclipse.jdt.annotation.NonNull;

/**
 * A Board object represents the state of a board at a given point in time.
 * Each cell may contain a mark from either player, or no mark at all
 * (represented by null).
 * 
 * <p><b>Important:</b> Boards are immutable; the update method returns a
 * modified COPY of the board, without modifying the original board.
 * 
 * <p>Boards have no public constructors.  Instead, they should be created by
 * starting with EMPTY and repeatedly calling update(...).
 */
public class Board {
	/** The number of rows on any board. */
	public static final int NUM_ROWS = 9;
	
	/** The number of columns on any board. */
	public static final int NUM_COLS = 9;
	
	/** The empty board. */
	public static final @NonNull Board EMPTY  = new Board();
	
	/** The set of all valid locations. */
	public static final @NonNull Iterable<Location> LOCATIONS;
	
	/** The possible states of the game. */
	public enum State { HAS_WINNER, DRAW, NOT_OVER }

	private final @NonNull Player[][] board;  // contains NUM_ROWS x NUM_COLS
	private final Victory             winner; // the winner if state is HAS_WINNER
	private final @NonNull State      state;
	
	// set up LOCATIONS
	static {
		ArrayList<Location> l = new ArrayList<Location>(NUM_ROWS * NUM_COLS);
		for (int r = 0; r < NUM_ROWS; r++)
			for (int c = 0; c < NUM_COLS; c++)
				l.add(new Location(r,c));
		LOCATIONS = l;
	}
	
	/** Create an empty board */
	// Do not make this public: to create a Board, start with EMPTY and call update.
	private Board() {
		this.board  = new Player[NUM_ROWS][NUM_COLS];
		this.winner = null;
		this.state  = State.NOT_OVER;
	}
	
	/** Create a copy of other, except that locToChange is mapped to p. */
	// Do not make this public: to create a board, start with EMPTY and call update.
	private Board(@NonNull Board other,
	              @NonNull Location locToChange,
	              @NonNull Player p)
	{
		this.board = new Player[NUM_ROWS][NUM_COLS];
		
		int numBlank = NUM_ROWS * NUM_COLS;
		for (Location loc : LOCATIONS) {
			// invariant1: numBlank = number of nulls in this.board
			// invariant2: all processed locations have been appropriately
			//             updated from other
			Player mark = loc.equals(locToChange) ? p : other.get(loc);
			this.board[loc.row][loc.col] = mark;
			if (mark != null)
				numBlank--;
		}

		this.winner = checkVictoryFrom(locToChange);
		if (winner != null)
			this.state = State.HAS_WINNER;
		else if (numBlank == 0)
			this.state = State.DRAW;
		else
			this.state = State.NOT_OVER;
	}

	/**
	 * Return a new board that is the same as this with loc mapped to p.
	 * @throws IllegalArgumentException if loc has already been played
	 */
	public Board update(@NonNull Player p, @NonNull Location loc)
	             throws IllegalArgumentException
	{
		if (board[loc.row][loc.col] != null)
			throw new IllegalArgumentException(loc + " is already taken");

		if (state != State.NOT_OVER)
			throw new IllegalStateException("Can only submit moves if the game is not over");
		
		return new Board(this,loc,p);
	}

	/** Return the player who has played in loc, or null if loc is empty. */
	public Player get(@NonNull Location loc) {
		return this.board[loc.row][loc.col];
	}
	
	/** Return the player who has played in (r,c), or null if it is empty. */
	public Player get(int row, int col) {
		return this.board[row][col];
	}

	/**
	 * Return a Victory object if the game has been won.
	 * @throws IllegalStateException if the game has not been won.
	 */
	public @NonNull Victory getWinner() throws IllegalStateException {
		return this.winner;
	}
	
	/**
	 * Return the state of the board
	 */
	public @NonNull State getState() {
		return this.state;
	}
	
	/**
	 * If there is a victory going through loc, return it.
	 * Precondition: get(loc) != null
	 */
	private Victory checkVictoryFrom(Location loc) {
		Victory row   = checkVictoryFrom(loc.row, loc.col,  0,1);
		if (row != null) return row;
		
		Victory col   = checkVictoryFrom(loc.row, loc.col,  1,0);
		if (col != null) return col;
		
		Victory bdiag = checkVictoryFrom(loc.row, loc.col,  1,1);
		if (bdiag != null) return bdiag;
		
		Victory fdiag = checkVictoryFrom(loc.row, loc.col, -1,1);
		if (fdiag != null) return fdiag;
		
		return null;
	}
	
	/**
	 * Check to see if there is a victory going through (r,c) with slope (dr,dc).
	 * Return a Line object if so, or null otherwise.
	 * 
	 * Precondition: get(r,c) != null
	 */
	private Victory checkVictoryFrom(int r, int c, int dr, int dc) {
		int start = 1;
		//                       (r,c)-start*(dr,dc)|      |(r,c)|
		// invariant: board: [          ?           |  =p  |  p  |  ?  ]
		while (start > -Line.SIZE
		    && r - start*dr >= 0 && r - start*dr < NUM_ROWS
		    && c - start*dc >= 0 && c - start*dc < NUM_COLS
		    && get(r-start*dr, c-start*dc) == get(r,c)
		)
			start++;
		
		int end = 1;
		//                       |(r,c)|      |(r,c)+end*(dr,dc)
		// invariant: board: [ ? |  p  |  =p  |          ?          ]
		while (end + start - 1 < Line.SIZE
		    && r + end*dr >= 0 && r + end*dr < NUM_ROWS
		    && c + end*dc >= 0 && c + end*dc < NUM_COLS
		    && get(r+end*dr, c+end*dc) == get(r,c))
			end++;
		
		// now:        (r,c)-start*(dr,dc)|        |(r,c)+end*(dr,dc)
		//          [       ?             |   =p   |        ?           ]
		if (end + start - 1 >= Line.SIZE)
			return new Victory(get(r,c), new Line(r-(start-1)*dr, c-(start-1)*dc, dr, dc));
		else
			return null;
	}	
}
