package minesweeper;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.JOptionPane;

public class Minesweeper extends JFrame {
	
	public Minesweeper() {
		super();
		
		Difficulties[] itens = {Difficulties.EASY,
				Difficulties.INTERMEDIATE,
				Difficulties.ADVANCED};
		Difficulties dif = (Difficulties) JOptionPane.showInputDialog(null, "Please, choose a difficulty.", "Difficulties", JOptionPane.INFORMATION_MESSAGE, null, itens, itens[0]);
		
		//this.setLayout();
		JPanel restart = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton rest = new JButton("restart");
		//JButton count = new JButton("counter");
		restart.add(rest);
		//restart.add(count);
		JPanel botButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton mark = new JButton("mark");
		botButton.add(mark);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.add(restart);
		panel.add(new Grid(dif));
		panel.add(botButton);
		
		
		
		this.add(panel);
		//this.setSize(500,500);
		this.pack();
		this.setResizable(false);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Minesweeper();
			}
		});
	}

}
