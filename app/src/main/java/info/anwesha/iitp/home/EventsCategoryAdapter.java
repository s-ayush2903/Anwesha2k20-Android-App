package info.anwesha.iitp.home;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import info.anwesha.iitp.R;

public class EventsCategoryAdapter extends RecyclerView.Adapter<info.anwesha.iitp.home.EventsCategoryAdapter.ViewHolder> {

    private Context context;
    private List<String> categoryList;

    public EventsCategoryAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_event_category, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        if (categoryList != null) {
            final String current = categoryList.get(position);


            String show="Test";
            if(current.equals("awelfare")){
                show="Arts & Welfare";
            }else if (current.equals("cultural")){
                show="Cultural";
            }else if (current.equals("technical")){
                show="Technical";
            }
            else if (current.equals("pronite")){
                show="Pronite";
            }
            else if (current.equals("proshow")){
                show="Proshow";
            }
            else if (current.equals("pre-anwesha")){
                show="Pre-Anwesha";
            }else if (current.equals("informal")){
                show="Informal";
            }
            holder.title.setText(show);
//            Glide.with(context)
//                    .load(R.raw.gif_abc)
//                    .centerCrop()
//                    .into(holder.imageView);

            holder.rootLayout.setOnClickListener(v -> {
                Bundle b = new Bundle();
                b.putString("data", current);
                    Navigation.findNavController(v).navigate(R.id.nav_events, b);
            });


        } else {
            holder.title.setText("Loading ...");
        }
    }

    @Override
    public int getItemCount() {
        if (categoryList != null)
            return categoryList.size();
        else return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        View rootLayout;
        ImageView imageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            rootLayout = itemView.findViewById(R.id.root_event_category);
            imageView = itemView.findViewById(R.id.image);
        }
    }

    void setEventCategoryList(List<String> eventCategoryList) {
        categoryList = eventCategoryList;
        notifyDataSetChanged();
    }
}
