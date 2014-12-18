package cashmanager.helo.com.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cashmanager.helo.com.R;
import cashmanager.helo.com.model.bd.Budget;
import cashmanager.helo.com.model.bd.Record;
import cashmanager.helo.com.view.Utils;

/**
 * Created by Oleg on 17.12.2014.
 */
public class BudgetAdapter extends ArrayAdapter<Budget> {

    private LayoutInflater mInflater;
    private List<Budget> mItemsList;

    private Context mContext;

    public BudgetAdapter(Context context, List<Budget> recordList) {
        super(context, 0, recordList);
        mContext = context;
        mItemsList = recordList;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return (mItemsList != null) ? mItemsList.size() : 0;
    }

    @Override
    public Budget getItem(int position) {
        return mItemsList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Budget item = getItem(position);
        RecordHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_record, null);
            holder = new RecordHolder();
            holder.date = (TextView) convertView.findViewById(R.id.txt_date);
            holder.category = (TextView) convertView.findViewById(R.id.txt_category);
            holder.category.setVisibility(View.GONE);
            holder.description = (TextView) convertView.findViewById(R.id.txt_description);
            holder.description.setVisibility(View.GONE);
            holder.cost = (TextView) convertView.findViewById(R.id.txt_cost);
            convertView.setTag(holder);
        } else {
            holder = (RecordHolder) convertView.getTag();
        }

        initItem(holder, item);

        return convertView;
    }

    public void deleteItem(int position) {
        mItemsList.remove(position);
    }

    private void initItem(RecordHolder holder, Budget item) {
        holder.date.setText(Utils.getDate(item.date));
        //holder.category.setText(item.category.title);
        holder.cost.setTextColor(Color.GREEN);
        holder.cost.setText(Integer.toString(Math.abs(item.value)));
    }

    public class RecordHolder {
        private TextView date;
        private TextView category;
        private TextView description;
        private TextView cost;
    }

    public void updateItemsList(List<Budget> itemsList) {
        mItemsList.clear();
        mItemsList.addAll(itemsList);
        this.notifyDataSetChanged();
    }

    public void addItem(Budget budget) {
        mItemsList.add(budget);
        this.notifyDataSetChanged();
    }
}

