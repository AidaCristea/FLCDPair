package domain.Table;

import java.util.List;

public class Table {
    List<TableRow> table;


    private void add( String info, Integer parent,Integer rightParent){
        TableRow row=new TableRow(table.size(),info,parent,rightParent);
        table.add(row);
    }


    @Override
    public String toString() {
        return "Table{" +
                "table=" + table +
                '}';
    }
}
