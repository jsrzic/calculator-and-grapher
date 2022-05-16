package hr.fer.zemris.java.gui.layouts;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.junit.jupiter.api.Test;


public class CalcLayoutTest {
	
	@Test
	public void testLayoutPreferredSize1() {
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(10,30));
		JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(20,15));
		p.add(l1, new RCPosition(2,2));
		p.add(l2, new RCPosition(3,3));
		Dimension dim = p.getPreferredSize();
		
		assertEquals(dim.getWidth(), 152);
		assertEquals(dim.getHeight(), 158);
	}
	
	@Test
	public void testLayoutPreferredSize2() {
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(108,15));
		JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(16,30));
		p.add(l1, new RCPosition(1,1));
		p.add(l2, new RCPosition(3,3));
		Dimension dim = p.getPreferredSize();
		
		assertEquals(dim.getWidth(), 152);
		assertEquals(dim.getHeight(), 158);
	}
	
	@Test
	public void testInvalidPositionConstraint1() {
		JPanel p = new JPanel(new CalcLayout(2));
		assertThrows(CalcLayoutException.class, () -> p.add(new JLabel(""), new RCPosition(7, 2)));
		assertThrows(CalcLayoutException.class, () -> p.add(new JLabel(""), new RCPosition(0, 2)));
		assertThrows(CalcLayoutException.class, () -> p.add(new JLabel(""), new RCPosition(4, 8)));
		assertThrows(CalcLayoutException.class, () -> p.add(new JLabel(""), new RCPosition(9, 9)));
	}
	
	@Test
	public void testInvalidPositionConstraint2() {
		JPanel p = new JPanel(new CalcLayout(2));
		assertThrows(CalcLayoutException.class, () -> p.add(new JLabel(""), new RCPosition(1, 2)));
		assertThrows(CalcLayoutException.class, () -> p.add(new JLabel(""), new RCPosition(1, 3)));
		assertThrows(CalcLayoutException.class, () -> p.add(new JLabel(""), new RCPosition(1, 4)));
		assertThrows(CalcLayoutException.class, () -> p.add(new JLabel(""), new RCPosition(1, 5)));
	}
	
	@Test
	public void testMultipleEqualConstraints() {
		JPanel p = new JPanel(new CalcLayout(2));
		p.add(new JLabel(""), new RCPosition(3, 3));
		assertThrows(CalcLayoutException.class, () -> p.add(new JLabel(""), new RCPosition(3, 3)));
	}
}
