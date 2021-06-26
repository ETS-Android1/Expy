package com.xdev.expy.ui.main.home;

import android.annotation.SuppressLint;
import android.content.Context;
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

        private final Context context;
        private final ItemProductBinding binding;

        public ViewHolder(@NonNull ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = itemView.getContext();
        }

        public void bind(@NonNull ProductWithReminders productWithReminders) {
            ProductEntity product = productWithReminders.getProduct();
            product.setReminders(productWithReminders.getReminders());

            int dte = (int) differenceOfDates(product.getExpiryDate(), getCurrentDate());
            if (dte < 0) dte = 0;

            setText(product, dte);
            setColoring(product, dte);
            setClickListener(product);
        }

        private void setText(ProductEntity product, int dte) {
            binding.tvName.setText(product.getName());
            binding.tvExpiryDate.setText(getFormattedDate(product.getExpiryDate(), true));
            binding.tvCountdown.setText(context.getResources().getQuantityString(
                    R.plurals.number_of_days_remaining_countdown, dte, dte));
        }

        private void setColoring(ProductEntity product, int dte) {
            if (!product.getIsFinished() || (product.getIsFinished() && dte <= 0) ) {
                int countdownBg;
                int countdownColor;
                if (dte <= 3) {
                    countdownBg = R.drawable.bg_countdown_red;
                    countdownColor = R.color.red;
                } else if (dte <= 7) {
                    countdownBg = R.drawable.bg_countdown_orange;
                    countdownColor = R.color.orange;
                } else if (dte <= 30) {
                    countdownBg = R.drawable.bg_countdown_yellow;
                    countdownColor = R.color.yellow;
                } else {
                    countdownBg = R.drawable.bg_countdown_green;
                    countdownColor = R.color.green_primary;
                }
                int black = ContextCompat.getColor(context, R.color.black);
                int darkGray = ContextCompat.getColor(context, R.color.gray_dark);
                int white = ContextCompat.getColor(context, R.color.white);
                binding.tvName.setTextColor(black);
                binding.tvExpiryDate.setTextColor(darkGray);
                binding.imgIcon.setColorFilter(ContextCompat.getColor(context, countdownColor));
                binding.layoutCountdown.setBackgroundResource(countdownBg);
                binding.tvCountdown.setTextColor(white);
            } else if (product.isFinished && dte > 0){
                int gray = ContextCompat.getColor(context, R.color.gray);
                int lightGray = ContextCompat.getColor(context, R.color.gray_light);
                int countdownBg = R.drawable.bg_countdown_gray;
                binding.tvName.setTextColor(gray);
                binding.tvExpiryDate.setTextColor(gray);
                binding.imgIcon.setColorFilter(gray);
                binding.layoutCountdown.setBackgroundResource(countdownBg);
                binding.tvCountdown.setTextColor(lightGray);
            }
        }

        private void setClickListener(ProductEntity product) {
            itemView.setOnClickListener(view -> listener.onProductClicked(product));
        }
    }

    interface ProductAdapterClickListener {
        void onProductClicked(ProductEntity product);
    }
}
