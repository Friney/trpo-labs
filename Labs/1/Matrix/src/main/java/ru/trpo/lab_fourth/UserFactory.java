package ru.trpo.lab_fourth;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import ru.trpo.lab_third.IntUserType;
import ru.trpo.lab_third.MatrixUserType;
import ru.trpo.lab_third.UserType;

public class UserFactory {

    private final Map<String, UserType> registry = new LinkedHashMap<>();

    public UserFactory() {
        registry.put("Integer", new IntUserType());
        registry.put("Matrix", new MatrixUserType());
    }

    public ArrayList<String> getTypeNameList() {
        return new ArrayList<>(registry.keySet());
    }

    public UserType getBuilderByName(String name) {
        UserType proto = registry.get(name);
        if (proto == null) {
            throw new IllegalArgumentException("Unknown type: " + name);
        }
        return (UserType) proto.clone();
    }
}
