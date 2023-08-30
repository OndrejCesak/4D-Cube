import java.awt.Color;
import java.util.ArrayList;

public class World {

        private static double[][] pointsDefault = new double[][] {
                        { 0, 0, 0, 1 }, // 1
                        { 1, 0, 0, 1 }, // 2
                        { 1, 0, 1, 1 }, // 3
                        { 0, 0, 1, 1 }, // 4

                        { 0, 1, 0, 1 }, // 5
                        { 1, 1, 0, 1 }, // 6
                        { 1, 1, 1, 1 }, // 7
                        { 0, 1, 1, 1 }, // 8

                        { 0, 0, 0, 0 }, // 9
                        { 1, 0, 0, 0 }, // 10
                        { 1, 0, 1, 0 }, // 11
                        { 0, 0, 1, 0 }, // 12

                        { 0, 1, 0, 0 }, // 13
                        { 1, 1, 0, 0 }, // 14
                        { 1, 1, 1, 0 }, // 15
                        { 0, 1, 1, 0 }, // 16
        };

        private static int[][] edges = new int[][] {
                        { 0, 1 }, { 1, 2 }, // 0, 1
                        { 2, 3 }, { 3, 0 }, // 2, 3
                        { 4, 5 }, { 5, 6 }, // 4, 5
                        { 6, 7 }, { 7, 4 }, // 6, 7
                        { 8, 9 }, { 9, 10 }, // 8, 9
                        { 10, 11 }, { 11, 8 }, // 10, 11
                        { 12, 13 }, { 13, 14 }, // 12, 13
                        { 14, 15 }, { 15, 12 }, // 14, 15
                        { 0, 8 }, { 1, 9 }, // 16, 17
                        { 2, 10 }, { 3, 11 }, // 18, 19
                        { 4, 12 }, { 5, 13 }, // 20, 21
                        { 6, 14 }, { 7, 15 }, // 22, 23
                        { 0, 4 }, { 1, 5 }, // 24, 25
                        { 2, 6 }, { 3, 7 }, // 26, 27
                        { 8, 12 }, { 9, 13 }, // 28, 29
                        { 10, 14 }, { 11, 15 } // 30, 31
        };

        private static int[][] cubes = new int[][] {
                        { 0, 1, 2, 3, 4, 5, 6, 7, 24, 25, 26, 27 },
                        { 8, 9, 10, 11, 12, 13, 14, 15, 28, 29, 30, 31 },
                        { 0, 25, 4, 24, 8, 29, 12, 28, 16, 17, 21, 20 },
                        { 1, 26, 5, 25, 9, 30, 13, 29, 18, 22, 21, 17 },
                        { 2, 27, 6, 26, 10, 31, 14, 30, 22, 23, 18, 19 },
                        { 3, 24, 7, 27, 11, 28, 15, 31, 20, 23, 19, 16 },
                        { 0, 1, 2, 3, 8, 9, 10, 11, 16, 17, 18, 19 },
                        { 4, 5, 6, 7, 12, 13, 14, 15, 20, 21, 22, 23 },
        };

        public static ArrayList<Integer> furthermostCuts;

        public static double[][] points;

        private static double zw = 0;
        private static double yw = 0;
        private static double xw = 0;
        private static double y = 0;

        public final static double zOffset = 5;

        private static Color[] colors = new Color[8];

        public static ArrayList<Color> cubeColors;

        public static ArrayList<ArrayList<double[]>> seenCubes;

        // --- INITIALIZATION ---

        public static void init() {
                points = new double[pointsDefault.length][4];
                rotatePoints1();
                setCubeColors();
                setCubePositions();

                update();
        }

        private static void setCubeColors() {
                for (int i = 0; i < colors.length; i++)
                        colors[i] = Color.getHSBColor((float) i / (colors.length - 1) * 0.75f, 1, 1);
        }

        // --- NEW METHOD ---

