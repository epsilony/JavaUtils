/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math.util;

import java.awt.geom.Point2D;
import java.util.Iterator;

/**
 *
 * @author epsilon
 */
public class Geometry {
    public static boolean isCounterClockwise(Iterator<? extends Point2D.Double> pointsIt){
        Point2D.Double first=pointsIt.next();
        Point2D.Double latest=pointsIt.next();
        double latestX=latest.x-first.x;
        double latestY=latest.y-first.y;
        int sum=0;
        while(pointsIt.hasNext()){
            Point2D.Double now=pointsIt.next();
            double nowX=now.x-latest.x;
            double nowY=now.y-latest.y;
            sum+=Math.signum(EYMath.vectorProduct(latestX, latestY, nowX, nowY));
            latestX=nowX;
            latestY=nowY;
            latest=now;
        }
        sum+=Math.signum(EYMath.vectorProduct(latestX, latestY, first.x-latest.x, first.y-latest.y));
        if(sum>0){
            return true;
        }else{
            return false;
        }
    }
}
