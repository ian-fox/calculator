package graphingCalculator;
class Derivative {
    public static Expression derivative(Expression e) {
        return Simplify.simplify(derivativeInternal(e));
    }
    
    private static Expression derivativeInternal(Expression e) {
        if (e.op.equals("")) return e.isStatic ? new Expression(0) : new Expression(1); // term is either a constant or x
        
        if (e.rightExists) {
            switch(e.op) {
            case "+": return new Expression(derivativeInternal(e.left), "+", derivativeInternal(e.right));
            case "-": return new Expression(derivativeInternal(e.left), "-", derivativeInternal(e.right));
            case "*": return new Expression(new Expression(derivativeInternal(e.left), "*", e.right), "+", new Expression(e.left, "*", derivativeInternal(e.right)));
            case "/": return new Expression(new Expression(new Expression(derivativeInternal(e.left), "*", e.right), "-", new Expression(e.left, "*", derivativeInternal(e.right))), "/", new Expression(e.right, "^", new Expression(2)));
            case "^": return new Expression(e.right, "*", new Expression(e.left, "^", new Expression(e.right, "-", new Expression(1))));
            }
            e.error = true;
            return e;
        } else {
            return new Expression(derivativeTerm(e), "*", derivativeInternal(e.left));
        }
    }
    
    private static Expression derivativeTerm(Expression e) {
        switch (e.op) {
        case "sin": return new Expression("cos", e.left);
        case "cos": return new Expression(new Expression(0), "-", new Expression("sin", e.left));
        case "tan": return new Expression(new Expression(new Expression(1), "/", new Expression("cos", e.left)), "^", new Expression(2));
        case "ln": return new Expression(new Expression(1), "/", e.left);
        case "log": return new Expression(new Expression(1), "/", new Expression(e.left, "*", new Expression("ln", new Expression(10))));
        }
        e.error = true;
        return e;
    }   
}
