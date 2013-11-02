package org.texone.geometry.bool;

import java.util.ArrayList;
import java.util.List;

import org.texone.geometry.Face;
import org.texone.geometry.Line;
import org.texone.geometry.Mesh;
import org.texone.geometry.Segment;
import org.texone.geometry.Vertex;

/**
 * Code for boolean operations is based on code for java3d from Danilo Balby Silva Castanheira
 * TODO http://www.geocities.com/danbalby/
 * Class used to apply boolean operations on solids.
 * 
 * <br>
 * <br>
 * Two 'Solid' objects are submitted to this class constructor. There is a
 * methods for each boolean operation. Each of these return a 'Solid' resulting
 * from the application of its operation into the submitted solids.
 * 
 * <br>
 * <br>
 * See: D. H. Laidlaw, W. B. Trumbore, and J. F. Hughes. "Constructive Solid
 * Geometry for Polyhedral Objects" SIGGRAPH Proceedings, 1986, p.161.
 */
public abstract class BooleanMesh extends Mesh{
	/** tolerance value to test equalities */
	private static final double TOL = 1e-10f;

	// --------------------------------CONSTRUCTORS----------------------------------//

	/**
	 * Constructs a BooleanModeller object to apply boolean operation in two
	 * solids. Makes preliminary calculations
	 * 
	 * @param theMesh1
	 *            first solid where boolean operations will be applied
	 * @param theMesh2
	 *            second solid where boolean operations will be applied
	 */
	public BooleanMesh(final Mesh theMesh1, final Mesh theMesh2) {

		// split the faces so that none of them intercepts each other
		long nanoTime = System.nanoTime();
		splitFaces(theMesh1, theMesh2);
		splitFaces(theMesh2, theMesh1);
		System.out.println("SPLT FACE");
		System.out.println("_______________________");
		System.out.println(System.nanoTime() - nanoTime);
		// classify faces as being inside or outside the other solid
		
		nanoTime = System.nanoTime();
		classifyFaces(theMesh1, theMesh2);
		classifyFaces(theMesh2, theMesh1);
		System.out.println("CLASSIFY FACE");
		System.out.println("_______________________");
		System.out.println(System.nanoTime() - nanoTime);
		
		nanoTime = System.nanoTime();
		createMesh(theMesh1,theMesh2);
		System.out.println("CREATE FACE");
		System.out.println("_______________________");
		System.out.println(System.nanoTime() - nanoTime);
	}
	
	protected abstract void createMesh(final Mesh theMesh1, final Mesh theMesh2);

	// --------------------------PRIVATES--------------------------------------------//

	/**
	 * Composes a solid based on the faces status of the two operators solids:
	 * Face.INSIDE, Face.OUTSIDE, Face.SAME, Face.OPPOSITE
	 * 
	 * @param faceStatus1
	 *            status expected for the first solid faces
	 * @param faceStatus2
	 *            other status expected for the first solid faces (expected a
	 *            status for the faces coincident with second solid faces)
	 * @param faceStatus3
	 *            status expected for the second solid faces
	 */
	protected void createMesh(final Mesh theMesh1, final Mesh theMesh2, int faceStatus1, int faceStatus2, int faceStatus3) {
		List vertices = new ArrayList();
		List indices = new ArrayList();

		// group the elements of the two solids whose faces fit with the desired
		// status
		groupObjectComponents(theMesh1, vertices, indices, faceStatus1, faceStatus2);
		groupObjectComponents(theMesh2, vertices, indices, faceStatus3, faceStatus3);

		// turn the arrayLists to arrays
		Vertex[] verticesArray = new Vertex[vertices.size()];
		for (int i = 0; i < vertices.size(); i++) {
			verticesArray[i] = ((Vertex) vertices.get(i)).getPosition();
		}
		int[] indicesArray = new int[indices.size()];
		for (int i = 0; i < indices.size(); i++) {
			indicesArray[i] = ((Integer) indices.get(i)).intValue();
		}

		// returns the solid containing the grouped elements
		set(verticesArray, indicesArray);
	}

