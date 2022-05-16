package hr.fer.zemris.java.gui.layouts;

/**
 * This exception is thrown by {@link CalcLayout} whenever user tries to add a component to an invalid position or 
 * a position that is already taken. 
 * @author Josip
 *
 */
public class CalcLayoutException extends RuntimeException {

	private static final long serialVersionUID = -3503721957041493250L;

	public CalcLayoutException(String message) {
		super(message);
	}
	
}
