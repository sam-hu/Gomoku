package model;

/**
 * A GameListener is any object that wants to respond whenever a Game object
 * changes.
 * 
 * @see Game
 * @see Game#addListener
 */
public interface GameListener {
	/** Respond appropriately to changes in the game g. */
	void gameChanged(Game g);
}
