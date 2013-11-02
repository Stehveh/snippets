package org.texone.geometry;

import java.util.List;


/**
 * Representation of a bound - the extremes of a 3d component for each
 * coordinate.
 */
public class BoundingBox {
	/** maximum from the x coordinate */
	private double xMax;
	/** minimum from the x coordinate */
	private double xMin;
	/** maximum from the y coordinate */
	private double yMax;
	/** minimum from the y coordinate */
	private double yMin;
	/** maximum from the z coordinate */
	private double zMax;
	/** minimum from the z coordinate */
	private double zMin;

	/** tolerance value to test equalities */
	private static final double TOL = 1e-10f;

	/**
	 * Bound constructor for a face
	 * 
	 * @param p1
	 *            point relative to the first vertex
	 * @param p2
	 *            point relative to the second vertex
	 * @param p3
	 *            point relative to the third vertex
	 */
	public BoundingBox(final Vertex p1, Vertex p2, Vertex p3) {
		xMax = xMin = p1.x;
		yMax = yMin = p1.y;
		zMax = zMin = p1.z;

		checkVertex(p2);
		checkVertex(p3);
	}

	/**
	 * Bound constructor for a object 3d
	 * 
	 * @param vertices the object vertices
	 */
	public BoundingBox(Vertex[] vertices) {
		xMax = xMin = vertices[0].x;
		yMax = yMin = vertices[0].y;
		zMax = zMin = vertices[0].z;

		for (int i = 0; i < vertices.length;i++) {
			checkVertex(vertices[i]);
		}
	}
	
	public BoundingBox(List vertices) {
		xMax = xMin = ((Vertex)vertices.get(0)).x;
		yMax = yMin = ((Vertex)vertices.get(0)).y;
		zMax = zMin = ((Vertex)vertices.get(0)).z;

		for (int i = 0; i < vertices.size();i++) {
			Vertex vertex = (Vertex)vertices.get(i);
			checkVertex(vertex);
		}
	}

	/**
	 * Makes a string definition for the bound object
	 * 
	 * @return the string definition
	 */
	public String toString() {
		return "x: " + xMin + " .. " + xMax + "\ny: " + yMin + " .. " + yMax + "\nz: " + zMin + " .. " + zMax;
	}

	/**
	 * Checks if a bound overlaps other one
	 * 
	 * @param bound other bound to make the comparison
	 * @return true if they insersect, false otherwise
	 */
	public boolean overlap(BoundingBox bound) {
		if (
			(xMin > bound.xMax + TOL) || (xMax < bound.xMin - TOL) || 
			(yMin > bound.yMax + TOL) || (yMax < bound.yMin - TOL) || 
			(zMin > bound.zMax + TOL) || (zMax < bound.zMin - TOL)
		) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Checks if one of the coordinates of a vertex exceed the ones found before
	 * 
	 * @param vertex
	 *            vertex to be tested
	 */ 
	private void checkVertex(Vertex vertex) {
		if (vertex.x > xMax) {
			xMax = vertex.x;
		} else if (vertex.x < xMin) {
			xMin = vertex.x;
		}

		if (vertex.y > yMax) {
			yMax = vertex.y;
		} else if (vertex.y < yMin) {
			yMin = vertex.y;
		}

		if (vertex.z > zMax) {
			zMax = vertex.z;
		} else if (vertex.z < zMin) {
			zMin = vertex.z;
		}
	}
}
