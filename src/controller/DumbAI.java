package controller;

import model.Board;
import model.Game;
import model.Location;
import model.Player;

/**
 * A DumbAI is a Controller that always chooses the blank space with the
 * smallest column number from the row with the smallest row number.
 */
public class DumbAI extends Controller {

	public DumbAI(Player me) {
		super(me);
		// TODO Auto-generated constructor stub
	}

	protected @Override Location nextMove(Game g) {
		// Note: Calling delay here will make the CLUI work a little more
		// nicely when competing different AIs against each other.
		
		// TODO Auto-generated method stub
		delay();
		for (int r = 0; r < Board.NUM_ROWS; r++)
			for (int c = 0; c < Board.NUM_COLS; c++) {
				Location loc= new Location(r,c);
				if (g.getBoard().get(new Location(r,c))==null)
					return loc;
			}
		return null;
	}
}
