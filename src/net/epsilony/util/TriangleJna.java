/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.util;

import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.DoubleBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author epsilon
 */
public class TriangleJna {
//
//    public static class TriangluationIn extends Structure {
//
//        public double[] pointlist;                                               /* In / out */
//
//        public double[] pointattributelist;                                      /* In / out */
//
//        public int[] pointmarkerlist;                                          /* In / out */
//
//        public int numberofpoints;                                            /* In / out */
//
//        public int numberofpointattributes;                                   /* In / out */
//
//        public int[] trianglelist;                                             /* In / out */
//
//        public double[] triangleattributelist;                                   /* In / out */
//
//        public double[] trianglearealist;                                         /* In only */
////  public IntBuffer neighborlist;                                             /* Out only */
//
//        public int numberoftriangles;                                         /* In / out */
//
//        public int numberofcorners;                                           /* In / out */
//
//        public int numberoftriangleattributes;                                /* In / out */
//
//        public int[] segmentlist;                                              /* In / out */
//
//        public int[] segmentmarkerlist;                                        /* In / out */
//
//        public int numberofsegments;                                          /* In / out */
//
//        public double[] holelist;                        /* In / pointer to array copied out */
//
//        public int numberofholes;                                      /* In / copied out */
//
//        public double[] regionlist;                      /* In / pointer to array copied out */
//
//        public int numberofregions;                                    /* In / copied out */
//
////  public IntBuffer edgelist;                                                 /* Out only */
////  public IntBuffer edgemarkerlist;            /* Not used with Voronoi diagram; out only */
////  public DoubleBuffer normlist;                /* Used only with Voronoi diagram; out only */
////  public int numberofedges;                                             /* Out only */
//    }

    public static class triangulateio extends Structure {
//        public Buffer pointlist=null;

        public Pointer pointlist = Pointer.NULL;                                               /* In / out */

        public Pointer pointattributelist = Pointer.NULL;                                      /* In / out */

        public Pointer pointmarkerlist = Pointer.NULL;                                          /* In / out */

        public int numberofpoints;                                            /* In / out */

        public int numberofpointattributes;                                   /* In / out */

        public Pointer trianglelist = Pointer.NULL;                                             /* In / out */

        public Pointer triangleattributelist = Pointer.NULL;                                   /* In / out */

        public Pointer trianglearealist = Pointer.NULL;                                         /* In only */

        public Pointer neighborlist = Pointer.NULL;                                             /* Out only */

        public int numberoftriangles;                                         /* In / out */

        public int numberofcorners;                                           /* In / out */

        public int numberoftriangleattributes;                                /* In / out */

        public Pointer segmentlist = Pointer.NULL;                                              /* In / out */

        public Pointer segmentmarkerlist = Pointer.NULL;                                        /* In / out */

        public int numberofsegments;                                          /* In / out */

        public Pointer holelist = Pointer.NULL;                        /* In / pointer to array copied out */

        public int numberofholes;                                      /* In / copied out */

        public Pointer regionlist = Pointer.NULL;                      /* In / pointer to array copied out */

        public int numberofregions;                                    /* In / copied out */

        public Pointer edgelist = Pointer.NULL;                                                 /* Out only */

        public Pointer edgemarkerlist = Pointer.NULL;            /* Not used with Voronoi diagram; out only */

        public Pointer normlist = Pointer.NULL;                /* Used only with Voronoi diagram; out only */
//        public Buffer normlist;

        public int numberofedges;

        public triangulateio() {
        }

        public void setArrayField(String name, double[] in) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
            Field field = triangulateio.class.getField(name);
            Memory mem = new Memory(Double.SIZE * in.length);
            mem.write(0, in, 0, in.length);
            field.set(this, mem);
        }

        public void setArrayField(String name, int[] in) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
            Field field = triangulateio.class.getField(name);
            Memory mem = new Memory(Integer.SIZE * in.length);
            mem.write(0, in, 0, in.length);
            field.set(this, mem);
        }

        public static triangulateio instanceWithoutAttributes(int numberOfPoints, double[] pointList, int[] pointMarkerlist, int numberOfSegments, int[] segmentlist, int[] segmentmarkerlist, int numberOfHoles, double[] holeList) {
            triangulateio result = new triangulateio();
            try {
                
                result.numberofpoints = numberOfPoints;
                result.setArrayField("pointlist", pointList);
//                result.pointlist=DoubleBuffer.wrap(pointList);
                result.setArrayField("pointmarkerlist", pointMarkerlist);
                result.numberofsegments = numberOfSegments;
                result.setArrayField("segmentlist", segmentlist);
                result.setArrayField("segmentmarkerlist", segmentmarkerlist);
                result.numberofholes = numberOfHoles;
                result.setArrayField("holelist", holeList);

            } catch (NoSuchFieldException ex) {
                Logger.getLogger(TriangleJna.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(TriangleJna.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(TriangleJna.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                return result;
            }
        }
    }

    public interface LibTriangleJna extends Library {

        LibTriangleJna INSTANCE = (LibTriangleJna) Native.loadLibrary("Triangle", LibTriangleJna.class);

        public void triangulate(String flag, triangulateio in, triangulateio out, triangulateio vorout);
    }
}
