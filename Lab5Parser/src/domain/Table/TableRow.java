package domain.Table;

public class TableRow {
    private Integer index;
    private String info;
    private Integer parent;
    private Integer rightParent;

    public TableRow(Integer index, String info, Integer parent, Integer rightParent) {
        this.index = index;
        this.info = info;
        this.parent = parent;
        this.rightParent = rightParent;
    }

    @Override
    public String toString() {
        return "TableRow{" +
                "index=" + index +
                ", info='" + info + '\'' +
                ", parent=" + parent +
                ", rightParent=" + rightParent +
                '}';
    }
}
