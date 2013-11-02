package org.texone.geometry.primitive;

import org.texone.geometry.formular.Formular;
import org.texone.geometry.formular.calculation.CosinusTable;
import org.texone.geometry.formular.calculation.LookUpTable;
import org.texone.geometry.formular.calculation.SinusTable;

/**
 * @nosuperclasses
 * @author christianr
 * @example sphere 
 */
public class Sphere extends Formular {

	private LookUpTable sinThetaTable;

	private LookUpTable cosThetaTable;

	private LookUpTable sinPhiTable;

	private LookUpTable cosPhiTable;

	/**
	 * @param i_g
	 *            A PGraphics object where the surface should be drawn in.
	 *            Mostly this is g, the current PGraphics object of your sketch.
	 * @param i_phiSteps
	 *            The horizontal resolution of the surface.
	 * @param i_thetaSteps
	 *            The vertical resolution of the surface.
	 * @releated surface 
	 */
	public Sphere(final float theLength, final float theWidth, final float theHeight, final int i_phiSteps, final int i_thetaSteps) {
		super(i_phiSteps, i_thetaSteps, 0, PI, 0, TWO_PI, null);
		scale(theLength, theWidth, theHeight);
	}
	
	public Sphere(final float theLength, final float theWidth, final float theHeight) {
		this(theLength, theWidth, theHeight, 40,40);
	}
	
	protected void initValues() {
		sinThetaTable = new SinusTable(thetaSteps, minTheta, maxTheta);
		cosThetaTable = new CosinusTable(thetaSteps, minTheta, maxTheta);
		sinPhiTable = new SinusTable(phiSteps, minPhi, maxPhi);
		cosPhiTable = new CosinusTable(phiSteps, minPhi, maxPhi);
	}

	protected float calculateX(final int i_phiStep, final int i_thetaStep) {
		return sinThetaTable.getValue(i_thetaStep) * cosPhiTable.getValue(i_phiStep);
	}

	protected float calculateY(final int i_phiStep, final int i_thetaStep) {
		return sinThetaTable.getValue(i_thetaStep) * sinPhiTable.getValue(i_phiStep);
	}

	protected float calculateZ(final int i_phiStep, final int i_thetaStep) {
		return cosThetaTable.getValue(i_thetaStep);
	}

}