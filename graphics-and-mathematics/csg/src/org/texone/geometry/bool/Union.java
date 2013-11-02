package org.texone.geometry.bool;

import org.texone.geometry.Face;
import org.texone.geometry.Mesh;

/**
 * Mesh defined by the union of two meshes
 * @author texone
 *
 */
public class Union extends BooleanMesh{

	public Union(final Mesh theMesh1, final Mesh theMesh2) {
		super(theMesh1, theMesh2);
	}
	

	public void createMesh(Mesh theMesh1, Mesh theMesh2) {
		createMesh(theMesh1,theMesh2,Face.OUTSIDE, Face.SAME, Face.OUTSIDE);
	}
}
