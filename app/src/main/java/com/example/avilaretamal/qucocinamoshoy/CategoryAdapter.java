package com.example.avilaretamal.qucocinamoshoy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class CategoryAdapter extends BaseAdapter {
    private Context GridContext;
    private String[] CategoriesNames;

    public CategoryAdapter(Context context, String[] categorias) {
        GridContext = context;
        CategoriesNames = categorias;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) GridContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;

        if (convertView == null) {
            //gridView = new View(GridContext);
            gridView = inflater.inflate(R.layout.category_grid, null);
        } else {
            gridView = (View) convertView;
        }
        TextView textView = (TextView) gridView.findViewById(R.id.category_name);
        textView.setText(CategoriesNames[position]);
        return gridView;
    }

    @Override
    public int getCount() {
        return CategoriesNames.length;
    }
}
