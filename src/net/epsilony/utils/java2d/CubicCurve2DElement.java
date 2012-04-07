/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.epsilony.java2dutil;

import java.awt.Shape;
import java.awt.geom.CubicCurve2D;

/**
 *
 * @author epsilonyuan@gmail.com
 */
public class CubicCurve2DElement extends Element2DAbstract{
    public CubicCurve2DElement(double x1,double y1,double ctrlx1,double ctrly1,double ctrlx2,double ctrly2,double x2,double y2) {
        super("CubicCurve2DElement");
        shape=new CubicCurve2D.Double(x1,y1,ctrlx1,ctrly1,ctrlx2,ctrly2,x2,y2);
    }
    
    public CubicCurve2DElement(double[] args){
        super("CubicCurve2DElement");
        if(args.length<8){
            throw new IllegalArgumentException();
        }
        shape=new CubicCurve2D.Double(args[0],args[1],args[2],args[3],args[4],args[5],args[6],args[7]);
    }

    @Override
    public Shape getShape() {
        return shape;
    }
}
