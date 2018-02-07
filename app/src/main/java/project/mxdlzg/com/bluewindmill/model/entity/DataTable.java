package project.mxdlzg.com.bluewindmill.model.entity;

import com.lzy.okgo.db.ColumnEntity;
import com.lzy.okgo.db.TableEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 廷江 on 2017/12/24.
 */

public class DataTable<T> extends TableEntity{
    private List<T> rowList = null;

    /**
     * Cons
     * @param rowList rowList
     */
    public DataTable(List<T> rowList) {
        super("Default");
        this.rowList = rowList;
    }

    /**
     * @param tableName tableName
     * @param list columnHeaderList
     */
    public DataTable(String tableName, List<ColumnEntity> list){
        super(tableName);
        addColumnHeader(list);
        initList();
    }

    /**
     * T ,add row to Table
     * @param row row
     */
    public void addRow(T row){
        if (rowList == null){
            initList();
        }
        rowList.add(row);
    }

    /**
     * List<T>, add rows to Table
     * @param rowList rowList
     */
    public void addRow(List<T> rowList){
        if (this.rowList == null){
            initList();
        }
        this.rowList.addAll(rowList);
    }

    /**
     * init
     */
    private void initList(){
        this.rowList = new ArrayList<>();
    }

    /**
     * header list,But it can not match with rows properlly
     * @param list header list
     */
    public void addColumnHeader(List<ColumnEntity> list){
        for (ColumnEntity ce :
                list) {
            super.addColumn(ce);
        }
    }

    public List<T> getRowList() {
        return rowList;
    }

    public void setRowList(List<T> rowList) {
        this.rowList = rowList;
    }
}
