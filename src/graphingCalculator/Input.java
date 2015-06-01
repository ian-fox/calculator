package graphingCalculator;
//TODO: parses ((2.0*x)*(0.0+2.0)) as ((2.0*x)*(0.0+20.0)) (bug)
import java.util.Scanner;
import java.util.ArrayList;

class Input {
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
            arr = simplify(arr);
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
        System.out.println("Error"); // TODO: More descriptive error message
        System.out.println(stringArg);
        System.exit(0);
        return arg;
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
