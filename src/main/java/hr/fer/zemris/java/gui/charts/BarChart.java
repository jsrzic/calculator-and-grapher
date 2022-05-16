package hr.fer.zemris.java.gui.charts;

import java.util.List;

/**
 * Model representation of a bar chart.
 * @author Josip
 *
 */
public class BarChart {
	/**
	 * List of values this bar chart should show.
	 */
	private List<XYValue> values;
	
	/**
	 * Name of the x axis.
	 */
	private String xDescription;
	
	/**
	 * Name of the y axis.
	 */
	private String yDescription;
	
	/**
	 * Minimal y axis value.
	 */
	private int yMin;
	
	/**
	 * Maximal y axis value.
	 */
	private int yMax;
	
	/**
	 * Space between two adjacent values on the y axis.
	 */
	private int space;
	
	/**
	 * Creates new {@link BarChart} and initializes its fields.
	 * @throws IllegalArgumentException if yMin < 0, yMin >= yMax or if there is a y-value smaller than yMin
	 */
	public BarChart(List<XYValue> values, String xDescription, String yDescription, int yMin, int yMax, int space) {
		super();
		if (yMin < 0) throw new IllegalArgumentException("yMin can not be negative");
		
		if (yMax <= yMin) throw new IllegalArgumentException("yMax must be larger than yMin");
		
		for(XYValue value : values) {
			if(value.getY() < yMin) {
				throw new IllegalArgumentException("All y-values must be equal or larger than yMin");
			}
		}
		
		while((yMax - yMin) % space != 0) {
			yMax++;
		}
		
		this.values = values;
		this.xDescription = xDescription;
		this.yDescription = yDescription;
		this.yMin = yMin;
		this.yMax = yMax;
		this.space = space;
	}
	
	public List<XYValue> getValues() {
		return values;
	}

	public String getxDescription() {
		return xDescription;
	}

	public String getyDescription() {
		return yDescription;
	}

	public int getyMin() {
		return yMin;
	}

	public int getyMax() {
		return yMax;
	}

	public int getSpace() {
		return space;
	}
	
	
}
