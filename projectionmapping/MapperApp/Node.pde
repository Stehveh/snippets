int nodeCounter = 0;

class Node{
  float x;
  float y;
  
  int id;
  
  boolean isSelected;
  
  Node(float _x, float _y, int _id){
    x = _x;
    y = _y;
    id = _id;
    isSelected = false;
  }
  
  Node(float _x, float _y){
    this(_x, _y, nodeCounter++);
  }
  
  void draw(){
    ellipse(x,y,10,10);
  }
}
