package com.example.converter;

import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;


public class DistanceConverter {

    private final Map<DConverter, Double> converterMap = new HashMap<DConverter, Double>() {{
        put(new DConverter("Kilometers", "Kilometers"), 1.0);
        put(new DConverter("Meters", "Meters"), 1.0);
        put(new DConverter("Miles", "Miles"), 1.0);
        put(new DConverter("Centimeters", "Centimeters"), 1.0);

        put(new DConverter("Kilometers", "Miles"), 0.6214);
        put(new DConverter("Kilometers", "Meters"), 1000.0);
        put(new DConverter("Kilometers", "Centimeters"), 100000.0);

        put(new DConverter("Miles", "Kilometers"), 1.609);
        put(new DConverter("Miles", "Meters"), 1609.34);
        put(new DConverter("Miles", "Centimeters"), 160934.0);

        put(new DConverter("Meters", "Kilometers"), 0.001);
        put(new DConverter("Meters", "Miles"), 0.000621371);
        put(new DConverter("Meters", "Centimeters"), 100.0);

        put(new DConverter("Centimeters", "Kilometers"), 0.00001);
        put(new DConverter("Centimeters", "Miles"), 0.0000062137);
        put(new DConverter("Centimeters", "Meters"), 0.01);
    }};

    public void Convert(DConverter converter, String amount, TextView textField) {
        Double rate = converterMap.get(converter);
        textField.setText(String.valueOf(Double.parseDouble(amount) * rate));
    }
}

class DConverter {

    private String from;
    private String to;

    DConverter(String from, String to) {
        this.from = from;
        this.to = to;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DConverter that = (DConverter) o;
        if (!from.equals(that.from)) return false;
        return to.equals(that.to);
    }

    @Override public int hashCode() {
        int result = from.hashCode();
        result = 31 * result + to.hashCode();
        return result;
    }
}
