package graphingCalculator;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.*;

import java.util.ArrayList;

class InteractionPanel extends JPanel{
    int padding, width, height;
    JTextField expression;
    JLabel input;
    JButton submit;
    JComboBox relation;
    ArrayList<Relation> relations;
    Graph graph;
    JFrame frame;
    
    public InteractionPanel(Graph graph) { 
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(509, 538);
        frame.setBackground(Color.white);
        frame.setTitle("Graphing Calculator v0.1");
        frame.add(this);
        
        frame.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                resize(e);
            }

            @Override
            public void componentHidden(ComponentEvent e) {}

            @Override
            public void componentMoved(ComponentEvent e) {}

            @Override
            public void componentShown(ComponentEvent e) {}
        });
        
        frame.setVisible(true);
        this.graph = graph;
        setLayout(null);
         
        relation = new JComboBox();
        
        arrangeComponents();
    }
    
    private void arrangeComponents() {
        System.out.println(relation.getBounds());
        relation.setBounds(padding, padding, width - 2 * padding, 20); 
    }
    
    private void resize(ComponentEvent e) {
        width = e.getComponent().getWidth() - 9;
        height = e.getComponent().getHeight() - 38;
        
        frame.repaint();
    }
}
