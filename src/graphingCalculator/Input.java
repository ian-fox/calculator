package graphingCalculator;

import java.util.Scanner;
import java.util.ArrayList;

class Input {
    public static Expression parse(String arg) {
        Expression e = new Expression();
        Scanner s = new Scanner(arg);
        // Check if expression is valid
        if (s.findInLine("[^\\d+-/\\*\\^\\(\\)x [sin][cos][tan][ln][log]]*") != null) {
            e.error = true;
            System.out.println("Invalid Input");
            s.close();
            return e;   
        }        
        s.close();
        
        // Convert multi letter operators to single letters for easier parsing
        arg = arg.replaceAll("sin", "s");
        arg = arg.replaceAll("cos", "c");
        arg = arg.replaceAll("tan", "t");
        arg = arg.replaceAll("log", "g");
        arg = arg.replaceAll("ln", "n");

        
        ArrayList<PartialExp> arr = new ArrayList<PartialExp>();
        
        char current;
        String partial = "";
        boolean pointUsed = false;
        boolean lastWasNum = false;;
        
        String ops = "+-/*sctgn^()x";
        String nums = "1234567890";
        
        for (int i = 0; i < arg.length(); i++) {
            current = arg.charAt(i);
            
            if (nums.indexOf(current) != -1) {
                partial += current;
                lastWasNum = true;
            } else if (current == '.' && !pointUsed) {
                partial += current;
                pointUsed = true;
                lastWasNum = true;
            } else if (ops.indexOf(current) != -1) {
                if (current == '-' && !lastWasNum) { // Check if - could be a negative sign
                    lastWasNum = true;
                    partial += current;
                } else {
                    s = new Scanner(partial);
                    arr.add(new PartialExp(new Expression(s.nextFloat())));
                    s.close();
                    partial = "";
                    if (current == 'x' && lastWasNum) arr.add(new PartialExp('*'));
                    arr.add(new PartialExp(current));
                    lastWasNum = false;
                }
            }
        }
        
        // Step through array to create expression
        while (arr.size() > 1) {
            arr = simplify(arr);
        }
        
        
        return e;
    }
    
    private static ArrayList<PartialExp> simplify (ArrayList<PartialExp> arg) {
        // Parentheses
        while (arg.contains(')')) {
            int end = arg.indexOf(')');
            int start = end;
            while (arg.get(start).op != '(') {
                start --;
            }
        }
        
        // Trig, logs
        
        // Exponents
        
        // Multipliplation and Divison
        
        // Addition and Subtraction
        
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
