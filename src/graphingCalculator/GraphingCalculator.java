package graphingCalculator;


public class GraphingCalculator {
    public static void main(String[] args) {
        new Graph(new Expression("sin(2x) + 5").derivative());
    }
}