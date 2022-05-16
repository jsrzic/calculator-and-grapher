package hr.fer.zemris.java.gui.calc.components;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;

/**
 * Button which holds a digit.
 * @author Josip
 *
 */
public class DigitButton extends JButton {

	private static final long serialVersionUID = 1L;

	public DigitButton(String text, CalcModel model) {
		super(text);

		this.addActionListener((e) -> {
			try {
				model.insertDigit(Integer.parseInt(text));
			} catch (CalculatorInputException ex) {
			    JOptionPane.showMessageDialog(DigitButton.this.getParent(),ex.getMessage() + "\nPlease reset the calculator.","Warning",JOptionPane.WARNING_MESSAGE);     
			}
		});
	}
	
}
