package org.texone.geometry.primitive;



import org.texone.geometry.Mesh;
import org.texone.geometry.Vertex;

/**
 * Class representing a cone
 */
public class Cone extends Mesh {
	private double _myXradius;
	private double _myZradius;
	private double _myHeight;

	/**
	 * Constructs a customized Cone object
	 * 
	 * @param name cone name
	 * @param length cone length
	 * @param theXradius cone ray in X
	 * @param theZradius cone ray in Z
	 * @param color cone color
	 */
	public Cone(final double theHeight, final double theXradius, final double theZradius, final int theResolution) {
		super();
		_myHeight = theHeight;
		_myXradius = theXradius;
		_myZradius = theZradius;

		double angle = (Math.PI * 2)/theResolution;
		Vertex upperCenter = addVertex(0,-_myHeight/2,0);
		Vertex lowerCenter = addVertex(0,_myHeight/2,0);
		
		Vertex lastLowerVertex = null;
		Vertex lowerVertex;
		
		for (int i = 0; i <= theResolution; i++) {
			
			double x = Math.sin(angle * i) * _myXradius / 2;
			double z = Math.cos(angle * i) * _myZradius / 2;

			if(i == 0){
				lastLowerVertex = addVertex(x,_myHeight/2,z);
			}else{
				lowerVertex = addVertex(x,_myHeight/2,z);
				
				addFace(lastLowerVertex,upperCenter,lowerVertex);
				addFace(lowerCenter,lastLowerVertex,lowerVertex);
				lastLowerVertex = lowerVertex;
			}
		}
	}
}