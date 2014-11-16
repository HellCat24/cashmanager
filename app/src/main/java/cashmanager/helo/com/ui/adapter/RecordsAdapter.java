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
import cashmanager.helo.com.model.bd.Record;
import cashmanager.helo.com.view.Utils;

/**
 * Created by Mazhukin Oleh on 10.11.2014.
 */
public class RecordsAdapter extends ArrayAdapter<Record> {

    private LayoutInflater mInflater;
    private List<Record> mItemsList;

    private Context mContext;

    public RecordsAdapter(Context context, List<Record> recordList) {
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
    public Record getItem(int position) {
        return mItemsList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Record item = getItem(position);
        RecordHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_record, null);
            holder = new RecordHolder();
            holder.title = (TextView) convertView.findViewById(R.id.txt_title);
            holder.date = (TextView) convertView.findViewById(R.id.txt_date);
            holder.category = (TextView) convertView.findViewById(R.id.txt_category);
            holder.description = (TextView) convertView.findViewById(R.id.txt_description);
            holder.cost = (TextView) convertView.findViewById(R.id.txt_cost);
            convertView.setTag(holder);
        } else {
            holder = (RecordHolder) convertView.getTag();
        }

        initItem(holder, item);

        return convertView;
    }

    private void initItem(RecordHolder holder, Record item) {
        holder.date.setText(Utils.getDate(item.date));
        holder.title.setText(item.title);
        holder.description.setText(item.description);
        //holder.category.setText(item.category.title);
        if (item.cost > 0) {
            holder.cost.setTextColor(Color.GREEN);
        } else {
            holder.cost.setTextColor(Color.RED);
        }
        holder.cost.setText(Integer.toString(item.cost));
    }

    public class RecordHolder {
        private TextView title;
        private TextView date;
        private TextView category;
        private TextView description;
        private TextView cost;
    }

    public void updateItemsList(List<Record> itemsList) {
        mItemsList.clear();
        mItemsList.addAll(itemsList);
        this.notifyDataSetChanged();
    }
}
