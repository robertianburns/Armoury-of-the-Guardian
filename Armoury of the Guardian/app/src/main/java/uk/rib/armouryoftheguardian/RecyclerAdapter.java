package uk.rib.armouryoftheguardian;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * This is a base class for an Adapter. Adapters provide a binding from an app-specific data set to
 * views that are displayed within a RecyclerView.
 * .
 *
 * @version 22.3
 * @since 22.3
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    private static final float BLUR_RADIUS = 25f;
    Context context;
    ArrayList<Weapon> list;

    public RecyclerAdapter(Context context, ArrayList<Weapon> list) {
        this.context = context;
        this.list = list;
    }

    /**
     * This method calls onCreateViewHolder to create a new RecyclerView.ViewHolder and initialises
     * a view to be used by RecyclerView.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to an
     *                 recyclerAdapter position.
     * @param viewType The view type of the new View.
     * @return A new RecyclerView.ViewHolder with a view to be used by RecyclerView.
     * @since 22.3
     */
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weapon_search_adapter_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    /**
     * This method internally calls onBindViewHolder to update the RecyclerView.ViewHolder contents
     * with the item at the given position (int) while also setting up fields to be used by
     * RecyclerView (weapon name and image).
     *
     * @param recyclerViewHolder The ViewHolder which should be updated to represent the contents of
     *                           the item at the given position in the data set.
     * @param position           The position of the item within the recyclerAdapter's data set.
     * @since 22.3
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int position) {
        recyclerViewHolder.weaponName.setText(list.get(position).weaponName());
        recyclerViewHolder.weaponImage.setImageBitmap(list.get(position).getWeaponIcon());
        recyclerViewHolder.weaponImageSmall.setImageBitmap(list.get(position).getWeaponIcon());

        recyclerViewHolder.holderWeaponLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, WeaponViewActivity.class);
            intent.putExtra("weapon", list.get(position));
            context.startActivity(intent);
        });
    }

    /**
     * This method returns the total number of items in the data set held by the recyclerAdapter.
     *
     * @return The total number of items in the data set held by the recyclerAdapter.
     * @since 22.3
     */
    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * This extends RecyclerView.ViewHolder, with a ViewHolder describing an item view and metadata
     * about its place within the RecyclerView.
     * <p>
     * RecyclerView.Adapter implementations subclass the ViewHolder and adds fields for caching
     * possibly expensive View.findViewById results (the weapon name, image, and layout).
     * Implementations assume that individual item views will hold strong references to ViewHolder
     * objects and that RecyclerView instances may hold strong references to extra off-screen item
     * views for caching purposes.
     *
     * @since 22.3
     */
    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView weaponName;
        ImageView weaponImage;
        ImageView weaponImageSmall;
        LinearLayout holderWeaponLayout;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            weaponName = itemView.findViewById(R.id.holder_weapon_name);
            weaponImage = itemView.findViewById(R.id.holder_weapon_image);
            weaponImage.setColorFilter(Color.rgb(130, 130, 130), android.graphics.PorterDuff.Mode.MULTIPLY); // Darken the back image.
            weaponImageSmall = itemView.findViewById(R.id.holder_weapon_image_small);
            holderWeaponLayout = itemView.findViewById(R.id.holder_weapon_layout);
        }
    }

}
