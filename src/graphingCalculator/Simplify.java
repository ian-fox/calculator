package graphingCalculator;

public class Simplify {
    public static Expression simplify(Expression e) {
        Expression old;
        do {
            old = e;
            e = simplifyInternal(e);
        } while (!e.equals(old));
                
        return e;
    }
    
    private static Expression simplifyInternal(Expression e) {

        // Simplification Cases
        switch(e.op) {
        case "*": 
            if ((e.left.isStatic && e.left.val == 0) || (e.right.isStatic && e.right.val == 0) ) e = new Expression(0);
            else if (e.left.isStatic && e.right.isStatic) e = new Expression(e.left.val * e.right.val);
            else if (e.left.isStatic) {
                if (e.left.val == 1) e = e.right;
                else if (e.right.op.equals("")) break;
                else if (e.right.left.isStatic) e = new Expression(new Expression(e.left.val * e.right.left.val), "*", e.right.right);
                else if (e.right.rightExists) {
                    if (e.right.right.isStatic) e = new Expression(new Expression(e.left.val * e.right.right.val), "*", e.right.left);
                }
            } else if (e.right.isStatic) {
                if (e.right.val == 1) e = e.left;
                else if (e.left.op.equals("")) break;
                else if (e.left.left.isStatic) e = new Expression(new Expression(e.right.val * e.left.left.val), "*", e.left.right);
                else if (e.left.rightExists) {
                    if (e.left.right.isStatic) e = new Expression(new Expression(e.right.val * e.left.right.val), "*", e.left.left);
                }
            }
            break;
        case "+":
            if (e.left.isStatic && e.left.val == 0) e = e.right;
            else if (e.right.isStatic && e.right.val == 0) e = e.left;
            else if (e.left.isStatic && e.right.isStatic) e = new Expression(e.left.val + e.right.val);
            break;
        case "-":
            if (e.right.isStatic && e.right.val == 0) e = e.left;
            else if (e.left.isStatic && e.right.isStatic) e = new Expression(e.right.val - e.left.val);
            break;
        case "/":
            if (e.left.isStatic && e.left.val == 0) e = new Expression(0);
            else if (e.right.isStatic && e.right.val == 1) e = e.left;
            else if (e.right.isStatic && e.left.isStatic) e = new Expression(e.right.val / e.left.val);
            break;
        default:
        } 
        
        
        // Recursion
        if (!e.op.equals("")) {
            e.left = simplifyInternal(e.left);

            if (e.rightExists) {
                e.right = simplifyInternal(e.right);
            }
        }
        
        
        return e;
    }
}