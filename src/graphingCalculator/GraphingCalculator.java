package graphingCalculator;

public class GraphingCalculator {
    public static void main(String[] args) {
        Expression exp = Input.parse("(2*5^(2x-3)(ln5 + 6xln5 - 3))/(1 + 6x)^2");
        System.out.println(exp.eval(10));
    }
}