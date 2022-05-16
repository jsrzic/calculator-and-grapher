package hr.fer.zemris.java.gui.calc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

/**
 * Implementation of a simple calculator model.
 * @author Josip
 *
 */
public class CalcModelImpl implements CalcModel {
	private boolean isEditable;
	private boolean isPositive;
	private String stringValue;
	private double doubleValue;
	private String frozenStringValue;
	private Double activeOperand;
	private DoubleBinaryOperator pendingOperation;
	private List<CalcValueListener> listeners;
	
	public CalcModelImpl() {
		this.isEditable = true;
		this.isPositive = true;
		this.stringValue = "";
		this.doubleValue = 0;
		this.frozenStringValue = null;
		this.activeOperand = null;
		this.pendingOperation = null;
		this.listeners = new ArrayList<>();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addCalcValueListener(CalcValueListener l) {
		listeners.add(l);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeCalcValueListener(CalcValueListener l) {
		listeners.remove(l);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getValue() {
		return doubleValue * (isPositive ? 1 : -1);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(double value) {
		isPositive = value >= 0;
		doubleValue = Math.abs(value);
		stringValue = String.valueOf(doubleValue);
		isEditable = false;
		notifyAllListeners();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEditable() {
		return isEditable;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		stringValue = "";
		doubleValue = 0;
		isEditable = true;
		isPositive = true;
		notifyAllListeners();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearAll() {
		clear();
		activeOperand = null;
		pendingOperation = null;
		frozenStringValue = null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void swapSign() throws CalculatorInputException {
		if(!isEditable) throw new CalculatorInputException("Calculator is not editable.");
		
		isPositive = isPositive ? false : true;
		frozenStringValue = null;
		notifyAllListeners();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void insertDecimalPoint() throws CalculatorInputException {
		if(!isEditable) throw new CalculatorInputException("Calculator is not editable.");
		
		if(stringValue.equals("")) throw new CalculatorInputException("No digits were added before the decimal point.");
		
		if(stringValue.contains(".")) throw new CalculatorInputException("Decimal point is already present.");
		
		
		stringValue += ".";
		doubleValue = Double.parseDouble(stringValue);
		frozenStringValue = null;
		notifyAllListeners();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {
		if (digit < 0 || digit > 9) throw new IllegalArgumentException("Given number is not a digit: " + digit);
		
		if(!isEditable) throw new CalculatorInputException("Calculator is not editable.");
		
		if (digit == 0 && stringValue.equals("0")) return;
		
		String newStringValue = (stringValue.equals("0") ? "" : stringValue) + digit;
		double newDoubleValue;
		try {
			newDoubleValue = Double.parseDouble(newStringValue);
		} catch (NumberFormatException e) {
			throw new CalculatorInputException("New string value can not be parsed into a double.");
		}
		
		if (newDoubleValue > Double.MAX_VALUE) throw new CalculatorInputException("Input value is too big.");
		
		stringValue = newStringValue;
		doubleValue = newDoubleValue;
		frozenStringValue = null;
		notifyAllListeners();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		if (frozenStringValue != null) return frozenStringValue;
		
		if (stringValue.equals("")) {
			return isPositive ? "0" : "-0";
		}
		
		else {
			if(doubleValue == Math.floor(doubleValue) && !Double.isInfinite(doubleValue)) {
				return (isPositive ? "" : "-") + (int)doubleValue;
			}
			return (isPositive ? "" : "-") + stringValue;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isActiveOperandSet() {
		return activeOperand != null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getActiveOperand() throws IllegalStateException {
		if(!isActiveOperandSet()) throw new IllegalStateException("Active operand is not set.");
		
		return activeOperand;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setActiveOperand(double activeOperand) {
		this.activeOperand = activeOperand;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearActiveOperand() {
		activeOperand = null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DoubleBinaryOperator getPendingBinaryOperation() {
		return pendingOperation;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPendingBinaryOperation(DoubleBinaryOperator op) {
		this.pendingOperation = op;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void freezeValue(String value) {
		frozenStringValue = value;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasFrozenValue() {
		return frozenStringValue != null;
	}
	
	/**
	 * Notifies all listeners that a value has been changed.
	 */
	private void notifyAllListeners() {
		for(CalcValueListener listener : listeners) {
			listener.valueChanged(this);
		}
	}
}
