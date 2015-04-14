package graphingCalculator;

import java.util.Scanner;

class Interval {
    double start, end;
    boolean includeStart, includeEnd;
    boolean infStart = false;
    boolean infEnd = false;
    
    public Interval (double d) {
        this.start = d;
        this.end = d;
        this.includeStart = true;
        this.includeEnd = true;
    }
    
    public Interval(char inclStart, double start, double end, char inclEnd) {
        this.start = start;
        this.end = end;
        includeStart = inclStart == '[';
        includeEnd = inclEnd == ']';
    }
    
    public Interval(double end, char inclEnd) {
        this.end = end;
        infStart = true;
        includeStart = false;
        includeEnd = inclEnd == ']';
    }
    
    public Interval(char inclStart, double start) {
        this.start = start;
        includeStart = inclStart == '[';
        infEnd = true;
        includeEnd = false;
    }
    
    public Interval() {
        infStart = true;
        includeStart = false;
        infEnd = true;
        includeEnd = false;
    }
    
    public Interval(String s) {
        // Test validity
        if (!s.matches("(?:\\(|\\[)(?:(?:- *inf)|(?:-? *\\d+\\.?\\d*)) *, *(?:(?:inf)|(?:-? *\\d+\\.?\\d*))(?:\\]|\\))")) {
            infStart = true;
            infEnd = true;
            includeStart = false;
            includeEnd = false;
            System.out.println("Invalid interval: \"" + s + "\"");
            return;
        }
        
        includeStart = s.charAt(0) == '(';
        includeEnd = s.charAt(s.length() - 1) == ')';
        
        Scanner sc = new Scanner(s.substring(1).split(",")[0]);
        if (sc.hasNextDouble()) {
            start = sc.nextDouble();
        } else {
            infStart = true;
        }
        sc.close();
        
        
        sc = new Scanner(s.substring(1, s.length() - 1).split(",")[1]);
        if (sc.hasNextDouble()) {
            end = sc.nextDouble();
        } else {
            infEnd = true;
        }
        sc.close();
    }
    
    public boolean isInInterval(double d) {
        if (infStart && infEnd) return true;
        if (infStart && includeEnd) return d <= end;
        if (infStart) return d < end;
        if (includeStart && infEnd) return d >= start;
        if (infEnd) return d > start;
        if (includeStart && includeEnd) return d >= start && d <= end;
        if (includeStart) return d >= start && d < end;
        if (includeEnd) return d > start && d <= end;
        return d > start && d < end;
    }
    
    public String toString() {
        String s = "";
        s += includeStart ? "[" : "(";
        s += infStart ? "-inf" : start;
        s += ", ";
        s += infEnd ? "inf" : end;
        s += includeEnd ? "]" : ")";
        return s;
    }
}
