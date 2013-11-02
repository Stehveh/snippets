package org.texone.geometry.formular;

import java.util.Arrays;

import org.texone.geometry.Mesh;
import org.texone.geometry.Vertex;


/**
 * This is the basic formular class all the other formulars extend from.
 * 
 * @author christianr
 * 
 */
public class Formular extends Mesh{

	static protected float PI = (float) Math.PI;

	static protected float TWO_PI = 2 * PI;

	static protected float tan(final float i_value) {
		return (float) Math.tan(i_value);
	}

	static protected float exp(final float i_value) {
		return (float) Math.exp(i_value);
	}

	static protected float cosh(final float i_value) {
		return (exp(i_value) + exp(-i_value)) / 2.0f;
	}

	static protected float sinh(final float i_value) {
		return (exp(i_value) - exp(-i_value)) / 2.0f;
	}

	static protected float tanh(final float i_value) {
		return sinh(i_value) / cosh(i_value);
	}

	protected final int phiSteps;

	protected final int thetaSteps;

	protected final float minTheta;

	protected final float maxTheta;

	protected final float minPhi;

	protected final float maxPhi;

	protected final float[] parameter;

	private float xScale = 1;

	private float yScale = 1;

	private float zScale = 1;

	private float[][][] pointScaleDifference;

	final Vertex[][] coords;

	private final float[] bounds;

	private int[][] colors;

	/**
	 * @param i_phiSteps
	 *            the horizontal resolution of your surface
	 * @param i_thetaSteps
	 *            the vertical resolution of your surface
	 * @param i_minTheta
	 *            the minimal value of theta
	 * @param i_maxTheta
	 *            the maximal value of theta
	 * @param i_minPhi
	 *            the minimal value of phi
	 * @param i_maxPhi
	 *            the maximal value of phi
	 * @param i_parameter
	 *            some surface, like SuperShapes or the SnailSurface, can be
	 *            initialized with different parameters
	 * @param i_g
	 *            a PGraphics Object where your surface will drawn on, mainly
	 *            this is g
	 */
	public Formular(
		final int i_phiSteps, final int i_thetaSteps, 
		final float i_minTheta, final float i_maxTheta, 
		final float i_minPhi, final float i_maxPhi,
		final float[] i_parameter
	) {
		super();
		phiSteps = i_phiSteps;
		thetaSteps = i_thetaSteps;

		minTheta = i_minTheta;
		maxTheta = i_maxTheta;
		minPhi = i_minPhi;
		maxPhi = i_maxPhi;

		parameter = i_parameter;

		coords = new Vertex[phiSteps][thetaSteps];
		bounds = new float[6];
		Arrays.fill(bounds, 0);

		pointScaleDifference = new float[phiSteps][thetaSteps][3];
		for (int i = 0; i < phiSteps; i++) {
			for (int j = 0; j < thetaSteps; j++) {
				for (int k = 0; k < 3; k++) {
					pointScaleDifference[i][j][k] = 0;
				}
			}
		}

		initValues();
		setSurface();
	}

	/**
	 * @param i_surface
	 */
	public Formular(final Formular i_surface) {
		this(
			i_surface.phiSteps, i_surface.thetaSteps,
			i_surface.maxTheta, i_surface.maxTheta, i_surface.minPhi,
			i_surface.minPhi, i_surface.parameter
		);

		for (int phiStep = 0; phiStep < phiSteps; phiStep++) {
			for (int thetaStep = 0; thetaStep < thetaSteps; thetaStep++) {
				coords[phiStep][thetaStep] = new Vertex(
					i_surface.coords[phiStep][thetaStep].x,
					i_surface.coords[phiStep][thetaStep].y,
					i_surface.coords[phiStep][thetaStep].z
				);
			}
		}
		if (i_surface.colors != null) {
			colors = new int[phiSteps][thetaSteps];
			for (int i = 0; i < phiSteps; i++) {
				for (int j = 0; j < thetaSteps; j++) {
					colors[i][j] = i_surface.colors[i][j];
				}
			}
		}
	}

	/**
	 * Overwrite this method to set any values before setting up the surface
	 * 
	 */
	protected void initValues() {

	}

	/**
	 * @return int, the horizontal resolution of your surface
	 */
	public int phiSteps() {
		return phiSteps;
	}

	/**
	 * @return int, the vertical resolution of your surface
	 */
	public int thetaSteps() {
		return thetaSteps;
	}

	/**
	 * Use this method to get an array holding the coordinates of the surface.
	 * The array is organized in the form [phi][theta][x,y,z]
	 * 
	 * @return float[][][], the complete coordinate array in the form
	 *         [phi][theta][x,y,z]
	 */
	public Vertex[][] coords() {
		return coords;
	}

	/**
	 * Use this method to get the xyz coordinate for a given point on the
	 * surface
	 * 
	 * @param i_indexPhi
	 *            int, a value between 0 and phiSteps
	 * @param i_indexTheta
	 *            int, a value between 0 and thetaSteps
	 * @return float[], an array with the x,y,z coordinates
	 */
	public Vertex getVertex(int i_indexPhi, int i_indexTheta) {
		i_indexPhi = Math.max(0, Math.min(i_indexPhi, phiSteps - 1));
		i_indexTheta = Math.max(0, Math.min(i_indexTheta, thetaSteps - 1));
		return coords[i_indexPhi][i_indexTheta];
	}

