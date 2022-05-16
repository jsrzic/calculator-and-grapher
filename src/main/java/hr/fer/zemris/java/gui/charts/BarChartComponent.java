package hr.fer.zemris.java.gui.charts;


import java.awt.geom.AffineTransform;
import java.awt.*;

import javax.swing.JComponent;

/**
 * Component in charge of drawing the bar chart.
 * @author Josip
 *
 */
public class BarChartComponent extends JComponent {

	private static final long serialVersionUID = 1L;
	
	private static final int BORDER_MARGIN = 50;
	private static final int DESCRIPTION_TO_LABELS = 20;
	private static final int LABELS_TO_AXIS = 20;
	private static final Color GRID_COLOR = new Color(245, 206, 66, 120);
	private static final Color MAIN_COLOR = Color.BLACK;
	private static final Color DATA_COLOR = new Color(217, 100, 46);
	private static final Color SHADOW_COLOR = new Color(97, 96, 96, 70);
	private static final Font FONT_BOLD = new Font("Arial", Font.BOLD, 15);
	private static final Font FONT_PLAIN = new Font("Arial", Font.PLAIN, 20);
	
	private BarChart model;
	
	public BarChartComponent(BarChart model) {
		this.model = model;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		FontMetrics fm = g2.getFontMetrics();
		Dimension parentSize = getParent().getSize();
		Point origin = new Point(BORDER_MARGIN + fm.getHeight() + DESCRIPTION_TO_LABELS + fm.stringWidth(String.valueOf(model.getyMax())) + LABELS_TO_AXIS
				, parentSize.height - BORDER_MARGIN - 2 * fm.getHeight() - DESCRIPTION_TO_LABELS - LABELS_TO_AXIS);
		
		int barWidth = (int)Math.round((double)(
				parentSize.width - BORDER_MARGIN - origin.x) 
				/ model.getValues().size());
		
		int yCount = (model.getyMax() - model.getyMin())/model.getSpace() + 1;
		int verticalStep = (int)Math.round((double)(origin.y - BORDER_MARGIN) / yCount);
		Stroke defaultStroke = g2.getStroke();
		
		g2.setFont(FONT_PLAIN);
		AffineTransform defaultAt = g2.getTransform();
		AffineTransform at = new AffineTransform();
		at.rotate(-Math.PI / 2);

		g2.setTransform(at);
		g2.drawString(model.getyDescription(), -Math.round(parentSize.height - origin.y + verticalStep*yCount/2.0 + fm.stringWidth(model.getyDescription())/2.0), BORDER_MARGIN);
		g2.setTransform(defaultAt);
		g2.drawString(model.getxDescription(), Math.round(origin.x + model.getValues().size() * barWidth / 2.0 - fm.stringWidth(model.getxDescription())/2.0), parentSize.height - BORDER_MARGIN);
		
		g2.setStroke(new BasicStroke(2));
		g2.drawLine(origin.x, origin.y, origin.x + model.getValues().size() * barWidth + 20, origin.y);
		drawArrowHeadX(new Point(origin.x + model.getValues().size() * barWidth + 20, origin.y), g2);
		
		g2.drawLine(origin.x, origin.y, origin.x, BORDER_MARGIN);
		drawArrowHeadY(new Point(origin.x, BORDER_MARGIN), g2);
		
		g2.setFont(FONT_BOLD);
		int spaceCounter = 0;
		for(int label = model.getyMin(); label <= model.getyMax(); label += model.getSpace()) {
			g2.setColor(MAIN_COLOR);
			g2.setStroke(new BasicStroke(2));
			//hatch
			g2.drawLine(origin.x, origin.y - spaceCounter, origin.x - 5, origin.y - spaceCounter);
			g2.setStroke(defaultStroke);
			g2.drawString(String.valueOf(label), origin.x - LABELS_TO_AXIS - fm.stringWidth(String.valueOf(label)), origin.y + fm.getHeight()/2 - spaceCounter - 3);
			g2.setColor(GRID_COLOR);
			if (label != model.getyMin()) {				
				g2.drawLine(origin.x, origin.y - spaceCounter, origin.x + barWidth * model.getValues().size(), origin.y - spaceCounter);
			}
			spaceCounter += verticalStep;
		}
		
		int lineOffset = barWidth;
		int labelOffset = barWidth / 2;
		int barOffset = 0;
		for(int i = 0; i < model.getValues().size(); i++) {
			XYValue value = model.getValues().get(i);
			g2.setColor(GRID_COLOR);
			g2.drawLine(origin.x + lineOffset, origin.y, origin.x + lineOffset, origin.y - verticalStep * (yCount - 1));
			g2.setColor(MAIN_COLOR);
			g2.setStroke(new BasicStroke(2));
			//hatch
			g2.drawLine(origin.x + lineOffset, origin.y, origin.x + lineOffset, origin.y + 10);
			g2.setStroke(defaultStroke);
			g2.drawString(String.valueOf(value.getX()), origin.x + labelOffset - fm.stringWidth(String.valueOf(value.getX()))/2, origin.y + LABELS_TO_AXIS + fm.getHeight());
			g2.setColor(DATA_COLOR);
			g2.fillRect(origin.x + barOffset, (int)Math.round(origin.y - (double)(value.getY() - model.getyMin()) / model.getSpace() * verticalStep), barWidth - 1, (int)Math.round((double)(value.getY() - model.getyMin()) / model.getSpace() * verticalStep));
			if(i == model.getValues().size() - 1 || model.getValues().get(i).getY() > model.getValues().get(i + 1).getY()) {
				g2.setColor(SHADOW_COLOR);
				g2.fillRect(origin.x + barOffset + barWidth - 1, (int)Math.round(origin.y - (double)(value.getY() - model.getyMin()) / model.getSpace() * verticalStep) + 2, 5,
						i == model.getValues().size() - 1 ? (int)Math.round((double)(value.getY() - model.getyMin()) / model.getSpace() * verticalStep) - 2 : (int)Math.round((double)(value.getY() - model.getValues().get(i + 1).getY()) / model.getSpace() * verticalStep) - 2);
			}
			
			
			lineOffset += barWidth;
			labelOffset += barWidth;
			barOffset += barWidth;
		}
	}
	
	
	private void drawArrowHeadX(Point point, Graphics2D g2) {
		g2.fillPolygon(new int[] {point.x, point.x, point.x + 8}, new int[] {point.y + 4, point.y - 4, point.y}, 3);
	}
	
	private void drawArrowHeadY(Point point, Graphics2D g2) {
		g2.fillPolygon(new int[] {point.x + 4, point.x - 4, point.x}, new int[] {point.y, point.y, point.y - 8}, 3);
	}
	
}
