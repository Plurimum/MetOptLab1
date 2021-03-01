package mygdx.methods;

import java.util.*;
import java.util.function.Function;

public class Algorithms {
    public static List<Method> methodList(Function<Double, Double> func) {

        return Arrays.asList(new Method[]{
                new DichotomyMethod(func, 1e-3),
                new GoldenSectionMethod(func),
                new FibonacciMethod(func),
                new ParabolaMethod(func)});
    }
    public static void main(String[] args) {

//        methods.forEach((k, v) -> System.out.println(k + ": " + v.findMin(0.5, 4, 1e-5)));
    }
}
