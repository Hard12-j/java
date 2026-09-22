package org.example.classes;

public class man implements Person{
    private String name;
    private int age;
    private String city;
    private String country;

    public man(String name, int age, String city, String country) {
        this.name = name;
        this.age = age;
        this.city = city;
        this.country = country;
    }

    @Override
    public void introduce(String name){
        System.out.println(name);
    }

    @Override
    public void sayAge(int age){
        System.out.println(age);
    }

    @Override
    public void sayWhereFrom(String city, String country) {
        System.out.println(city + " " + country);
    }

    public String getName() {
        return name;
    }
}
