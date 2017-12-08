package tree;

import base.BoundingBox;
import base.TraversPlane;

public class BinaryTreeV2 {
    TreeNodeV2 root;

    public BinaryTreeV2(TraversPlane box){
        root = new TreeNodeV2(null, box);
    }

    public TreeNodeV2 getRoot(){
        return root;
    }
}