	/**
	 * Fills solid arrays with data about faces of an object generated whose
	 * status is as required
	 * 
	 * @param object3d
	 *            solid object used to fill the arrays
	 * @param vertices
	 *            vertices array to be filled
	 * @param indices
	 *            indices array to be filled
	 * @param colors
	 *            colors array to be filled
	 * @param faceStatus1
	 *            a status expected for the faces used to to fill the data
	 *            arrays
	 * @param faceStatus2
	 *            a status expected for the faces used to to fill the data
	 *            arrays
	 */
	private void groupObjectComponents(Mesh object, List vertices, List indices, int faceStatus1, int faceStatus2) {
		Face face;
		// for each face..
		for (int i = 0; i < object.getNumFaces(); i++) {
			face = object.getFace(i);
			// if the face status fits with the desired status...
			if (face.getStatus() == faceStatus1 || face.getStatus() == faceStatus2) {
				// adds the face elements into the arrays
				Vertex[] faceVerts = { face.v1, face.v2, face.v3 };
				for (int j = 0; j < faceVerts.length; j++) {
					if (vertices.contains(faceVerts[j])) {
						indices.add(new Integer(vertices.indexOf(faceVerts[j])));
					} else {
						indices.add(new Integer(vertices.size()));
						vertices.add(faceVerts[j]);
					}
				}
			}
		}
	}

	/**
	 * Split faces so that none face is intercepted by a face of other object
	 * 
	 * @param mesh2
	 *            the other object 3d used to make the split
	 */
	public void splitFaces(Mesh mesh1, Mesh mesh2) {
		Line line;
		Face face1, face2;
		Segment segment1, segment2;
		double distFace1Vert1, distFace1Vert2, distFace1Vert3, distFace2Vert1, distFace2Vert2, distFace2Vert3;
		int signFace1Vert1, signFace1Vert2, signFace1Vert3, signFace2Vert1, signFace2Vert2, signFace2Vert3;

		if (!mesh1.getBound().overlap(mesh2.getBound()))return;
		// if the objects bounds overlap...
		// for each object1 face..
		for (int i = 0; i < mesh1.getNumFaces(); i++) {
			// if object1 face bound and object2 bound overlap ...
			face1 = mesh1.getFace(i);

			if (!face1.getBound().overlap(mesh2.getBound()))continue;
			
			
			long milliTime = System.currentTimeMillis();
			// for each object2 face...
			for (int j = 0; j < mesh2.getNumFaces(); j++) {
				// if object1 face bound and object2 face bound overlap...
				face2 = mesh2.getFace(j);

				if (!face1.getBound().overlap(face2.getBound()))continue;
				
				// PART I - DO TWO POLIGONS INTERSECT?
				// POSSIBLE RESULTS: INTERSECT, NOT_INTERSECT, COPLANAR

				
				// distance from the face1 vertices to the face2 plane
				distFace1Vert1 = face2.computeDistance(face1.v1);
				distFace1Vert2 = face2.computeDistance(face1.v2);
				distFace1Vert3 = face2.computeDistance(face1.v3);

				// distances signs from the face1 vertices to the face2 plane
				signFace1Vert1 = (distFace1Vert1 > TOL ? 1 : (distFace1Vert1 < -TOL ? -1 : 0));
				signFace1Vert2 = (distFace1Vert2 > TOL ? 1 : (distFace1Vert2 < -TOL ? -1 : 0));
				signFace1Vert3 = (distFace1Vert3 > TOL ? 1 : (distFace1Vert3 < -TOL ? -1 : 0));

				// if all the signs are zero, the planes are coplanar
				// if all the signs are positive or negative, the planes do not
				// intersect
				// if the signs are not equal...
				if (signFace1Vert1 == signFace1Vert2 && signFace1Vert2 == signFace1Vert3)continue;
	
				// distance from the face2 vertices to the face1 plane
				distFace2Vert1 = face1.computeDistance(face2.v1);
				distFace2Vert2 = face1.computeDistance(face2.v2);
				distFace2Vert3 = face1.computeDistance(face2.v3);

				// distances signs from the face2 vertices to the face1 plane
				signFace2Vert1 = (distFace2Vert1 > TOL ? 1 : (distFace2Vert1 < -TOL ? -1 : 0));
				signFace2Vert2 = (distFace2Vert2 > TOL ? 1 : (distFace2Vert2 < -TOL ? -1 : 0));
				signFace2Vert3 = (distFace2Vert3 > TOL ? 1 : (distFace2Vert3 < -TOL ? -1 : 0));

				// if the signs are not equal...
				if (signFace2Vert1 == signFace2Vert2 && signFace2Vert2 == signFace2Vert3)continue;
				long nanoTime1 = System.nanoTime();
				line = new Line(face1, face2);

				// intersection of the face1 and the plane of face2
				segment1 = new Segment(line, face1, signFace1Vert1, signFace1Vert2, signFace1Vert3);

				// intersection of the face2 and the plane of face1
				segment2 = new Segment(line, face2, signFace2Vert1, signFace2Vert2, signFace2Vert3);

				// if the two segments intersect...
				if (!segment1.intersect(segment2))continue;
				
				splitFace(mesh1, i, segment1, segment2);

				// prevent from infinite loop (with a loss of faces...)
				// if(numFacesStart*20<getNumFaces())
				// {
				// System.out.println("possible infinite loop situation:
				// terminating faces split");
				// return;
				// }

				// if the face in the position isn't the same, there was
				// a break
				if (face1 == mesh1.getFace(i))continue;
				
				// if the generated solid is equal the origin...
				if (face1.equals(mesh1.getFace(mesh1.getNumFaces() - 1))) {
					// return it to its position and jump it
					if (i != (mesh1.getNumFaces() - 1)) {
						mesh1.faces().remove(mesh1.getNumFaces() - 1);
						mesh1.faces().add(i, face1);
					} else {
						continue;
					}
				}
				// else: test next face
				else {
					i--;
					break;
				}
			}
		}
	}
	
