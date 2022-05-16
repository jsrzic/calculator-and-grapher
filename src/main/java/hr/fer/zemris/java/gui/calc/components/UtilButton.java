package hr.fer.zemris.java.gui.calc.components;

import javax.swing.JButton;

import java.awt.event.ActionListener;

/**
 * Button used for some utility (not digit nor operator).
 * @author Josip
 *
 */
public class UtilButton extends JButton {

	private static final long serialVersionUID = 1L;

	public UtilButton(String text, ActionListener listener) {
		super(text);
		this.addActionListener(listener);
	}
}
