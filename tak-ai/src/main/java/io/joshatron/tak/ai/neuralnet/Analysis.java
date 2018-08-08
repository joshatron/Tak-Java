package io.joshatron.tak.ai.neuralnet;

import java.util.ArrayList;

public class Analysis {

    private ArrayList<Double> values;
    private String name;
    private double total;
    private int num;

    public Analysis(String name) {
        values = new ArrayList<>();
        this.name = name;
        total = 0;
        num = 0;
    }

    public void addValue(double value) {
        values.add(value);
        total += value;
        num++;
    }

    public double getAverage() {
        if(num == 0) {
            return 0;
        }

        return total / num;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Analysis)) {
            return false;
        }

        Analysis other = (Analysis)o;

        return name.equals(other.getName());
    }
}