	/**
	 * Split an individual face
	 * 
	 * @param facePos face position on the array of faces
	 * @param segment1 segment representing the intersection of the face with the plane
	 * of another face
	 * @return segment2 segment representing the intersection of other face with the
	 * plane of the current face plane
	 */
	void splitFace(Mesh theMesh, int facePos, Segment segment1, Segment segment2) {
		Vertex startPos, endPos;
		int startType, endType, middleType;
		double startDist, endDist;

		Face face =  theMesh.getFace(facePos);
		Vertex startVertex = segment1.getStartVertex();
		Vertex endVertex = segment1.getEndVertex();

		//starting point: deeper starting point 		
		if (segment2.getStartDistance() > segment1.getStartDistance() + TOL) {
			startDist = segment2.getStartDistance();
			startType = segment1.getIntermediateType();
			startPos = segment2.getStartPosition();
		} else {
			startDist = segment1.getStartDistance();
			startType = segment1.getStartType();
			startPos = segment1.getStartPosition();
		}

		//ending point: deepest ending point
		if (segment2.getEndDistance() < segment1.getEndDistance() - TOL) {
			endDist = segment2.getEndDistance();
			endType = segment1.getIntermediateType();
			endPos = segment2.getEndPosition();
		} else {
			endDist = segment1.getEndDistance();
			endType = segment1.getEndType();
			endPos = segment1.getEndPosition();
		}
		middleType = segment1.getIntermediateType();

		//set vertex to BOUNDARY if it is start type		
		if (startType == Segment.VERTEX) {
			startVertex.setStatus(Vertex.BOUNDARY);
		}

		//set vertex to BOUNDARY if it is end type
		if (endType == Segment.VERTEX) {
			endVertex.setStatus(Vertex.BOUNDARY);
		}

		//VERTEX-_______-VERTEX 
		if (startType == Segment.VERTEX && endType == Segment.VERTEX) {
			return;
		}

		//______-EDGE-______
		else if (middleType == Segment.EDGE) {
			//gets the edge 
			int splitEdge;
			if ((startVertex == face.v1 && endVertex == face.v2) || (startVertex == face.v2 && endVertex == face.v1)) {
				splitEdge = 1;
			} else if ((startVertex == face.v2 && endVertex == face.v3) || (startVertex == face.v3 && endVertex == face.v2)) {
				splitEdge = 2;
			} else {
				splitEdge = 3;
			}

			//VERTEX-EDGE-EDGE
			if (startType == Segment.VERTEX) {
				breakFaceInTwo(theMesh, facePos, endPos, splitEdge);
				return;
			}

			//EDGE-EDGE-VERTEX
			else if (endType == Segment.VERTEX) {
				breakFaceInTwo(theMesh, facePos, startPos, splitEdge);
				return;
			}

			// EDGE-EDGE-EDGE
			else if (startDist == endDist) {
				breakFaceInTwo(theMesh, facePos, endPos, splitEdge);
			} else {
				if ((startVertex == face.v1 && endVertex == face.v2) || (startVertex == face.v2 && endVertex == face.v3) || (startVertex == face.v3 && endVertex == face.v1)) {
					breakFaceInThree(theMesh, facePos, startPos, endPos, splitEdge);
				} else {
					breakFaceInThree(theMesh, facePos, endPos, startPos, splitEdge);
				}
			}
			return;
		}

		//______-FACE-______

		//VERTEX-FACE-EDGE
		else if (startType == Segment.VERTEX && endType == Segment.EDGE) {
			breakFaceInTwo(theMesh, facePos, endPos, endVertex);
		}
		//EDGE-FACE-VERTEX
		else if (startType == Segment.EDGE && endType == Segment.VERTEX) {
			breakFaceInTwo(theMesh, facePos, startPos, startVertex);
		}
		//VERTEX-FACE-FACE
		else if (startType == Segment.VERTEX && endType == Segment.FACE) {
			breakFaceInThree(theMesh, facePos, endPos, startVertex);
		}
		//FACE-FACE-VERTEX
		else if (startType == Segment.FACE && endType == Segment.VERTEX) {
			breakFaceInThree(theMesh, facePos, startPos, endVertex);
		}
		//EDGE-FACE-EDGE
		else if (startType == Segment.EDGE && endType == Segment.EDGE) {
			breakFaceInThree(theMesh, facePos, startPos, endPos, startVertex, endVertex);
		}
		//EDGE-FACE-FACE
		else if (startType == Segment.EDGE && endType == Segment.FACE) {
			breakFaceInFour(theMesh, facePos, startPos, endPos, startVertex);
		}
		//FACE-FACE-EDGE
		else if (startType == Segment.FACE && endType == Segment.EDGE) {
			breakFaceInFour(theMesh, facePos, endPos, startPos, endVertex);
		}
		//FACE-FACE-FACE
		else if (startType == Segment.FACE && endType == Segment.FACE) {
			Vertex segmentVector = new Vertex(startPos.x - endPos.x, startPos.y - endPos.y, startPos.z - endPos.z);

			//if the intersection segment is a point only...
			if (Math.abs(segmentVector.x) < TOL && Math.abs(segmentVector.y) < TOL && Math.abs(segmentVector.z) < TOL) {
				breakFaceInThree(theMesh, facePos, startPos);
				return;
			}

			//gets the vertex more lined with the intersection segment
			int linedVertex;
			Vertex linedVertexPos;
			Vertex vertexVector = new Vertex(endPos.x - face.v1.x, endPos.y - face.v1.y, endPos.z - face.v1.z);
			vertexVector.normalize();
			double dot1 = Math.abs(segmentVector.dot(vertexVector));
			vertexVector = new Vertex(endPos.x - face.v2.x, endPos.y - face.v2.y, endPos.z - face.v2.z);
			vertexVector.normalize();
			double dot2 = Math.abs(segmentVector.dot(vertexVector));
			vertexVector = new Vertex(endPos.x - face.v3.x, endPos.y - face.v3.y, endPos.z - face.v3.z);
			vertexVector.normalize();
			double dot3 = Math.abs(segmentVector.dot(vertexVector));
			if (dot1 > dot2 && dot1 > dot3) {
				linedVertex = 1;
				linedVertexPos = face.v1.getPosition();
			} else if (dot2 > dot3 && dot2 > dot1) {
				linedVertex = 2;
				linedVertexPos = face.v2.getPosition();
			} else {
				linedVertex = 3;
				linedVertexPos = face.v3.getPosition();
			}

			// Now find which of the intersection endpoints is nearest to that vertex.
			if (linedVertexPos.distance(startPos) > linedVertexPos.distance(endPos)) {
				breakFaceInFive(theMesh, facePos, startPos, endPos, linedVertex);
			} else {
				breakFaceInFive(theMesh, facePos, endPos, startPos, linedVertex);
			}
		}
	}

