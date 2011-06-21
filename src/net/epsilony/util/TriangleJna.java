/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.util;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

/**
 *
 * @author epsilon
 */
public class TriangleJna {

    public static class TriangluationIn extends Structure {

        double[] pointlist;                                               /* In / out */

        double[] pointattributelist;                                      /* In / out */

        int[] pointmarkerlist;                                          /* In / out */

        int numberofpoints;                                            /* In / out */

        int numberofpointattributes;                                   /* In / out */

        int[] trianglelist;                                             /* In / out */

        double[] triangleattributelist;                                   /* In / out */

        double[] trianglearealist;                                         /* In only */

        // int[] neighborlist;                                             /* Out only */
        int numberoftriangles;                                         /* In / out */

        int numberofcorners;                                           /* In / out */

        int numberoftriangleattributes;                                /* In / out */

        int[] segmentlist;                                              /* In / out */

        int[] segmentmarkerlist;                                        /* In / out */

        int numberofsegments;                                          /* In / out */

        double[] holelist;                        /* In / pointer to array copied out */

        int numberofholes;                                      /* In / copied out */

        double[] regionlist;                      /* In / pointer to array copied out */

        int numberofregions;                                    /* In / copied out */

        //int[] edgelist;                                                 /* Out only */
        //int[] edgemarkerlist;            /* Not used with Voronoi diagram; out only */
        //double[] normlist;                /* Used only with Voronoi diagram; out only */
        //int numberofedges;                                             /* Out only */
    }

    public static class TriangluationOut extends Structure {

        Pointer pointlist = Pointer.NULL;                                               /* In / out */

        Pointer pointattributelist = Pointer.NULL;                                      /* In / out */

        Pointer pointmarkerlist = Pointer.NULL;                                          /* In / out */

        int numberofpoints;                                            /* In / out */

        int numberofpointattributes;                                   /* In / out */

        Pointer trianglelist = Pointer.NULL;                                             /* In / out */

        Pointer triangleattributelist = Pointer.NULL;                                   /* In / out */

        Pointer trianglearealist = Pointer.NULL;                                         /* In only */

        // int[] neighborlist;                                             /* Out only */
        int numberoftriangles;                                         /* In / out */

        int numberofcorners;                                           /* In / out */

        int numberoftriangleattributes;                                /* In / out */

        Pointer segmentlist = Pointer.NULL;                                              /* In / out */

        Pointer segmentmarkerlist = Pointer.NULL;                                        /* In / out */

        int numberofsegments;                                          /* In / out */

        Pointer holelist = Pointer.NULL;                        /* In / pointer to array copied out */

        int numberofholes;                                      /* In / copied out */

        Pointer regionlist = Pointer.NULL;                      /* In / pointer to array copied out */

        int numberofregions;                                    /* In / copied out */

        //int[] edgelist;                                                 /* Out only */
        //int[] edgemarkerlist;            /* Not used with Voronoi diagram; out only */
        //double[] normlist;                /* Used only with Voronoi diagram; out only */
        //int numberofedges;       
    }

    public interface LibTriangleJna extends Library {

        LibTriangleJna INSTANCE = (LibTriangleJna) Native.loadLibrary("Triangle", LibTriangleJna.class);

        public void triangulate(String flag, Pointer in, Pointer out, Pointer vorout);
    }
}
