package ru.trpo.lab_third;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class IntUserType implements UserType {

    private Integer value;

    public IntUserType() {
        this.value = 0;
    }

    public IntUserType(int value) {
        this.value = value;
    }

    @Override
    public String typeName() {
        return "Integer";
    }

    @Override
    public Object create() {
        return new IntUserType();
    }

    @Override
    public Object clone() {
        return new IntUserType(value);
    }

    @Override
    public Object readValue(InputStreamReader in) throws IOException {
        BufferedReader br = new BufferedReader(in);
        this.value = Integer.parseInt(br.readLine().trim());
        return this;
    }

    @Override
    public Object parseValue(String ss) {
        this.value = Integer.parseInt(ss.trim());
        return this;
    }

    @Override
    public Comparator getTypeComparator() {
        return (o1, o2) ->
                String.valueOf(((IntUserType) o1).value)
                        .compareTo(String.valueOf(((IntUserType) o2).value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}