package de.hfkbremen.interaktionundraum.programmingbasics.events;


import java.util.Vector;
import processing.core.PApplet;


public class SketchFiringEvents
        extends PApplet {

    private EventDispatcher mDispatcher;

    private EventSimpleReceiver mSimpleton;

    private Vector<EventReceiver> mReceivers;

    public void setup() {
        size(1024, 768);
        mDispatcher = new EventDispatcher();
        mDispatcher.position.set(width / 2, height / 2);
        mDispatcher.radius = 20;

        mReceivers = new Vector<EventReceiver>();
        for (int i = 0; i < 10; i++) {
            EventReceiver r = new EventReceiver();
            r.position.set(random(width), random(height));
            r.radius = random(5, 15);
            mReceivers.add(r);
            mDispatcher.register(r);
        }

        mSimpleton = new EventSimpleReceiver();
        mDispatcher.register(mSimpleton);
    }

    public void draw() {
        background(255);

        mDispatcher.loop(pmouseX, pmouseY);
        mDispatcher.draw(g);

        for (EventReceiver r : mReceivers) {
            r.draw(g);
        }

        mSimpleton.draw(g);
    }

    public static void main(String[] args) {
        PApplet.main(SketchFiringEvents.class.getName());
    }
}
