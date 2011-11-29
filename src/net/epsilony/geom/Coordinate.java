/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.geom;

/**
 *
 * @author epsilonyuan@gmail.com
 */
public class Coordinate {
    public double x,y,z;

    public Coordinate(double x, double y) {
        this.x=x;
        this.y=y;
    }
    
    public Coordinate(double x,double y,double z){
        this.x=x;
        this.y=y;
        this.z=z;
    }
    
    public Coordinate(){
        
    }
    
    public boolean equals2D(Coordinate cd){
        if(cd.x==x&&cd.y==y){
            return true;
        }else
        {
            return false;
        }
    }
    
    public boolean equals3D(Coordinate cd){
        if(cd.x==x&&cd.y==y&&cd.z==z){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public String toString() {
        return String.format("c(%f,%f,%f)", x,y,z);     
    }
    
    
}
