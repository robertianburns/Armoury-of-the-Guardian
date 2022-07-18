package uk.rib.armouryoftheguardian;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * This is the logic for the end user's selected weapon's inspection screen.
 *
 * @version 22.3
 * @since 22.3
 */
public class WeaponViewActivity extends AppCompatActivity {

    /* Misc */
    Context context = this;
    ProgressBar weapon_progressbar;
    RecyclerView statRecycler;
    ScrollView scrollView;
    WeaponStatisticsAdapter weaponStatisticsAdapter;

    ImageView weaponScreenshot;
    TextView weaponChampionModsType;
    TextView weaponElement;
    TextView weaponFamily;
    TextView weaponFlavourText;
    TextView weaponName;
    TextView weaponSlot;
    Weapon weapon;
    JsonObject weaponDefinition;

    /* Perk variables */
    ImageView intrinsicWeaponPerkIcon;
    LinearLayout curatedWeaponPerkGridLayout;
    LinearLayout intrinsicWeaponPerkDisplay;
    LinearLayout perkSocketLayout;
    LinearLayout randomWeaponPerkGridLayout;
    TextView curatedWeaponPerkHeader;
    TextView intrinsicWeaponPerkDescription;
    TextView intrinsicWeaponPerkName;
    TextView randomWeaponPerkHeader;

