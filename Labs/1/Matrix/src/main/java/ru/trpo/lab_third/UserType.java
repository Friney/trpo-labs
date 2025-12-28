package ru.trpo.lab_third;

import java.io.IOException;
import java.io.InputStreamReader;

public interface UserType {
    String typeName();

    Object create();

    Object clone();

    Object readValue(InputStreamReader in) throws IOException;

    Object parseValue(String value);

    Comparator getTypeComparator();
}
