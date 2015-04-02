package graphingCalculator;

public class GraphingCalculator {
    public static void main(String[] args) {
        Expression a = new Expression(new Expression(new Expression(2), "*", new Expression()), "-", new Expression(3)); // 2x - 3
        Expression b = new Expression(new Expression(5), "^", a); // 5^(2x-3)
        Expression c = new Expression(new Expression(2), "*", b); // 2*5^(2x-3)
        Expression d = new Expression("ln", new Expression(5)); // ln 5
        Expression e = new Expression(new Expression(6), "*", new Expression());
        Expression f = new Expression(e, "*", d);
        Expression g = new Expression(d, "+", f);
        Expression h = new Expression(g, "-", new Expression(3)); // ln5 + 6xln5 - 3
        Expression i = new Expression(h, "*", c);
        Expression j = new Expression(new Expression(6), "*", new Expression());
        Expression k = new Expression(new Expression(1), "+", j);
        Expression l = new Expression(k, "^", new Expression(2));
        Expression m = new Expression(i, "/", l);
        System.out.println(m.eval(10));
        Expression exp = Input.parse("(2*5^(2x-3)*(ln5 + 6x*ln5 - 3))/(1 + 6x)^2");
        System.out.println(exp.eval(10));
    }
}

//TODO: Implied multiplication