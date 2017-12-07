package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A line represents SIZE contiguous locations along a given row, column, or
 * diagonal.
 */

public class Line implements Iterable<Location> {

	/** The number of marks in a row required for victory. */
	public static final int SIZE = 5;
	
	/** The set of all valid lines */
	public static final Iterable<Line> ALL_LINES;
	
	private final int r, c;
	private final int dr, dc;
	
	static {
		// set up LINES
		ArrayList<Line> lines = new ArrayList<Line>();

		// Notes on bounds:
		//    bounds on start of segment <=====>   bounds on end of segment
		//    c < NUM_COLS - SIZE + 1    <=====>   c + (SIZE-1)*1 < NUM_COLS
		//    r < NUM_ROWS - SIZE + 1    <=====>   r + (SIZE-1)*1 < NUM_ROWS
		//    SIZE - 1 <= r              <=====>   0 <= r + (SIZE-1)*(-1)
		//    SIZE - 1 <= c              <=====>   0 <= c + (SIZE-1)*(-1)

		// horizontal lines
		for (int r = 0; r < Board.NUM_ROWS; r++)
			for (int c = 0; c < Board.NUM_COLS - SIZE + 1; c++)
				lines.add(new Line(r,c,0,1)); 
		
		// vertical lines
		for (int r = 0; r < Board.NUM_ROWS - SIZE + 1; r++)
			for (int c = 0; c < Board.NUM_COLS; c++)
				lines.add(new Line(r,c,1,0));
		

		// lines going northeast
		for (int r = SIZE-1; r < Board.NUM_ROWS; r++)
			for (int c = 0; c < Board.NUM_COLS - SIZE + 1; c++)
				lines.add(new Line(r,c,-1,1));
		
		// lines going southeast
		for (int r = 0; r < Board.NUM_ROWS - SIZE + 1; r++)
			for (int c = 0; c < Board.NUM_COLS - SIZE + 1; c++)
				lines.add(new Line(r,c,1,1));
		
		ALL_LINES = lines;
	}
	
	/**
	 * Create a line consisting of the locations (r,c), (r+dr, c+dc),
	 * (r+2dr, c+2dc), ...
	 * 
	 * Create a Line object consisting of SIZE marks in a line, starting at
	 * position (r,c) and moving in direction (dr,dc).  In other words, the
	 * contains locations (r,c), (r+dr, c+dc), (r+2dr, c+2dc), ...,
	 * (r+(SIZE-1)*dr,c+(SIZE-1)*dc).
	 *
	 * @throws IllegalArgumentException if any of these locations are invalid.
	 * @throws IllegalArgumentException if (dr,dc) is (0,0) or |dr| > 1 or |dc| > 1
	 */
	public Line(int r, int c, int dr, int dc) throws IllegalArgumentException {
		// check the bounds for the start and endpoint (location constructor throws
		// exception if not):
		new Location(r,c);
		new Location(r + (SIZE-1)*dr, c + (SIZE-1)*dc);
		
		// check that (dr,dc) defines a valid line
		if (dr < -1 || dr > 1 || dc < -1 || dc > 1)
			throw new IllegalArgumentException();
		if (dr == 0 && dc == 0)
			throw new IllegalArgumentException();
		
		this.r = r;    this.dr = dr;
		this.c = c;    this.dc = dc;
	}
	
	/** Iterate over the locations in this line */
	public @Override Iterator<Location> iterator() {
		return new LocationIterator();
	}
	
	/** Return true if the given location is in this line */
	public boolean contains(int r, int c) {
		// if (r,c) is on the line, then (r,c) = (this.r,this.c) + n(dr,dc)
		// with 0 <= n < SIZE.
		//
		// Then (r-this.r, c-this.c) = n(dr,dc), which means (if dr and dc != 0)
		// that n = (r-this.r)/dr = (c-this.c)/dc multiplying both sides by
		// dr*dc, we get n dr dc = (r-this.r)*dc = (c-this.c)*dr
		//
		// note that this formula is still correct even if one of dr or dc is 0:
		// for example, if dr is 0, then the point is on the line iff r = this.r,
		// which is true iff (r-this.r)*dc = (c-this.c)*dr.  (Note that dr and dc
		// cannot BOTH be 0).

		boolean slopeSame = dc*(r - this.r) == dr*(c - this.c);

		// we must also check that 0 <= n < SIZE.  We have to avoid dividing by 0
		int n = dr != 0 ? (r-this.r)/dr : (c-this.c)/dc;
		
		return slopeSame && 0 <= n && n < SIZE;
	}

	public @Override String toString() {
		StringBuilder result = new StringBuilder();
		for (Location loc : this)
			result.append(loc + " ");
		return result.toString();
	}
	
	private class LocationIterator implements Iterator<Location> {
		private int i = 0;
		
		public boolean hasNext() {
			return i < SIZE;
		}

		@Override
		public Location next() {
			if (i >= 5)
				throw new NoSuchElementException();
			i = i+1;
			return new Location(r + (i-1)*dr, c + (i-1)*dc);
		}
	}


}
