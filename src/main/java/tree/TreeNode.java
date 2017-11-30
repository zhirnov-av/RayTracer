package tree;

import base.BoundingBox;

public class TreeNode implements Comparable{
    BoundingBox element;
    TreeNode left;
    TreeNode right;
    TreeNode parent;
    int level;

    public TreeNode(TreeNode parent, BoundingBox element){
        if (parent == null){
            level = 0;
        }else {
            level = parent.level + 1;
        }
        this.element = element;
        this.parent = parent;
    }

    public TreeNode addLeft(BoundingBox element){
        left = new TreeNode(this, element);
        return left;
    }

    public TreeNode addRight(BoundingBox element){
        right = new TreeNode(this, element);
        return right;
    }

    public TreeNode getLeft(){
        return left;
    }

    public TreeNode getRight(){
        return right;
    }

    public BoundingBox getElement() {
        return element;
    }

    public TreeNode getParent() {
        return parent;
    }

    public boolean isHasChild(){
        if (left != null || right != null){
            return true;
        }
        return false;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof TreeNode){
            TreeNode node = (TreeNode)o;
            if (node.element.distanceToCamera > this.element.distanceToCamera)
                return -1;
            else
                return 1;
        }
        return 0;
    }
}
