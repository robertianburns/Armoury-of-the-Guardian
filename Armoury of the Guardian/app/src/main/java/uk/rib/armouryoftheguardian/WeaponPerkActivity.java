package uk.rib.armouryoftheguardian;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * This is the activity for viewing the end user's selected weapon's perk.
 *
 * @version 22.3
 * @since 22.3
 */
public class WeaponPerkActivity extends AppCompatActivity {

    ImageView weaponPerkIconView;
    LinearLayout weaponPerkStatisticDisplay;
    TextView weaponPerkDescriptionView;
    TextView weaponPerkNameView;
    TextView weaponPerkTypeView;
    TextView weaponPerkStatisticHeading;
    WeaponPerk weaponPerk;

    /**
     * This creates the activity of viewing the end user's selected weapon's perk.
     *
     * @param savedInstanceState If the weapon view activity is being re-initialized after
     *                           previously being shut down, then this Bundle contains the data it
     *                           most recently supplied in onSaveInstanceState(Bundle). If not
     *                           though, then it is null.
     * @since 22.3
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weapon_perk_view);

        weaponPerkDescriptionView = findViewById(R.id.weapon_perk_description_view);
        weaponPerkIconView = findViewById(R.id.weapon_perk_icon_view);
        weaponPerkNameView = findViewById(R.id.weapon_perk_name_view);
        weaponPerkStatisticDisplay = findViewById(R.id.weapon_perk_statistic_view);
        weaponPerkStatisticHeading = findViewById(R.id.weapon_perk_statistic_heading);
        weaponPerkTypeView = findViewById(R.id.weapon_perk_type_view);

        weaponPerk = getIntent().getParcelableExtra("weaponPerk");
        weaponPerkDescriptionView.setText(weaponPerk.getWeaponPerkDefinition().getAsJsonObject("displayProperties").get("description").getAsString());
        weaponPerkIconView.setImageBitmap(weaponPerk.getWeaponPerkIcon());
        weaponPerkNameView.setText(weaponPerk.getWeaponPerkName());
        weaponPerkTypeView.setText(weaponPerk.getWeaponPerkDefinition().get("itemTypeAndTierDisplayName").getAsString());

        JsonArray weaponPerkStatistics = weaponPerk.getWeaponPerkDefinition().getAsJsonArray("investmentStats");
        if (weaponPerkStatistics.size() > 0) {
            weaponPerkStatisticHeading.setVisibility(View.VISIBLE);
            for (JsonElement jsonElement : weaponPerkStatistics) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonObject weaponPerkStatisticDefinition = new JsonObject();
                try {
                    InputStream inputStream = getResources().openRawResource(R.raw.statistics);
                    JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                    jsonReader.beginObject();
                    while (jsonReader.hasNext()) {
                        String token = jsonReader.nextName();
                        if (token.equals(jsonObject.get("statTypeHash").toString())) {
                            weaponPerkStatisticDefinition = JsonParser.parseReader(jsonReader).getAsJsonObject();
                        } else {
                            jsonReader.skipValue();
                        }
                    }
                    jsonReader.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                String weaponPerkStatisticName = weaponPerkStatisticDefinition.getAsJsonObject("displayProperties").get("name").getAsString();
                int weaponPerkStatisticValue = jsonObject.get("value").getAsInt();
                String value;
                if (weaponPerkStatisticValue > 0) {
                    value = "+" + weaponPerkStatisticValue;
                } else {
                    value = "" + weaponPerkStatisticValue;
                }

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, marginDP(5), 0, 0);
                TextView weaponPerkStatisticTextView = new TextView(this);
                weaponPerkStatisticTextView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                weaponPerkStatisticTextView.setText("\t" + weaponPerkStatisticName + ": " + value);
                weaponPerkStatisticTextView.setTextSize(20);
                weaponPerkStatisticTextView.setTextColor(getResources().getColor(R.color.text_main));
                weaponPerkStatisticDisplay.addView(weaponPerkStatisticTextView);
            }
        }
    }

    /**
     * This creates a margin from device-independent pixels. This is a unit of length and allows
     * mobile devices to scale the display of information and user interaction to different screen
     * sizes.
     *
     * @param dp The inputted device-independent pixel amount.
     * @since 22.3
     */
    public int marginDP(int dp) {
        float density = this.getResources().getDisplayMetrics().density;
        return (int) (dp * density);
    }
}