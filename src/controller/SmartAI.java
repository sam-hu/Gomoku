package controller;

import java.util.ArrayList;
import java.util.Collections;

import model.Board;
import model.Line;
import model.Location;
import model.Player;

/**
 * The SmartAI uses min-max search with reasonable heuristics to choose
 * the next move to make.
 *
 * <p>This implementation uses a search depth of 4, only places moves adjacent
 * to moves that have already been played, and uses a reasonable heuristic
 * measure of goodness (described below).
 */
public class SmartAI extends MinMaxAI {

	public SmartAI(Player me) {
		super(me, 3);
	}

	
	/**
	 * A particular line of 5 adjacent cells is "winnable" for player p if
	 * it does not contain any of the opponent's marks.
	 * 
	 * <p>We measure goodness by counting the number of winnable lines, and
	 * scoring them based on the number of the player's marks as follows:
	 * 
	 * <table><tr><th> Number of marks </th> <th>  Score    </th></tr>
	 *        <tr><td>     0           </td> <td>  0        </td></tr>
	 *        <tr><td>     1           </td> <td>  1        </td></tr>
	 *        <tr><td>     2           </td> <td>  10       </td></tr>
	 *        <tr><td>     3           </td> <td>  100      </td></tr>
	 *        <tr><td>     4           </td> <td>  1000     </td></tr>
	 *        <tr><td>     5           </td> <td>  10000    </td></tr>
	 * </table>
	 *
	 * <p>Note that overlapping segments will be counted multiple times, so
	 * that, for example, the following board:
	 * <pre> 
	 *       O O
	 *       OXO
	 *       O O
	 * </pre>
	 * will count as 5 points for X, since there are 5 vertical line segments
	 * passing through X, while
	 * <pre>
	 *       OOO
	 *       OXO
	 *       O O
	 * </pre>
	 * will only count for 1 point, since only the line segment proceeding down
	 * from X is winnable.
	 * 
	 * The estimate is the difference between the player's score and his
	 * opponent's
	 */
	@Override
	public int estimate(Board b) {
		return score(b,me) - score(b,me.opponent());
	}
	
	/** return the positive score for player p */
	public int score(Board b, Player p) {
		int result = 0;
		for (Line s : Line.ALL_LINES) {
			int score = score(b,p,s);
			result += score;
		}
		return result;
	}
	
	/** Return the score for player p, for the line s. */
	public int score(Board b, Player p, Line s) {
		int result = 1;
		for (Location loc : s) {
			if (b.get(loc) == p)
				result *= 10;
			if (b.get(loc) == p.opponent())
				return 0;
		}
		return result/10;
	}

	/**
	 * Return the set of locations that are adjacent to played locations, or
	 * the center of the board if no moves have been played.
	 */
	public @Override Iterable<Location> moves(Board b) {
		ArrayList<Location> result = new ArrayList<>();
		for (Location loc : Board.LOCATIONS)
			if (reasonableMove(b,loc))
				result.add(loc);
		
		if (result.isEmpty())
			return Collections.singleton(new Location(Board.NUM_ROWS/2, Board.NUM_COLS/2));
		return result;
	}

	/** Returns true iff loc is empty and has a neighbor. */
	public boolean reasonableMove(Board b, Location loc) {
		if (b.get(loc) != null)
			return false;
		for (int dr = -1; dr <= 1; dr++)
			for (int dc = -1; dc <= 1; dc++)
				if (isPlayed(b,loc.row+dr,loc.col+dc))
					return true;
		return false;
	}
	
	/** Returns true iff (r,c) is in bounds and b(r,c) is non-null */
	private boolean isPlayed(Board b, int r, int c) {
		return 0 <= r && r < Board.NUM_ROWS
		    && 0 <= c && c < Board.NUM_COLS
		    && b.get(r,c) != null;
	}
	
}
