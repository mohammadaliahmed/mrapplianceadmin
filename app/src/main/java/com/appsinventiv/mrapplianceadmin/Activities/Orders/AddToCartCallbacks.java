package com.appsinventiv.mrapplianceadmin.Activities.Orders;

import com.appsinventiv.mrapplianceadmin.Services.SubServiceModel;

public interface AddToCartCallbacks {
    public void addedToCart(SubServiceModel services, int quantity,int position);
    public void deletedFromCart(SubServiceModel services,int position);
    public void quantityUpdate(SubServiceModel services, int quantity,int position);

}
