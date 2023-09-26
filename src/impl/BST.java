package impl;

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

    public TreeNode search(int data) {
      TreeNode node = root;
      if (root == null)
        return null;
      if (root.data == data)
        return root;
      node = searchRec(root, data);
      return node;
    }

    private TreeNode searchRec(TreeNode root, int data) {
      if (root.data == data) {
        return root;
      } else if (data < root.data) {
        return searchRec(root.left, data);
      } else {
        return searchRec(root.right, data);
      }
    }

    public boolean deleteNode(int data) {
      if (root == null)
        return false;
      TreeNode found = search(data);
      if (found != null) {
        // Now we have found the node to be deleted.
        // Assumption no duplicates here.

      }
      // TODO
      return false;
    }

    public String inOrder() {
      StringBuilder sb = new StringBuilder("");
      inOrderRec(root, sb);
      return sb.toString();
    }

    private void inOrderRec(TreeNode root, StringBuilder sb) {
      if (root.left != null)
        inOrderRec(root.left, sb);
      sb.append(root.data + "->");
      if (root.right != null)
        inOrderRec(root.right, sb);
    }

  }

  public static void main(String[] args) {
    BST b = new BST();
    BinarySearchTree bst = b.new BinarySearchTree();
    bst.insert(50);
    bst.insert(30);
    bst.insert(20);
    bst.insert(40);
    bst.insert(70);
    bst.insert(60);
    bst.insert(80);
    bst.insert(10);

    System.out.println("Inorder traversal:");
    System.out.println(bst.inOrder());
  }

}