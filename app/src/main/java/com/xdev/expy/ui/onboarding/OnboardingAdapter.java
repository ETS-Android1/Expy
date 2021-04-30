package com.xdev.expy.ui.onboarding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xdev.expy.R;
import com.xdev.expy.data.Onboarding;

import java.util.List;

public class OnboardingAdapter  extends RecyclerView.Adapter<OnboardingAdapter.ViewHolder>{

    private List<Onboarding> intros;

    public OnboardingAdapter(List<Onboarding> intros) {
        this.intros = intros;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_onboarding, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIntroData(intros.get(position));
    }

    @Override
    public int getItemCount() {
        return intros.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textTitle;
        private TextView textDescription;
        private ImageView imageIntro;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDescription = itemView.findViewById(R.id.textDescription);
            imageIntro = itemView.findViewById(R.id.imageIntro);
        }

        void setIntroData(Onboarding intro){
            textTitle.setText(intro.getTitle());
            textDescription.setText(intro.getDescription());
            imageIntro.setImageResource(intro.getImage());
        }

    }
}