	/**
	 * Face breaker for VERTEX-EDGE-EDGE / EDGE-EDGE-VERTEX
	 * 
	 * @param facePos face position on the faces array
	 * @param newPos new vertex position
	 * @param edge that will be split 
	 */
	private void breakFaceInTwo(Mesh theMesh, int facePos, Vertex newPos, int splitEdge) {
		Face face =  (Face)theMesh.faces().remove(facePos);

		Vertex vertex = theMesh.addVertex(newPos, Vertex.BOUNDARY);

		if (splitEdge == 1) {
			theMesh.addFace(face.v1, vertex, face.v3);
			theMesh.addFace(vertex, face.v2, face.v3);
		} else if (splitEdge == 2) {
			theMesh.addFace(face.v2, vertex, face.v1);
			theMesh.addFace(vertex, face.v3, face.v1);
		} else {
			theMesh.addFace(face.v3, vertex, face.v2);
			theMesh.addFace(vertex, face.v1, face.v2);
		}
	}

	/**
	 * Face breaker for VERTEX-FACE-EDGE / EDGE-FACE-VERTEX
	 * 
	 * @param facePos face position on the faces array
	 * @param newPos new vertex position
	 * @param endVertex vertex used for splitting 
	 */
	private void breakFaceInTwo(Mesh theMesh, int facePos, Vertex newPos, Vertex endVertex) {
		Face face =  (Face)theMesh.faces().remove(facePos);

		Vertex vertex = theMesh.addVertex(newPos, Vertex.BOUNDARY);

		if (endVertex.equals(face.v1)) {
			theMesh.addFace(face.v1, vertex, face.v3);
			theMesh.addFace(vertex, face.v2, face.v3);
		} else if (endVertex.equals(face.v2)) {
			theMesh.addFace(face.v2, vertex, face.v1);
			theMesh.addFace(vertex, face.v3, face.v1);
		} else {
			theMesh.addFace(face.v3, vertex, face.v2);
			theMesh.addFace(vertex, face.v1, face.v2);
		}
	}

