package uk.rib.armouryoftheguardian;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * The weapon statistics adapter.
 *
 * @version 22.3
 * @since 22.3
 */
public class WeaponStatisticsAdapter extends RecyclerView.Adapter<WeaponStatisticsAdapter.WeaponStatisticViewHolder> {

    Context context;
    ArrayList<WeaponStatistics> weaponStatisticsList;

    public WeaponStatisticsAdapter(Context context, ArrayList<WeaponStatistics> weaponStatisticsList) {
        this.context = context;
        this.weaponStatisticsList = weaponStatisticsList;
    }

    /**
     * The 'createViewHolder(ViewGroup parent, int viewType)' method calls
     * onCreateViewHolder(ViewGroup, int) to create a new RecyclerView.ViewHolder and initialises
     * some fields to be used by RecyclerView. The ViewHolder describes the weapon statistic view
     * and metadata about its place within the RecyclerView.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to an
     *                 recyclerAdapter position.
     * @param viewType The view type of the new View.
     * @return A new weapon statistic view.
     * @since 22.3
     */
    @NonNull
    @Override
    public WeaponStatisticViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View weaponStatisticView = LayoutInflater.from(context).inflate(R.layout.weapon_statistic_adapter_layout, parent, false);
        return new WeaponStatisticViewHolder(weaponStatisticView);
    }

    /**
     * This is called by RecyclerView to display the data at the specified position. Armoury of the
     * Guardian will get new unused view holders and will fill them with statistic data to display
     * to the end user.
     * <p>
     * 'android.graphics.PorterDuff.Mode.SRC_IN' keeps the source pixels that cover the destination
     * pixels, discards the remaining source and destination pixels.
     *
     * @param weaponStatisticViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param weaponStatisticPosition   The position of the item within the recyclerAdapter's data set.
     * @since 22.3
     */
    @Override
    public void onBindViewHolder(@NonNull WeaponStatisticViewHolder weaponStatisticViewHolder, int weaponStatisticPosition) {
        weaponStatisticViewHolder.weaponStatisticName.setText(weaponStatisticsList.get(weaponStatisticPosition).toString());
        if (weaponStatisticsList.get(weaponStatisticPosition).getWeaponStatisticCategories()) {
            weaponStatisticViewHolder.weaponStatisticBar.setVisibility(View.VISIBLE);
            weaponStatisticViewHolder.weaponStatisticBar.setProgress(weaponStatisticsList.get(weaponStatisticPosition).getWeaponStatisticValue());
            weaponStatisticViewHolder.weaponStatisticBar.getProgressDrawable().setColorFilter(context.getResources().getColor(R.color.text_main), android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            weaponStatisticViewHolder.weaponStatisticBar.setVisibility(View.GONE);
            weaponStatisticViewHolder.weaponStatisticName.setText(weaponStatisticsList.get(weaponStatisticPosition).toString());
            try {
                if (weaponStatisticsList.get(weaponStatisticPosition).weaponStatisticName().equals("Recoil Direction")) {
                    int weaponStatisticPositionValue = weaponStatisticsList.get(weaponStatisticPosition).getWeaponStatisticValue();
                    double weaponRecoil = Math.sin((weaponStatisticPositionValue + 5) * 2 * Math.PI / 20) * (100 - weaponStatisticPositionValue);
                    if (Math.abs(weaponRecoil) < 1) {
                        weaponRecoil = 0;
                    }
                    if (weaponRecoil > 0) {
                        weaponStatisticViewHolder.weaponStatisticName.setText(String.format("%s (goes right)", weaponStatisticsList.get(weaponStatisticPosition).toString()));
                    } else if (weaponRecoil < 0) {
                        weaponStatisticViewHolder.weaponStatisticName.setText(String.format("%s  (goes left)", weaponStatisticsList.get(weaponStatisticPosition).toString()));
                    } else {
                        weaponStatisticViewHolder.weaponStatisticName.setText(String.format("%s  (stays vertical)", weaponStatisticsList.get(weaponStatisticPosition).toString()));
                    }
                }
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }
    }

    /**
     * Returns the total number of items in the data set held by the recyclerAdapter.
     *
     * @return The total number of items in the data set held by the recyclerAdapter.
     * @since 22.3
     */
    @Override
    public int getItemCount() {
        return weaponStatisticsList.size();
    }

    /**
     * Set some views of the WeaponStatisticViewHolder.
     *
     * @since 22.3
     */
    public static class WeaponStatisticViewHolder extends RecyclerView.ViewHolder {
        TextView weaponStatisticName;
        ProgressBar weaponStatisticBar;
        LinearLayout weaponStatisticLayout;

        /**
         * @param weaponStatisticView The end user's selected weapon's statistic's view.
         * @since 22.3
         */
        public WeaponStatisticViewHolder(@NonNull View weaponStatisticView) {
            super(weaponStatisticView);
            weaponStatisticName = weaponStatisticView.findViewById(R.id.weapon_statistic_name);
            weaponStatisticBar = weaponStatisticView.findViewById(R.id.weapon_statistic_bar);
            weaponStatisticLayout = weaponStatisticView.findViewById(R.id.weapon_statistic_layout);
        }
    }
}
