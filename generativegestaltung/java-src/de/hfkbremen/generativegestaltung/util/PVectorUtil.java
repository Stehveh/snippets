package de.hfkbremen.generativegestaltung.util;


import processing.core.PVector;


public class PVectorUtil {

    public static void mult(PVector p, PVector s) {
        p.x *= s.x;
        p.y *= s.y;
        p.z *= s.z;
    }

    public static void div(PVector p, PVector s) {
        p.x /= s.x;
        p.y /= s.y;
        p.z /= s.z;
    }
}
