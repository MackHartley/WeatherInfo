package hu.ait.recylerviewdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import hu.ait.recylerviewdemo.MainActivity;
import hu.ait.recylerviewdemo.R;
import hu.ait.recylerviewdemo.data.Item;
import hu.ait.recylerviewdemo.touch.TodoTouchHelperAdapter;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;


public class TodoRecyclerAdapter
        extends RecyclerView.Adapter<TodoRecyclerAdapter.ViewHolder>
        implements TodoTouchHelperAdapter {

    private static final String itemName = "itemName";
    private List<Item> itemList;
    private Context context;
    private Realm realmTodo;

    public TodoRecyclerAdapter(Context context, Realm realmTodo) {
        this.context = context;
        this.realmTodo = realmTodo;
        RealmResults<Item> itemResult = prepItemResultVar(realmTodo);
        itemList = new ArrayList<Item>();
        populateRows(itemResult);
    }

    private void populateRows(RealmResults<Item> itemResult) {
        for (int i = 0; i < itemResult.size(); i++) {
            itemList.add(itemResult.get(i));
        }
    }

    private RealmResults<Item> prepItemResultVar(Realm realmTodo) {
        return realmTodo.where(Item.class).findAll().sort(itemName,
                Sort.ASCENDING);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.todo_row, parent, false);

        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        setUpHolder(holder, position);

        holder.cbDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realmTodo.beginTransaction();
                itemList.get(holder.getAdapterPosition()).setDone(holder.cbDone.isChecked());
                realmTodo.commitTransaction();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).openEditActivity(holder.getAdapterPosition(),
                        itemList.get(holder.getAdapterPosition()).getTodoID()
                );
            }
        });
    }

    private void setUpHolder(ViewHolder holder, int position) {
        holder.tvTodo.setText(itemList.get(position).getItemName());
        holder.cbDone.setChecked(itemList.get(position).isDone());
        holder.price.setText(Integer.toString(itemList.get(position).getPrice()));
        setRowItemWithAppropriateImage(holder, position);
    }

    private void setRowItemWithAppropriateImage(ViewHolder holder, int position) {
        String catText = itemList.get(position).getCategory().toLowerCase();
        int imgId;
        if (catText.equals(context.getString(R.string.food))) {
            imgId = R.drawable.food;
        } else if (catText.equals(context.getString(R.string.book))) {
            imgId = R.drawable.book;
        } else if (catText.equals(context.getString(R.string.ele))) {
            imgId = R.drawable.electronic;
        } else {
            imgId = R.drawable.cart;
        }
        holder.category.setImageResource(imgId);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public void onItemDismiss(int position) {
        realmTodo.beginTransaction();
        itemList.get(position).deleteFromRealm();
        realmTodo.commitTransaction();

        itemList.remove(position);
        notifyItemRemoved(position);
    }

    public void deleteAll() {
        for (int i = itemList.size() - 1; i >= 0; i--) {
            realmTodo.beginTransaction();
            itemList.get(i).deleteFromRealm();
            realmTodo.commitTransaction();
            itemList.remove(i);
            notifyItemRemoved(i);
        }
    }

    public void deleteFinished() {
        for (int i = itemList.size() - 1; i >= 0; i--) {
            Item curItem = itemList.get(i);
            if (curItem.isDone()) {
                realmTodo.beginTransaction();
                curItem.deleteFromRealm();
                realmTodo.commitTransaction();
                itemList.remove(i);
                notifyItemRemoved(i);
            }
        }
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        /*todoList.add(toPosition, todoList.get(fromPosition));
        todoList.remove(fromPosition);*/

        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(itemList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(itemList, i, i - 1);
            }
        }


        notifyItemMoved(fromPosition, toPosition);
    }

    public void addItem(String itemTitle, int price, String category, String desc) {
        realmTodo.beginTransaction();
        Item newItem = realmTodo.createObject(Item.class, UUID.randomUUID().toString());
        prepareNewItem(itemTitle, price, category, desc, newItem);

        realmTodo.commitTransaction();

        itemList.add(0, newItem);
        notifyItemInserted(0);
    }

    private void prepareNewItem(String itemTitle, int price, String category, String desc, Item newItem) {
        newItem.setItemTitle(itemTitle);
        newItem.setDone(false);
        newItem.setPrice(price);
        newItem.setCategory(category);
        newItem.setDesc(desc);
    }

    public void updateTodo(String todoID, int positionToEdit) {
        Item item = realmTodo.where(Item.class)
                .equalTo("todoID", todoID)
                .findFirst();

        itemList.set(positionToEdit, item);

        notifyItemChanged(positionToEdit);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox cbDone;
        private TextView tvTodo;
        private TextView price;
        private ImageView category;

        public ViewHolder(View itemView) {
            super(itemView);

            getTargetRowViews(itemView);
        }

        private void getTargetRowViews(View itemView) {
            cbDone = (CheckBox) itemView.findViewById(R.id.cbDone);
            tvTodo = (TextView) itemView.findViewById(R.id.tvTodo);
            price = (TextView) itemView.findViewById(R.id.itemPrice);
            category = (ImageView) itemView.findViewById(R.id.categoryImg);
        }
    }
}
