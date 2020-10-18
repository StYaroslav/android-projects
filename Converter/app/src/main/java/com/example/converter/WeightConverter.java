package com.example.converter;

import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class WeightConverter {

    private final Map<WConverter, Double> converterMap = new HashMap<WConverter, Double>() {{
        put(new WConverter("Kilograms", "Kilograms"), 1.0);
        put(new WConverter("Tons", "Tons"), 1.0);
        put(new WConverter("Grams", "Grams"), 1.0);
        put(new WConverter("Pounds", "Pounds"), 1.0);

        put(new WConverter("Kilograms", "Tons"), 0.001);
        put(new WConverter("Kilograms", "Grams"), 1000.0);
        put(new WConverter("Kilograms", "Pounds"), 2.20462);

        put(new WConverter("Tons", "Kilograms"), 1000.0);
        put(new WConverter("Tons", "Grams"), 1000000.0);
        put(new WConverter("Tons", "Pounds"), 2204.62);

        put(new WConverter("Grams", "Kilograms"), 0.001);
        put(new WConverter("Grams", "Tons"), 0.000001);
        put(new WConverter("Grams", "Pounds"), 0.00220462);

        put(new WConverter("Pounds", "Kilograms"), 0.453592);
        put(new WConverter("Pounds", "Tons"), 0.000453592);
        put(new WConverter("Pounds", "Grams"), 453592.0);
    }};

    public String Convert(WConverter converter, String amount) {
        Double rate = converterMap.get(converter);
        return String.valueOf(Double.parseDouble(amount) * rate);
    }
}


class WConverter {

    private String from;
    private String to;

    WConverter(String from, String to) {
        this.from = from;
        this.to = to;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WConverter that = (WConverter) o;
        if (!from.equals(that.from)) return false;
        return to.equals(that.to);
    }

    @Override public int hashCode() {
        int result = from.hashCode();
        result = 31 * result + to.hashCode();
        return result;
    }
}