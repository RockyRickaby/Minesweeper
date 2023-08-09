package minesweeper;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import javax.swing.JOptionPane;

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
        dif = (Difficulties) JOptionPane.showInputDialog(this, "Please, choose a difficulty", "Difficulties", JOptionPane.INFORMATION_MESSAGE, null, itens, itens[0]);
        
        if (dif == null) {
        	System.exit(1);
        }
		
        JPanel restart = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton rest = new JButton("restart");
        rest.addActionListener(this);
        restart.add(rest);
	
        JButton mark = new JButton("mark");
        mark.addActionListener(this);
        restart.add(mark);
		
        grid = Grid.getInstance(dif);
        JPanel gridContainer = new JPanel(new GridBagLayout());
        gridContainer.add(grid);
        JScrollPane scrollGrid = new JScrollPane(gridContainer);
		
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(restart);
        panel.add(scrollGrid);
		this.setTitle("Minesweeper");
        this.getContentPane().add(panel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        //Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        if (Toolkit.getDefaultToolkit().getScreenSize().getHeight() >= 1080 || dif == Difficulties.EASY) {
        	this.pack();
        } else {
        	this.setSize(800, 600);
        }
        this.setResizable(false);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        String buttonText = button.getText();
        if (buttonText.equals("restart")) {
        	System.out.println("Restarting the game...");
            this.grid.reset();
        }
        if (buttonText.equals("mark")) {
            this.grid.toggleMarkMode();
            if (this.grid.inMarkMode()) {
            	System.out.println("Mark mode toggled on");
            } else {
            	System.out.println("Mark mode toggled off");
            }
        }
    }
}
