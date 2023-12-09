package domain.Table;

import java.util.ArrayList;
import java.util.List;

public class Table {
    List<TableRow> table;
    int currentIdx;

    public Table(){
        this.table=new ArrayList<>();
        this.currentIdx=0;
    }

    public void add( String info, Integer parent,Integer leftSibling){
        TableRow row=new TableRow(table.size(),info,parent,leftSibling);
        table.add(row);
        this.currentIdx++;
    }

    public List<TableRow> getTable() {
        return table;
    }

    public int getCurrentIdx()
    {
        return currentIdx;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("+-------+---------+--------+-------------+\n");
        result.append("| Index |  Info   | Parent | LeftSibling |\n");
        result.append("+-------+---------+--------+-------------+\n");

        for (TableRow row : table) {
            result.append(String.format("| %-5d | %-7s |", row.getIndex(), row.getInfo()));

            // Check for null values and print accordingly
            if (row.getParent() != null) {
                result.append(String.format(" %-6d |", row.getParent()));
            } else {
                result.append("        |");
            }

            if (row.getLeftSibling() != null) {
                result.append(String.format(" %-11d |", row.getLeftSibling()));
            } else {
                result.append("             |");
            }

            result.append("\n+-------+---------+--------+-------------+\n");
        }

        return result.toString();
    }
}
