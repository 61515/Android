package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by 宋健 on 2018/7/25.
 */

///循环布局的适配器 单布局view  parent
public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder>
{
    private List<Fruit>mFruitList;
    static class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView fruitImage;
        TextView fruitName;
        View fruitView;
        public ViewHolder(View view)
        {
            super(view);
            fruitView=view;
            fruitImage=(ImageView)view.findViewById(R.id.fruit_image);
            fruitName=view.findViewById(R.id.fruit_name);
        }
    }
    public FruitAdapter(List<Fruit>fruitList)
    {
        mFruitList=fruitList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType)
    {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.fruit_item,
                parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.fruitView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int position=holder.getAdapterPosition();
                Fruit fruit=mFruitList.get(position);
                switch(fruit.getName())
                {
                    case "Apple":
                        Intent intent=new Intent(MyApplication.getContext(),CalculateActivity.class);
                        MyApplication.getContext().startActivity(intent);
                        break;
                    default:
                        Toast.makeText(v.getContext(),"you clicked view "+
                                fruit.getName(),Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position)
    {
        Fruit fruit=mFruitList.get(position);
        holder.fruitImage.setImageResource(fruit.getImageId());
        holder.fruitName.setText(fruit.getName());
    }
    @Override
    public int getItemCount()
    {
        return mFruitList.size();
    }
}

