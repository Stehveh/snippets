package org.texone.geometry.primitive;


import org.texone.geometry.Mesh;
import org.texone.geometry.Vertex;

/**
 * Box Primitive
 */
public class Box extends Mesh {
	private double _myLength;
	private double _myHeight;
	private double _myWidth;

	/**
	 * Constructs a customized Box object
	 * 
	 * @param name box name
	 * @param length box length
	 * @param height box height
	 * @param width box width
	 * @param color box color
	 */
	public Box(double length, double height, double width) {
		super(
			new Vertex[]{
			    new Vertex(-0.5,-0.5,-0.5),
			    new Vertex( 0.5,-0.5,-0.5),
			    new Vertex(-0.5, 0.5,-0.5),
			    new Vertex( 0.5, 0.5,-0.5),
			    new Vertex(-0.5,-0.5, 0.5),
			    new Vertex( 0.5,-0.5, 0.5),
			    new Vertex(-0.5, 0.5, 0.5),
			    new Vertex( 0.5, 0.5, 0.5),
			},
			new int[]{
				0, 2, 3, 3, 1, 0, 
				4, 5, 7, 7, 6, 4, 
				0, 1, 5, 5, 4, 0, 
				1, 3, 7, 7, 5, 1, 
				3, 2, 6, 6, 7, 3, 
				2, 0, 4, 4, 6, 2 	
			}
		);
	    
		_myLength = length;
		_myHeight = height;
		_myWidth = width;
	    
		scale(_myWidth, _myHeight, _myLength);
	}
}