/**
* a simple app to map things onto 3D objects.
* saves and loads mapping data to XML file.
* inital version created by Christian Riekoff
* for a Projection Mapping Workshop @HfKBremen
* in October 2012
*/

Mapper mapper;

String MAPPING_FILE = "mapper.xml";

void setup() {
  size(1280, 720);
  frameRate(30);
  mapper = new Mapper();
  mapper.addVisual(new SineVisual(mapper));
}

void update(float deltaTime) {
  mapper.update(deltaTime);
}

void draw() {
  update(1 / frameRate);

  background(50);
  mapper.draw();

  for (Face face:mapper.faces) {
    fill(random(255));
    face.draw();
  }
}

void mouseMoved() {
  mapper.mouseMoved();
}

void mouseDragged() {
  mapper.mouseDragged();
}

void mousePressed() {
  mapper.mousePressed();
}

void mouseReleased() {
  mapper.mouseReleased();
}

void keyPressed() {
  mapper.keyPressed();
}

void keyReleased() {
  mapper.keyReleased();
}

