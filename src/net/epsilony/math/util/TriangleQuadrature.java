/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math.util;

import java.util.Arrays;

/**
 *
 * @author epsilon
 */
public class TriangleQuadrature {

    final static int[] numPts = new int[]{1, 3, 4, 6, 7, 12, 13};
    final static double[][] weights = new double[][]{{1},
        {1 / 3d, 1 / 3d, 1 / 3d},
        {-27 / 48d, 25 / 48d, 25 / 48d, 25 / 48d},
        {0.109951743655322, 0.109951743655322, 0.109951743655322, 0.223381589678011, 0.223381589678011, 0.223381589678011},
        {0.225000000000000, 0.125939180544827, 0.125939180544827, 0.125939180544827, 0.132394152788506, 0.132394152788506, 0.132394152788506},
        {0.050844906370207, 0.050844906370207, 0.050844906370207, 0.116786275726379, 0.116786275726379, 0.116786275726379, 0.082851075618374, 0.082851075618374, 0.082851075618374, 0.082851075618374, 0.082851075618374, 0.082851075618374},
        {-0.149570044467670, 0.175615257433204, 0.175615257433204, 0.175615257433204, 0.053347235608839, 0.053347235608839, 0.053347235608839, 0.077113760890257, 0.077113760890257, 0.077113760890257, 0.077113760890257, 0.077113760890257, 0.077113760890257}};
    final static double[][] areaCoods = new double[][]{{1 / 3d, 1 / 3d, 1 / 3d},
        {2 / 3d, 1 / 6d, 1 / 6d, 1 / 6d, 2 / 3d, 1 / 6d, 1 / 6d, 1 / 6d, 2 / 3d},
        {1 / 3d, 1 / 3d, 1 / 3d, 0.6, 0.2, 0.2, 0.2, 0.6, 0.2, 0.2, 0.2, 0.6},
        {0.816847572980459, 0.091576213509771, 0.091576213509771, 0.091576213509771, 0.816847572980459, 0.091576213509771, 0.091576213509771, 0.091576213509771, 0.816847572980459, 0.108103018168070, 0.445948490915965, 0.445948490915965, 0.445948490915965, 0.108103018168070, 0.445948490915965, 0.445948490915965, 0.445948490915965, 0.108103018168070},
        {1 / 3d, 1 / 3d, 1 / 3d, 0.797426985353087, 0.101286507323456, 0.101286507323456, 0.101286507323456, 0.797426985353087, 0.101286507323456, 0.101286507323456, 0.101286507323456, 0.797426985353087, 0.059715871789770, 0.470142064105115, 0.470142064105115, 0.470142064105115, 0.059715871789770, 0.470142064105115, 0.470142064105115, 0.470142064105115, 0.059715871789770},
        {0.873821971016996, 0.063089014491502, 0.063089014491502, 0.063089014491502, 0.873821971016996, 0.063089014491502, 0.063089014491502, 0.063089014491502, 0.873821971016996, 0.501426509658179, 0.249286745170910, 0.249286745170910, 0.249286745170910, 0.501426509658179, 0.249286745170910, 0.249286745170910, 0.249286745170910, 0.501426509658179, 0.626502499121399, 0.310352451033785, 0.053145049844816, 0.626502499121399, 0.053145049844816, 0.310352451033785, 0.310352451033785, 0.626502499121399, 0.053145049844816, 0.310352451033785, 0.053145049844816, 0.626502499121399, 0.053145049844816, 0.626502499121399, 0.310352451033785, 0.053145049844816, 0.310352451033785, 0.626502499121399},
        {1 / 3d, 1 / 3d, 1 / 3d, 0.479308067841923, 0.260345966079038, 0.260345966079038, 0.260345966079038, 0.479308067841923, 0.260345966079038, 0.260345966079038, 0.260345966079038, 0.479308067841923, 0.869739794195568, 0.065130102902216, 0.065130102902216, 0.065130102902216, 0.869739794195568, 0.065130102902216, 0.065130102902216, 0.065130102902216, 0.869739794195568, 0.638444188569809, 0.312865496004875, 0.048690315425316, 0.638444188569809, 0.048690315425316, 0.312865496004875, 0.312865496004875, 0.638444188569809, 0.048690315425316, 0.312865496004875, 0.048690315425316, 0.638444188569809, 0.048690315425316, 0.638444188569809, 0.312865496004875, 0.048690315425316, 0.312865496004875, 0.638444188569809}
    };
//    static {
//        for(int i=0;i<weights.length;i++){
//            System.out.println(Arrays.toString(weights[i]));
//        }
//    }

    public static double[] getAreaCoordinates(int nStep) {
        return Arrays.copyOf(areaCoods[nStep - 1], areaCoods[nStep - 1].length);

    }

    public static double[] getWeights(int nStep) {
        return Arrays.copyOf(weights[nStep - 1], weights[nStep - 1].length);
    }
}
