package java;

class BST {
  class TreeNode {
    int data;
    TreeNode left;
    TreeNode right;

    public TreeNode(int data) {
      this.data = data;
      this.left = null;
      this.right = null;
    }
  }

  class BinarySearchTree {
    TreeNode root;

    public void insert(int data) {
      root = insertRec(root, data);
    }

    private TreeNode insertRec(TreeNode root, int data) {
      if (root == null) {
        root = new TreeNode(data);
        return root;
      }

      if (data < root.data) {
        root.left = insertRec(root.left, data);
      } else {
        root.right = insertRec(root.right, data);
      }
      return root;
    }

  }

}