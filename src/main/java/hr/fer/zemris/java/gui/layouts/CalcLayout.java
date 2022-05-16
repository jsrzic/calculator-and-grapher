package hr.fer.zemris.java.gui.layouts;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

import hr.fer.zemris.java.gui.calc.Calculator;

/**
 * Custom layout used for {@link Calculator}.
 * @author Josip
 *
 */
public class CalcLayout implements LayoutManager2 {
	
	/**
	 * Maps positions to components.
	 */
	private Map<RCPosition, Component> layoutMap;
	
	/**
	 * Distance between components.
	 */
	private int spaceSize;
	
	
	/**
	 * Creates new {@link CalcLayout} with given space size.
	 * @param spaceSize distance between components
	 */
	public CalcLayout(int spaceSize) {
		super();
		this.layoutMap = new HashMap<>();
		this.spaceSize = spaceSize;
	}
	
	/**
	 * Creates new {@link CalcLayout} with space size set to 0.
	 */
	public CalcLayout() {
		this(0);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addLayoutComponent(String name, Component comp) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeLayoutComponent(Component comp) {
		for (Iterator<Map.Entry<RCPosition, Component>> it = layoutMap.entrySet().iterator(); it.hasNext();) {
			 Map.Entry<RCPosition, Component> e = it.next();
			 if (e.getValue().equals(comp)) {
				 it.remove();
			 }
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return calculateLayoutSize(parent, -1, c -> c.getPreferredSize());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return calculateLayoutSize(parent, -1, c -> c.getMinimumSize());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void layoutContainer(Container parent) {
		Rectangle r = parent.getBounds();
		
		int[] widths = distributeWidths(r.width - 6 * spaceSize - parent.getInsets().left - parent.getInsets().right);
		int[] heights = distributeHeights(r.height - 4 * spaceSize - parent.getInsets().top - parent.getInsets().bottom);
		
		for (RCPosition position : layoutMap.keySet()) {
			Component c = layoutMap.get(position);
			if(position.equals(new RCPosition(1, 1))) {
				c.setBounds(r.x, r.y, widths[0] + widths[1] + widths[2] + widths[3] + widths[4] + 4 * spaceSize, heights[0]);
			}
			
			else {
				c.setBounds(r.x + sumInRange(widths, 0, position.getColumn() - 2) + (position.getColumn() - 1) * spaceSize, 
					r.y + sumInRange(heights, 0, position.getRow() - 2) + (position.getRow() - 1) * spaceSize,
					widths[position.getColumn() - 1], heights[position.getRow() - 1]);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		RCPosition position;
		
		if (comp == null || constraints == null) {
			throw new NullPointerException("Component and constraint must not be null.");
		}
		
		if (constraints instanceof RCPosition) {
			position = (RCPosition)constraints;
			
		}
		
		else if (constraints instanceof String) {
			position = RCPosition.parse((String)constraints);
		}
		
		else {
			throw new IllegalArgumentException("Constraint must be of type RCPosition or String.");
		}
		
		if(!isValidPosition(position)) {
			throw new CalcLayoutException("Not a valid position: " + position);
		}
		
		if(layoutMap.containsKey(position)) {
			throw new CalcLayoutException("Position already present: " + position);
		}
		
		layoutMap.put(position, comp);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dimension maximumLayoutSize(Container target) {
		return calculateLayoutSize(target, Integer.MAX_VALUE, c -> c.getMaximumSize());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void invalidateLayout(Container target) {
	}
	
	/**
	 * Determines whether given position is valid.
	 * @param position given position
	 * @return <code>true</code> if given position is valid, otherwise <code>false</code>
	 */
	private boolean isValidPosition(RCPosition position) {
		int r = position.getRow();
		int c = position.getColumn();
		if (r < 1 || r > 5 || c < 1 || c > 7) {
			return false;
		}
		
		if (r == 1 && c > 1 && c < 6) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Calculates size of the layout (preferredSize, maximumSize or minimumSize, based on parameter getSize).
	 * @param parent parent container
	 * @param startValue start value
	 * @param getSize either getPreferredSize, or getMaximumSize, or getMinimumSize
	 * @return dimension of the 
	 */
	private Dimension calculateLayoutSize(Container parent, int startValue, Function<Component, Dimension> getSize) {
		int preferredWidth = startValue;
		int preferredHeight = startValue;
		
		for (RCPosition position : layoutMap.keySet()) {
			Dimension dim = getSize.apply(layoutMap.get(position));
			
			if(dim == null) {
				continue;
			}
			
			double width = dim.getWidth();
			double height = dim.getHeight();
			
			if (position.equals(new RCPosition(1, 1))) {
				width = (width - 4 * spaceSize) / 5;
			}
			
			int roundedWidth = (int)Math.round(width);
			int roundedHeight = (int)Math.round(height);
			
			if (startValue < 0) {
				if(roundedWidth > preferredWidth) {
					preferredWidth = roundedWidth;
				}
				
				if(roundedHeight > preferredHeight) {
					preferredHeight = roundedHeight;
				}
			}
			
			else {
				if(roundedWidth < preferredWidth) {
					preferredWidth = roundedWidth;
				}
				
				if(roundedHeight < preferredHeight) {
					preferredHeight = roundedHeight;
				}
			}
			
		}
		
		preferredWidth = preferredWidth < 0 || preferredWidth == Integer.MAX_VALUE ? 50 : preferredWidth;
		preferredHeight = preferredHeight < 0 || preferredHeight == Integer.MAX_VALUE ? 50 : preferredHeight;
		
		Insets insets = parent.getInsets();
		
		int totalWidth = 7 * preferredWidth + 6 * spaceSize + insets.left + insets.right;
		int totalHeight = 5 * preferredHeight + 4 * spaceSize + insets.top + insets.bottom;
		
		return new Dimension(totalWidth, totalHeight);
	}
	
	/**
	 * Helper function for distributing widths of columns as evenly as possible.
	 * @param parentWidth width of the whole layout container
	 * @return distributed widths
	 */
	private int[] distributeWidths(int parentWidth) {
		int width = (int)Math.round((double)parentWidth / 7);
		int diff = parentWidth - 7 * width;
		int count = Math.abs(diff);
				
		int[] widths = new int[7];
		
		for (int i = 0; i < widths.length; i++) {
			widths[i] = width;
		}
		
		if(diff == 0) {
			return widths;
		}
		
		switch(count) {
			case 1 -> {
				recalculateDimension(widths, diff, 3);
			}
			case 2 -> {
				recalculateDimension(widths, diff, 1, 5);
			}
			case 3 -> {
				recalculateDimension(widths, diff, 1, 3, 5);
			}
			case 4 -> {
				recalculateDimension(widths, diff, 0, 2, 4, 6);
			}
			case 5 -> {
				recalculateDimension(widths, diff, 0, 1, 3, 5, 6);
			}
			case 6 -> {
				recalculateDimension(widths, diff, 0, 1, 2, 4, 5, 6);
			}
		}
		
		return widths;
	}
	
	/**
	 * Helper function for distributing heights of rows as evenly as possible.
	 * @param parentHeight height of the whole layout container
	 * @return distributed heights
	 */
	private int[] distributeHeights(int parentHeight) {
		int height = (int)Math.round((double)parentHeight / 5);
		int diff = parentHeight - 7 * height;
		int count = Math.abs(diff);
				
		int[] heights = new int[5];
		
		for (int i = 0; i < heights.length; i++) {
			heights[i] = height;
		}
		
		if(diff == 0) {
			return heights;
		}
		
		switch(count) {
			case 1 -> {
				recalculateDimension(heights, diff, 3);
			}
			case 2 -> {
				recalculateDimension(heights, diff, 1, 3);
			}
			case 3 -> {
				recalculateDimension(heights, diff, 0, 2, 4);
			}
			case 4 -> {
				recalculateDimension(heights, diff, 0, 1, 3, 4);
			}
		}
		
		return heights;
	}
	
	/**
	 * Helper function.
	 */
	private void recalculateDimension(int[] array, int diff, int ...indexes) {
		for (int i = 0; i < indexes.length; i++) {
			array[indexes[i]] = diff < 0 ? array[indexes[i]] - 1 : array[indexes[i]] + 1;
		}
	}
	
	/**
	 * Helper function.
	 */
	private int sumInRange(int[] array, int start, int end) {
		int result = 0;
		for (int i = start; i <= end; i++) {
			result += array[i];
		}
		
		return result;
	}
}

