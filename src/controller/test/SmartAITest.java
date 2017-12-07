package controller.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import controller.SmartAI;
import model.Board;
import model.Location;
import model.Player;

public class SmartAITest {

	// Boards:
	//      |       |        |          |         |            |---
	//      |       |        |   O O    |  OOO    |            |X
	//      |   X   |   XX   |   OXO    |  OXO    |  XXXXX     |
	//      |       |    X   |   O O    |  O O    |            |
	//      |       |        |          |         |            |
	//  (0)    (1)      (2)       (3)       (4)     (5)           (6)

	
	//        |         |        |
	//   XX   |   XXO   |  XX    |
	//    O   |    O    |   O    |
	//        |         |    O   |
	//  (7)       (8)      (9)
	
	// counts for 1, 2, 3, 4, 5 consecutive marks:
	//   ****X  ***X*  **X**  *X*** X****                         5
	//   ****X  ***XX  **XX*  *XX** XX*** X****                   42
	//   ****X  ***XX  **XXX  *XXX* XXX** XX*** X****             322
	//   ****X  ***XX  **XXX  *XXXX XXXX* XXX** XX*** X****       2222
	//   ****X  ***XX  **XXX  *XXXX XXXXX XXXX* XXX** XX*** X**** 12222

	private Board[] boards;
	private SmartAI aix, aio;
	
	@Before
	public void setUp() {
		aix    = new SmartAI(Player.X);
		aio    = new SmartAI(Player.O);
		
		boards = new Board[10];
		
		boards[0] = Board.EMPTY;
		boards[1] = boards[0].update(Player.X, new Location(4,4));
		boards[2] = boards[1].update(Player.X, new Location(3,4))
		                     .update(Player.X, new Location(3,3));
		boards[3] = boards[1].update(Player.O, new Location(3,3))
				             .update(Player.O, new Location(3,5))
				             .update(Player.O, new Location(4,3))
				             .update(Player.O, new Location(4,5))
				             .update(Player.O, new Location(5,3))
				             .update(Player.O, new Location(5,5));
		boards[4] = boards[3].update(Player.O, new Location(3,4));
		boards[5] = boards[1].update(Player.X, new Location(4,2))
				             .update(Player.X, new Location(4,3))
				             .update(Player.X, new Location(4,5))
				             .update(Player.X, new Location(4,6));
		boards[6] = boards[0].update(Player.X, new Location(0,0));
		boards[7] = boards[1].update(Player.X, new Location(4,5))
				             .update(Player.O, new Location(5,5));
		boards[8] = boards[7].update(Player.O, new Location(4,6));
		boards[9] = boards[7].update(Player.O, new Location(6,6));
	}
	
	@Test
	public void testEstimate() {
		int[] xScores = new int[boards.length];
		int[] oScores = new int[boards.length];
		
		for (int i = 0; i < boards.length; i++) {
			xScores[i] = aix.score(boards[i], Player.X);
			oScores[i] = aix.score(boards[i], Player.O);
		}
		assertEquals(xScores[0], 0);
		assertEquals(xScores[1], 20);
		assertEquals(xScores[2], 148);
		assertEquals(xScores[3], 5);
		assertEquals(xScores[4], 1);
		assertTrue(xScores[5] > 10000);
		assertEquals(xScores[6], 3);
		assertTrue(oScores[8] - xScores[8] > oScores[9] - xScores[9]);
		assertTrue(aio.estimate(boards[8]) > aio.estimate(boards[9]));
	}

}
