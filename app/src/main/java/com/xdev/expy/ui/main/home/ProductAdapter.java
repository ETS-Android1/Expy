package com.xdev.expy.ui.main.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.xdev.expy.R;
import com.xdev.expy.data.source.remote.entity.ProductEntity;
import com.xdev.expy.databinding.ItemProductBinding;

import java.util.ArrayList;
import java.util.List;

import static com.xdev.expy.utils.DateUtils.differenceOfDates;
import static com.xdev.expy.utils.DateUtils.getCurrentDate;
import static com.xdev.expy.utils.DateUtils.getFormattedDate;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private final List<ProductEntity> productList = new ArrayList<>();
    private final ProductAdapterClickListener listener;

    public ProductAdapter(ProductAdapterClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<ProductEntity> productList) {
        this.productList.clear();
        this.productList.addAll(productList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        ProductEntity product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemProductBinding binding;

        public ViewHolder(@NonNull ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ProductEntity product) {
            String expiryDate = product.getExpiryDate();
            int countdown = differenceOfDates(expiryDate, getCurrentDate());

            binding.tvName.setText(product.getName());
            binding.tvExpiryDate.setText(getFormattedDate(expiryDate, true));
            binding.tvCountdown.setText(countdown + " hari");

            int background;
            int color;
            if (countdown <= 3) {
                background = R.drawable.bg_countdown_red;
                color = R.color.red;
            } else if (countdown <= 7) {
                background = R.drawable.bg_countdown_orange;
                color = R.color.orange;
            } else if (countdown <= 30) {
                background = R.drawable.bg_countdown_yellow;
                color = R.color.yellow;
            } else {
                background = R.drawable.bg_countdown_green;
                color = R.color.green_primary;
            }
            binding.layoutCountdown.setBackgroundResource(background);
            binding.imgIcon.setColorFilter(ContextCompat.getColor(itemView.getContext(), color));

            itemView.setOnClickListener(view -> listener.onProductClicked(product));
        }
    }

    interface ProductAdapterClickListener {
        void onProductClicked(ProductEntity product);
    }
}
