package tree;

import base.BoundingBox;

public class BinaryTree {
    TreeNode root;

    public BinaryTree(BoundingBox box){
        root = new TreeNode(null, box);
    }

    public TreeNode getRoot(){
        return root;
    }
}
