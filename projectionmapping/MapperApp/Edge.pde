class Edge{
  Node node1;
  Node node2;
 
 Edge(Node _node1, Node _node2){
  node1 = _node1;
  node2 = _node2;
 } 
 
 void draw(){
   line(node1.x, node1.y, node2.x, node2.y);
 }
}
