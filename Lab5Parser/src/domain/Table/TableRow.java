package domain.Table;

public class TableRow {
    private Integer index;
    private String info;
    private Integer parent;
    //sibling
    private Integer leftSibling;

    public TableRow(Integer index, String info, Integer parent, Integer leftSibling) {
        this.index = index;
        this.info = info;
        this.parent = parent;
        this.leftSibling = leftSibling;
    }

    @Override
    public String toString() {
        return "TableRow{" +
                "index=" + index +
                ", info='" + info + '\'' +
                ", parent=" + parent +
                ", leftSibling=" + leftSibling +
                '}';
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public Integer getLeftSibling() {
        return leftSibling;
    }

    public void setLeftSibling(Integer leftSibling) {
        this.leftSibling = leftSibling;
    }
}
