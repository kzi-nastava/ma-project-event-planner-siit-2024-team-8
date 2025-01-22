package com.example.myapplication.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.domain.PriceListItem;
import com.example.myapplication.services.PriceListService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PriceListAdapter extends RecyclerView.Adapter<PriceListAdapter.PriceListViewHolder> {

    private final List<PriceListItem> priceList;
    private final PriceListService priceListService;

    public PriceListAdapter(List<PriceListItem> priceList, PriceListService service) {
        this.priceList = priceList;
        this.priceListService = service;
    }

    @NonNull
    @Override
    public PriceListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_price_list, parent, false);
        return new PriceListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PriceListViewHolder holder, @SuppressLint("RecyclerView") int position) {
        PriceListItem item = priceList.get(position);

        holder.tvName.setText(item.getName());
        holder.etPrice.setText(String.valueOf(item.getPrice()));
        holder.etDiscount.setText(String.valueOf(item.getDiscount()));
        holder.tvDiscountedPrice.setText(String.format("€%.2f", item.getPrice() - (item.getPrice() * item.getDiscount() / 100)));

        holder.btnSave.setOnClickListener(v -> {
            String priceInput = holder.etPrice.getText().toString();
            String discountInput = holder.etDiscount.getText().toString();

            try {
                double newPrice = Double.parseDouble(priceInput);
                double newDiscount = Double.parseDouble(discountInput);

                if (newDiscount < 0 || newDiscount > 100) {
                    holder.etDiscount.setError("Discount must be between 0 and 100");
                    return;
                }

                priceListService.updatePriceAndDiscount(item.getAssetId(), newPrice, newDiscount, new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            item.setPrice(newPrice);
                            item.setDiscount(newDiscount);
                            notifyItemChanged(position);
                        } else {
                            Log.e("PriceListAdapter", "API Error: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("PriceListAdapter", "Error updating item", t);
                    }
                });

            } catch (NumberFormatException e) {
                if (priceInput.isEmpty()) {
                    holder.etPrice.setError("Price cannot be empty");
                } else {
                    holder.etPrice.setError("Enter a valid number");
                }

                if (discountInput.isEmpty()) {
                    holder.etDiscount.setError("Discount cannot be empty");
                } else {
                    holder.etDiscount.setError("Enter a valid number");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return priceList.size();
    }

    static class PriceListViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDiscountedPrice;
        EditText etPrice, etDiscount;
        Button btnSave;

        public PriceListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.item_name);
            etPrice = itemView.findViewById(R.id.item_price);
            etDiscount = itemView.findViewById(R.id.item_discount);
            tvDiscountedPrice = itemView.findViewById(R.id.item_discounted_price);
            btnSave = itemView.findViewById(R.id.btn_save);
        }
    }
}