    /**
     * This creates the end user's selected weapon's inspection screen.
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
        setContentView(R.layout.activity_weapon_view);

        scrollView = findViewById(R.id.home_scrollview);
        scrollView.setVisibility(View.GONE);
        weapon_progressbar = findViewById(R.id.weapon_progressbar);
        weapon_progressbar.setVisibility(View.GONE);

        weaponChampionModsType = findViewById(R.id.weapon_mods_champion_type);
        weaponElement = findViewById(R.id.weapon_element);
        weaponFamily = findViewById(R.id.weapon_family);
        weaponFlavourText = findViewById(R.id.weapon_text_flavour);
        weaponName = findViewById(R.id.weapon_text_name);
        weaponScreenshot = findViewById(R.id.weapon_image_screenshot);
        weaponSlot = findViewById(R.id.weapon_slot);

        weapon = getIntent().getParcelableExtra("weapon");
        weaponDefinition = weapon.getWeaponDefinition();

        new LoadWeaponTask().execute(weaponDefinition);

        weaponName.setText(weapon.weaponName());
        weaponFamily.setText(weapon.weaponType());
        weaponFlavourText.setText(weapon.weaponFlavourText());

        curatedWeaponPerkGridLayout = findViewById(R.id.weapon_perk_curated_gridlayout);
        curatedWeaponPerkHeader = findViewById(R.id.weapon_perk_curated_header);
        intrinsicWeaponPerkDescription = findViewById(R.id.weapon_perk_intrinsic_description);
        intrinsicWeaponPerkDisplay = findViewById(R.id.weapon_perk_intrinsic_display);
        intrinsicWeaponPerkIcon = findViewById(R.id.weapon_perk_intrinsic_icon);
        intrinsicWeaponPerkName = findViewById(R.id.weapon_perk_intrinsic_name);
        perkSocketLayout = findViewById(R.id.weapon_perk_socketlayout);
        randomWeaponPerkGridLayout = findViewById(R.id.weapon_perk_random_gridlayout);
        randomWeaponPerkHeader = findViewById(R.id.weapon_perk_random_header);

        statRecycler = findViewById(R.id.weapon_statistic_recycler);
    }

    /**
     * This is loads all the stuff for the end user's selected weapon's inspection screen.
     *
     * <ol>
     *      <li>First, the end user's selected weapon's screenshot is loaded.</li>
     *      <li>Second, do the end user's selected weapon's's damage types, what ammo it uses, and if it uses Champion mods.</li>
     *      <li>Third, do the end user's selected weapon's's statistics.</li>
     * </ol>
     *
     * @param list An array list for the end user's selected weapon's inspection screen.
     * @since 22.3
     */
    public void loadWeaponInspect(ArrayList<Object> list) {

        /* First, get the end user's selected weapon's screenshot */
        weapon.setWeaponScreenshot((Bitmap) list.get(0));
        weaponScreenshot.setImageBitmap(weapon.getWeaponScreenshot());

        /* Second, get the end user's selected weapon's damage types, what ammo it uses, and if it uses Champion mods. */
        int weaponDamageType = weaponDefinition.get("defaultDamageType").getAsInt();
        switch (weaponDamageType) {
            case 1:
//              Kinetic
                Drawable kineticDrawable = ContextCompat.getDrawable(this, R.drawable.kinetic);
                Objects.requireNonNull(kineticDrawable).setBounds(0, 0, 85, 85);
                weaponElement.setCompoundDrawables(kineticDrawable, null, null, null);
                String kineticDescription = " This does KINETIC damage!";
                SpannableString kineticSpannableString = new SpannableString(kineticDescription);
                ForegroundColorSpan kineticForegroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.text_weapon_kinetic));
                StyleSpan kineticStyleSpan = new StyleSpan(Typeface.BOLD);
                kineticSpannableString.setSpan(kineticStyleSpan, 11, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                kineticSpannableString.setSpan(kineticForegroundColorSpan, 11, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                weaponElement.setText(kineticSpannableString);
                break;
            case 2:
//              Arc
                Drawable arcDrawable = ContextCompat.getDrawable(this, R.drawable.arc);
                Objects.requireNonNull(arcDrawable).setBounds(0, 0, 85, 85);
                weaponElement.setCompoundDrawables(arcDrawable, null, null, null);
                String arcDescription = " This does ARC damage!";
                SpannableString arcSpannableString = new SpannableString(arcDescription);
                ForegroundColorSpan arcForegroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.text_weapon_arc));
                StyleSpan arcStyleSpan = new StyleSpan(Typeface.BOLD);
                arcSpannableString.setSpan(arcStyleSpan, 11, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                arcSpannableString.setSpan(arcForegroundColorSpan, 11, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                weaponElement.setText(arcSpannableString);
                break;
            case 3:
//              Solar
                Drawable solarDrawable = ContextCompat.getDrawable(this, R.drawable.solar);
                Objects.requireNonNull(solarDrawable).setBounds(0, 0, 85, 85);
                weaponElement.setCompoundDrawables(solarDrawable, null, null, null);
                String solarDescription = " This does SOLAR damage!";
                SpannableString solarSpannableString = new SpannableString(solarDescription);
                ForegroundColorSpan solarForegroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.text_weapon_solar));
                StyleSpan solarStyleSpan = new StyleSpan(Typeface.BOLD);
                solarSpannableString.setSpan(solarStyleSpan, 11, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                solarSpannableString.setSpan(solarForegroundColorSpan, 11, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                weaponElement.setText(solarSpannableString);
                break;
            case 4:
//              Void
                Drawable voidDrawable = ContextCompat.getDrawable(this, R.drawable.void_);
                Objects.requireNonNull(voidDrawable).setBounds(0, 0, 85, 85);
                weaponElement.setCompoundDrawables(voidDrawable, null, null, null);
                String voidDescription = " This does VOID damage!";
                SpannableString voidSpannableString = new SpannableString(voidDescription);
                ForegroundColorSpan voidForegroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.text_weapon_void));
                StyleSpan voidStyleSpan = new StyleSpan(Typeface.BOLD);
                voidSpannableString.setSpan(voidStyleSpan, 11, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                voidSpannableString.setSpan(voidForegroundColorSpan, 11, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                weaponElement.setText(voidSpannableString);
                break;
            case 6:
//              Stasis
                Drawable stasisDrawable = ContextCompat.getDrawable(this, R.drawable.stasis);
                Objects.requireNonNull(stasisDrawable).setBounds(0, 0, 85, 85);
                weaponElement.setCompoundDrawables(stasisDrawable, null, null, null);
                String stasisDescription = " This does STASIS damage!";
                SpannableString stasisSpannableString = new SpannableString(stasisDescription);
                ForegroundColorSpan stasisForegroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.text_weapon_stasis));
                StyleSpan stasisStyleSpan = new StyleSpan(Typeface.BOLD);
                stasisSpannableString.setSpan(stasisStyleSpan, 11, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                stasisSpannableString.setSpan(stasisForegroundColorSpan, 11, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                weaponElement.setText(stasisSpannableString);
                break;
        }

        int weaponSlotType = weaponDefinition.getAsJsonObject("equippingBlock").get("ammoType").getAsInt();
        switch (weaponSlotType) {
            case 1:
//              Primary
                Drawable primaryDrawable = ContextCompat.getDrawable(this, R.drawable.primary);
                Objects.requireNonNull(primaryDrawable).setBounds(0, 0, 90, 60);
                weaponSlot.setCompoundDrawables(primaryDrawable, null, null, null);
                String primaryDescription = " This uses PRIMARY ammo!";
                SpannableString primarySpannableString = new SpannableString(primaryDescription);
                ForegroundColorSpan primaryForegroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.text_weapon_primary));
                StyleSpan primaryStyleSpan = new StyleSpan(Typeface.BOLD);
                primarySpannableString.setSpan(primaryStyleSpan, 11, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                primarySpannableString.setSpan(primaryForegroundColorSpan, 11, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                weaponSlot.setText(primarySpannableString);
                break;
            case 2:
//              Special
                Drawable specialDrawable = ContextCompat.getDrawable(this, R.drawable.special);
                Objects.requireNonNull(specialDrawable).setBounds(0, 0, 90, 60);
                weaponSlot.setCompoundDrawables(specialDrawable, null, null, null);
                String specialDescription = " This uses SPECIAL ammo!";
                SpannableString specialSpannableString = new SpannableString(specialDescription);
                ForegroundColorSpan specialForegroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.text_weapon_special));
                StyleSpan specialStyleSpan = new StyleSpan(Typeface.BOLD);
                specialSpannableString.setSpan(specialStyleSpan, 11, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                specialSpannableString.setSpan(specialForegroundColorSpan, 11, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                weaponSlot.setText(specialSpannableString);
                break;
            case 3:
//              Heavy
                Drawable heavyDrawable = ContextCompat.getDrawable(this, R.drawable.heavy);
                Objects.requireNonNull(heavyDrawable).setBounds(0, 0, 90, 60);
                weaponSlot.setCompoundDrawables(heavyDrawable, null, null, null);
                String heavyDescription = " This uses HEAVY ammo!";
                SpannableString heavySpannableString = new SpannableString(heavyDescription);
                ForegroundColorSpan heavyForegroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.text_weapon_heavy));
                StyleSpan heavyStyleSpan = new StyleSpan(Typeface.BOLD);
                heavySpannableString.setSpan(heavyStyleSpan, 11, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                heavySpannableString.setSpan(heavyForegroundColorSpan, 11, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                weaponSlot.setText(heavySpannableString);
                break;
        }

        int weaponChampionModType = weaponDefinition.get("breakerType").getAsInt();
        switch (weaponChampionModType) {
            case 1:
//              Barrier Champions
                weaponChampionModsType.setVisibility(View.VISIBLE);
                Drawable barrierDrawable = ContextCompat.getDrawable(this, R.drawable.barrier);
                Objects.requireNonNull(barrierDrawable).setBounds(0, 0, 85, 85);
                weaponChampionModsType.setCompoundDrawables(barrierDrawable, null, null, null);
                String barrierDescription = " This breaks BARRIER shields!";
                SpannableString barrierSpannableString = new SpannableString(barrierDescription);
                ForegroundColorSpan barrierForegroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.text_main));
                StyleSpan barrierStyleSpan = new StyleSpan(Typeface.BOLD);
                barrierSpannableString.setSpan(barrierStyleSpan, 12, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                barrierSpannableString.setSpan(barrierForegroundColorSpan, 12, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                weaponChampionModsType.setText(barrierSpannableString);
                break;
            case 2:
//              Overload Champions
                weaponChampionModsType.setVisibility(View.VISIBLE);
                Drawable overloadDrawable = ContextCompat.getDrawable(this, R.drawable.overload);
                Objects.requireNonNull(overloadDrawable).setBounds(0, 0, 85, 85);
                weaponChampionModsType.setCompoundDrawables(overloadDrawable, null, null, null);
                String overloadDescription = " This disrupts OVERLOAD enemies!";
                SpannableString overloadSpannableString = new SpannableString(overloadDescription);
                ForegroundColorSpan overloadForegroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.text_main));
                StyleSpan overloadStyleSpan = new StyleSpan(Typeface.BOLD);
                overloadSpannableString.setSpan(overloadStyleSpan, 12, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                overloadSpannableString.setSpan(overloadForegroundColorSpan, 20, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                weaponChampionModsType.setText(overloadSpannableString);
                break;
            case 3:
//              Unstoppable Champions
                weaponChampionModsType.setVisibility(View.VISIBLE);
                Drawable unstoppableDrawable = ContextCompat.getDrawable(this, R.drawable.unstoppable);
                Objects.requireNonNull(unstoppableDrawable).setBounds(0, 0, 85, 85);
                weaponChampionModsType.setCompoundDrawables(unstoppableDrawable, null, null, null);
                String unstoppableDescription = " This disrupts OVERLOAD enemies!";
                SpannableString unstoppableSpannableString = new SpannableString(unstoppableDescription);
                ForegroundColorSpan unstoppableForegroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.text_main));
                StyleSpan unstoppableStyleSpan = new StyleSpan(Typeface.BOLD);
                unstoppableSpannableString.setSpan(unstoppableStyleSpan, 12, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                unstoppableSpannableString.setSpan(unstoppableForegroundColorSpan, 20, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                weaponChampionModsType.setText(unstoppableSpannableString);
                break;
        }

        /* Third, get the end user's selected weapon's statistics. */
        weapon.setWeaponStatistics((ArrayList<WeaponStatistics>) list.get(1));
        weaponStatisticsAdapter = new WeaponStatisticsAdapter(this, weapon.getWeaponStatistics());
        statRecycler.setAdapter(weaponStatisticsAdapter);
        statRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        weaponStatisticsAdapter.notifyDataSetChanged();
        weapon.setPerkSocketList((ArrayList<WeaponPerkSocket>) list.get(2));

        for (WeaponPerk archetype : weapon.getPerkSocketList().get(0).getCuratedWeaponPerkList()) {
            intrinsicWeaponPerkIcon.setImageBitmap(archetype.getWeaponPerkIcon());
            intrinsicWeaponPerkName.setText(archetype.getWeaponPerkName());
            intrinsicWeaponPerkDescription.setText(archetype.getWeaponPerkDescription());
            intrinsicWeaponPerkDisplay.setOnClickListener(view -> {
                Intent intent = new Intent(context, WeaponPerkActivity.class);
                intent.putExtra("weaponPerk", archetype);
                context.startActivity(intent);
            });
        }

//      If the end user's selected weapon has perks...
        if (weapon.getPerkSocketList().size() > 1) {
            perkSocketLayout.setVisibility(View.VISIBLE);
            for (int i = 1; i < weapon.getPerkSocketList().size(); i++) {
                curatedWeaponPerkHeader.setVisibility(View.VISIBLE);
                curatedWeaponPerkGridLayout.setVisibility(View.VISIBLE);
                WeaponPerkSocket perkColumn = weapon.getPerkSocketList().get(i);
                ArrayList<WeaponPerk> curatedWeaponPerkList = perkColumn.getCuratedWeaponPerkList();
                LinearLayout curatedWeaponPerkColumnLayout = new LinearLayout(this);
                curatedWeaponPerkColumnLayout.setOrientation(LinearLayout.VERTICAL);
//              LayoutParams is used to tell its parent (LinearLayout) how it wants to be laid out.
                LinearLayout.LayoutParams curatedWeaponPerkParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                curatedWeaponPerkParams.setMargins(pixelMargin(5), pixelMargin(5), pixelMargin(5), pixelMargin(5));
                curatedWeaponPerkColumnLayout.setLayoutParams(curatedWeaponPerkParams);
                for (WeaponPerk curatedWeaponPerk : curatedWeaponPerkList) {
                    ImageView curatedWeaponPerkIcon = new ImageView(this);
                    curatedWeaponPerkIcon.setLayoutParams(new LinearLayout.LayoutParams(pixelMargin(70), pixelMargin(70)));
                    if (curatedWeaponPerk.getWeaponPerkIcon() == null) {
                        String curatedWeaponPerkUrl = "https://bungie.net" + curatedWeaponPerk.getWeaponPerkDefinition().getAsJsonObject("displayProperties").get("icon").getAsString();
                        Bitmap curatedWeaponPerkImage = null;
                        try {
                            curatedWeaponPerkImage = new DownloadWeaponImageTask().execute(curatedWeaponPerkUrl).get();
                        } catch (InterruptedException | ExecutionException exception) {
                            exception.printStackTrace();
                        }
                        curatedWeaponPerk.setWeaponPerkIcon(curatedWeaponPerkImage);
                    } else {
                        curatedWeaponPerkIcon.setImageBitmap(curatedWeaponPerk.getWeaponPerkIcon());
                    }
                    curatedWeaponPerkIcon.setOnClickListener(view -> {
                        Intent intent = new Intent(context, WeaponPerkActivity.class);
                        intent.putExtra("weaponPerk", curatedWeaponPerk);
                        context.startActivity(intent);
                    });
                    curatedWeaponPerkColumnLayout.addView(curatedWeaponPerkIcon);
                }
                curatedWeaponPerkGridLayout.addView(curatedWeaponPerkColumnLayout);

//              If the end user's selected weapon has random perks...
                if (perkColumn.getRandomWeaponPerkList() != null) {
                    randomWeaponPerkHeader.setVisibility(View.VISIBLE);
                    randomWeaponPerkGridLayout.setVisibility(View.VISIBLE);
                    ArrayList<WeaponPerk> randomWeaponPerkList = perkColumn.getRandomWeaponPerkList();
                    LinearLayout randomWeaponPerkColumnLayout = new LinearLayout(this);
                    randomWeaponPerkColumnLayout.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams randomWeaponPerkParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    randomWeaponPerkParams.setMargins(pixelMargin(5), pixelMargin(5), pixelMargin(5), pixelMargin(5));
                    randomWeaponPerkColumnLayout.setLayoutParams(randomWeaponPerkParams);
//                  ...and for every random weapon perk in the randomWeaponPerkList...
                    for (WeaponPerk randomWeaponPerk : randomWeaponPerkList) {
                        ImageView randomWeaponPerkIcon = new ImageView(this);
                        randomWeaponPerkIcon.setLayoutParams(new LinearLayout.LayoutParams(pixelMargin(70), pixelMargin(70)));
                        if (randomWeaponPerk.getWeaponPerkIcon() == null) {
                            String randomWeaponPerkIconUrl = "https://bungie.net" + randomWeaponPerk.getWeaponPerkDefinition().getAsJsonObject("displayProperties").get("icon").getAsString();
                            Bitmap randomWeaponPerkIconImage = null;
                            try {
                                randomWeaponPerkIconImage = new DownloadWeaponImageTask().execute(randomWeaponPerkIconUrl).get();
                            } catch (ExecutionException | InterruptedException exception) {
                                exception.printStackTrace();
                            }
                            randomWeaponPerk.setWeaponPerkIcon(randomWeaponPerkIconImage);
                        } else {
                            randomWeaponPerkIcon.setImageBitmap(randomWeaponPerk.getWeaponPerkIcon());
                        }
                        randomWeaponPerkIcon.setOnClickListener(view -> {
                            Intent intent = new Intent(context, WeaponPerkActivity.class);
                            intent.putExtra("weaponPerk", randomWeaponPerk);
                            context.startActivity(intent);
                        });
                        randomWeaponPerkColumnLayout.addView(randomWeaponPerkIcon);
                    }
//                  ...add the perk to the column layout...
                    randomWeaponPerkGridLayout.addView(randomWeaponPerkColumnLayout);
                }
            }
        }
        weapon_progressbar.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);
    }

    /**
     * A custom margin.
     *
     * @param margin The given integer for the margin.
     * @return A margin.
     * @since 22.3
     */
    public int pixelMargin(int margin) {
        float density = this.getResources().getDisplayMetrics().density;
        return (int) (margin * density);
    }

    /**
     * This is when the end user presses on a weapon from the selection list and it loads the weapon
     * information.
     *
     * @since 22.3
     */
    public class LoadWeaponTask extends AsyncTask<JsonObject, Void, ArrayList<Object>> {

        /**
         * onPreExecute() is invoked on the UI thread before the task is executed. This is used to
         * show a progress bar in the user interface.
         *
         * @since 22.3
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weapon_progressbar.setVisibility(View.VISIBLE);
        }

        /**
         * doInBackground is invoked on the background thread immediately after onPreExecute()
         * finishes executing. This is used to perform background computation that can take a long
         * time, such as retrieving the weapon screenshot, icons, statistics, and perks. The
         * parameters of the asynchronous task are passed to this step. The result of the computation
         * must be returned by this step and will be passed back to the last step.
         * <p>
         * Some weapon statistics, such as '1885944937', are intended for internal use by Bungie.
         * These should match some weapon statistics like power and attack, but are modified during
         * runtime by scripts and game-side logic that those aside from Bungie can't see and can't
         * access. This is why these are filtered out.
         *
         * @param params The parameters of the task.
         * @return The end user's selected weapon's details.
         * @since 22.3
         */
        @Override
        protected ArrayList<Object> doInBackground(JsonObject... params) {
            ArrayList<Object> weaponView = new ArrayList<>();
            JsonObject selectedWeapon = params[0];

            if (selectedWeapon.has("screenshot")) {
                String weaponScreenshotUrl = "https://www.bungie.net" + selectedWeapon.get("screenshot").getAsString();
                Bitmap weaponScreenshot = null;
                try {
                    InputStream inputStream = new URL(weaponScreenshotUrl).openStream();
                    weaponScreenshot = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                if (selectedWeapon.has("secondaryIcon")) {
                    String secondaryIconUrl = "https://www.bungie.net" + selectedWeapon.get("secondaryIcon").getAsString();
                    Bitmap secondaryIcon = null;
                    try {
                        InputStream inputStream = new URL(secondaryIconUrl).openStream();
                        secondaryIcon = BitmapFactory.decodeStream(inputStream);
                        inputStream.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }

                    if (weaponScreenshot != null) {
                        weaponScreenshot = weaponScreenshot.copy(weaponScreenshot.getConfig(), true);
                        Canvas canvas = new Canvas(weaponScreenshot);
                        if (secondaryIcon != null) {
                            canvas.drawBitmap(secondaryIcon, 0, 0, null);
                        }
                    }
                }
                weaponScreenshot = Bitmap.createScaledBitmap(weaponScreenshot, 1280, 720, false);
                weaponView.add(weaponScreenshot);

                ArrayList<WeaponStatistics> weaponStatisticList = new ArrayList<>();
                JsonObject weaponStatistics = selectedWeapon.getAsJsonObject("stats").getAsJsonObject("stats");
                Set<String> weaponStatisticsKeySet = weaponStatistics.keySet();
                for (String weaponStatisticKey : weaponStatisticsKeySet) {
/*                  Some weapon statistics, such as '1885944937', are intended for internal use by
                    Bungie. These should match some weapon statistics like power and attack, but are
                    modified during runtime by scripts and game-side logic that those aside from
                    Bungie can't see and can't access. This is why these are filtered out.        */
                    if (!(weaponStatisticKey.equals("hasDisplayableStats") || weaponStatisticKey.equals("primaryBaseStatHash") || weaponStatisticKey.equals("1935470627") ||
                            weaponStatisticKey.equals("1885944937") || weaponStatisticKey.equals("3784226438") || weaponStatisticKey.equals("1480404414") ||
                            weaponStatisticKey.equals("3597844532") || weaponStatisticKey.equals("3988418950") || weaponStatisticKey.equals("3907551967") ||
                            weaponStatisticKey.equals("3291498656") || weaponStatisticKey.equals("3291498659") || weaponStatisticKey.equals("3291498658") ||
                            weaponStatisticKey.equals("3291498661") || weaponStatisticKey.equals("3409715177") || weaponStatisticKey.equals("953546184") ||
                            weaponStatisticKey.equals("2299076437") || weaponStatisticKey.equals("3123546339"))) {
                        JsonObject weaponStatistic = weaponStatistics.getAsJsonObject(weaponStatisticKey);
                        String weaponStatisticHash = weaponStatistic.get("statHash").getAsString();
                        int weaponStatisticValue = weaponStatistic.get("value").getAsInt();
//                      If the weapon statistic actually has a weaponStatisticValue (so an Auto Rifle doesn't get
//                      the velocity statistic from a Rocket Launcher)...
                        if (weaponStatisticValue > 0) {
                            JsonObject weaponStatisticDefinition = new JsonObject();
                            try {
                                InputStream inputStream = getResources().openRawResource(R.raw.statistics);
                                JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                                jsonReader.beginObject();
//                              ...and while the jsonReader has weapon statistics...
                                while (jsonReader.hasNext()) {
                                    String token = jsonReader.nextName();
//                                  ...and token is equal to the weapon statistic weaponStatisticHash then get the
//                                     weapon statistic weaponStatisticHash.
                                    if (token.equals(weaponStatisticHash)) {
                                        weaponStatisticDefinition = JsonParser.parseReader(jsonReader).getAsJsonObject();
//                                  ...or skip it.
                                    } else {
                                        jsonReader.skipValue();
                                    }
                                }
                                jsonReader.close();
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                            weaponStatisticList.add(new WeaponStatistics(weaponStatisticHash, weaponStatisticDefinition, weaponStatisticValue));
                        }
                    }
                }
                Collections.sort(weaponStatisticList);
                weaponView.add(weaponStatisticList);
            }

//          Perk sockets
            ArrayList<WeaponPerkSocket> weaponWeaponPerkSocketList = new ArrayList<>();
            JsonArray weaponPerkSocketEntries = selectedWeapon.getAsJsonObject("sockets").getAsJsonArray("socketEntries");
//          For 'i' being less than the amount of perk sockets, get perks.
            for (int i = 0; i < weaponPerkSocketEntries.size(); i++) {
                String weaponPerkSocketTypeHash = weaponPerkSocketEntries.get(i).getAsJsonObject().get("socketTypeHash").getAsString();
                JsonObject weaponPerkSocketTypeDefinition;
                ArrayList<WeaponPerk> curatedWeaponPerkList = new ArrayList<>();
                ArrayList<WeaponPerk> randomWeaponPerkList = null;
//              If the perk socket weaponStatisticHash isn't 0, then get perk socket descriptions.
                if (!weaponPerkSocketTypeHash.equals("0")) {
                    weaponPerkSocketTypeDefinition = new JsonObject();
                    try {
                        InputStream inputStream = getResources().openRawResource(R.raw.sockets);
                        JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                        jsonReader.beginObject();
                        while (jsonReader.hasNext()) {
                            String token = jsonReader.nextName();
                            if (token.equals(weaponPerkSocketTypeHash)) {
                                weaponPerkSocketTypeDefinition = JsonParser.parseReader(jsonReader).getAsJsonObject();
                            } else {
                                jsonReader.skipValue();
                            }
                        }
                        jsonReader.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }

//                  Just making sure certain sockets work
                    if (weaponPerkSocketTypeDefinition.get("socketCategoryHash").getAsString().equals("4241085061") ||
                            weaponPerkSocketTypeDefinition.get("socketCategoryHash").getAsString().equals("3956125808")) {
//                      If the previous 'i' is less than 5, then check if weaponPerkSocketEntries has plugs.
                        if (i < 5) {
/*                          Reusable plugs can be shared across multiple sockets. For example, with
                            the Emote Wheel, every wheel socket will be pointing to the same 'Plug
                            Set' that defines its reusable plugs, instead of the plugs duplicating in
                            each individual socket. Sockets have an optional reusablePlugSetHash
                            property to define this relationship.                                 */
                            if (weaponPerkSocketEntries.get(i).getAsJsonObject().has("reusablePlugSetHash")) {
                                String reusableWeaponPlugSetHash = weaponPerkSocketEntries.get(i).getAsJsonObject().get("reusablePlugSetHash").getAsString();
                                JsonObject reusableWeaponPlugSetDefinition = new JsonObject();
                                try {
                                    InputStream inputStream = getResources().openRawResource(R.raw.plugs);
                                    JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                                    jsonReader.beginObject();
                                    while (jsonReader.hasNext()) {
                                        String token = jsonReader.nextName();
                                        if (token.equals(reusableWeaponPlugSetHash)) {
                                            reusableWeaponPlugSetDefinition = JsonParser.parseReader(jsonReader).getAsJsonObject();
                                        } else {
                                            jsonReader.skipValue();
                                        }
                                    }
                                    jsonReader.close();
                                } catch (IOException ioException) {
                                    ioException.printStackTrace();
                                }
                                JsonArray curatedWeaponPerkArray = reusableWeaponPlugSetDefinition.getAsJsonArray("reusablePlugItems");
//                              For 'i' being less than the amount of curated perks a weapon has...
                                for (int j = 0; j < curatedWeaponPerkArray.size(); j++) {
//                                  ...get curated perks if the weapon has them.
                                    if (curatedWeaponPerkArray.get(j).getAsJsonObject().get("currentlyCanRoll").getAsBoolean()) {
                                        JsonObject curatedWeaponPerkDefinition = new JsonObject();
                                        try {
                                            InputStream inputStream = getResources().openRawResource(R.raw.inventoryitems);
                                            JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                                            jsonReader.beginObject();
                                            while (jsonReader.hasNext()) {
                                                String token = jsonReader.nextName();
                                                if (token.equals(curatedWeaponPerkArray.get(j).getAsJsonObject().get("plugItemHash").getAsString())) {
                                                    curatedWeaponPerkDefinition = JsonParser.parseReader(jsonReader).getAsJsonObject();
                                                } else {
                                                    jsonReader.skipValue();
                                                }
                                            }
                                            jsonReader.close();
                                        } catch (IOException ioException) {
                                            ioException.printStackTrace();
                                        }

                                        Bitmap curatedWeaponPerkIcon = null;
                                        String curatedWeaponPerkIconURL = "https://www.bungie.net" + curatedWeaponPerkDefinition.getAsJsonObject("displayProperties").get("icon").getAsString();
                                        try {
                                            InputStream inputStream = new URL(curatedWeaponPerkIconURL).openStream();
                                            curatedWeaponPerkIcon = BitmapFactory.decodeStream(inputStream);
                                            inputStream.close();
                                        } catch (MalformedURLException malformedURLException) {
                                            malformedURLException.printStackTrace();
                                        } catch (IOException ioException) {
                                            curatedWeaponPerkIcon = null;
                                            ioException.printStackTrace();
                                        }
                                        curatedWeaponPerkList.add(new WeaponPerk(curatedWeaponPerkDefinition, curatedWeaponPerkIcon));
                                    }
                                }
//                          If the reusablePlugItems size is more than 0, get the reusable weapon
//                          plug items.
                            } else if (weaponPerkSocketEntries.get(i).getAsJsonObject().getAsJsonArray("reusablePlugItems").size() > 0) {
                                JsonArray reusableWeaponPlugItems = weaponPerkSocketEntries.get(i).getAsJsonObject().getAsJsonArray("reusablePlugItems");
                                for (JsonElement element : reusableWeaponPlugItems) {
                                    JsonObject reusableWeaponPlugItem = element.getAsJsonObject();
                                    String plugItemHash = reusableWeaponPlugItem.get("plugItemHash").getAsString();
                                    JsonObject curatedWeaponPerkDefinition = new JsonObject();
                                    try {
                                        InputStream inputStream = getResources().openRawResource(R.raw.inventoryitems);
                                        JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                                        jsonReader.beginObject();
                                        while (jsonReader.hasNext()) {
                                            String token = jsonReader.nextName();
                                            if (token.equals(plugItemHash)) {
                                                curatedWeaponPerkDefinition = JsonParser.parseReader(jsonReader).getAsJsonObject();
                                            } else {
                                                jsonReader.skipValue();
                                            }
                                        }
                                        jsonReader.close();
                                    } catch (IOException ioException) {
                                        ioException.printStackTrace();
                                    }

//                                  Get the curated perk icon.
                                    Bitmap curatedWeaponPerkIcon = null;
                                    String curatedWeaponPerkIconURL = "https://www.bungie.net" + curatedWeaponPerkDefinition.getAsJsonObject("displayProperties").get("icon").getAsString();
                                    try {
                                        InputStream inputStream = new URL(curatedWeaponPerkIconURL).openStream();
                                        curatedWeaponPerkIcon = BitmapFactory.decodeStream(inputStream);
                                        inputStream.close();
                                    } catch (MalformedURLException malformedURLException) {
                                        malformedURLException.printStackTrace();
                                    } catch (IOException ioException) {
                                        curatedWeaponPerkIcon = null;
                                        ioException.printStackTrace();
                                    }
                                    curatedWeaponPerkList.add(new WeaponPerk(curatedWeaponPerkDefinition, curatedWeaponPerkIcon));
                                }
//                          Else, if the reusablePlugItems size is NOT more than 0 but EQUALS 0,
//                          get curated weapon perks.
                            } else {
                                JsonObject curatedWeaponPerkDefinition = new JsonObject();
                                if (weaponPerkSocketEntries.get(i).getAsJsonObject().get("singleInitialItemHash").getAsString().equals("0")) {
                                    break;
                                }
                                try {
                                    InputStream inputStream = getResources().openRawResource(R.raw.inventoryitems);
                                    JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                                    jsonReader.beginObject();
                                    while (jsonReader.hasNext()) {
                                        String token = jsonReader.nextName();
                                        if (token.equals(weaponPerkSocketEntries.get(i).getAsJsonObject().get("singleInitialItemHash").getAsString())) {
                                            curatedWeaponPerkDefinition = JsonParser.parseReader(jsonReader).getAsJsonObject();
                                        } else {
                                            jsonReader.skipValue();
                                        }
                                    }
                                    jsonReader.close();
                                } catch (IOException ioException) {
                                    ioException.printStackTrace();
                                }

                                Bitmap curatedWeaponPerkIcon = null;
                                String curatedWeaponPerkIconURL = "https://bungie.net" + curatedWeaponPerkDefinition.getAsJsonObject("displayProperties").get("icon").getAsString();
                                try {
                                    InputStream inputStream = new URL(curatedWeaponPerkIconURL).openStream();
                                    curatedWeaponPerkIcon = BitmapFactory.decodeStream(inputStream);
                                    inputStream.close();
                                } catch (MalformedURLException malformedURLException) {
                                    malformedURLException.printStackTrace();
                                } catch (IOException ioException) {
                                    curatedWeaponPerkIcon = null;
                                    ioException.printStackTrace();
                                }
                                curatedWeaponPerkList.add(new WeaponPerk(curatedWeaponPerkDefinition, curatedWeaponPerkIcon));
                            }

//                          If the perk sockets has a randomised plug set, then the weapon can use
//                          randomised perks, so get the random perks.
                            if (weaponPerkSocketEntries.get(i).getAsJsonObject().has("randomizedPlugSetHash")) {
                                weapon.setHasRandomisedPerks(true);
                                randomWeaponPerkList = new ArrayList<>();

                                JsonObject weaponPlugSetDefinition = new JsonObject();
                                try {
                                    InputStream inputStream = getResources().openRawResource(R.raw.plugs);
                                    JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                                    jsonReader.beginObject();
                                    while (jsonReader.hasNext()) {
                                        String token = jsonReader.nextName();
                                        if (token.equals(weaponPerkSocketEntries.get(i).getAsJsonObject().get("randomizedPlugSetHash").getAsString())) {
                                            weaponPlugSetDefinition = JsonParser.parseReader(jsonReader).getAsJsonObject();
                                        } else {
                                            jsonReader.skipValue();
                                        }
                                    }
                                    jsonReader.close();
                                } catch (IOException ioException) {
                                    ioException.printStackTrace();
                                }

//                              If the perk sockets has a randomised plug set, then the weapon can use
//                              randomised perks, so get the random perks.
                                JsonArray randomWeaponPerkArray = weaponPlugSetDefinition.getAsJsonArray("reusablePlugItems");
                                ArrayList<String> randomWeaponPerkHashArray = new ArrayList<>();
                                for (JsonElement element : randomWeaponPerkArray) {
                                    JsonObject randomWeaponPerkDefinition = element.getAsJsonObject();
                                    if (randomWeaponPerkDefinition.get("currentlyCanRoll").getAsBoolean()) {
                                        randomWeaponPerkHashArray.add(randomWeaponPerkDefinition.get("plugItemHash").getAsString());
                                    }
                                }
                                ArrayList<JsonObject> randomWeaponPerkDefinitionArray = new ArrayList<>();
                                try {
                                    InputStream inputStream = getResources().openRawResource(R.raw.inventoryitems);
                                    JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                                    jsonReader.beginObject();
                                    while (jsonReader.hasNext()) {
                                        String token = jsonReader.nextName();
                                        if (randomWeaponPerkHashArray.contains(token)) {
                                            randomWeaponPerkDefinitionArray.add(JsonParser.parseReader(jsonReader).getAsJsonObject());
                                            if (randomWeaponPerkDefinitionArray.size() == randomWeaponPerkHashArray.size()) {
                                                break;
                                            }
                                        } else {
                                            jsonReader.skipValue();
                                        }
                                    }
                                    jsonReader.close();
                                } catch (IOException ioException) {
                                    ioException.printStackTrace();
                                }
                                for (JsonObject perk : randomWeaponPerkDefinitionArray) {
                                    Bitmap randomWeaponPerkIcon = null;
                                    String randomWeaponPerkIconURL = "https://www.bungie.net" + perk.getAsJsonObject("displayProperties").get("icon").getAsString();
                                    try {
                                        InputStream inputStream = new URL(randomWeaponPerkIconURL).openStream();
                                        randomWeaponPerkIcon = BitmapFactory.decodeStream(inputStream);
                                        inputStream.close();
                                    } catch (MalformedURLException malformedURLException) {
                                        malformedURLException.printStackTrace();
                                    } catch (IOException ioException) {
                                        randomWeaponPerkIcon = null;
                                        ioException.printStackTrace();
                                    }

                                    WeaponPerk randomWeaponPerk = new WeaponPerk(perk, randomWeaponPerkIcon);
                                    boolean hasPerk = false;
                                    for (WeaponPerk weaponPerk : randomWeaponPerkList) {
                                        if (weaponPerk.getWeaponPerkHash().equals(randomWeaponPerk.getWeaponPerkHash())) {
                                            hasPerk = true;
                                            break;
                                        }
                                    }
                                    if (!hasPerk) {
                                        randomWeaponPerkList.add(randomWeaponPerk);
                                    }
                                }
                            }
                            WeaponPerkSocket weaponPerkSocket = new WeaponPerkSocket(weaponPerkSocketTypeDefinition, curatedWeaponPerkList, randomWeaponPerkList);
                            weaponWeaponPerkSocketList.add(weaponPerkSocket);
                        }
                    }
                }
            }
            weaponView.add(weaponWeaponPerkSocketList);
            return weaponView;
        }

        /**
         * onPostExecute is invoked on the UI thread after the background computation finishes. The
         * result of the background computation is then passed to this step as a parameter.
         *
         * @param weapon The result (weapon) of the operation computed by doInBackground.
         * @since 22.3
         */
        @Override
        protected void onPostExecute(ArrayList<Object> weapon) {
            super.onPostExecute(weapon);
            loadWeaponInspect(weapon);
        }
    }
}