        private static void setCubePositions() {
                cubePositions = new double[cubes.length][4];

                for (int i = 0; i < cubes.length; i++) {
                        double x = 0;
                        double y = 0;
                        double z = 0;
                        double w = 0;

                        for (int j = 0; j < cubes[i].length; j++) {
                                x += points[edges[cubes[i][j]][0]][0] + points[edges[cubes[i][j]][1]][0];
                                y += points[edges[cubes[i][j]][0]][1] + points[edges[cubes[i][j]][1]][1];
                                z += points[edges[cubes[i][j]][0]][2] + points[edges[cubes[i][j]][1]][2];
                                w += points[edges[cubes[i][j]][0]][3] + points[edges[cubes[i][j]][1]][3];
                        }

                        x /= cubes[i].length * 3;
                        y /= cubes[i].length * 3;
                        z /= cubes[i].length * 3;
                        w /= cubes[i].length * 3;

                        cubePositions[i][0] = x;
                        cubePositions[i][1] = y;
                        cubePositions[i][2] = z;
                        cubePositions[i][3] = w;
                }
        }

        // --- --- ---

        private static boolean contains(ArrayList<double[]> list, double[] array) {
                for (int i = 0; i < list.size(); i++) {
                        for (int j = 0; j < list.get(i).length; j++) {
                                if (list.get(i)[j] != array[j])
                                        break;

                                if (j == list.get(i).length - 1)
                                        return true;
                        }
                }

                return false;
        }

        // --- NEW METHOD ---

        private static double[][] cubePositions;

        private static ArrayList<double[]> interPoints;
        private static ArrayList<Integer> interPointsIs;

        public static ArrayList<ArrayList<double[]>> cuts;
        public static ArrayList<Integer> cutCubes;

        private static ArrayList<ArrayList<Integer>> cubePoints;

        public static ArrayList<Color> cutColors;

        private static void searchIntersectPoints() {
                interPoints = new ArrayList<>();
                interPointsIs = new ArrayList<>();
                cubePoints = new ArrayList<>();
                for (int i = 0; i < cubes.length; i++) {
                        cubePoints.add(new ArrayList<>());
                }

                for (int i = 0; i < edges.length; i++) {
                        for (int j = 0; j <= 1; j++) {
                                double[] v0 = points[edges[i][j]].clone();
                                double[] v1 = points[edges[i][1 - j]].clone();

                                ArrayList<Integer> cubesToAdd = new ArrayList<>();

                                for (int k = 0; k < cubes.length; k++)
                                        for (int l = 0; l < cubes[k].length; l++)
                                                if (i == cubes[k][l])
                                                        cubesToAdd.add(k);

                                if (v0[3] == 0 && !contains(interPoints, v0)) {
                                        interPoints.add(v0.clone());
                                        interPointsIs.add(j);
                                        for (int k = 0; k < cubesToAdd.size(); k++)
                                                cubePoints.get(cubesToAdd.get(k)).add(interPoints.size() - 1);
                                } else if (v0[3] < 0 && v1[3] > 0) {
                                        double[] vector = new double[] {
                                                        (v1[0] - v0[0]) * -v0[3] / (v1[3] - v0[3]),
                                                        (v1[1] - v0[1]) * -v0[3] / (v1[3] - v0[3]),
                                                        (v1[2] - v0[2]) * -v0[3] / (v1[3] - v0[3]),
                                                        (v1[3] - v0[3]) * -v0[3] / (v1[3] - v0[3])
                                        };
                                        v0[0] += vector[0];
                                        v0[1] += vector[1];
                                        v0[2] += vector[2];
                                        v0[3] += vector[3];
                                        // v0 is now point of "edge X w-axis" intersection

                                        if (!contains(interPoints, v0)) {
                                                interPoints.add(v0.clone());
                                                interPointsIs.add(j);
                                                for (int k = 0; k < cubesToAdd.size(); k++)
                                                        cubePoints.get(cubesToAdd.get(k)).add(interPoints.size() - 1);
                                                break;
                                        }
                                }
                        }
                }
        }

        private static void performPlaneMaking() {
                if (interPoints.size() < 4)
                        return;

                cuts = new ArrayList<>();
                cutCubes = new ArrayList<>();
                cutColors = new ArrayList<>();

                makePlanes1();
        }

