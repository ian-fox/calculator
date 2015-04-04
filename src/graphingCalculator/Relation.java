package graphingCalculator;

import java.awt.Color;
import java.util.ArrayList;

class Relation {
    boolean isVertical, isPieceWise, graphDashed, graphDotted;
    Color c;
    Interval interval = new Interval();
    char axis = 'x';
    Expression exp;
    ArrayList<Relation> pieces;
    
    public Relation (Expression exp) {
        this.exp = exp;
    }
    
    public Relation (Double d) {
        isVertical = true;
        interval = new Interval (d);
    }
    
    public Relation (Expression exp, Interval i) {
        this.exp = exp;
        this.interval = i;
    }
    
    // TODO: y-axis relations, piecewise relations, parametric relations
}
