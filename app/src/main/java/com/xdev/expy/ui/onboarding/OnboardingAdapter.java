package com.xdev.expy.ui.onboarding;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xdev.expy.databinding.ItemOnboardingBinding;

import java.util.ArrayList;
import java.util.List;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.ViewHolder>{

    private final List<Onboarding> onboardingList = new ArrayList<>();

    public void submitList(List<Onboarding> onboardingList){
        this.onboardingList.clear();
        this.onboardingList.addAll(onboardingList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOnboardingBinding binding = ItemOnboardingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Onboarding onboarding = onboardingList.get(position);
        holder.bind(onboarding);
    }

    @Override
    public int getItemCount() {
        return onboardingList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView tvTitle;
        private final TextView tvDescription;
        private final ImageView imgIllustration;

        ViewHolder(@NonNull ItemOnboardingBinding binding) {
            super(binding.getRoot());
            tvTitle = binding.tvTitle;
            tvDescription = binding.tvDescription;
            imgIllustration = binding.imgIllustration;
        }

        void bind(@NonNull Onboarding onboarding){
            tvTitle.setText(onboarding.getTitle());
            tvDescription.setText(onboarding.getDescription());
            imgIllustration.setImageResource(onboarding.getImageRes());
        }

    }
}