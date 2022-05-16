package hr.fer.zemris.java.gui.calc.components;

import java.util.function.DoubleBinaryOperator;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;

/**
 * Button which holds a binary operator.
 * @author Josip
 *
 */
public class BinaryOperationButton extends JButton implements ItemListener {
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
	private DoubleBinaryOperator op1;
	
	/**
	 * The second operator name.
	 */
	private String altOperation;
	
	/**
	 * The second operator.
	 */
	private DoubleBinaryOperator op2;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public BinaryOperationButton(String text, DoubleBinaryOperator op, CalcModel model) {
		this(text, op, text, op, model);
	}
	
	public BinaryOperationButton(String mainOperation, DoubleBinaryOperator op1, String altOperation, DoubleBinaryOperator op2, CalcModel model) {
		super(mainOperation);
		this.mainOperation = mainOperation;
		this.op1 = op1;
		this.altOperation = altOperation;
		this.op2 = op2;
		
		this.addActionListener(e -> {
			try {
				if(model.hasFrozenValue()) throw new CalculatorInputException("Value was frozen. Expected digit, not operator.");
				
				DoubleBinaryOperator pendingOp = model.getPendingBinaryOperation();
				if(pendingOp != null) {
					model.setValue(pendingOp.applyAsDouble(model.getActiveOperand(), model.getValue()));
				}
				model.setActiveOperand(model.getValue());
				model.setPendingBinaryOperation(mainActive ? op1 : op2);
				model.freezeValue(model.toString());
				model.clear();
			} catch(CalculatorInputException|IllegalStateException|IllegalArgumentException ex) {
			    JOptionPane.showMessageDialog(BinaryOperationButton.this.getParent(),ex.getMessage() + "\nPlease reset the calculator.","Warning",JOptionPane.WARNING_MESSAGE);     
			}
		});
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
