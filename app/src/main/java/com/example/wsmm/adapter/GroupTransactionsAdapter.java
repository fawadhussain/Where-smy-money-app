package com.example.wsmm.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wsmm.R;
import com.example.wsmm.model.Category;
import com.example.wsmm.util.GeneralUtils;
import com.example.wsmm.util.SPManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by abubaker on 08/05/2016.
 */
public class GroupTransactionsAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<String> listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, ArrayList<Category>> listDataChild;


    public GroupTransactionsAdapter(Context context,ArrayList<String> listDataHeader,
                                    HashMap<String, ArrayList<Category>> listChildData){
        this.mContext = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupPosition;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = listDataHeader.get(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.header_layout, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.header);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Category object = (Category) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.custom_row, null);
        }

        TextView title = (TextView)convertView.findViewById(R.id.row_title);
        TextView  price = (TextView)convertView.findViewById(R.id.text_price);
        ImageView icon = (ImageView)convertView.findViewById(R.id.category_icon);


        title.setText(object.getCategoryTitle());
        //  holder.price.setText(expenseObject.getPrice() + " $");

        if (SPManager.getCurrency(mContext)!= -1){
            price.setText(object.getPrice() +" "+ GeneralUtils.getCurrencySymbol(mContext,SPManager.getCurrency
                    (mContext)));
        }else {
            price.setText("$ " + object.getPrice());
        }

        if (object.getCategoryName()!=null){
            int id = mContext.getResources().getIdentifier(object.getCategoryName().replaceAll("\\s+", "").toLowerCase(), "drawable", mContext.getPackageName());
            icon.setImageResource(id);

        }

        return convertView;
    }




    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public void removeGroup(int group) {
        //TODO: Remove the according group. Dont forget to remove the children aswell!
        Log.v("Adapter", "Removing group"+group);
        notifyDataSetChanged();
    }

    public void removeChild(int group, int child) {
        //TODO: Remove the according child
        Log.v("Adapter", "Removing child "+child+" in group "+group);
        notifyDataSetChanged();
    }
}
