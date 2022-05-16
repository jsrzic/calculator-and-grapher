package hr.fer.zemris.java.gui.calc.components;

import javax.swing.JLabel;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;

/**
 * Label used as a display for anything shown by the calculator.
 * @author Josip
 *
 */
public class Display extends JLabel implements CalcValueListener {
	
	private static final long serialVersionUID = 1L;
	
	public Display(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
	}

	@Override
	public void valueChanged(CalcModel model) {
		this.setText(model.toString());
	}
	
}
