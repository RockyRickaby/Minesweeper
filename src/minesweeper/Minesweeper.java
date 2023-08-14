package minesweeper;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class Minesweeper extends JFrame {
    private Grid grid;
    private Difficulties difficulty;
    private static int clock = 0;
    
    public Minesweeper() {
        super();
		
        Difficulties[] difficulties = {Difficulties.BEGINNER,
                                       Difficulties.INTERMEDIATE,
                                       Difficulties.ADVANCED};
        this.difficulty = (Difficulties) JOptionPane.showInputDialog(this, "Please choose a difficulty", "Difficulties", JOptionPane.INFORMATION_MESSAGE, null, difficulties, difficulties[0]);
        
        if (this.difficulty == null) {
        	System.exit(1);
        }  
        grid = Grid.getInstance(this.difficulty);

        int buttonsSize = Difficulties.BEGINNER.cellSize;
        
        JButton mark = new JButton();
        mark.setPreferredSize(new Dimension(buttonsSize, buttonsSize));
        mark.addActionListener(a -> {
        	grid.toggleMarkMode();
            if (grid.inMarkMode()) {
            	System.out.println("Mark mode toggled on");
            	mark.setIcon(Grid.getImage("marking"));
            } else {
            	System.out.println("Mark mode toggled off");
            	mark.setIcon(Grid.getImage("shovel"));
            }
        });
        mark.setIcon(Grid.getImage("shovel"));
        
        JButton timeElapsed = new JButton("0");
        timeElapsed.setPreferredSize(new Dimension(buttonsSize, buttonsSize));
        
        JButton restart = new JButton();
        restart.setPreferredSize(new Dimension(buttonsSize, buttonsSize));
        restart.addActionListener(a -> {
            grid.reset();
            mark.setIcon(Grid.getImage("shovel"));
            timeElapsed.setText("0");
            clock = 0;
        });
        restart.setIcon(Grid.getImage("smiley"));
        
        JButton mines = new JButton(String.valueOf(difficulty.numOfBombs));
        mines.setPreferredSize(new Dimension(buttonsSize, buttonsSize));

        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.add(mines);
        buttons.add(Box.createHorizontalGlue());
        buttons.add(restart);
        buttons.add(mark);
        buttons.add(Box.createHorizontalGlue());
        buttons.add(timeElapsed);
        buttons.setBackground(new Color(173, 173, 173));
        buttons.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedSoftBevelBorder(), BorderFactory.createLoweredSoftBevelBorder()));
        
        JPanel gridContainer = new JPanel(new GridBagLayout());
        gridContainer.add(grid);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.gridheight = GridBagConstraints.RELATIVE;
        c.anchor = GridBagConstraints.LINE_END;
        panel.add(buttons, c);
        // c.weightx = .3;
        // c.weighty = .3;
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        panel.add(grid, c);

        JScrollPane scrollPan = new JScrollPane(panel);
        //scrollPan.setMaximumSize(new Dimension((difficulty.numOfCellsY * difficulty.cellSize) + 500, (difficulty.numOfCellsX * difficulty.cellSize) + 500));

        // JPanel panel2 = new JPanel();
        // panel2.setLayout(new BoxLayout(panel2, BoxLayout.PAGE_AXIS));
        // panel2.add(scrollPan);

        this.getContentPane().add(scrollPan, BorderLayout.CENTER);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Minesweeper");
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double monitorHeight = screenSize.getHeight();
        double monitorWidth = screenSize.getWidth();
        if (monitorHeight >= 1080 || this.difficulty == Difficulties.BEGINNER) {
            this.pack();
        } else {
            this.setSize(686, 600);
        }
        //this.setResizable(false);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        timer(timeElapsed);
    }
    private void timer(JButton timeElapsed) {
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch(InterruptedException e) {}
                    timeElapsed.setText(String.valueOf(clock++));
                }
            }
        }.start();
    }
}
