package mathTools;

import java.util.ArrayList;
import java.util.Scanner;


public class MathTools {    
    public static void main(String[] args) {
        System.out.println(new Expression("x^2").derivative());
    }
    
    public static Expression derivative(Expression e) {
        return MathTools.simplify(MathTools.derivativeInternal(e));
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
            return new Expression(MathTools.derivativeTerm(e), "*", derivativeInternal(e.left));
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

    public static Expression parse(String arg) {
        // Check if expression is valid
        if (!arg.matches("[\\d+-/\\*\\^\\(\\)xe [sin][cos][tan][ln][log][pi]]*")) {
            Expression e = new Expression();
            e.error = true;
            System.out.println("Invalid Input");
            return e;   
        }        
        
        // Convert multi letter operators to single letters for easier parsing
        arg = arg.replaceAll("sin", "s");
        arg = arg.replaceAll("cos", "c");
        arg = arg.replaceAll("tan", "t");
        arg = arg.replaceAll("log", "g");
        arg = arg.replaceAll("ln", "n");
        arg = arg.replaceAll("pi", "p");
    
        
        ArrayList<PartialExp> arr = new ArrayList<PartialExp>();
        
        char current = '\u0000'; // Shouldn't this be the default for char current; ?
        char last = '\u0000';
        String partial = "";
        boolean pointUsed = false;
        boolean lastWasNum = false;;
        
        String ops = "+-/*sctgn^()xep";
        String nums = "1234567890";
        
        Scanner s;
        
        for (int i = 0; i < arg.length(); i++) {
            if (arg.charAt(i) == ' ') { // Ignore whitespace
                continue;
            }
            
            last = current;
            current = arg.charAt(i);
            
            if (nums.indexOf(current) != -1) { // Character is a digit, add to list of current digits
                partial += current;
                lastWasNum = true;                
            } else if (current == '.' && !pointUsed) { // Character is a point, keep parsing as a float
                partial += current;
                pointUsed = true;
                lastWasNum = true;
            } else if (ops.indexOf(current) != -1) { // Character is an operator or special number
                if (current == '-' && !lastWasNum  && last != 'p' && last != 'e' && last != 'x') { // Character is a negative sign
                    lastWasNum = true;
                    partial += current;
                } else {
                    s = new Scanner(partial);
                    if (s.hasNextFloat()) arr.add(new PartialExp(new Expression(s.nextFloat())));
                    s.close();
                    partial = "";
                    pointUsed = false;
                    if (current == 'x') arr.add(new PartialExp(new Expression()));
                    else if (current == 'p') arr.add(new PartialExp(new Expression(Math.PI)));
                    else if (current == 'e') arr.add(new PartialExp(new Expression(Math.E)));
                    else arr.add(new PartialExp(current));
                    lastWasNum = false;
                }
            }
        }
        
        // Handle trailing numbers
        s = new Scanner(partial);
        if (s.hasNextFloat()) arr.add(new PartialExp(new Expression(s.nextFloat())));
        s.close();
                        
        // Step through array to create expression
        while (arr.size() > 1) {
            arr = MathTools.simplify(arr);
        }
        
        return arr.get(0).exp;
    }

    private static ArrayList<PartialExp> simplify (ArrayList<PartialExp> arg) {
        // Convert to string for easier searching for operators
        int size = arg.size();
        String stringArg = "";
        for (int i = 0; i < size; i++) {
            PartialExp item = arg.get(i);
            if (item.type.equals("exp")) {
                stringArg += '#'; // Placeholder for an expression
            } else {
                stringArg += item.op;
            }
        }
        
        // Pi, e
        if (stringArg.indexOf('p') != -1) {
            arg.set(stringArg.indexOf('p'), new PartialExp(new Expression(Math.PI)));
            return arg;
        }
        
        if (stringArg.indexOf('e') != -1) {
            arg.set(stringArg.indexOf('e'), new PartialExp(new Expression(Math.E)));
            return arg;
        }
        
        // Implied Multiplication
        if (stringArg.indexOf("##") != -1) {
            arg.add(stringArg.indexOf("##") + 1, new PartialExp('*'));
            return arg;
        }
                
        // Parentheses
        if (stringArg.indexOf(')') != -1) {
            int end = stringArg.indexOf(')');
            int start = end;
            while (stringArg.charAt(start) != '(') {
                start--;
            }
            ArrayList<PartialExp> subExp = new ArrayList<PartialExp>();
            for (int i = start + 1; i < end; i++) {
                subExp.add(arg.get(i));
            }
            
            // Simplify the part in brackets
            ArrayList<PartialExp> simplifiedSubExp = simplify(subExp);
            while (simplifiedSubExp.size() > 1) {
                simplifiedSubExp = simplify(simplifiedSubExp);
            }
            
            // Remove terms that were simplified from list and add what they compute to
            for (int i = end; i >= start; i--) {
                arg.remove(i);
            }
            
            arg.add(start, simplifiedSubExp.get(0));
            
            return arg;
        }
        
        // Other operators
        
        char[] ops = {'s','c','t','g','n','^','*','/','+','-'};
        String[] operations = {"sin","cos","tan","log","ln","^","*","/","+","-"};
        
        // One argument
        for (int i = 0; i < 5; i++) {
            char op = ops[i];
            if (stringArg.indexOf(op) != -1) {
                int index = stringArg.indexOf(op);
                PartialExp subExp = arg.get(index + 1);
                arg.remove(index);
                arg.set(index, new PartialExp(new Expression(operations[i], subExp.exp)));
                return arg;
            }
        }
        
        // Two arguments
        for (int i = 5; i < 10; i++) {
            char op = ops[i];
            if (stringArg.indexOf(op) != -1) {
                int index = stringArg.indexOf(op);
                PartialExp subExp1 = arg.get(index - 1);
                PartialExp subExp2 = arg.get(index + 1);
                arg.remove(index - 1);
                arg.remove(index - 1);
                arg.set(index - 1, new PartialExp(new Expression(subExp1.exp, operations[i], subExp2.exp)));
                return arg;
            }
        }
        
        // Error
        throw new java.lang.AbstractMethodError("Error parsing arg: " + stringArg);
    }

    public static Expression simplify(Expression e) {
        Expression old;
        do {
            old = e;
            e = MathTools.simplifyInternal(e);
        } while (!e.equals(old));
                
        return e;
    }

    private static Expression simplifyInternal(Expression e) {
        // Recursion
        if (!e.op.equals("")) {
            e.left = simplifyInternal(e.left);
    
            if (e.rightExists) {
                e.right = simplifyInternal(e.right);
            }
        }
        
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
        
        return e;
    }
}

class PartialExp {
    Expression exp;
    char op;
    String type;
    
    public PartialExp(char arg) {
        op = arg;
        type = "op";
    }
    
    public PartialExp(Expression arg) {
        exp = arg;
        type = "exp";
    }
}
