package clui;

import java.util.Scanner;

import controller.Controller;
import model.Game;
import model.Location;
import model.Player;

/**
 * A Controller that queries the user for moves and then submits them.
 */
public class ConsoleController extends Controller {

	public ConsoleController(Player me) {
		super(me);
	}

	/**
	 * Prompt the user for the next move.  Requires one of the following inputs:
     *  exit                  --terminate the program
     *  quit                  --terminate the program
     *  two integers r and c  --submit a move to location (r, c).
     */
	@Override
	protected Location nextMove(Game g) {
		Scanner lines = new Scanner(System.in);
		while (true) {
			try {
				System.out.print("enter row and column for " + g.nextTurn() + ": ");
				String in = lines.nextLine();
				if (in.equals("exit") || in.equals("quit"))
					System.exit(0);
				Scanner line = new Scanner(in);
				return new Location(line.nextInt(), line.nextInt());
			} catch (Exception e) {
				System.out.println("error: " + e);
			}
		}
	}
}
