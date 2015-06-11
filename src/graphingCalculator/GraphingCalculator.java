package graphingCalculator;


public class GraphingCalculator {
    public static void main(String[] args) {
        //final Graph graph = new Graph(500, 500, -10, -10, 10, 10, 1, 1);
        //final InteractionPanel interactionPanel = new InteractionPanel(graph);
        System.out.println(new Expression("sin(2x) + 5").derivative());
    }
}