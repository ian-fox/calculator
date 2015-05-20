package graphingCalculator;

class Expression {
    Expression left;
    Expression right;
    Expression derivative;
    Boolean foundDerivative = false;
    boolean isStatic = false;
    double val;
    boolean error = false;
    String op = "";
    boolean rightExists;
    
    // Constructors
    public Expression() {} // Default to x
    
    public Expression(double i) {
        isStatic = true;
        val = i;
    }
    
    public Expression(Expression leftArg, String opArg, Expression rightArg) {
        left = leftArg;
        right = rightArg;
        op = opArg;
        rightExists = true;
    }
    
    public Expression(String opArg, Expression arg) {
        left = arg;
        op = opArg;
        rightExists = false;
    }
    
    public Expression(String stringArg) {
        Expression newExp = Input.parse(stringArg);
        left = newExp.left;
        right = newExp.right;
        op = newExp.op;
        rightExists = newExp.rightExists;
        isStatic = newExp.isStatic;
        val = newExp.val;
    }
        
    public double eval(double x) {
        switch(op) {
        case "": return isStatic ? val : x;
        case "+": return left.eval(x) + right.eval(x);
        case "-": return left.eval(x) - right.eval(x);
        case "/": return left.eval(x) / right.eval(x);
        case "*": return left.eval(x) * right.eval(x);
        case "^": return Math.pow(left.eval(x), right.eval(x));
        case "sin": return Math.sin(left.eval(x));
        case "cos": return Math.cos(left.eval(x));
        case "tan": return Math.tan(left.eval(x));
        case "ln": return Math.log(left.eval(x));
        case "log": return Math.log10(left.eval(x));
        }
        return Double.NaN;
    }
    
    public double eval() {
        return eval(0);
    }
    
    public double integrate(Interval i) {
        double total = 0;
        if (i.infEnd || i.infStart) {
            System.out.println("Integral error: indefinite integral");
            return Double.NaN;
        }
        double step = (i.end - i.start) / 1000; // How high can I reasonably push this?
        
        try {
            for (double d = i.start + step; d < i.end - step; d += step) {
                    total += this.eval(d);   
            }
            total += this.eval(i.start + 0.0001) / 2;
            total += this.eval(i.end - 0.0001) / 2;
            
            total = total * step;
            
        } catch (java.lang.ArithmeticException e) {
            System.out.println("Integral error: Expression is undefined on interval");
            return Double.NaN;
        }
        
        return total;
    }
    
    public double integrate(String s) {
        Interval i = new Interval(s);
        return this.integrate(i);
    }
    
    public Expression derivative() {
        if (foundDerivative) return derivative;
        else {
            derivative = Derivative.derivative(this);
            foundDerivative = true;
            return derivative;
        }
    }
    
    @Override
    public String toString() {
        if (isStatic) {
            return Double.toString(val);
        } else if (op == "") {
            return "x";
        } else if (rightExists) {
            return "(" + left.toString() + op + right.toString() + ")";
        } else {
            return op + left.toString();
        }
    }
}
