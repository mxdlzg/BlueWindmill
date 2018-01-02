package android.mxdlzg.com.bluewindmill.model.entity;

/**
 * Created by 廷江 on 2018/1/2.
 */

public class SCInfo {
    private int totalItems;
    private int totalPages;
    private int itemCountPerPage;
    private int remainder;

    public SCInfo(int totalItems, int totalPages) {
        this.totalItems = totalItems;
        this.totalPages = totalPages;
        itemCountPerPage = totalItems/totalPages;
        remainder = totalItems%totalPages;
    }

    public int getRemainder() {
        return remainder;
    }

    public int getItemCountPerPage() {
        return itemCountPerPage;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
