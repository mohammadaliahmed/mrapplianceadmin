package com.appsinventiv.mrapplianceadmin.Activities.Orders;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.appsinventiv.mrapplianceadmin.Models.ServiceCountModel;
import com.appsinventiv.mrapplianceadmin.R;
import com.appsinventiv.mrapplianceadmin.Services.SubServiceModel;
import com.appsinventiv.mrapplianceadmin.Utils.CommonUtils;

import java.util.ArrayList;


public class ModifyOrderAdapter extends RecyclerView.Adapter<ModifyOrderAdapter.ViewHolder> {
    Context context;
    ArrayList<SubServiceModel> itemlist;

    AddToCartCallbacks addToCartInterface;
    ArrayList<ServiceCountModel> userCartServiceList;

    public ModifyOrderAdapter(Context context, ArrayList<SubServiceModel> itemlist,
                              ArrayList<ServiceCountModel> userCartServiceList,
                              AddToCartCallbacks addToCartInterface) {
        this.context = context;
        this.itemlist = itemlist;
        this.addToCartInterface = addToCartInterface;
        this.userCartServiceList = userCartServiceList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.service_modify_layout, viewGroup, false);
        ModifyOrderAdapter.ViewHolder viewHolder = new ModifyOrderAdapter.ViewHolder(view);

        return viewHolder;
    }
    public void setUserCartList(ArrayList<ServiceCountModel> models){
        this.userCartServiceList=models;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final SubServiceModel model = itemlist.get(position);
        holder.title.setText(model.getName());
        final int[] count = {0};
        ServiceCountModel serviceCountModel = null;
        boolean flag = false;


        if (userCartServiceList.size() > 0) {
            for (int i = 0; i < userCartServiceList.size(); i++) {
                if (model.getName() != null && model.getName().equals(userCartServiceList.get(i).getService().getName())) {
                    flag = true;
                    serviceCountModel = userCartServiceList.get(i);
                }

            }
        } else {

        }
        if (flag) {
//            holder.relativeLayout.setBackgroundResource(R.drawable.add_to_cart_bg_transparent);
            holder.count.setTextColor(context.getResources().getColor(R.color.colorBlack));

            count[0] = serviceCountModel.getQuantity();
            holder.count.setText("" + count[0]);
            holder.increase.setVisibility(View.VISIBLE);

            if (count[0] > 1) {
                holder.decrease.setImageResource(R.drawable.ic_decrease_btn);
                holder.decrease.setVisibility(View.VISIBLE);
//                addToCartInterface.addedToCart(model, count[0],position);
            } else {
//                addToCartInterface.addedToCart(model, count[0],position);

                holder.decrease.setImageResource(R.drawable.ic_decrease_btn);
                holder.decrease.setVisibility(View.VISIBLE);
            }
        } else

        {

//            holder.relativeLayout.setBackgroundResource(R.drawable.add_to_cart_bg_transparent);
            holder.count.setTextColor(context.getResources().getColor(R.color.colorBlack));
            holder.count.setText("0");
            holder.increase.setVisibility(View.VISIBLE);
            holder.decrease.setVisibility(View.VISIBLE);

        }

        flag = false;


        holder.increase.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                    if (count[0] <= model.getMax()) {
                        if (count[0] >= 1) {
                            count[0] += 1;
                            holder.count.setText("" + count[0]);
                            holder.decrease.setImageResource(R.drawable.ic_decrease_btn);
                            addToCartInterface.quantityUpdate(model, count[0],position);
                        } else {
//                            holder.relativeLayout.setBackgroundResource(R.drawable.add_to_cart_bg_transparent);
                            holder.count.setTextColor(context.getResources().getColor(R.color.colorBlack));
                            count[0] = 1;
                            holder.count.setText("" + count[0]);
                            holder.increase.setVisibility(View.VISIBLE);
                            holder.decrease.setVisibility(View.VISIBLE);

                            addToCartInterface.addedToCart(model, count[0],position);
                        }
                    } else {
                        CommonUtils.showToast("Max limit reached");
                    }



            }
        });
        holder.decrease.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                    if (CommonUtils.isNetworkConnected()) {
                        if (count[0] > 2) {
                            count[0] -= 1;
                            holder.count.setText("" + count[0]);
                            addToCartInterface.quantityUpdate(model, count[0],position);


                        } else if (count[0] > 1) {
                            {
                                count[0] -= 1;
                                holder.count.setText("" + count[0]);
                                holder.decrease.setImageResource(R.drawable.ic_decrease_btn);
                                addToCartInterface.quantityUpdate(model, count[0],position);


                            }
                        } else if (count[0] == 1) {
//                        holder.relativeLayout.setBackgroundResource(R.drawable.add_to_cart_bg_transparent);
                            holder.count.setTextColor(context.getResources().getColor(R.color.colorBlack));

                            holder.count.setText("0");
                            holder.increase.setVisibility(View.VISIBLE);
                            holder.decrease.setVisibility(View.VISIBLE);
                            count[0]=0;
                            addToCartInterface.deletedFromCart(model,position);

                        }
                    } else {
                        CommonUtils.showToast("Please connect to internet");
                    }

            }
        });

//        holder.itemView.setOnClickListener(new View.OnClickListener()
//
//        {
//            @Override
//            public void onClick(View view) {
//                if (CommonUtils.isNetworkConnected()) {
//                    Intent i = new Intent(context, ViewProduct.class);
//                    i.putExtra("productId", model.getCategory() + "/" + model.getId());
//                    context.startActivity(i);
//                } else {
//                    CommonUtils.showToast("Please connect to internet");
//                }
//
//            }
//        });


        holder.itemView.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return itemlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, price, count;
        RelativeLayout relativeLayout;
        ImageView increase, decrease;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            price = itemView.findViewById(R.id.price);
            increase = itemView.findViewById(R.id.increase);
            decrease = itemView.findViewById(R.id.decrease);
            count = itemView.findViewById(R.id.count);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);


        }
    }



}
