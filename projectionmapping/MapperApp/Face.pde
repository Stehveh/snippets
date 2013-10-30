class Face {
  ArrayList<Node> nodes = new ArrayList<Node>();

  float angle2D(float _x1, float _y1, float _x2, float _y2) {
    float theta1 = (float)Math.atan2(_y1, _x1);
    float theta2 = (float)Math.atan2(_y2, _x2);
    float dtheta = theta2 - theta1;

    while (dtheta > PI)
      dtheta -= TWO_PI;
    while (dtheta < -PI)
      dtheta += TWO_PI;

    return dtheta;
  }

  boolean isInFace(float _x, float _y) {
    float R = 0;

    for (int i = 0; i < nodes.size(); i++) {
      float p1x = nodes.get(i).x - _x;
      float p1y = nodes.get(i).y - _y;
      float p2x = nodes.get((i + 1) % nodes.size()).x - _x;
      float p2y = nodes.get((i + 1) % nodes.size()).y - _y;

      R += angle2D(p1x, p1y, p2x, p2y);
    }

    if (abs(R) < PI)
      return false;
    else
      return true;
  }

  void draw() {
    beginShape();
    for (Node node:nodes) {
      vertex(node.x, node.y);
    }
    endShape(CLOSE);
  }
}

