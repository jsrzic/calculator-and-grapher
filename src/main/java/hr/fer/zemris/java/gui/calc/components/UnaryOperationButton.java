package hr.fer.zemris.java.gui.calc.components;

import java.util.function.UnaryOperator;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;

/**
 * Button which holds a unary operator.
 * @author Josip
 *
 */
public class UnaryOperationButton extends JButton implements ItemListener{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Flag which determines which of the two BinaryOperators is used when the button is clicked.
	 */
	boolean mainActive = true;
	
	/**
	 * The first operator name.
	 */
	private String mainOperation;
	
	/**
	 * The first operator.
	 */
	private UnaryOperator<Double> op1;
	
	/**
	 * The second operator name.
	 */
	private String altOperation;
	
	/**
	 * The second operator.
	 */
	private UnaryOperator<Double> op2;
	
	/**
	 * Constructor.
	 */
	public UnaryOperationButton(String mainOperation, UnaryOperator<Double> op1, String altOperation, UnaryOperator<Double> op2, CalcModel model) {
		super(mainOperation);
		this.mainOperation = mainOperation;
		this.op1 = op1;
		this.altOperation = altOperation;
		this.op2 = op2;
		this.addActionListener((e) -> {
			try {
				if(model.hasFrozenValue()) throw new CalculatorInputException("Value was frozen. Expected digit, not operator.");
				model.setValue(performOperation(model.getValue()));
			} catch(CalculatorInputException ex) {
			    JOptionPane.showMessageDialog(UnaryOperationButton.this.getParent(),ex.getMessage() + "\nPlease reset the calculator.","Warning",JOptionPane.WARNING_MESSAGE);     
			}
		});
	}
	
	/**
	 * Performs the actual unary operation. Uses mainActive flag to determine which operator it should use.
	 * @param value operand
	 * @return result of the operation
	 */
	private double performOperation(double value) {
		return mainActive ? op1.apply(value) : op2.apply(value);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getStateChange() == 1) {
			this.setText(altOperation);
			mainActive = false;
		}
		else {
			this.setText(mainOperation);
			mainActive = true;
		}
	}
	
}
