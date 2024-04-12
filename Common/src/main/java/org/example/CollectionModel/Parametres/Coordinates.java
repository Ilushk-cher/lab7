package org.example.CollectionModel.Parametres;

import org.example.CollectionModel.Interfaces.Validator;

import java.io.Serializable;
import java.util.Objects;

/**
 * Класс координат
 */
public class Coordinates implements Validator, Serializable, Comparable<Coordinates> {
    private int x;
    private double y; //Значение поля должно быть больше -157

    public Coordinates(int x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getDistanceToCentre() {
        return Math.pow(getX(), 2) + Math.pow(getY(), 2);
    }

    @Override
    public int compareTo(Coordinates o) {
        if (Objects.isNull(o)) return 1;
        return Double.compare(this.getDistanceToCentre(), o.getDistanceToCentre());
    }

    @Override
    public boolean validate() {
        return y > -157;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Coordinates p = (Coordinates) o;
        return p.x == this.x && p.y == this.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
