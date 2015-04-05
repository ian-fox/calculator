package graphingCalculator;

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