	/**
	 * Use this method to change the scale of every point individually . The
	 * value i_pointScaleDifference be will multiply with the x-, y- and zScale
	 * for the given point (i_IndexPhi,i_IndexTheta)
	 * 
	 * @param i_IndexPhi
	 * @param i_IndexTheta
	 * @param i_pointScaleDifference
	 *            This value will be multiply with x-, y- and zScale.
	 * @related setScale ( )
	 * @related setXScale ( )
	 * @related setYScale ( )
	 * @related setZScale ( )
	 * 
	 * @example setPointScaleDifference
	 * @applet setPointScaleDifference
	 * 
	 */
	public void setPointScaleDifference(final int i_IndexPhi,final int i_IndexTheta, final float i_pointScaleDifference) {
		Arrays.fill(pointScaleDifference[i_IndexPhi][i_IndexTheta],i_pointScaleDifference);
	}

	/**
	 * Use this method to change the scale of every point individually . The
	 * values i_pointScaleDifferenceX, i_pointScaleDifferenceY and
	 * i_pointScaleDifferenceZ be will multiply with the x-, y- and zScale for
	 * the given point (i_IndexPhi,i_IndexTheta)
	 * 
	 * @param i_IndexPhi
	 * @param i_IndexTheta
	 * @param i_pointScaleDifferenceX
	 *            This value will be multiply with xScale.
	 * @param i_pointScaleDifferenceY
	 *            This value will be multiply with yScale.
	 * @param i_pointScaleDifferenceZ
	 *            This value will be multiply with zScale.
	 * @example setPointScaleDifference ( )
	 * @related setScale ( )
	 * @related setXScale ( )
	 * @related setYScale ( )
	 * @related setZScale ( )
	 */
	public void setPointScaleDifference(
		final int i_IndexPhi, final int i_IndexTheta, 
		final float i_pointScaleDifferenceX,
		final float i_pointScaleDifferenceY,
		final float i_pointScaleDifferenceZ
	) {
		pointScaleDifference[i_IndexPhi][i_IndexTheta][0] = i_pointScaleDifferenceX;
		pointScaleDifference[i_IndexPhi][i_IndexTheta][1] = i_pointScaleDifferenceY;
		pointScaleDifference[i_IndexPhi][i_IndexTheta][2] = i_pointScaleDifferenceZ;
	}

	/**
	 * Returns the 3 values that will be multiply with scaleX/Y/Z for the point
	 * (_IndexPhi, i_IndexTheta) as an float array (x,y,z).
	 * 
	 * @param i_IndexPhi
	 * @param i_IndexTheta
	 * @return float,
	 * @related setPointScaleDifference ( )
	 */
	public float[] getPointScaleDifference(final int i_IndexPhi,final int i_IndexTheta) {
		return pointScaleDifference[i_IndexPhi][i_IndexTheta];
	}

	/**
	 * @return an array with the 6 bounding coordinates
	 *         (maxX,minX,maxY,minY,maxZ,minZ) of the surface
	 */
	public float[] getBounds() {
		return bounds;
	}

	/**
	 * Every surface must implement this method to define how the x value is
	 * calculated for the given phi and theta step.
	 * 
	 * @param i_phiStep
	 * @param i_thetaStep
	 * @return the calculated x value
	 */
	protected float calculateX(final int i_phiStep, final int i_thetaStep) {
		return 0;
	}

	/**
	 * Every surface must implement this method to define how the y value is
	 * calculated for the given phi and theta step.
	 * 
	 * @param i_phiStep
	 * @param i_thetaStep
	 * @return the calculated y value
	 */
	protected float calculateY(final int i_phiStep, final int i_thetaStep) {
		return 0;
	}

	/**
	 * Every surface must implement this method to define how the z value is
	 * calculated for the given phi and theta step.
	 * 
	 * @param i_phiStep
	 * @param i_thetaStep
	 * @return the calculated z value
	 */
	protected float calculateZ(final int i_phiStep, final int i_thetaStep) {
		return 0;
	}

	/**
	 * Calculates the coordinates of the surface
	 * 
	 */
	protected void setSurface() {
		float x = 0;
		float y = 0;
		float z = 0;

		for (int phiStep = 0; phiStep < phiSteps; phiStep++) {
			for (int thetaStep = 0; thetaStep < thetaSteps; thetaStep++) {

				x = calculateX(phiStep, thetaStep);
				y = calculateY(phiStep, thetaStep);
				z = calculateZ(phiStep, thetaStep);

				if (x > bounds[0])
					bounds[0] = x;
				if (x < bounds[1])
					bounds[1] = x;
				if (y > bounds[2])
					bounds[2] = y;
				if (y < bounds[3])
					bounds[3] = y;
				if (z > bounds[4])
					bounds[4] = z;
				if (z < bounds[5])
					bounds[5] = z;

				coords[phiStep][thetaStep] = addVertex(
					x * (xScale + pointScaleDifference[phiStep][thetaStep][0]),
					y * (yScale + pointScaleDifference[phiStep][thetaStep][1]),
					z * (zScale + pointScaleDifference[phiStep][thetaStep][2])
				);
			}
		}

		for (int i = 0; i < phiSteps; i++) {
			for (int j = 0; j < thetaSteps; j++) {
				int nextI = (i + 1) % phiSteps;
				int nextJ = (j + 1) % thetaSteps;

				Vertex v1 = coords[i][j];
				Vertex v2 = coords[nextI][j];
				Vertex v3 = coords[nextI][nextJ];
				Vertex v4 = coords[i][nextJ];
				
				addFace(v1, v3, v2);
				addFace(v1, v4, v3);
			}
		}
	}

	

	protected int phiSub = 1;

}
