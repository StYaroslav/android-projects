package com.example.converter;

import java.util.HashMap;
import java.util.Map;

public class DataConverter {

    private final Map<Converter, Double> converterMap = new HashMap<Converter, Double>() {{
        put(new Converter("Bit", "Bit"), 1.0);
        put(new Converter("KBit", "KBit"), 1.0);
        put(new Converter("MGBit", "MGBit"), 1.0);
        put(new Converter("GBit", "GBit"), 1.0);
        put(new Converter("Bite", "Bite"), 1.0);
        put(new Converter("KBite", "KBite"), 1.0);
        put(new Converter("MGBite", "MGBite"), 1.0);
        put(new Converter("GBite", "GBite"), 1.0);

        put(new Converter("Bit", "KBit"), 0.001);
        put(new Converter("Bit", "MGBit"), 0.000001);
        put(new Converter("Bit", "GBit"), 0.000000001);
        put(new Converter("Bit", "Bite"), 0.125);
        put(new Converter("Bit", "KBite"), 0.000125);
        put(new Converter("Bit", "MGBite"), 0.0000125);
        put(new Converter("Bit", "GBite"), 0.000000125);

        put(new Converter("KBit", "Bit"), 1000.0);
        put(new Converter("KBit", "MGBit"), 0.001);
        put(new Converter("KBit", "GBit"), 0.000001);
        put(new Converter("KBit", "Bite"), 125.0);
        put(new Converter("KBit", "KBite"), 0.125);
        put(new Converter("KBit", "MGBite"), 0.000125);
        put(new Converter("KBit", "GBite"), 0.000000125);

        put(new Converter("MGBit", "Bit"), 1000000.0);
        put(new Converter("MGBit", "KBit"), 1000.0);
        put(new Converter("MGBit", "GBit"), 0.001);
        put(new Converter("MGBit", "Bite"), 125000.0);
        put(new Converter("MGBit", "KBite"), 125.0);
        put(new Converter("MGBit", "MGBite"), 0.125);
        put(new Converter("MGBit", "GBite"), 0.000125);

        put(new Converter("GBit", "Bit"), 1000000000.0);
        put(new Converter("GBit", "KBit"), 1000000.0);
        put(new Converter("GBit", "MBit"), 1000.0);
        put(new Converter("GBit", "Bite"), 125000000.0);
        put(new Converter("GBit", "KBite"), 125000.0);
        put(new Converter("GBit", "MGBite"), 125.0);
        put(new Converter("GBit", "GBite"), 0.125);

        put(new Converter("Meters", "Kilometers"), 0.001);
        put(new Converter("Meters", "Miles"), 0.000621371);
        put(new Converter("Meters", "Centimeters"), 100.0);

        put(new Converter("Centimeters", "Kilometers"), 0.00001);
        put(new Converter("Centimeters", "Miles"), 0.0000062137);
        put(new Converter("Centimeters", "Meters"), 0.01);
    }};

    public String Convert(Converter converter, String amount) {
        Double rate = converterMap.get(converter);
        return String.valueOf(Double.parseDouble(amount) * rate);
    }
}

class Converter {

    private String from;
    private String to;

    Converter(String from, String to) {
        this.from = from;
        this.to = to;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Converter that = (Converter) o;
        if (!from.equals(that.from)) return false;
        return to.equals(that.to);
    }

    @Override public int hashCode() {
        int result = from.hashCode();
        result = 31 * result + to.hashCode();
        return result;
    }
}
