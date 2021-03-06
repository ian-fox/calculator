package mathTools;

import java.awt.Color;

class Relation {
    Color color = Color.black;
    Interval interval = new Interval();
    char axis = 'x';
    Expression exp;
    
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
    
    public Relation(Expression exp, Interval i, char axis, Color color) {
        this.exp = exp;
        interval = i;
        this.axis = axis;
        this.color = color;
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
    
    public Relation(double d, char axis, Color color) { // asymptote
        this.color = color;
        exp = new Expression(d);
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
        return new Relation(exp.derivative(), interval, axis, color);
    }
}
