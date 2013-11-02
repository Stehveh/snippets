/**
 * shamelessly ripped from texone.org
 */
package org.texone.geometry;


import java.util.ArrayList;
import java.util.List;


import processing.core.PApplet;
import processing.core.PConstants;


/**
 *
 */
public class Mesh {

    private boolean _myChangedBounds = true;

    /**
     * solid vertices
     */
    private List vertices;

    /**
     * solid faces
     */
    private List faces;

    /**
     * mesh edges
     */
    private List edges;

    /**
     * object representing the solid extremes
     */
    private BoundingBox bound;

    /**
     * tolerance value to test equalities
     */
    private static final double TOL = 1e-10f;

    //----------------------------------CONSTRUCTOR---------------------------------//
    /**
     * Constructs a Object3d object based on a solid file.
     *
     * @param solid solid used to construct the Object3d object
     */
    public Mesh(Vertex[] theVertices, int[] theIndices) {
        set(theVertices, theIndices);
    }

    public Mesh() {
        vertices = new ArrayList();
        faces = new ArrayList();
        edges = new ArrayList();
    }

    public void set(Vertex[] theVertices, int[] indices) {
        Vertex v1, v2, v3, vertex;
        List verticesTemp = new ArrayList();

        //create vertices
        vertices = new ArrayList();
        for (int i = 0; i < theVertices.length; i++) {

            vertex = addVertex(theVertices[i], Vertex.UNKNOWN);
            verticesTemp.add(vertex);
        }

        //create faces
        faces = new ArrayList();
        for (int i = 0; i < indices.length; i = i + 3) {
            v1 = (Vertex) verticesTemp.get(indices[i]);
            v2 = (Vertex) verticesTemp.get(indices[i + 1]);
            v3 = (Vertex) verticesTemp.get(indices[i + 2]);
            addFace(v1, v2, v3);
        }

        //create bound
        bound = new BoundingBox(theVertices);
    }

    //--------------------------------------GETS------------------------------------//
    /**
     * Gets the number of faces
     *
     * @return number of faces
     */
    public int getNumFaces() {
        return faces().size();
    }

    /**
     * Gets a face reference for a given position
     *
     * @param index required face position
     * @return face reference , null if the position is invalid
     */
    public Face getFace(int index) {
        if (index < 0 || index >= faces().size()) {
            return null;
        } else {
            return (Face) faces().get(index);
        }
    }

    /**
     * Gets the solid bound
     *
     * @return solid bound
     */
    public BoundingBox getBound() {
        if (_myChangedBounds) {
            bound = new BoundingBox(vertices);
        }
        return bound;
    }

    //------------------------------------ADDS----------------------------------------//
    /**
     * Method used to add a face properly for internal methods
     *
     * @param v1 a face vertex
     * @param v2 a face vertex
     * @param v3 a face vertex
     */
    public Face addFace(Vertex v1, Vertex v2, Vertex v3) {
        if (v1.equals(v2) || v1.equals(v3) || v2.equals(v3)) {
            System.out.println("VERTICES MATCH");
            return null;
        }
        Face face = new Face(v1, v2, v3);

        if (face.getArea() < TOL) {
            return null;
        }
        faces().add(face);
        return face;


    }

    /**
     * Method used to add a vertex properly for internal methods
     *
     * @param pos vertex position
     * @param color vertex color
     * @param status vertex status
     * @return the vertex inserted (if a similar vertex already exists, this is
     * returned)
     */
    public Vertex addVertex(Vertex pos, int status) {
        //if already there is an equal vertex, it is not inserted
        Vertex vertex = new Vertex(pos, status);
        for (int i = 0; i < vertices.size(); i++) {
            Vertex vertex2 = (Vertex) vertices.get(i);
            if (vertex.equals(vertex2)) {
                vertex = vertex2;
                vertex.setStatus(status);
                return vertex;
            }
        }

        vertices.add(vertex);
        return vertex;
    }

    public Vertex addVertex(final double theX, final double theY, final double theZ) {
        return addVertex(new Vertex(theX, theY, theZ), Vertex.UNKNOWN);
    }

    // TRANSFORMATIONS
    public void scale(final double theScaleX, final double theScaleY, final double theScaleZ) {
        _myChangedBounds = true;
        for (int i = 0; i < vertices.size(); i++) {
            Vertex vertex = (Vertex) vertices.get(i);
            vertex.scale(theScaleX, theScaleY, theScaleZ);
        }
    }

    public void scale(final float theScale) {
        _myChangedBounds = true;
        for (int i = 0; i < vertices.size(); i++) {
            Vertex vertex = (Vertex) vertices.get(i);
            vertex.scale(theScale);
        }
    }

    public void rotateX(final float theAngle) {
        _myChangedBounds = true;
        for (int i = 0; i < vertices.size(); i++) {
            Vertex vertex = (Vertex) vertices.get(i);
            vertex.rotateX(theAngle);
        }
    }

    public void rotateY(final float theAngle) {
        _myChangedBounds = true;
        for (int i = 0; i < vertices.size(); i++) {
            Vertex vertex = (Vertex) vertices.get(i);
            vertex.rotateY(theAngle);
        }
    }

    public void rotateZ(final float theAngle) {
        _myChangedBounds = true;
        for (int i = 0; i < vertices.size(); i++) {
            Vertex vertex = (Vertex) vertices.get(i);
            vertex.rotateZ(theAngle);
        }
    }

    public void translate(final float theX, final float theY, final float theZ) {
        _myChangedBounds = true;
        for (int i = 0; i < vertices.size(); i++) {
            Vertex vertex = (Vertex) vertices.get(i);
            vertex.add(theX, theY, theZ);
        }
    }

    /**
     * Inverts faces classified as INSIDE, making its normals point outside.
     * Usually used into the second solid when the difference is applied.
     */
    public void invertInsideFaces() {
        for (int i = 0; i < faces.size(); i++) {
            Face face = (Face) faces.get(i);
            if (face.getStatus() == Face.INSIDE) {
                face.invert();
            }
        }
    }

    public void setFaces(List faces) {
        this.faces = faces;
    }

    public List faces() {
        return faces;
    }

    public void draw(PApplet p) {
        for (int i = 0; i < faces.size(); i++) {
            Face face = (Face) faces.get(i);
            p.beginShape();
//			p.fill(255,0,0);
            p.vertex((float) face.v1.x, (float) face.v1.y, (float) face.v1.z);

//			p.fill(0,255,0);
            p.vertex((float) face.v2.x, (float) face.v2.y, (float) face.v2.z);
//			p.fill(0,0,255);
            p.vertex((float) face.v3.x, (float) face.v3.y, (float) face.v3.z);
            p.endShape(PConstants.CLOSE);
        }
    }
}
