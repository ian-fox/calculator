package graphingCalculator;

import java.awt.Color;
import java.util.ArrayList;

class Relation {
    Color color = Color.black;
    Interval interval = new Interval();
    char axis = 'x';
    Expression exp;
    ArrayList<Relation> pieces;
    
    public Relation(Expression exp) {
        this.exp = exp;
    }
    
    public Relation(Expression exp, Interval i) {
        this.exp = exp;
        interval = i;
    }
    
    public Relation(Expression exp, char axis) {
        this.exp = exp;
        this.axis = axis;
    }
    
    public Relation(Expression exp, Interval i, char axis) {
        this.exp = exp;
        interval = i;
        this.axis = axis;
    }
    
    public Relation(double d, Interval i) {
        exp = new Expression(d);
        interval = i;
    }
    
    public Relation (double d, Interval i, char axis) {
        exp = new Expression(d);
        interval = i;
        this.axis = axis;
    }
    
    public String toString() {
        return exp.toString();
    }
    
    public double eval(double d) {
        return exp.eval(d);
    }
    
    public double integrate() {
        return exp.integrate(interval);
    }
    
    public Relation derivative() {
        return new Relation(exp.derivative(), interval);
    }
}
