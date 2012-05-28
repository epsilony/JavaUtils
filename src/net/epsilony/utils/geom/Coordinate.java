/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.utils.geom;

/**
 *
 * @author epsilonyuan@gmail.com
 */
public class Coordinate {

    public double x, y, z;

    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Coordinate() {
    }

    public Coordinate(double[] coord) {
        x = coord[0];
        y = coord[1];
        if (coord.length >= 3) {
            z = coord[2];
        }
    }

    public Coordinate(Coordinate coord) {
        x = coord.x;
        y = coord.y;
        z = coord.z;
    }

    public boolean equals2D(Coordinate cd) {
        if (cd.x == x && cd.y == y) {
            return true;
        } else {
            return false;
        }
    }

    public boolean equals3D(Coordinate cd) {
        if (cd.x == x && cd.y == y && cd.z == z) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.format("c(%f,%f,%f)", x, y, z);
    }

    public void scale(double s) {
        x *= s;
        y *= s;
        z *= s;
    }

    public double getDim(int dim) {
        switch (dim) {
            case 0:
                return x;
            case 1:
                return y;
            case 2:
                return z;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    public void setDim(int dim, double value) {
        switch (dim) {
            case 0:
                x = value;
                break;
            case 1:
                y = value;
                break;
            case 2:
                z = value;
                break;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    public void set(Coordinate input) {
        x = input.x;
        y = input.y;
        z = input.z;
    }
    
    public double[] toArray(double[] output){
        if(null==output){
            return new double[]{x,y,z};
        }
        output[0]=x;
        output[1]=y;
        output[2]=z;
        return output;
    }
}
