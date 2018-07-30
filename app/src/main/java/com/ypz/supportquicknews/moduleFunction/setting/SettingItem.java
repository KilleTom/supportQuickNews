package com.ypz.supportquicknews.moduleFunction.setting;

/**
 * Created by kingadmin on 2018/4/8.
 */

public class SettingItem {

    private String message;

    private int drawable;

    private ItemClickToDo itemClickToDo;

    public SettingItem(String message, int drawable, ItemClickToDo itemClickToDo) {
        this.message = message;
        this.drawable = drawable;
        this.itemClickToDo = itemClickToDo;
    }

    public String getMessage() {
        return message;
    }

    public int getDrawable() {
        return drawable;
    }

    public ItemClickToDo getItemClickToDo() {
        return itemClickToDo;
    }

    public interface ItemClickToDo{

        void clickDoSomeThing();
    }

}
