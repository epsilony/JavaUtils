/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.utils.java2d;

import java.awt.geom.Point2D;
import java.util.Iterator;
import net.epsilony.utils.math.EYMath;

/**
 *
 * @author epsilonyuan@gmail.com
 */
public class Java2DGeometryMath {
    
    public static boolean isCounterClockwise(Iterable<? extends Point2D.Double> pointsIt){
        return isCounterClockwise(pointsIt.iterator());
    }
    
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
