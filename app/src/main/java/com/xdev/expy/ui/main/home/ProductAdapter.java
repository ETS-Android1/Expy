package com.xdev.expy.ui.main.home;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.xdev.expy.R;
import com.xdev.expy.data.source.local.entity.ProductEntity;
import com.xdev.expy.data.source.local.entity.ProductWithReminders;
import com.xdev.expy.databinding.ItemProductBinding;

import static com.xdev.expy.utils.DateUtils.differenceOfDates;
import static com.xdev.expy.utils.DateUtils.getCurrentDate;
import static com.xdev.expy.utils.DateUtils.getFormattedDate;

public class ProductAdapter extends PagedListAdapter<ProductWithReminders, ProductAdapter.ViewHolder> {

    private final ProductAdapterClickListener listener;

    public ProductAdapter(ProductAdapterClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<ProductWithReminders> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<ProductWithReminders>() {
                @Override
                public boolean areItemsTheSame(@NonNull ProductWithReminders oldItem, @NonNull ProductWithReminders newItem) {
                    return oldItem.getProduct().getId().equals(newItem.getProduct().getId());
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(@NonNull ProductWithReminders oldItem, @NonNull ProductWithReminders newItem) {
                    return oldItem.equals(newItem);
                }
            };

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        ProductWithReminders product = getItem(position);
        if (product != null) holder.bind(product);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemProductBinding binding;

        public ViewHolder(@NonNull ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ProductWithReminders product) {
            String expiryDate = product.getProduct().getExpiryDate();
            long dte = differenceOfDates(expiryDate, getCurrentDate());
            if (dte < 0) dte = 0;

            binding.tvName.setText(product.getProduct().getName());
            binding.tvExpiryDate.setText(getFormattedDate(expiryDate, true));
            binding.tvCountdown.setText(dte + " hari");

            int background;
            int color;
            if (dte <= 3) {
                background = R.drawable.bg_countdown_red;
                color = R.color.red;
            } else if (dte <= 7) {
                background = R.drawable.bg_countdown_orange;
                color = R.color.orange;
            } else if (dte <= 30) {
                background = R.drawable.bg_countdown_yellow;
                color = R.color.yellow;
            } else {
                background = R.drawable.bg_countdown_green;
                color = R.color.green_primary;
            }
            binding.layoutCountdown.setBackgroundResource(background);
            binding.imgIcon.setColorFilter(ContextCompat.getColor(itemView.getContext(), color));

            itemView.setOnClickListener(view -> {
                ProductEntity productIntent = product.getProduct();
                productIntent.setReminders(product.getReminders());
                listener.onProductClicked(productIntent);
            });
        }
    }

    interface ProductAdapterClickListener {
        void onProductClicked(ProductEntity product);
    }
}
