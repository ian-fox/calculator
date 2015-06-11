package graphingCalculator;

import java.awt.*;

import javax.swing.*;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

class Graph extends JPanel {
    double xStart, yStart, xEnd, yEnd, xTickInterval, yTickInterval, xUnitsPerPixel, yUnitsPerPixel, xOffset, yOffset;
    boolean useTicks, tickLines;
    ArrayList<Relation> relations = new ArrayList<Relation>();
    int width, height;
    JFrame frame;
    
    protected void paintComponent(Graphics g) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        
        // Calculate ratios
        xUnitsPerPixel = (xEnd - xStart) / width;
        yUnitsPerPixel = (yEnd - yStart) / height;
        
        // Draw Axes
        drawRelation(new Relation(new Expression(0)), g2d);
        drawRelation(new Relation(new Expression(0), 'y'), g2d);
        
        // Draw Ticks
        if (useTicks) {
            Interval interval = tickLines ? new Interval() : new Interval('[', -5 * yUnitsPerPixel, 5 * yUnitsPerPixel, ']');
            for (double d = xTickInterval * Math.floor(xStart / xTickInterval); d <= xEnd; d += xTickInterval) {
                drawRelation(new Relation(d, interval), g2d);
            }
            for (double d = yTickInterval * Math.floor(yStart / yTickInterval); d < yEnd; d += yTickInterval) {
                drawRelation(new Relation(d, interval, 'y'), g2d);
            }
        }
        
        // Draw Expressions
        
        for (int i = 0; i < relations.size(); i++) {
            drawRelation(relations.get(i), g2d);
        }
        
        Graphics2D g2dComponent = (Graphics2D) g;
        g2dComponent.drawImage(bufferedImage, null, 0, 0);
    }
    
    public Graph(int width, int height, double xStart, double yStart, double xEnd, double yEnd, double xTickInterval, double yTickInterval) {
        useTicks = true;
        this.width = width;
        this.height = height;
        this.xStart = xStart;
        this.xEnd = xEnd;
        this.yStart = yStart;
        this.yEnd = yEnd;
        this.xTickInterval = xTickInterval;
        this.yTickInterval = yTickInterval;
        
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width + 9, height + 38);
        frame.setBackground(Color.white);
        frame.setTitle("Graph");
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
    }
    
    public Graph(int width, int height, double xStart, double yStart, double xEnd, double yEnd, double xTickInterval, double yTickInterval, Relation r) {
        this(width, height, xStart, yStart, xEnd, yEnd, xTickInterval, yTickInterval);
        this.relations.add(r);
        this.frame.setTitle(r.toString());
        this.frame.repaint();
    }
    
    public Graph(int width, int height, double xStart, double yStart, double xEnd, double yEnd, double xTickInterval, double yTickInterval, Expression e) {
        this(width, height, xStart, yStart, xEnd, yEnd, xTickInterval, yTickInterval);
        Relation r = new Relation(e);
        this.relations.add(r);
        this.frame.setTitle(r.toString());
        this.frame.repaint();
    }
    
    public Graph(int width, int height, double xStart, double yStart, double xEnd, double yEnd, double xTickInterval, double yTickInterval, String s) {
        this(width, height, xStart, yStart, xEnd, yEnd, xTickInterval, yTickInterval);
        Relation r = new Relation(Input.parse(s));
        this.relations.add(r);
        this.frame.setTitle(r.toString());
        this.frame.repaint();
    }
    
    private void drawRelation(Relation r, Graphics2D g2d) {
        g2d.setColor(r.color);
        boolean useLast = false;
        int xOld = 0; // in theory I think I should be able to say int xOld, but eclipse complained about things not being initialized.
        int yOld = 0;
        int end = r.axis == 'x' ? width : height;
        for (int i = 0; i < end; i++) {
            double independent = r.axis == 'x' ? xStart + i * xUnitsPerPixel : yStart + i * yUnitsPerPixel;
            try {
                if (r.interval.isInInterval(independent)) {
                    int x = r.axis == 'x' ? i : (int) Math.round(-(yStart + r.eval(independent)) / xUnitsPerPixel);
                    int y = r.axis == 'y' ? i : (int) Math.round(-(xStart + r.eval(independent)) / yUnitsPerPixel);
                    if (useLast) {
                        g2d.drawLine(xOld, yOld, x, y);
                    } else {
                        g2d.fillRect(x, y, 1, 1);
                    }
                    xOld = x;
                    yOld = y;
                    useLast = true;
                } else {
                    useLast = false;
                }
            } catch (java.lang.ArithmeticException e) { // I think this should catch divideby0
                useLast = false;
                System.out.println("Error");
            } catch (Error e) {
                System.out.println(e);
            }
        }
    }
    
    private void resize(ComponentEvent e) {
        width = e.getComponent().getWidth() - 9;
        height = e.getComponent().getHeight() - 38;
        frame.repaint();
    }
}
