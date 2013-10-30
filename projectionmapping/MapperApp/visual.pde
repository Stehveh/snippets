class Visual{
  Mapper mapper;
  float alpha;
  
  Visual(Mapper _mapper){
    mapper = _mapper;
    alpha = 1.0;
  }
  
  void onLoad(){}
  
  void update(float _deltaTime){}
  
  void draw(){}
}

class EditorVisual extends Visual{
  
  EditorVisual(Mapper _mapper){
    super(_mapper);
  }
  
  void draw(){
    if(alpha == 0)return;
    
    noStroke();
    
    stroke(255,100 * alpha);
    fill(255,50 * alpha);
    for (Face face:mapper.faces) {
      face.draw();
    }
    for (Node myNode:mapper.nodes) {
      if (myNode.isSelected)fill(255, 0, 0);
      else fill(255);
      myNode.draw();
    }
  }
}

class SineFace{
  Face face;
  float frequency;
  float fill;
  float angle;
  
  SineFace(Face _face){
    face = _face;
    frequency = random(1,5);
    angle = 0;
  }
  
  void update(float deltaTime){
    angle += deltaTime * frequency;
    fill = map(sin(angle), -1, 1, 0, 255);
  }
  
  void draw(){
    fill(fill);
    face.draw();
  }
}

class SineVisual extends Visual{
  ArrayList<SineFace> faces = new ArrayList<SineFace>();
  
  SineVisual(Mapper _mapper){
    super(_mapper);
  }
  
  void onLoad(){
    faces.clear();
    
    for(Face face:mapper.faces){
      faces.add(new SineFace(face));
    }
  }
  
  void update(float deltaTime){
    for(SineFace face:faces){
      face.update(deltaTime);
    }
  }
  
  void draw(){
    for(SineFace face:faces){
      face.draw();
    }
  }
}
