package org.dominik.pass.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.dominik.pass.R;
import org.dominik.pass.models.wrappers.AllData;

import java.util.LinkedList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
  private static final String TAG = "ITEMS_ADAPTER";

  private List<AllData> allData;

  public ItemsAdapter() {
    this.allData = new LinkedList<>();
  }

  private Context context;

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    context = parent.getContext();
    LayoutInflater inflater = LayoutInflater.from(context);

    View itemsView = inflater.inflate(R.layout.item_row, parent, false);

    return new ViewHolder(itemsView);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    AllData data = allData.get(position);

    TextView textView = holder.getDataTitle();
    ImageView icon = holder.getDataIcon();

    textView.setText(data.getEntryTitle());

    switch (data.getType()) {
      case PASSWORD:
        icon.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_lock));
        break;
      case ADDRESS:
        icon.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_contact));
        break;
      case SITE:
        icon.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_website));
        break;
      case NOTE:
        icon.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_note));
    }
  }

  @Override
  public int getItemCount() {
    return allData.size();
  }

  public void replaceData(List<AllData> data) {
    allData = data;
    notifyItemRangeChanged(0, data.size());
  }

  public static final class ViewHolder extends RecyclerView.ViewHolder {
    private TextView dataTitle;
    private ImageView dataIcon;

    public ViewHolder(View itemView) {
      super(itemView);

      dataTitle = itemView.findViewById(R.id.item_title);
      dataIcon = itemView.findViewById(R.id.item_icon);
    }

    public TextView getDataTitle() {
      return dataTitle;
    }

    public void setDataTitle(TextView dataTitle) {
      this.dataTitle = dataTitle;
    }

    public ImageView getDataIcon() {
      return dataIcon;
    }

    public void setDataIcon(ImageView dataIcon) {
      this.dataIcon = dataIcon;
    }
  }
}
