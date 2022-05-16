package hr.fer.zemris.java.gui.calc;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ItemListener;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.zemris.java.gui.calc.components.BinaryOperationButton;
import hr.fer.zemris.java.gui.calc.components.DigitButton;
import hr.fer.zemris.java.gui.calc.components.Display;
import hr.fer.zemris.java.gui.calc.components.UnaryOperationButton;
import hr.fer.zemris.java.gui.calc.components.UtilButton;
import hr.fer.zemris.java.gui.calc.model.CalcModelImpl;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;
import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

/**
 * Simple calculator application.
 * @author Josip
 *
 */
public class Calculator extends JFrame {
	
	
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public Calculator() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Calculator");
		initGUI();
		pack();
	}
	
	/**
	 * Adds and styles all GUI components.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		CalcModelImpl model = new CalcModelImpl();
		cp.setLayout(new CalcLayout(3));
		
		Stack<Double> stack = new Stack<>();
		
		Display display = new Display(model.toString(), SwingConstants.RIGHT);
		model.addCalcValueListener(display);
		
		JCheckBox invCheckbox = new JCheckBox("Inv");
		invCheckbox.setFont(invCheckbox.getFont().deriveFont(20f));
		
		
		cp.add(styleDisplay(display), new RCPosition(1, 1));
		
		
		cp.add(styleButton(addInvListener(invCheckbox, new UnaryOperationButton("1/x", x -> 1.0/x, "1/x", x -> 1.0/x, model))), new RCPosition(2, 1));
		cp.add(styleButton(addInvListener(invCheckbox, new UnaryOperationButton("log", Math::log10, "10^x", x -> Math.pow(10, x), model))), new RCPosition(3, 1));
		cp.add(styleButton(addInvListener(invCheckbox, new UnaryOperationButton("ln", Math::log, "e^x", Math::exp, model))), new RCPosition(4, 1));
		cp.add(styleButton(addInvListener(invCheckbox, new UnaryOperationButton("sin", Math::sin, "arcsin", Math::asin, model))), new RCPosition(2, 2));
		cp.add(styleButton(addInvListener(invCheckbox, new UnaryOperationButton("cos", Math::cos, "arccos", Math::acos, model))), new RCPosition(3, 2));
		cp.add(styleButton(addInvListener(invCheckbox, new UnaryOperationButton("tan", Math::tan, "arctan", Math::atan, model))), new RCPosition(4, 2));
		cp.add(styleButton(addInvListener(invCheckbox, new UnaryOperationButton("ctg", x -> 1.0/Math.tan(x), "arcctg", x -> Math.atan(1.0/x), model))), new RCPosition(5, 2));
		
		cp.add(styleButton(new DigitButton("0", model)), new RCPosition(5, 3));
		cp.add(styleButton(new DigitButton("1", model)), new RCPosition(4, 3));
		cp.add(styleButton(new DigitButton("2", model)), new RCPosition(4, 4));
		cp.add(styleButton(new DigitButton("3", model)), new RCPosition(4, 5));
		cp.add(styleButton(new DigitButton("4", model)), new RCPosition(3, 3));
		cp.add(styleButton(new DigitButton("5", model)), new RCPosition(3, 4));
		cp.add(styleButton(new DigitButton("6", model)), new RCPosition(3, 5));
		cp.add(styleButton(new DigitButton("7", model)), new RCPosition(2, 3));
		cp.add(styleButton(new DigitButton("8", model)), new RCPosition(2, 4));
		cp.add(styleButton(new DigitButton("9", model)), new RCPosition(2, 5));
		
		
		cp.add(styleButton(addInvListener(invCheckbox, new BinaryOperationButton("x^n", (x, n) -> Math.pow(x, n), "x^(1/n)", (x, n) -> Math.pow(x, 1.0/n), model))), new RCPosition(5, 1));
		cp.add(styleButton(new BinaryOperationButton("/", (a, b) -> (double)a/b, model)), new RCPosition(2, 6));
		cp.add(styleButton(new BinaryOperationButton("*", (a, b) -> a * b, model)), new RCPosition(3, 6));
		cp.add(styleButton(new BinaryOperationButton("-", (a, b) -> a - b, model)), new RCPosition(4, 6));
		cp.add(styleButton(new BinaryOperationButton("+", (a, b) -> a + b, model)), new RCPosition(5, 6));
		
		cp.add(styleButton(new UtilButton("=", e -> {
			try {
				model.setValue(model.getPendingBinaryOperation().applyAsDouble(model.getActiveOperand(), model.getValue()));
				model.clearActiveOperand();
				model.setPendingBinaryOperation(null);
			} catch (CalculatorInputException|IllegalStateException ex) {
			    JOptionPane.showMessageDialog(this.getParent(),ex.getMessage() + "\nPlease reset the calculator.","Warning",JOptionPane.WARNING_MESSAGE);     
			}
		})), new RCPosition(1, 6));
		
		cp.add(styleButton(new UtilButton("+/-", e -> {
			try {
				model.swapSign();
			} catch(CalculatorInputException ex) {
			    JOptionPane.showMessageDialog(this.getParent(),ex.getMessage() + "\nPlease reset the calculator.","Warning",JOptionPane.WARNING_MESSAGE);     
			}
		
		})), new RCPosition(5, 4));
		
		cp.add(styleButton(new UtilButton(".", e -> {
			try {
				model.insertDecimalPoint();
			} catch(CalculatorInputException ex) {
			    JOptionPane.showMessageDialog(this.getParent(),ex.getMessage() + "\nPlease reset the calculator.","Warning",JOptionPane.WARNING_MESSAGE);     
			}
		})), new RCPosition(5, 5));
		
		cp.add(styleButton(new UtilButton("clr", e -> model.clear())), new RCPosition(1, 7));
		
		cp.add(styleButton(new UtilButton("reset", e -> model.clearAll())), new RCPosition(2, 7));
		
		cp.add(styleButton(new UtilButton("push", e -> stack.push(model.getValue()))), new RCPosition(3, 7));
		cp.add(styleButton(new UtilButton("pop", e -> model.setValue(stack.pop()))), new RCPosition(4, 7));
		
		cp.add(invCheckbox, new RCPosition(5, 7));
		
	}
	
	/**
	 * Styles given button and returns it.
	 * @param button button to style
	 * @return styled button
	 */
	private JButton styleButton(JButton button) {
		button.setFont(button.getFont().deriveFont(20f));
		button.setBackground(new Color(142, 175, 212));
		button.setOpaque(true);
		return button;
	}
	
	/**
	 * Styles given display and returns it.
	 * @param display display to style
	 * @return styled display
	 */
	private Display styleDisplay(Display display) {
		display.setFont(display.getFont().deriveFont(30f));
		display.setBackground(new Color(235, 205, 35));
		display.setOpaque(true);
		return display;
	}
	
	/**
	 * Adds listener for Inv checkbox.
	 * @param invCheckbox checkbox which inverts functions of some buttons
	 * @param button button which is a listener for the Inv checkbox
	 * @return the same button
	 */
	private JButton addInvListener(JCheckBox invCheckbox, JButton button) {
		invCheckbox.addItemListener((ItemListener)button);
		return button;
	}
	
	/**
	 * Starts the application.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(()->{
			new Calculator().setVisible(true);
		});
	}
}
