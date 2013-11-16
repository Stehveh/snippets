package de.hfkbremen.interaktionundraum.programmingbasics.events;


import java.util.Vector;
import processing.core.PGraphics;
import processing.core.PVector;


public class EventDispatcher {

    public static final int EVENT_ENTER = 0;

    public static final int EVENT_LEAVE = 1;

    public float radius;

    public PVector position = new PVector();

    private boolean mHit = false;

    private int mColor = 0;

    /* --- */
    public Vector<IEventListener> mEventListeners = new Vector<IEventListener>();

    public void fireEvent(int pEvent) {
        for (IEventListener mEventListener : mEventListeners) {
            mEventListener.fireEvent(pEvent);
        }
    }

    public void register(IEventListener pEventListener) {
        mEventListeners.add(pEventListener);
    }

    public boolean remove(IEventListener pEventListener) {
        return mEventListeners.remove(pEventListener);
    }

    /* --- */
    public void loop(int pmouseX, int pmouseY) {
        boolean mCurrentlyHit = hit(pmouseX, pmouseY);
        if (mCurrentlyHit && !mHit) {
            fireEvent(EVENT_ENTER);
        } else if (!mCurrentlyHit && mHit) {
            fireEvent(EVENT_LEAVE);
        }

        mHit = mCurrentlyHit;
    }

    private boolean hit(float mX, float mY) {
        boolean mCurrentlyHit =
                mX > (position.x - radius)
                && mX < (position.x + radius)
                && mY > (position.y - radius)
                && mY < (position.y + radius);
        return mCurrentlyHit;
    }

    public void draw(PGraphics g) {
        if (mHit) {
            g.noStroke();
            g.fill(mColor);
        } else {
            g.noFill();
            g.stroke(mColor);
        }
        g.ellipse(position.x, position.y, radius * 2, radius * 2);
    }
}
