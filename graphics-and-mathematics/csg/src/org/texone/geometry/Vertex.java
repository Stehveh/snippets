package org.texone.geometry;

import java.util.ArrayList;
import java.util.Random;

/** 
 * Represents of a 3d face vertex.
 */
public class Vertex implements Cloneable {
	/** references to vertices connected to it by an edge  */
	private ArrayList adjacentVertices;
	/** vertex status relative to other object */
	private int status;
	


	public double x, y, z;
	private static final long serialVersionUID = -1021495605082676516L;

	/**
	 * Utility for random
	 */
	static final Random generator = new Random();

	/**
	 * float array for returning an array representation of the vector
	 */
	private double[] _myArrayRepresentation = new double[3];

	/** tolerance value to test equalities */
	private static final double TOL = 1e-5;

	/** vertex status if it is still unknown */
	public static final int UNKNOWN = 1;
	/** vertex status if it is inside a solid */
	public static final int INSIDE = 2;
	/** vertex status if it is outside a solid */
	public static final int OUTSIDE = 3;
	/** vertex status if it on the boundary of a solid */
	public static final int BOUNDARY = 4;

	//----------------------------------CONSTRUCTORS--------------------------------//

	/**
	 * Constructs a vertex with unknown status
	 * 
	 * @param position vertex position
	 * @param color vertex color
	 */
	public Vertex(final Vertex position) {
		this(position,UNKNOWN);
	}

	/**
	 * Constructs a vertex with unknown status
	 * 
	 * @param theX coordinate on the x axis
	 * @param theY coordinate on the y axis
	 * @param theZ coordinate on the z axis
	 * @param color vertex color
	 */
	public Vertex(final double theX, final double theY, final double theZ) {
		this(theX,theY,theZ,UNKNOWN);
	}

	/**
	 * Constructs a vertex with definite status
	 * 
	 * @param position vertex position
	 * @param color vertex color
	 * @param status vertex status - UNKNOWN, BOUNDARY, INSIDE or OUTSIDE
	 */
	public Vertex(final Vertex position, final int status) {

		x = position.x;
		y = position.y;
		z = position.z;

		adjacentVertices = new ArrayList();
		this.status = status;
	}

	/**
	 * Constructs a vertex with a definite status
	 * 
	 * @param x coordinate on the x axis
	 * @param y coordinate on the y axis
	 * @param z coordinate on the z axis
	 * @param color vertex color
	 * @param status vertex status - UNKNOWN, BOUNDARY, INSIDE or OUTSIDE
	 */
	public Vertex(double x, double y, double z, int status) {

		this.x = x;
		this.y = y;
		this.z = z;

		adjacentVertices = new ArrayList();
		this.status = status;
	}

	public Vertex() {
		this(0,0,0);
	}

	/**
	 * Clones the vertex object
	 * 
	 * @return cloned vertex object
	 */
	public Object clone() {
		Vertex clone = new Vertex(x,y,z,status);
		clone.adjacentVertices = new ArrayList();
		for (int i = 0; i < adjacentVertices.size(); i++) {
			clone.adjacentVertices.add(((Vertex) adjacentVertices.get(i)).clone());
		}

		return clone;
	}
	
	public Vertex getPosition(){
		return new Vertex(x,y,z);
	}

