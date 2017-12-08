package tree;

import base.BoundingBox;
import base.Plane;
import base.TraversPlane;
import base.Vector3d;

public class TreeNodeV2{

    TraversPlane element;

    TreeNodeV2 left;
    TreeNodeV2 right;
    TreeNodeV2 parent;
    int level;

    public TreeNodeV2(TreeNodeV2 parent, TraversPlane element){
        if (parent == null){
            level = 0;
        }else {
            level = parent.level + 1;
        }
        this.element = element;
        this.parent = parent;
    }

    public TreeNodeV2 addLeft(TraversPlane element){
        left = new TreeNodeV2(this, element);
        return left;
    }

    public TreeNodeV2 addRight(TraversPlane element){
        right = new TreeNodeV2(this, element);
        return right;
    }

    public TreeNodeV2 getLeft(){
        return left;
    }

    public TreeNodeV2 getRight(){
        return right;
    }

    public TraversPlane getElement() {
        return element;
    }

    public TreeNodeV2 getParent() {
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

    /*
    @Override
    public int compareTo(Object o) {
        if (o instanceof TreeNodeV2){
            TreeNodeV2 node = (TreeNodeV2)o;
            if (node.element.distanceToCamera > this.element.distanceToCamera)
                return -1;
            else
                return 1;
        }
        return 0;
    }
    */
}
