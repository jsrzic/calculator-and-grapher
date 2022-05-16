package hr.fer.zemris.java.gui.layouts;

import java.util.Objects;

/**
 * Models a coordinate used by {@link CalcLayout}.
 * It is defined by two properties: row and column.
 * @author Josip
 *
 */
public class RCPosition {
	private int row;
	private int column;
	
	public RCPosition(int row, int column) {
		super();
		this.row = row;
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}
	
	public static RCPosition parse(String text) {
		String[] chunks = text.split(",");
		int row;
		int column;
		
		if (chunks.length != 2) {
			throw new IllegalArgumentException("Invalid position format: " + text);
		}
		
		try {
			row = Integer.parseInt(chunks[0]);
			column = Integer.parseInt(chunks[1]);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid position format: " + text);
		}
		
		return new RCPosition(row, column);
	}

	@Override
	public String toString() {
		return row + "," + column;
	}

	@Override
	public int hashCode() {
		return Objects.hash(column, row);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof RCPosition))
			return false;
		RCPosition other = (RCPosition) obj;
		return column == other.column && row == other.row;
	}
	
	
	
}
