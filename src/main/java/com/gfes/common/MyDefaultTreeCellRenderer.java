package com.gfes.common;

import sun.swing.DefaultLookup;

import java.awt.Component;
import java.net.URL;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * 自定义树描述类,将树的每个节点设置成不同的图标
 *
 */
@org.springframework.stereotype.Component
public class MyDefaultTreeCellRenderer extends DefaultTreeCellRenderer
{
    /**
     * ID
     */
    private static final long   serialVersionUID    = 1L;

    /**
     * 重写父类DefaultTreeCellRenderer的方法
     */
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean sel, boolean expanded, boolean leaf, int row,
                                                  boolean hasFocus)
    {

        //执行父类原型操作
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
                row, hasFocus);

        setText(value.toString());

        if (sel)
        {
            setForeground(getTextSelectionColor());
        }
        else
        {
            setForeground(getTextNonSelectionColor());
        }

        //得到每个节点的TreeNode
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

        //得到每个节点的text
        String str = node.toString();

        //判断是哪个文本的节点设置对应的值（这里如果节点传入的是一个实体,则可以根据实体里面的一个类型属性来显示对应的图标）
        URL resource = this.getClass().getResource("/image/group.png");
        Icon groupIcon = new ImageIcon(resource);
        if (node.getLevel() == 1){
            this.setIcon(groupIcon);
        }else if (node.getLevel() == 2 && node.getChildCount() == 0){
            this.setIcon(DefaultLookup.getIcon(this, ui, "Tree.closedIcon"));
        }else if (node.getLevel() == 3){
            resource = this.getClass().getResource("/image/protect_file.png");
            Icon protectFileIcon = new ImageIcon(resource);
            this.setIcon(protectFileIcon);
        }
        return this;
    }

}
