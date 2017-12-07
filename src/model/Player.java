package model;

/** There are two players; X and O */
public enum Player {
	X, O;

	/** Return the player's opponent (X.opponent() == O and O.opponent() == X). */
	public Player opponent() {
		return this == X ? O : X;
	}
}
