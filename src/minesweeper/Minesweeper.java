package minesweeper;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;

import javax.swing.JOptionPane;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class Minesweeper extends JFrame implements ActionListener {
	private Grid grid;
	private Difficulties dif;
	public Minesweeper() {
		super();
		
		Difficulties[] itens = {Difficulties.EASY,
				Difficulties.INTERMEDIATE,
				Difficulties.ADVANCED};
		dif = (Difficulties) JOptionPane.showInputDialog(null, "Please, choose a difficulty.", "Difficulties", JOptionPane.INFORMATION_MESSAGE, null, itens, itens[0]);
		
		JPanel restart = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton rest = new JButton("restart");
		//rest.addActionListener(this);
		rest.addActionListener(this);
		restart.add(rest);
		//restart.add(count);
	
		JButton mark = new JButton("mark");
		mark.addActionListener(this);
		restart.add(mark);
		
		grid = new Grid(dif);
		JPanel gridContainer = new JPanel(new GridBagLayout());
		gridContainer.add(grid);
		
		gridContainer.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				resize(grid, gridContainer);
			}
		});
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.add(restart);
		panel.add(gridContainer);
		
		this.getContentPane().add(panel);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		//this.add(panel);
		//this.setSize(780, 700);
		this.pack();
		this.setResizable(false);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
	
	private static void resize(JPanel grid, JPanel container) {
		int w = grid.getWidth();
		int h = grid.getHeight();
		int size = Math.min(w, h);
		System.out.println(size);
		grid.setPreferredSize(new Dimension(size, size));
		container.revalidate();
	}
	
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Minesweeper();
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		String buttonText = button.getText();
		if (buttonText.equals("restart")) {
			this.grid.reset();
		}
		if (buttonText.equals("mark")) {
			System.out.println("mark mode toggled");
			this.grid.toggleMarkMode();
		}
		
	}
}
