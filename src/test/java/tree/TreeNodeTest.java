package tree;

import base.AbstractObject;
import base.BoundingBox;

import static org.junit.Assert.*;

public class TreeNodeTest {
    @org.junit.Test
    public void addLeft() throws Exception {
        TreeNode node = new TreeNode(null, new BoundingBox(new AbstractObject()));
        node.addLeft(new BoundingBox(new AbstractObject()));

    }

    @org.junit.Test
    public void addRight() throws Exception {
    }

    @org.junit.Test
    public void getLeft() throws Exception {
    }

    @org.junit.Test
    public void getRight() throws Exception {
    }

    @org.junit.Test
    public void getParent() throws Exception {
    }

}