package de.hfkbremen.interaktionundraum.programmingbasics.events;


import processing.core.PGraphics;
import processing.core.PVector;


public class EventReceiver implements IEventListener {

    public float radius;

    public PVector position = new PVector();

    private float R;

    private float G;

    private float B;

    public void fireEvent(int pEvent) {
        if (pEvent == EventDispatcher.EVENT_ENTER) {
            R = (float) Math.random() * 255.0f;
            G = R;
            B = R;
        } else if (pEvent == EventDispatcher.EVENT_LEAVE) {
            R = (float) Math.random() * 255.0f;
            G = (float) Math.random() * 255.0f;
            B = (float) Math.random() * 255.0f;
        }
    }

    public void draw(PGraphics g) {
        g.noStroke();
        g.fill(g.color(R, G, B));
        g.ellipse(position.x, position.y, radius * 2, radius * 2);
    }
}
