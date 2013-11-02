package de.hfkbremen.interaktionundraum.csg;


import org.texone.geometry.Mesh;
import org.texone.geometry.bool.Difference;
import org.texone.geometry.bool.Intersection;
import org.texone.geometry.bool.Union;
import org.texone.geometry.primitive.Box;
import org.texone.geometry.primitive.Sphere;
import processing.core.PApplet;
import static processing.core.PConstants.OPENGL;


public class SketchSubtractSphereFromBox
        extends PApplet {

    private Mesh mCube;

    private Mesh mSphere;

    private Mesh mBoolean;

    public void setup() {
        size(1024, 768, OPENGL);
        createMeshes();
    }

    public void draw() {
        background(255);
        translate(width / 2, height / 2);

        if (mBoolean != null) {
            rotateWithMouse(mBoolean);

            fill(0, 0, 0);
            stroke(50, 50, 50);
            mBoolean.draw(this);
        } else {
            rotateWithMouse(mCube);

            fill(0, 127, 255);
            stroke(0, 192, 255);
            mCube.draw(this);

            fill(255, 127, 0);
            stroke(255, 192, 0);
            mSphere.draw(this);
        }
    }

    public void keyPressed() {
        if (key == ' ' || key == 'c') {
            createMeshes();
        }
        if (key == '-') {
            mBoolean = new Difference(mCube, mSphere);
        }
        if (key == '+') {
            mBoolean = new Union(mCube, mSphere);
        }
        if (key == '#') {
            mBoolean = new Intersection(mCube, mSphere);
        }
    }

    private void createMeshes() {
        final float mCubeSize = 300;
        mCube = new Box(mCubeSize, mCubeSize, mCubeSize);
        final float mSphereSize = 200;
        mSphere = new Sphere(mSphereSize, mSphereSize, mSphereSize);
        mBoolean = null;
    }

    private void rotateWithMouse(Mesh mMesh) {
        final float mScale = 2.5f;
        mMesh.rotateY((pmouseX - mouseX) / (float) width * mScale);
        mMesh.rotateX((pmouseY - mouseY) / (float) height * mScale);
    }

    public static void main(String[] args) {
        PApplet.main(SketchSubtractSphereFromBox.class.getName());
    }
}
