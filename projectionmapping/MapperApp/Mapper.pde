class Mapper {
  ArrayList<Node> nodes = new ArrayList<Node>();
  ArrayList<Face> faces = new ArrayList<Face>();
  ArrayList<Visual> visuals = new ArrayList<Visual>();

  Node selectedNode;
  Face activeFace;

  boolean showEditor = false;
  EditorVisual editorVisual;

  Mapper() {
    addVisual(editorVisual = new EditorVisual(this));
    editorVisual.alpha = 0;
  }

  void addVisual(Visual _visual) {
    visuals.add(_visual);
  }

  void mouseMoved() {
    selectedNode = null;
    for (Node myNode:nodes) {
      myNode.isSelected = false;

      if (dist(myNode.x, myNode.y, mouseX, mouseY) < 10) {
        myNode.isSelected = true;
        selectedNode = myNode;
        return;
      }
    }
  }

  boolean hasDragged = false;

  void mouseDragged() {
    hasDragged = true;
    if (selectedNode == null)return;

    selectedNode.x = mouseX;
    selectedNode.y = mouseY;
  }

  void mousePressed() {
    hasDragged = false;
  }

  boolean addExistingNodes = false;

  void keyPressed() {
    switch(key) {
    case 'F':
    case 'f':
      activeFace = new Face();
      faces.add(activeFace);
      break;
    case 'A':
    case 'a':
      addExistingNodes = true;
      break;
    }
  }

  void keyReleased() {
    switch(key) {
    case 'A':
    case 'a':
      addExistingNodes = false;
      break;
    case 's':
    case 'S':
      save();
      break;
    case 'l':
    case 'L':
      load();
      break;
    case 'e':
    case 'E':
      showEditor = !showEditor;
      /*
      if(showEditor){
       editorVisual.alpha = 1.0;
       }else{
       editorVisual.alpha = 0.0;
       }*/
      editorVisual.alpha = showEditor ? 1.0 : 0.0;
      break;
    }
  }

  void load() {
    faces.clear();
    nodes.clear();
    //    try {
    //      XML xml = new XML(MapperApp.this, MAPPING_FILE);
    XML xml = loadXML(MAPPING_FILE);
    //      xml.trim();
    XML nodesXML = xml.getChild("nodes");
    println(nodesXML);
    println("--");
    println(nodesXML.getChildren().length);
    HashMap<Integer, Node> nodeMap = new HashMap<Integer, Node>();
    for (XML nodeXML:nodesXML.getChildren()) {
      if (nodeXML.getName().equals("#text")) { // @TODO : this i quite stoopid and caused by whitespaces. should remove this right after loading the XML
        continue;
      }
      println(nodeXML);
      int id = nodeXML.getInt("id");
      if (id >= nodeCounter) {
        nodeCounter = id + 1;
      }
      float x = nodeXML.getFloat("x");
      float y = nodeXML.getFloat("y");
      Node node = new Node(x, y, id);
      nodeMap.put(id, node);
      nodes.add(node);
    }
    XML facesXML = xml.getChild("faces");
    for (XML faceXML:facesXML.getChildren()) {
      if (faceXML.getName().equals("#text")) {
        continue;
      }
      Face face = new Face();
      faces.add(face);
      for (XML nodeIDXML:faceXML.getChildren()) {
        if (nodeIDXML.getName().equals("#text")) {
          continue;
        }

        int id = nodeIDXML.getInt("id");
        Node node = nodeMap.get(id);
        face.nodes.add(node);
      }
    }
    //    }
    //    catch(IOException e) {
    //      e.printStackTrace();
    //    }
    //    catch(ParserConfigurationException e) {
    //      e.printStackTrace();
    //    }
    //    catch(SAXException e) {
    //      e.printStackTrace();
    //    }
    //    for (Visual visual:visuals) {
    //      visual.onLoad();
    //    }
  }

  void save() {
    XML xml = new XML("mapper");
    XML nodesXML = new XML("nodes");
    for (Node node:nodes) {
      XML nodeXML = new XML("node");
      nodeXML.setInt("id", node.id);
      nodeXML.setFloat("x", node.x);
      nodeXML.setFloat("y", node.y);
      nodesXML.addChild(nodeXML);
    }
    xml.addChild(nodesXML );

    XML facesXML = new XML("faces");
    for (Face face:faces) {
      if (face.nodes.size() == 0)continue;
      XML faceXML = new XML("face");
      for (Node node:face.nodes) {
        XML nodeIDXML = new XML("nodeID");
        nodeIDXML.setInt("id", node.id);
        faceXML.addChild(nodeIDXML);
      }
      facesXML.addChild(faceXML);
    }
    xml.addChild(facesXML);
    //    xml.save(createOutput("data/"+MAPPING_FILE));
    saveXML(xml, "data/"+MAPPING_FILE);
  }

  void mouseReleased() {
    if (activeFace == null) {
      activeFace = new Face();
      faces.add(activeFace);
    }

    if (selectedNode == null) {
      selectedNode = new Node(mouseX, mouseY);
      nodes.add(selectedNode);
      activeFace.nodes.add(selectedNode);
    }
    else {
      if (!hasDragged) {
        if (addExistingNodes) {
          activeFace.nodes.add(selectedNode);
        }
        else {
          nodes.remove(selectedNode);
          for (Face face:faces) {
            face.nodes.remove(selectedNode);
          }
        }
      }
    }
  }

  void update(float deltaTime) {
    for (Visual visual:visuals) {
      visual.update(deltaTime);
    }
  }

  void draw() {
    for (Visual visual:visuals) {
      visual.draw();
    }
  }
}

