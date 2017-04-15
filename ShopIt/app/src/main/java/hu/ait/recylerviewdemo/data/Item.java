package hu.ait.recylerviewdemo.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Peter on 2017. 03. 20..
 */

public class Item extends RealmObject {

    @PrimaryKey
    private String todoID;

    private String itemName;
    private boolean done;
    private int price;
    private String category;
    private String desc;


    public Item() {
    }

    public Item(String itemName, boolean done, int price, String category, String desc) {
        this.itemName = itemName;
        this.done = done;
        this.price = price;
        this.category = category;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemTitle(String todoText) {
        this.itemName = todoText;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getTodoID() {
        return todoID;
    }
}