	/**
	 * Makes a string definition for the Vertex object
	 * 
	 * @return the string definition
	 */
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}

	/**
	 * Checks if an vertex is equal to another. To be equal, they have to have the same
	 * coordinates(with some tolerance) and color
	 * 
	 * @param anObject the other vertex to be tested
	 * @return true if they are equal, false otherwise. 
	 */
	public boolean equals(Object anObject) {
		if (!(anObject instanceof Vertex)) {
			return false;
		} 
		return almost((Vertex)anObject);
	}

	/**
	 * Sets the vertex status
	 * 
	 * @param status vertex status - UNKNOWN, BOUNDARY, INSIDE or OUTSIDE
	 */
	public void setStatus(int status) {
		if (status >= UNKNOWN && status <= BOUNDARY) {
			this.status = status;
		}
	}

	/**
	 * Gets an array with the adjacent vertices
	 * 
	 * @return array of the adjacent vertices 
	 */
	public Vertex[] getAdjacentVertices() {
		Vertex[] vertices = new Vertex[adjacentVertices.size()];
		for (int i = 0; i < adjacentVertices.size(); i++) {
			vertices[i] = (Vertex) adjacentVertices.get(i);
		}
		return vertices;
	}

	/**
	 * Gets the vertex status
	 * 
	 * @return vertex status - UNKNOWN, BOUNDARY, INSIDE or OUTSIDE
	 */
	public int getStatus() {
		return status;
	}

	//----------------------------------OTHERS--------------------------------------//

	/**
	 * Sets a vertex as being adjacent to it
	 * 
	 * @param adjacentVertex an adjacent vertex
	 */
	public void addAdjacentVertex(Vertex adjacentVertex) {
		if (!adjacentVertices.contains(adjacentVertex)) {
			adjacentVertices.add(adjacentVertex);
		}
	}

	/**
	 * Sets the vertex status, setting equally the adjacent ones
	 * 
	 * @param status new status to be set
	 */
	public void mark(int status) {
		//mark vertex
		this.status = status;

		//mark adjacent vertices
		Vertex[] adjacentVerts = getAdjacentVertices();
		for (int i = 0; i < adjacentVerts.length; i++) {
			if (adjacentVerts[i].getStatus() == Vertex.UNKNOWN) {
				adjacentVerts[i].mark(status);
			}
		}
	}

	public final void set(final double theX, final double theY, final double theZ) {
		x = theX;
		y = theY;
		z = theZ;
	}

	public final void set(double theX, double theY) {
		x = theX;
		y = theY;
	}

	public final void set(float[] theVector) {
		x = theVector[0];
		y = theVector[1];
		z = theVector[2];
	}

	public final void set(int[] theVector) {
		x = theVector[0];
		y = theVector[1];
		z = theVector[2];
	}

	public final void set(final Vertex theVector) {
		x = theVector.x;
		y = theVector.y;
		z = theVector.z;
	}

	public final void add(final Vertex theVectorA, final Vertex theVectorB) {
		x = theVectorA.x + theVectorB.x;
		y = theVectorA.y + theVectorB.y;
		z = theVectorA.z + theVectorB.z;
	}

	public final void add(final Vertex theVector) {
		x += theVector.x;
		y += theVector.y;
		z += theVector.z;
	}

	public final void add(float theX, float theY) {
		x += theX;
		y += theY;
	}

	public final void add(final double theX, final double theY, final double theZ) {
		x += theX;
		y += theY;
		z += theZ;
	}

	public final void sub(final Vertex theVectorA, final Vertex theVectorB) {
		x = theVectorA.x - theVectorB.x;
		y = theVectorA.y - theVectorB.y;
		z = theVectorA.z - theVectorB.z;
	}

	public final void sub(final Vertex theVector) {
		x -= theVector.x;
		y -= theVector.y;
		z -= theVector.z;
	}

	/**
	 * Use this method to negate a vector. The result of the
	 * negation is vector with the same magnitude but opposite
	 * direction. Mathematically the negation is the additive
	 * inverse of the vector. The sum of a value and its additive
	 * inerse is always zero.
	 * @shortdesc Use this method to negate a vector.
	 * @related scale ( )
	 */
	//	    public final void negate() {
	//	        scale( -1);
	//	    }

	/**
	 * Use this method to scale a vector. To scale a vector each of its
	 * coordinates is multiplied with the given scalar. The result is a
	 * vector that is parallel with its origin, with a different length
	 * and possibly opposite direction.<br>
	 * You can also scale a vector with another vector, in this case each
	 * coord of the vector is multiplied with related coord of the given
	 * vector.<br>
	 * Another possibillity is to set and scale the vector, this means the
	 * vector is set to the given vector multiplied with the given scalar.
	 * @param theScalar float or int: the value the vector is scaled with
	 * @related devide ( )
	 * @related negate ( )
	 */
	public final void scale(final double theScalar) {
		x *= theScalar;
		y *= theScalar;
		z *= theScalar;
	}

	/**
	 *
	 * @param theVector Vector3f: vector with the value each coord is scaled with
	 */
	public final void scale(final Vertex theVector) {
		x *= theVector.x;
		y *= theVector.y;
		z *= theVector.z;
	}

	/**
	 *
	 * @param theVector Vector3f: vector with the value each coord is scaled with
	 */
	public final void scale(final double theX, final double theY, final double theZ) {
		x *= theX;
		y *= theY;
		z *= theZ;
	}

	/**
	 * @param theScalar float or int: value the given vector is scaled with
	 * @param theVector Vector3f: vector the vector is set to
	 */
	public final void scale(final double theScalar, final Vertex theVector) {
		x = theScalar * theVector.x;
		y = theScalar * theVector.y;
		z = theScalar * theVector.z;
	}

	/**
	 * Deviding is nearly the the same as scaling, except
	 * @param theDivisor
	 */
	public final void devide(final double theDivisor) {
		x /= theDivisor;
		y /= theDivisor;
		z /= theDivisor;
	}

	public final void divide(final Vertex theVector) {
		x /= theVector.x;
		y /= theVector.y;
		z /= theVector.z;
	}

	public void rotateX(float a) {
		double ry = Math.cos(a) * y - Math.sin(a) * z;
		double rz = Math.sin(a) * y + Math.cos(a) * z;
		y = ry;
		z = rz;
	}

	public void rotateY(float a) {
		double rx = Math.cos(a) * x - Math.sin(a) * z;
		double rz = Math.sin(a) * x + Math.cos(a) * z;
		x = rx;
		z = rz;
	}

	public void rotateZ(float a) {
		double rx = Math.cos(a) * x - Math.sin(a) * y;
		double ry = Math.sin(a) * x + Math.cos(a) * y;
		x = rx;
		y = ry;
	}

	public final double lengthSquared() {
		return x * x + y * y + z * z;
	}

	/**
	 * Use this method to calculate the length of a vector, the length of a vector is also
	 * known as its magnitude. Vectors have a magnitude and a direction. These values
	 * are not explicitly expressed in the vector so they have to be computed.
	 * @return float: the length of the vector
	 * @shortdesc Calculates the length of the vector.
	 */
	public final double length() {
		return Math.sqrt(lengthSquared());
	}

	/**
	 * Sets this vector to the cross product of two given vectors. The
	 * cross product returns a vector standing vertical
	 * on the two vectors.
	 * @param theVectorA
	 * @param theVectorB
	 */
	public final void cross(final Vertex theVectorA, final Vertex theVectorB) {
		set(theVectorA.cross(theVectorB));
	}

	/**
	 * Returns the cross product of two vectors. The
	 * cross product returns a vector standing vertical
	 * on the two vectors.
	 * @param i_vector the other vector
	 * @return the cross product
	 */
	public Vertex cross(final Vertex theVector) {
		return new Vertex(
			y * theVector.z - z * theVector.y, 
			z * theVector.x - x * theVector.z, 
			x * theVector.y - y * theVector.x
		);
	}

	/**
	 * Returns the dot product of two vectors. The dot
	 * product is the cosinus of the angle between two
	 * vectors
	 * @param theVector, the other vector
	 * @return float, dot product of two vectors
	 */
	public final double dot(final Vertex theVector) {
		return x * theVector.x + y * theVector.y + z * theVector.z;
	}

	/**
	 * Sets the vector to the given one and norms it to the length of 1
	 *
	 */
	public final void normalize(final Vertex theVector) {
		set(theVector);
		normalize();
	}

	/**
	 * Norms the vector to the length of 1
	 *
	 */
	public final void normalize() {
		double inverseMag = 1.0 / Math.sqrt(x * x + y * y + z * z);
		x *= inverseMag;
		y *= inverseMag;
		z *= inverseMag;
	}

	/**
	 * Interpolates between this vector and the given vector
	 * by a given blend value. The blend value has to be between 0
	 * and 1. A blend value 0 would change nothing, a blend value 1
	 * would set this vector to the given one.
	 * @param blend float, blend value for interpolation
	 * @param theVector Vector3f, other vector for interpolation
	 */
	public void interpolate(final double blend, final Vertex theVector) {
		x = theVector.x + blend * (x - theVector.x);
		y = theVector.y + blend * (y - theVector.y);
		z = theVector.z + blend * (z - theVector.z);
	}

	/**
	 * Sets a position randomly distributed inside a sphere of unit radius
	 * centered at the origin.  Orientation will be random and length will range
	 * between 0 and 1
	 */
	public void randomize() {
		do {
			x = generator.nextDouble() * 2.0 - 1.0;
			y = generator.nextDouble() * 2.0 - 1.0;
			z = generator.nextDouble() * 2.0 - 1.0;
		} while (lengthSquared() > 1.0);
	}

	public final double angle(final Vertex theVector) {
		double d = dot(theVector) / (length() * theVector.length());
		/** @todo check these lines. */
		if (d < -1.0f) {
			d = -1.0f;
		}
		if (d > 1.0f) {
			d = 1.0f;
		}
		return (float) Math.acos(d);
	}

	public final double distanceSquared(final Vertex thePoint) {
		double dx = x - thePoint.x;
		double dy = y - thePoint.y;
		double dz = z - thePoint.z;
		return dx * dx + dy * dy + dz * dz;
	}

	public final double distance(final Vertex thePoint) {
		double dx = x - thePoint.x;
		double dy = y - thePoint.y;
		double dz = z - thePoint.z;
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

	public final double distanceL1(final Vertex thePoint) {
		return Math.abs(x - thePoint.x) + Math.abs(y - thePoint.y) + Math.abs(z - thePoint.z);
	}

	public final void min(final Vertex theMin) {
		if (x < theMin.x) x = theMin.x;
		if (y < theMin.y) y = theMin.y;
		if (z < theMin.z) z = theMin.z;
	}

	public final void min(final double theX, final double theY, final double theZ) {
		if (x < theX) x = theX;
		if (y < theY) y = theY;
		if (z < theZ) z = theZ;
	}

	public final void max(final Vertex theMax) {
		if (x > theMax.x) x = theMax.x;
		if (y > theMax.y) y = theMax.y;
		if (z > theMax.z) z = theMax.z;
	}

	public final void max(final double theX, final double theY, final double theZ) {
		if (x > theX) x = theX;
		if (y > theY) y = theY;
		if (z > theZ) z = theZ;
	}

	public final double[] toArray() {
		_myArrayRepresentation[0] = x;
		_myArrayRepresentation[1] = y;
		_myArrayRepresentation[2] = z;
		return _myArrayRepresentation;
	}

	public final boolean isNaN() {
		if (Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z)) {
			return true;
		}
		return false;
	}

	//
	//	    public boolean equals(final Vector3f theVector) {
	//	        
	//	    }
	//
	//
	//	    public boolean equals(final Object theVector) {
	//	        if (! (theVector instanceof Vector3f)) {
	//	            return false;
	//	        }
	//
	//	        if (x == theVector.x && y == theVector.y && z == theVector.z) {
	//	            return true;
	//	        } else {
	//	            return false;
	//	        }
	//	    }

	public final boolean almost(final Vertex theVector) {
		if (
			Math.abs(x - theVector.x) < TOL && 
			Math.abs(y - theVector.y) < TOL && 
			Math.abs(z - theVector.z) < TOL
		){
			return true;
		}
		return false;

	}
}