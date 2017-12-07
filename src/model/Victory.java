package model;

import org.eclipse.jdt.annotation.NonNull;

/**
 * A Victory object contains information about a game that has been won: who
 * won, and which positions form their "five in a row".
 */
public class Victory {

	public final @NonNull Player winner;
	public final @NonNull Line   line;
	
	/**
	 * Create a new Victory with the given winner and line.
	 */
	public Victory(@NonNull Player winner, Line line) {
		this.winner = winner;
		this.line   = line;
	}
	
}