        private static void makePlanes1() {
                for (int i = 0; i < cubePositions.length; i++) {
                        if (i >= cubePoints.size())
                                continue;

                        ArrayList<double[]> cut = new ArrayList<>();

                        for (int j = 0; j < cubePoints.get(i).size(); j++) {
                                cut.add(interPoints.get(cubePoints.get(i).get(j)));
                        }

                        cuts.add(cut);
                        cutCubes.add(i);
                        cutColors.add(colors[i]);
                }
        }

        private static void orderCutPoints() {
                for (int i = 0; i < cuts.size(); i++) {
                        if (cuts.get(i).size() == 0)
                                continue;

                        ArrayList<double[]> orderedPoints = new ArrayList<>();
                        orderedPoints.add(cuts.get(i).get(0));

                        double[] cutCenter = new double[4];
                        for (int j = 0; j < cuts.get(i).size(); j++) {
                                cutCenter[0] += cuts.get(i).get(j)[0] / cuts.get(i).size();
                                cutCenter[1] += cuts.get(i).get(j)[1] / cuts.get(i).size();
                                cutCenter[2] += cuts.get(i).get(j)[2] / cuts.get(i).size();
                                cutCenter[3] += cuts.get(i).get(j)[3] / cuts.get(i).size();
                        }

                        for (int j = 0; j < cuts.get(i).size() - 1; j++) {
                                int currentIndex = orderedPoints.size() - 1;

                                double[] point1 = j == 0
                                                ? cutCenter.clone()
                                                : orderedPoints.get(currentIndex - 1).clone();

                                double[] currentVector = new double[] {
                                                point1[0] - orderedPoints.get(currentIndex)[0],
                                                point1[1] - orderedPoints.get(currentIndex)[1],
                                                point1[2] - orderedPoints.get(currentIndex)[2],
                                                point1[3] - orderedPoints.get(currentIndex)[3],
                                };
                                double currentVectorAbs = Math.sqrt(currentVector[0] * currentVector[0]
                                                + currentVector[1] * currentVector[1]
                                                + currentVector[2] * currentVector[2]
                                                + currentVector[3] * currentVector[3]);
                                currentVector = new double[] {
                                                currentVector[0] / currentVectorAbs,
                                                currentVector[1] / currentVectorAbs,
                                                currentVector[2] / currentVectorAbs,
                                                currentVector[3] / currentVectorAbs
                                };

                                int nextPointIndex = 0;
                                double biggestCos = Double.MAX_VALUE;

                                for (int k = 0; k < cuts.get(i).size(); k++) {
                                        if (orderedPoints.contains(cuts.get(i).get(k))) {
                                                continue;
                                        }

                                        double[] vector = new double[] {
                                                        cuts.get(i).get(k)[0] - orderedPoints.get(currentIndex)[0],
                                                        cuts.get(i).get(k)[1] - orderedPoints.get(currentIndex)[1],
                                                        cuts.get(i).get(k)[2] - orderedPoints.get(currentIndex)[2],
                                                        cuts.get(i).get(k)[3] - orderedPoints.get(currentIndex)[3],
                                        };

                                        double vectorAbs = Math.sqrt(vector[0] * vector[0]
                                                        + vector[1] * vector[1]
                                                        + vector[2] * vector[2]
                                                        + vector[3] * vector[3]);

                                        vector = new double[] {
                                                        vector[0] / vectorAbs,
                                                        vector[1] / vectorAbs,
                                                        vector[2] / vectorAbs,
                                                        vector[3] / vectorAbs
                                        };

                                        double cos = currentVector[0] * vector[0] +
                                                        currentVector[1] * vector[1] +
                                                        currentVector[2] * vector[2] +
                                                        currentVector[3] * vector[3];

                                        if (cos < biggestCos) {
                                                biggestCos = cos;
                                                nextPointIndex = k;
                                        }
                                }

                                if (nextPointIndex > 0) {
                                        orderedPoints.add(cuts.get(i).get(nextPointIndex));
                                }
                        }
                        cuts.set(i, orderedPoints);
                }
        }

