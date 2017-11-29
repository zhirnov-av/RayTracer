package tree;

public class TreeNode {
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
}
