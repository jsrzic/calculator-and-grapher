package hr.fer.zemris.java.gui.charts;

import java.awt.Container;
import java.awt.BorderLayout;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Application which plots a Bar Chart.
 * The data is read from a file with path given by command line argument.
 * @author Josip
 *
 */
public class BarChartDemo extends JFrame {
	private BarChart model;
	
	private static final long serialVersionUID = 1L;

	public BarChartDemo(String label, BarChart model) {
		this.model = model;
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Bar Chart Demo");
		setSize(800, 400);
		initGUI(label);
	}
	
	
	private void initGUI(String label) {
		Container cp = getContentPane();
		cp.add(new JLabel(label, SwingConstants.CENTER), BorderLayout.PAGE_START);
		cp.add(new BarChartComponent(model), BorderLayout.CENTER);
	}
	
	
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Invalid number of arguments.");
			return;
		}
		
		Path p = Path.of(args[0]);
		String xDescription;
		String yDescription;
		List<XYValue> values = new ArrayList<>();
		int yMin;
		int yMax;
		int space;
		Pattern pattern = Pattern.compile("(\\d+),(\\d+)");
		Matcher matcher;
		try (Scanner sc = new Scanner(p);){
			xDescription = sc.nextLine();
			yDescription = sc.nextLine();
			String[] valuesChunks = sc.nextLine().split("\\s+");
			for (int i = 0; i < valuesChunks.length; i++) {
				matcher = pattern.matcher(valuesChunks[i]);
				if (!matcher.matches()) {
					System.out.println("Invalid xy-value: " + valuesChunks[i]);
					return;
				}
				
				values.add(new XYValue(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))));
			}
			yMin = Integer.parseInt(sc.nextLine());
			yMax = Integer.parseInt(sc.nextLine());
			space = Integer.parseInt(sc.nextLine());
		} catch (IOException e) {
			System.out.println("Can not read file: " + args[0]);
			return;
		} catch (NumberFormatException e) {
			System.out.println("Values must be integers.");
			return;
		} 
		
		BarChart model = new BarChart(values, xDescription, yDescription, yMin, yMax, space);
				
		SwingUtilities.invokeLater(()->{
			new BarChartDemo(args[0], model).setVisible(true);
		});
	}
}