        private static void orderCuts3() {
                furthermostCuts = new ArrayList<>();

                for (int i = 0; i < cuts.size(); i++) {
                        if (cuts.get(i).size() == 0)
                                continue;

                        double centerx = 0, centery = 0, centerz = 0;

                        double middlex = 0, middley = 0, middlez = 0;
                        for (int j = 0; j < cuts.get(i).size(); j++) {
                                middlex += cuts.get(i).get(j)[0] / cuts.get(i).size();
                                middley += cuts.get(i).get(j)[1] / cuts.get(i).size();
                                middlez += cuts.get(i).get(j)[2] / cuts.get(i).size();
                        }

                        double v0x = cuts.get(i).get(0)[0] - middlex;
                        double v0y = cuts.get(i).get(0)[1] - middley;
                        double v0z = cuts.get(i).get(0)[2] - middlez;

                        double v1x = cuts.get(i).get(1)[0] - middlex;
                        double v1y = cuts.get(i).get(1)[1] - middley;
                        double v1z = cuts.get(i).get(1)[2] - middlez;

                        double v01x = v0y * v1z - v1y * v0z;
                        double v01y = v0z * v1x - v1z * v0x;
                        double v01z = v0x * v1y - v1x * v0y;

                        double posDist = Math.sqrt((middlex - centerx + v01x) * (middlex - centerx + v01x)
                                        + (middley - centery + v01y) * (middley - centery + v01y)
                                        + (middlez - centerz + v01z) * (middlez - centerz + v01z));

                        double negDist = Math.sqrt((middlex - centerx - v01x) * (middlex - centerx - v01x)
                                        + (middley - centery - v01y) * (middley - centery - v01y)
                                        + (middlez - centerz - v01z) * (middlez - centerz - v01z));

                        if (posDist < negDist) {
                                v01x = -v01x;
                                v01y = -v01y;
                                v01z = -v01z;
                        }

                        double v01abs = Math.sqrt(v01x * v01x + v01y * v01y + v01z * v01z);

                        double v01nx = v01x / v01abs;
                        double v01ny = v01y / v01abs;
                        double v01nz = v01z / v01abs;

                        double camx = -middlex;
                        double camy = -middley;
                        double camz = -middlez - zOffset;

                        double camabs = Math.sqrt(camx * camx + camy * camy + camz * camz);

                        double camnx = camx / camabs;
                        double camny = camy / camabs;
                        double camnz = camz / camabs;

                        double dot = camnx * v01nx + camny * v01ny + camnz * v01nz;

                        if (dot > 0) {
                                furthermostCuts.add(i);
                        }
                }
        }

        // --- --- ---

        public static boolean shift = false;

        public static void update() {
                rotatePoints1();
                searchIntersectPoints();
                performPlaneMaking();
                orderCutPoints();
                orderCuts3();
        }

        private static void rotatePoints1() {
                for (int i = 0; i < pointsDefault.length; i++) {
                        double x0 = pointsDefault[i][0] - 0.5;
                        double y0 = pointsDefault[i][1] - 0.5;
                        double z0 = pointsDefault[i][2] - 0.5;
                        double w0 = pointsDefault[i][3] - 0.5;

                        double x1 = x0 * Math.cos(xw) - w0 * Math.sin(xw);
                        double y1 = y0;
                        double z1 = z0;
                        double w1 = x0 * Math.sin(xw) + w0 * Math.cos(xw);

                        double x2 = x1;
                        double y2 = y1 * Math.cos(yw) - w1 * Math.sin(yw);
                        double z2 = z1;
                        double w2 = y1 * Math.sin(yw) + w1 * Math.cos(yw);

                        double x3 = x2;
                        double y3 = y2;
                        double z3 = z2 * Math.cos(zw) - w2 * Math.sin(zw);
                        double w3 = z2 * Math.sin(zw) + w2 * Math.cos(zw);

                        double x4 = x3 * Math.cos(y) - z3 * Math.sin(y);
                        double y4 = y3;
                        double z4 = x3 * Math.sin(y) + z3 * Math.cos(y);
                        double w4 = w3;

                        points[i] = new double[] { x4, y4, z4, w4 };
                }
        }
        // --- --- ---

        public static void rotate(double zw0, double yw0, double xw0, double y0) {
                zw += zw0;
                yw += yw0;
                xw += xw0;
                y += y0;
        }
}
