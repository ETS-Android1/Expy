package com.xdev.expy.ui.onboarding;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xdev.expy.databinding.ItemOnboardingBinding;

import java.util.ArrayList;
import java.util.List;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.ViewHolder> {

    private final List<Onboarding> onboardingList = new ArrayList<>();

    public void submitList(List<Onboarding> onboardingList) {
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

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemOnboardingBinding binding;

        ViewHolder(@NonNull ItemOnboardingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(@NonNull Onboarding onboarding) {
            binding.tvTitle.setText(onboarding.getTitle());
            binding.tvDescription.setText(onboarding.getDescription());
            binding.imgIllustration.setImageResource(onboarding.getImageRes());
        }
    }
}