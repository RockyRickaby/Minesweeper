package minesweeper;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class Minesweeper extends JFrame implements ActionListener {
    private Grid grid;
    private Difficulties difficulty;
    public Minesweeper() {
        super();
		
        Difficulties[] difficulties = {Difficulties.EASY,
                                Difficulties.INTERMEDIATE,
                                Difficulties.ADVANCED};
        this.difficulty = (Difficulties) JOptionPane.showInputDialog(this, "Please, choose a difficulty", "Difficulties", JOptionPane.INFORMATION_MESSAGE, null, difficulties, difficulties[0]);
        
        if (this.difficulty == null) {
        	System.exit(1);
        }
		
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        JButton restart = new JButton("restart");
        restart.addActionListener(this);
        buttons.add(restart);
	
        JButton mark = new JButton("mark");
        mark.addActionListener(this);
        buttons.add(mark);
		
        grid = Grid.getInstance(this.difficulty);
        JPanel gridContainer = new JPanel(new GridBagLayout());
        gridContainer.add(grid);
        JScrollPane scrollGrid = new JScrollPane(gridContainer);
		
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(buttons, BorderLayout.PAGE_START);
        panel.add(scrollGrid);
        
        this.getContentPane().add(panel, BorderLayout.CENTER);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Minesweeper");
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double monitorHeight = screenSize.getHeight();
        double monitorWidth = screenSize.getWidth();
        if (monitorHeight >= 1080 || this.difficulty == Difficulties.EASY || (this.difficulty == Difficulties.ADVANCED && monitorWidth > 1024)) {
            this.pack();
        } else {
            this.setSize(676, 600);
        }
        //this.setResizable(false);
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
        if (buttonText.equals("mark") || buttonText.equals("marking")) {
            this.grid.toggleMarkMode();
            if (this.grid.inMarkMode()) {
            	System.out.println("Mark mode toggled on");
            	button.setText("marking");
            } else {
            	System.out.println("Mark mode toggled off");
            	button.setText("mark");
            }
        }
    }
}
