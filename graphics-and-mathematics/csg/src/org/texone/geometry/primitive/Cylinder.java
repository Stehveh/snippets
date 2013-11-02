package org.texone.geometry.primitive;

import org.texone.geometry.Mesh;
import org.texone.geometry.Vertex;

/**
 * Cylinder
 */
public class Cylinder extends Mesh {
	/** cylinder ray in X */
	private double _myXradius;
	/** cylinder ray in Z */
	private double _myZradius;
	/** cylinder height */
	private double _myHeight;

	/**
	 * Constructs a customized Cylinder object
	 * 
	 * @param name cylinder name
	 * @param length cylinder length
	 * @param _myXradius cylinder ray in X
	 * @param _myZradius cylinder ray in Z
	 * @param color cylinder color
	 */
	public Cylinder(final double theHeight, final double theXradius, final double theZradius, int resolution) {
		super();

		_myHeight = theHeight;
		_myXradius = theXradius;
		_myZradius = theZradius;

		double angle = (Math.PI * 2)/resolution;
		Vertex upperCenter = addVertex(0,-_myHeight/2,0);
		Vertex lowerCenter = addVertex(0,_myHeight/2,0);
		
		Vertex lastUpperVertex = null;
		Vertex lastLowerVertex = null;
		
		Vertex upperVertex;
		Vertex lowerVertex;
		
		for (int i = 0; i <= resolution; i++) {
			
			double x = Math.sin(angle * i) * _myXradius / 2;
			double z = Math.cos(angle * i) * _myZradius / 2;

			if(i == 0){
				lastUpperVertex = addVertex(x,-_myHeight/2,z);
				lastLowerVertex = addVertex(x,_myHeight/2,z);
			}else{
				upperVertex = addVertex(x,-_myHeight/2,z);
				lowerVertex = addVertex(x,_myHeight/2,z);
				
				addFace(upperCenter,upperVertex,lastUpperVertex);
				addFace(lowerCenter,lastLowerVertex,lowerVertex);
				addFace(lastLowerVertex,lastUpperVertex,upperVertex);
				addFace(lowerVertex,lastLowerVertex,upperVertex);
				
				lastUpperVertex = upperVertex;
				lastLowerVertex = lowerVertex;
			}
		}
	}

	/**
	 * Gets height
	 * 
	 * @return height
	 */
	public double getHeight() {
		return _myHeight;
	}

	/**
	 * Gets ray in X
	 * 
	 * @return ray in X
	 */
	public double getRayX() {
		return _myXradius;
	}

	/**
	 * Gets ray in Z
	 * 
	 * @return ray in Z
	 */
	public double getRayZ() {
		return _myZradius;
	}
}