	/**
	 * Face breaker for EDGE-EDGE-EDGE
	 * 
	 * @param facePos face position on the faces array
	 * @param newPos1 new vertex position
	 * @param newPos2 new vertex position 
	 * @param splitEdge edge that will be split
	 */
	private void breakFaceInThree(Mesh theMesh, int facePos, Vertex newPos1, Vertex newPos2, int splitEdge) {
		Face face =  (Face)theMesh.faces().remove(facePos);

		Vertex vertex1 = theMesh.addVertex(newPos1, Vertex.BOUNDARY);
		Vertex vertex2 = theMesh.addVertex(newPos2, Vertex.BOUNDARY);

		if (splitEdge == 1) {
			theMesh.addFace(face.v1, vertex1, face.v3);
			theMesh.addFace(vertex1, vertex2, face.v3);
			theMesh.addFace(vertex2, face.v2, face.v3);
		} else if (splitEdge == 2) {
			theMesh.addFace(face.v2, vertex1, face.v1);
			theMesh.addFace(vertex1, vertex2, face.v1);
			theMesh.addFace(vertex2, face.v3, face.v1);
		} else {
			theMesh.addFace(face.v3, vertex1, face.v2);
			theMesh.addFace(vertex1, vertex2, face.v2);
			theMesh.addFace(vertex2, face.v1, face.v2);
		}
	}

	/**
	 * Face breaker for VERTEX-FACE-FACE / FACE-FACE-VERTEX
	 * 
	 * @param facePos face position on the faces array
	 * @param newPos new vertex position
	 * @param endVertex vertex used for the split
	 */
	private void breakFaceInThree(Mesh theMesh, int facePos, Vertex newPos, Vertex endVertex) {
		Face face =  (Face)theMesh.faces().remove(facePos);

		Vertex vertex = theMesh.addVertex(newPos, Vertex.BOUNDARY);

		if (endVertex.equals(face.v1)) {
			theMesh.addFace(face.v1, face.v2, vertex);
			theMesh.addFace(face.v2, face.v3, vertex);
			theMesh.addFace(face.v3, face.v1, vertex);
		} else if (endVertex.equals(face.v2)) {
			theMesh.addFace(face.v2, face.v3, vertex);
			theMesh.addFace(face.v3, face.v1, vertex);
			theMesh.addFace(face.v1, face.v2, vertex);
		} else {
			theMesh.addFace(face.v3, face.v1, vertex);
			theMesh.addFace(face.v1, face.v2, vertex);
			theMesh.addFace(face.v2, face.v3, vertex);
		}
	}

