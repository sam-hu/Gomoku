package clui;

import java.util.Scanner;

import controller.Controller;
import controller.DumbAI;
import controller.RandomAI;
import controller.SmartAI;
import model.Game;
import model.Player;

/**
 * Main application class for the CLUI
 */
public class Main {

	/**
	 * Ask the user what kind of controller should play for p; create and
	 * return the corresponding object.
	 */
	public static Controller createController(Player p) {
		Scanner s = new Scanner(System.in);
		while(true) {
			System.out.println("How should " + p + " be played?");
			System.out.println("1) by a human   2) by the dumb AI   3) by the RandomAI   4) by the SmartAI");
			switch(s.nextInt()) {
			case 1:
				return new ConsoleController(p);
			case 2:
				return new DumbAI(p);
			case 3:
				return new RandomAI(p);
			case 4:
				return new SmartAI(p);
			default:
				System.out.println("please enter 1 or 2 or 3 or 4");
			}
		}
	}
	
	/**
	 * Run a game at the console.  Ask the user what kind of players to use,
	 * then let them compete! */
	public static void main(String[] args) {
		Game g = new Game();
		
		// create the controllers for the two players
		Controller playerX = createController(Player.X);
		Controller playerO = createController(Player.O);

		// cause the board to be printed when it changes.
		g.addListener(new BoardPrinter());

		// cause the players to play when it is their turn
		g.addListener(playerX);
		g.addListener(playerO);

		// Note: playerX and playerY will make moves when it is their turn,
		// which will in turn change the board.  When the board changes, the
		// players will be notified, which will cause them to submit additional
		// moves.  In this way, the game will recursively run until no player
		// is ready to submit another move.
	}
}
