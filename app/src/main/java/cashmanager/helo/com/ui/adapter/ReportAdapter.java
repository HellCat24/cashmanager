package cashmanager.helo.com.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cashmanager.helo.com.R;
import cashmanager.helo.com.model.ReportItem;
import cashmanager.helo.com.model.bd.Record;
import cashmanager.helo.com.view.DateTimePicker;
import cashmanager.helo.com.view.Utils;

/**
 * Created by Oleg on 16.12.2014.
 */
public class ReportAdapter extends ArrayAdapter<ReportItem> {

    private LayoutInflater mInflater;
    private List<ReportItem> mItemsList;

    private Context mContext;

    public ReportAdapter(Context context, List<ReportItem> recordList) {
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
    public ReportItem getItem(int position) {
        return mItemsList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ReportItem item = getItem(position);
        RecordHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_report, null);
            holder = new RecordHolder();
            holder.color = (ImageView) convertView.findViewById(R.id.img_color);
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

    private void initItem(RecordHolder holder, ReportItem item) {
        holder.date.setText(Utils.getDate(item.date));
        if(item.description.length()>0){
            holder.description.setText("Description:"+" "+item.description);
        } else {
            holder.description.setText("Description: empty");
        }
        if(item.categoryTitle!=null&&item.categoryTitle.length()>0){
            holder.category.setText(item.categoryTitle);
        } else {
            holder.category.setText("-");
        }
        if (item.cost > 0) {
            holder.cost.setTextColor(Color.GREEN);
        } else {
            holder.cost.setTextColor(Color.RED);
        }
        holder.color.setBackgroundColor(item.color);
        holder.cost.setText(Integer.toString(item.cost));
    }

    public class RecordHolder {
        private ImageView color;
        private TextView date;
        private TextView category;
        private TextView description;
        private TextView cost;
    }

    public void updateItemsList(List<ReportItem> itemsList) {
        mItemsList.clear();
        mItemsList.addAll(itemsList);
        this.notifyDataSetChanged();
    }


}