	/**
	 * Face breaker for EDGE-FACE-EDGE
	 * 
	 * @param facePos face position on the faces array
	 * @param newPos1 new vertex position
	 * @param newPos2 new vertex position 
	 * @param startVertex vertex used the new faces creation
	 * @param endVertex vertex used for the new faces creation
	 */
	private void breakFaceInThree(Mesh theMesh, int facePos, Vertex newPos1, Vertex newPos2, Vertex startVertex, Vertex endVertex) {
		Face face =  (Face)theMesh.faces().remove(facePos);

		Vertex vertex1 = theMesh.addVertex(newPos1, Vertex.BOUNDARY);
		Vertex vertex2 = theMesh.addVertex(newPos2, Vertex.BOUNDARY);

		if (startVertex.equals(face.v1) && endVertex.equals(face.v2)) {
			theMesh.addFace(face.v1, vertex1, vertex2);
			theMesh.addFace(face.v1, vertex2, face.v3);
			theMesh.addFace(vertex1, face.v2, vertex2);
		} else if (startVertex.equals(face.v2) && endVertex.equals(face.v1)) {
			theMesh.addFace(face.v1, vertex2, vertex1);
			theMesh.addFace(face.v1, vertex1, face.v3);
			theMesh.addFace(vertex2, face.v2, vertex1);
		} else if (startVertex.equals(face.v2) && endVertex.equals(face.v3)) {
			theMesh.addFace(face.v2, vertex1, vertex2);
			theMesh.addFace(face.v2, vertex2, face.v1);
			theMesh.addFace(vertex1, face.v3, vertex2);
		} else if (startVertex.equals(face.v3) && endVertex.equals(face.v2)) {
			theMesh.addFace(face.v2, vertex2, vertex1);
			theMesh.addFace(face.v2, vertex1, face.v1);
			theMesh.addFace(vertex2, face.v3, vertex1);
		} else if (startVertex.equals(face.v3) && endVertex.equals(face.v1)) {
			theMesh.addFace(face.v3, vertex1, vertex2);
			theMesh.addFace(face.v3, vertex2, face.v2);
			theMesh.addFace(vertex1, face.v1, vertex2);
		} else {
			theMesh.addFace(face.v3, vertex2, vertex1);
			theMesh.addFace(face.v3, vertex1, face.v2);
			theMesh.addFace(vertex2, face.v1, vertex1);
		}
	}

	/**
	 * Face breaker for FACE-FACE-FACE (a point only)
	 * 
	 * @param facePos face position on the faces array
	 * @param newPos new vertex position
	 */
	private void breakFaceInThree(Mesh theMesh, int facePos, Vertex newPos) {
		Face face =  (Face)theMesh.faces().remove(facePos);

		Vertex vertex = theMesh.addVertex(newPos, Vertex.BOUNDARY);

		theMesh.addFace(face.v1, face.v2, vertex);
		theMesh.addFace(face.v2, face.v3, vertex);
		theMesh.addFace(face.v3, face.v1, vertex);
	}

	/**
	 * Face breaker for EDGE-FACE-FACE / FACE-FACE-EDGE
	 * 
	 * @param facePos face position on the faces array
	 * @param newPos1 new vertex position
	 * @param newPos2 new vertex position 
	 * @param endVertex vertex used for the split
	 */
	private void breakFaceInFour(Mesh theMesh, int facePos, Vertex newPos1, Vertex newPos2, Vertex endVertex) {
		Face face =  (Face)theMesh.faces().remove(facePos);

		Vertex vertex1 = theMesh.addVertex(newPos1, Vertex.BOUNDARY);
		Vertex vertex2 = theMesh.addVertex(newPos2, Vertex.BOUNDARY);

		if (endVertex.equals(face.v1)) {
			theMesh.addFace(face.v1, vertex1, vertex2);
			theMesh.addFace(vertex1, face.v2, vertex2);
			theMesh.addFace(face.v2, face.v3, vertex2);
			theMesh.addFace(face.v3, face.v1, vertex2);
		} else if (endVertex.equals(face.v2)) {
			theMesh.addFace(face.v2, vertex1, vertex2);
			theMesh.addFace(vertex1, face.v3, vertex2);
			theMesh.addFace(face.v3, face.v1, vertex2);
			theMesh.addFace(face.v1, face.v2, vertex2);
		} else {
			theMesh.addFace(face.v3, vertex1, vertex2);
			theMesh.addFace(vertex1, face.v1, vertex2);
			theMesh.addFace(face.v1, face.v2, vertex2);
			theMesh.addFace(face.v2, face.v3, vertex2);
		}
	}

