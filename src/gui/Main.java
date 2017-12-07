/* NetId(s): sh924. Time spent: 10 hours, 50 minutes. */

package gui;

import javax.swing.JOptionPane;
import controller.Controller;
import controller.DumbAI;
import controller.RandomAI;
import controller.SmartAI;
import model.Game;
import model.Player;

public class Main {
	/**
	 * Ask the user what kind of controller should play for p; create and
	 * return the corresponding object.
	 */
	public static Controller createController(Player p) {
		boolean selected = false;
		while (!selected) {
			Object[] options = { "Human", "DumbAI", "RandomAI", "SmartAI" };
			int n = JOptionPane.showOptionDialog(null, 
					"Please select who plays for " + p, 
					"Select player",
					JOptionPane.YES_NO_CANCEL_OPTION, 
					JOptionPane.QUESTION_MESSAGE, 
					null, 
					options, 
					null);
			switch (n) {
			case 0:
				selected = true;
				return null;
			case 1:
				selected = true;
				return new DumbAI(p);
			case 2:
				selected = true;
				return new RandomAI(p);
			case 3:
				selected = true;
				return new SmartAI(p);
			case JOptionPane.CLOSED_OPTION:
				System.exit(0);
			}
		}
		return null;
	}

	/**
	 * Run a game using GUIBoard.  Ask the user what kind of players to use,
	 * then let them compete! 
	 */
	public static void main(String[] args) {
		Game g = new Game();
		Controller playerX = createController(Player.X);
		Controller playerO = createController(Player.O);
		GUIBoard b = new GUIBoard(playerX == null, playerO == null);
		g.addListener(b);
		if (playerX == null)
			GUIBoard.humanX = true;
		else {
			GUIBoard.humanX = false;
			g.addListener(playerX);
		}
		if (playerO == null)
			GUIBoard.humanO = true;
		else {
			GUIBoard.humanO = false;
			g.addListener(playerO);
		}
	}
}
