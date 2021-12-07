package com.gfes.common;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.util.Vector;

public class CustomizedTable  extends JTable {


    public CustomizedTable(){
        super(null,null,null);
    }
    public CustomizedTable(TableModel dm) {
        super(dm, null, null);
    }

    public CustomizedTable(TableModel dm, TableColumnModel cm) {
        super(dm, cm, null);
    }

    public CustomizedTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
        super(dm, cm, sm);
    }

    public CustomizedTable(Vector rowData, Vector columnNames) {
        this(new DefaultTableModel(rowData, columnNames));
    }


    @Override
    public void columnMarginChanged(ChangeEvent e) {
    }


}
