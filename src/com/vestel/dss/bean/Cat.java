package com.vestel.dss.bean;

/**
 * FileName: Cat
 * Author: Chris
 * Date: 2018/9/3 14:10
 * Description: Cat class
 */
public class Cat {
    private String name;
    private int legs;

    public Cat() {
    }

    public Cat(String name, int legs) {
        this.name = name;
        this.legs = legs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLegs() {
        return legs;
    }


    public void setLegs(int legs) {
        this.legs = legs;
    }

    @Override
    public String toString() {
        return "Cat{" +
                "name='" + name + '\'' +
                ", legs=" + legs +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cat)) return false;

        Cat cat = (Cat) o;

        if (legs != cat.legs) return false;
        return name != null ? name.equals(cat.name) : cat.name == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + legs;
        return result;
    }
}
