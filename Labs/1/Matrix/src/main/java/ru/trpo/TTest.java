package ru.trpo;


public class TTest<T> {
    private T type;

    public TTest(T value) {
        this.type = value;
    }

    public void test() {
        if (type instanceof Integer) {
            System.out.println("type is Integer");
        } else {
            System.out.println("type is not Integer");
        }
    }
}
