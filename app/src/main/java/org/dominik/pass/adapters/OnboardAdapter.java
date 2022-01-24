package org.dominik.pass.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.dominik.pass.R;
import org.dominik.pass.models.OnboardItem;
import org.dominik.pass.utils.StringUtils;

import java.util.List;

public final class OnboardAdapter extends RecyclerView.Adapter<OnboardAdapter.OnboardViewHolder> {
  private Context ctx;
  private final List<OnboardItem> items;

  public OnboardAdapter(List<OnboardItem> items) {
    this.items = items;
  }

  @NonNull
  @Override
  public OnboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new OnboardViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.onboard_item_container, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull OnboardViewHolder holder, int position) {
    holder.setOnboardData(items.get(position));
  }

  @Override
  public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);
    ctx = recyclerView.getContext();
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  final class OnboardViewHolder extends RecyclerView.ViewHolder {
    private final TextView onboardTitle;
    private final TextView onboardMessage;
    private final ImageView onboardImage;

    public OnboardViewHolder(@NonNull View view) {
      super(view);
      onboardTitle = view.findViewById(R.id.onboard_title);
      onboardMessage = view.findViewById(R.id.onboard_message);
      onboardImage = view.findViewById(R.id.onboard_image);
    }

    void setOnboardData(OnboardItem item) {
      onboardTitle.setText(StringUtils.getInstance().prepareAppName(ctx, item.getTitle()));
      onboardMessage.setText(item.getMessage());
      onboardImage.setImageResource(item.getImageId());
    }
  }
}
