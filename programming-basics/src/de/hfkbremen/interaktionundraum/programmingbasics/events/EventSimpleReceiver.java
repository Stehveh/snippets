package de.hfkbremen.interaktionundraum.programmingbasics.events;


import processing.core.PGraphics;


public class EventSimpleReceiver implements IEventListener {

    private float R;

    private float G;

    private float B;

    public void fireEvent(int pEvent) {
        if (pEvent == EventDispatcher.EVENT_LEAVE) {
            R = (float) Math.random() * 255.0f;
            G = R;
            B = R;
        } else if (pEvent == EventDispatcher.EVENT_ENTER) {
            R = (float) Math.random() * 255.0f;
            G = (float) Math.random() * 255.0f;
            B = (float) Math.random() * 255.0f;
        }
    }

    public void draw(PGraphics g) {
        g.noFill();
        g.stroke(g.color(R, G, B));
        g.ellipse(g.width / 2, g.height / 2, 100, 100);
        g.ellipse(g.width / 2, g.height / 2, 115, 115);
        g.ellipse(g.width / 2, g.height / 2, 145, 145);
    }
}