	/**
	 * Face breaker for FACE-FACE-FACE
	 * 
	 * @param facePos face position on the faces array
	 * @param newPos1 new vertex position
	 * @param newPos2 new vertex position 
	 * @param linedVertex what vertex is more lined with the intersection found
	 */
	private void breakFaceInFive(Mesh theMesh, int facePos, Vertex newPos1, Vertex newPos2, int linedVertex) {
		Face face =  (Face)theMesh.faces().remove(facePos);

		Vertex vertex1 = theMesh.addVertex(newPos1, Vertex.BOUNDARY);
		Vertex vertex2 = theMesh.addVertex(newPos2, Vertex.BOUNDARY);

		if (linedVertex == 1) {
			theMesh.addFace(face.v2, face.v3, vertex1);
			theMesh.addFace(face.v2, vertex1, vertex2);
			theMesh.addFace(face.v3, vertex2, vertex1);
			theMesh.addFace(face.v2, vertex2, face.v1);
			theMesh.addFace(face.v3, face.v1, vertex2);
		} else if (linedVertex == 2) {
			theMesh.addFace(face.v3, face.v1, vertex1);
			theMesh.addFace(face.v3, vertex1, vertex2);
			theMesh.addFace(face.v1, vertex2, vertex1);
			theMesh.addFace(face.v3, vertex2, face.v2);
			theMesh.addFace(face.v1, face.v2, vertex2);
		} else {
			theMesh.addFace(face.v1, face.v2, vertex1);
			theMesh.addFace(face.v1, vertex1, vertex2);
			theMesh.addFace(face.v2, vertex2, vertex1);
			theMesh.addFace(face.v1, vertex2, face.v3);
			theMesh.addFace(face.v2, face.v3, vertex2);
		}
	}

	/**
	 * Classify faces as being inside, outside or on boundary of other object
	 * 
	 * @param object2
	 *            object 3d used for the comparison
	 */
	/**
	 * Classify faces as being inside, outside or on boundary of other object
	 * 
	 * @param object
	 *            object 3d used for the comparison
	 */
	public void classifyFaces(Mesh object1, Mesh object2) {
		// calculate adjacency information
		for (int i = 0; i < object1.faces().size();i++) {
			Face face = (Face)object1.faces().get(i);
			face.v1.addAdjacentVertex(face.v2);
			face.v1.addAdjacentVertex(face.v3);
			face.v2.addAdjacentVertex(face.v1);
			face.v2.addAdjacentVertex(face.v3);
			face.v3.addAdjacentVertex(face.v1);
			face.v3.addAdjacentVertex(face.v2);
		}
		
		// for each face
		for (int i = 0; i < object1.faces().size();i++) {
			Face face = (Face)object1.faces().get(i);
			long nanotime = System.nanoTime();
			boolean simple = face.simpleClassify();
			System.out.println("simple classify");
			System.out.println(System.nanoTime()-nanotime);
			// if the face vertices aren't classified to make the simple
			// classify
			if (simple == false) {
				// makes the ray trace classification
				nanotime = System.nanoTime();
				face.rayTraceClassify(object2);
				System.out.println("ray classify");
				System.out.println(System.nanoTime()-nanotime);

				// mark the vertices
				if (face.v1.getStatus() == Vertex.UNKNOWN) {
					face.v1.mark(face.getStatus());
				}
				if (face.v2.getStatus() == Vertex.UNKNOWN) {
					face.v2.mark(face.getStatus());
				}
				if (face.v3.getStatus() == Vertex.UNKNOWN) {
					face.v3.mark(face.getStatus());
				}
			}
		}
	}
}