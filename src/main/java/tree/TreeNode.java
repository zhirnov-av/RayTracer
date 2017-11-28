package tree;

public class TreeNode {
    BoundingBox element;
    TreeNode left;
    TreeNode right;

    public TreeNode(BoundingBox element){
        this.element = element;
    }

    public TreeNode addLeft(BoundingBox element){
        left = new TreeNode(element);
        return left;
    }

    public TreeNode addRight(BoundingBox element){
        right = new TreeNode(element);
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
}
