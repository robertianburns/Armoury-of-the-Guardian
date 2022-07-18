package uk.rib.armouryoftheguardian;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * The main activity of Armoury of the Guardian. The main activity is where the end user enters a
 * word, to which that word is checked against Bungie'a API to find any weapon containing said word.
 *
 * @version 22.3
 * @since 22.3
 */
public class MainActivity extends AppCompatActivity {

    ArrayList<Weapon> weaponArrayList;
    Button weaponSearchButton;
    Context context = this;
    EditText weaponSearchBar;
    ProgressBar progressBar;
    RecyclerAdapter recyclerAdapter;
    RecyclerView recyclerView;

    MediaPlayer currentMusic;

    /**
     * This creates the main activity.
     *
     * @param savedInstanceState If the main activity is being re-initialized after previously being
     *                           shut down, then this Bundle contains the data it  most recently
     *                           supplied in onSaveInstanceState(Bundle). If not though, then it is
     *                           null.
     * @since 22.3
     */
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.home_progressbar);
        progressBar.setVisibility(View.GONE);
        recyclerView = findViewById(R.id.home_recyclerview);
        weaponSearchBar = findViewById(R.id.home_weapon_searchbar);
        weaponSearchButton = findViewById(R.id.home_weapon_searchbutton);

        weaponArrayList = new ArrayList<>();
        recyclerAdapter = new RecyclerAdapter(this, weaponArrayList);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        weaponSearchButton.setOnClickListener(view -> {
            hideInputMethod();
            if (weaponSearchBar.getText().length() > 0) {
                weaponArrayList = new ArrayList<>();
                recyclerAdapter.notifyDataSetChanged(); // Notify any registered observers that the data set has changed.
                new GetWeaponList().execute(weaponSearchBar.getText().toString());
            }
        });

//      Initialises the Destiny music.
        final MediaPlayer d1VanillaMusic = MediaPlayer.create(this, R.raw.destiny1_music_vanilla);
        final MediaPlayer d1ttkMusic = MediaPlayer.create(this, R.raw.destiny1_music_thetakenking);
        final MediaPlayer d1roiMusic = MediaPlayer.create(this, R.raw.destiny1_music_riseofiron);
        final MediaPlayer d2VanillaMusic = MediaPlayer.create(this, R.raw.destiny2_music_vanilla);
        final MediaPlayer d2ForsakenMusic = MediaPlayer.create(this, R.raw.destiny2_music_forsaken);
        final MediaPlayer d2BeyondlightMusic = MediaPlayer.create(this, R.raw.destiny2_music_beyondlight);

        currentMusic = d1VanillaMusic;
        currentMusic.start();

        final Button home_button_music = findViewById(R.id.home_button_music);
        home_button_music.setOnClickListener(view -> {
            PopupMenu d1PopupMenu = new PopupMenu(MainActivity.this, home_button_music);

//          Inflating the popup menu from destiny1_popup_menu.xml file
            d1PopupMenu.getMenuInflater().inflate(R.menu.menu_music, d1PopupMenu.getMenu());
            d1PopupMenu.setOnMenuItemClickListener(menuItem -> {
                int id = menuItem.getItemId();
                switch (id) {
                    case (R.id.d1vanilla):
                        currentMusic.pause();
                        currentMusic.seekTo(0);
                        currentMusic = d1VanillaMusic;
                        currentMusic.start();
                        break;
                    case (R.id.d1ttk):
                        currentMusic.pause();
                        currentMusic.seekTo(0);
                        currentMusic = d1ttkMusic;
                        currentMusic.start();
                        break;
                    case (R.id.d1roi):
                        currentMusic.pause();
                        currentMusic.seekTo(0);
                        currentMusic = d1roiMusic;
                        currentMusic.start();
                        break;
                    case (R.id.d2vanilla):
                        currentMusic.pause();
                        currentMusic.seekTo(0);
                        currentMusic = d2VanillaMusic;
                        currentMusic.start();
                        break;
                    case (R.id.d2forsaken):
                        currentMusic.pause();
                        currentMusic.seekTo(0);
                        currentMusic = d2ForsakenMusic;
                        currentMusic.start();
                        break;
                    case (R.id.d2beyondlight):
                        currentMusic.pause();
                        currentMusic.seekTo(0);
                        currentMusic = d2BeyondlightMusic;
                        currentMusic.start();
                        break;
                    case (R.id.mute):
                        currentMusic.pause();
                        currentMusic.seekTo(0);
                        break;
                    default:
                        return false;
                }
                return false;
            });
            d1PopupMenu.show();
        });
    }

    /**
     * This is what is shown after the end user searches for a weapon.
     *
     * @param weaponList The weaponArrayList of weapons related to the end user's search.
     * @since 22.3
     */
    @SuppressLint("NotifyDataSetChanged")
    public void weaponSearchList(ArrayList<Weapon> weaponList) {
        weaponArrayList = weaponList;
        recyclerAdapter = new RecyclerAdapter(context, weaponArrayList);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
        if (weaponArrayList.size() == 0) {
            Snackbar.make(recyclerView, "No weapons were found!", Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * This is for more direct control on the visibility of the end user's input.
     *
     * @since 22.3
     */
    public void hideInputMethod() {
        View currentFocus = this.getCurrentFocus();
        if (currentFocus != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * This searches for the weapons after the end user has clicked the search button.
     * <p>
     * For 'extends AsyncTask'. the three types used by an asynchronous task are the following:
     * <ul>
     *      <li>The params, the type of the parameters sent to the task upon execution. This is the
     *      end user's searched term as a string.</li>
     *      <li>The progress, the type of the progress units published during the background computation.
     *      This is void.</li>
     *      <li>The result, the type of the result of the background computation. This is an array weaponArrayList
     *      of weapons.</li>
     * </ul>
     *
     * @since 22.3
     */
    @SuppressLint("StaticFieldLeak")
    public class GetWeaponList extends AsyncTask<String, Void, ArrayList<Weapon>> {

        /**
         * onPreExecute() is invoked on the UI thread before the task is executed. This is used to
         * show a progress bar in the user interface.
         *
         * @since 22.3
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        /**
         * This searches for weapons in the background.
         * <p>
         * doInBackground is invoked on the background thread immediately after onPreExecute()
         * finishes executing. This is used to perform background computation that can take a long
         * time, such as retrieving the weapon screenshot, icons, statistics, and perks. The
         * parameters of the asynchronous task are passed to this step. The result of the computation
         * must be returned by this step and will be passed back to the last step.
         *
         * @param params The parameters of the task.
         * @return The end user's selected weapon's details.
         * @since 22.3
         */
        @Override
        protected ArrayList<Weapon> doInBackground(String... params) {
            ArrayList<Weapon> list = new ArrayList<>();
            String myApiKey = "2a2399b5d964422884d1c371212a778a"; // Armoury of the Guardian's personal API key.
            String searchResult = "";
            String enduserInput;
/*          Each API endpoint is located under https://www.bungie.net/Platform. Some endpoints
            require authorisation and cannot be used without additional parameters being
            passed to the platform. All endpoints return RESTful JSON.                            */
            String url = "https://www.bungie.net/Platform/Destiny2/Armory/Search/DestinyInventoryItemDefinition/" + params[0] + "/";

            try {
                URL weaponURL = new URL(url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) weaponURL.openConnection();

/*              To fetch data from Bungie's API, it's as simple as sending a HTTP request to it
                along with the personal API key. The response is JSON-encoded.                    */
                httpURLConnection.setRequestProperty("X-API-KEY", myApiKey); // Use the API key.
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setReadTimeout(13000);
                httpURLConnection.setConnectTimeout(13000);
                httpURLConnection.connect();

                InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                while ((enduserInput = bufferedReader.readLine()) != null) {
                    searchResult = searchResult.concat(enduserInput);
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            JsonObject weaponSearchResults = JsonParser.parseString(searchResult).getAsJsonObject();
            JsonArray jsonArray = weaponSearchResults.getAsJsonObject("Response").getAsJsonObject("results").getAsJsonArray("results");
            if (jsonArray.size() > 0) {
                ArrayList<String> weaponHashArray = new ArrayList<>();
                for (JsonElement jsonElement : jsonArray) {
                    weaponHashArray.add(jsonElement.getAsJsonObject().get("hash").getAsString());
                }
                ArrayList<JsonObject> weaponSearchResultArray = new ArrayList<>();
                try {
                    InputStream inputStream = getResources().openRawResource(R.raw.inventoryitems);
                    JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                    jsonReader.beginObject();
                    while (jsonReader.hasNext()) {
                        String token = jsonReader.nextName();
                        if (weaponHashArray.contains(token)) {
                            weaponSearchResultArray.add(JsonParser.parseReader(jsonReader).getAsJsonObject());
                            if (weaponSearchResultArray.size() == weaponHashArray.size()) {
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

                for (JsonObject weaponSearchResultDefinition : weaponSearchResultArray) {
                    boolean weaponResult = false; // Is what is found a weapon?
                    if (weaponSearchResultDefinition.has("traitIds")) {
                        JsonArray traitIds = weaponSearchResultDefinition.getAsJsonArray("traitIds");
                        for (int i = 0; i < traitIds.size(); i++) {
                            if (traitIds.get(i).getAsString().equals("item_type.weapon")) {
                                weaponResult = true;
                                break;
                            }
                        }
                    }

                    // If a weapon has been found...
                    if (weaponResult) {
                        String weaponIconURL;
                        String weaponWatermarkURL;
                        Bitmap weaponIcon = null;
                        Bitmap weaponWatermark = null;

                        // If the weapon has an icon (which it always should)...
                        if (weaponSearchResultDefinition.getAsJsonObject("displayProperties").get("hasIcon").getAsBoolean()) {
                            weaponIconURL = weaponSearchResultDefinition.getAsJsonObject("displayProperties").get("icon").getAsString();
                            weaponIconURL = "https://www.bungie.net" + weaponIconURL;
                            try {
                                InputStream inputStream = new URL(weaponIconURL).openStream();
                                weaponIcon = BitmapFactory.decodeStream(inputStream);
                                inputStream.close();
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        }

                        // If the weapon has a rarity (common, rare, legendary, exotic)...
                        if (weaponSearchResultDefinition.has("quality")) {
                            weaponWatermarkURL = weaponSearchResultDefinition.getAsJsonObject("quality").getAsJsonArray("displayVersionWatermarkIcons").get(weaponSearchResultDefinition.getAsJsonObject("quality").getAsJsonArray("displayVersionWatermarkIcons").size() - 1).getAsString();
                            weaponWatermarkURL = "https://www.bungie.net" + weaponWatermarkURL;
                            try {
                                InputStream inputStream = new java.net.URL(weaponWatermarkURL).openStream();
                                weaponWatermark = BitmapFactory.decodeStream(inputStream);
                                inputStream.close();
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        } else if (weaponSearchResultDefinition.has("watermark")) {
                            weaponWatermarkURL = weaponSearchResultDefinition.get("watermark").getAsString();
                            weaponWatermarkURL = "https://www.bungie.net" + weaponWatermarkURL;
                            try {
                                InputStream inputStream = new java.net.URL(weaponWatermarkURL).openStream();
                                weaponWatermark = BitmapFactory.decodeStream(inputStream);
                                inputStream.close();
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        }

                        // If there is a weapon icon...
                        if (weaponIcon != null) {
                            weaponIcon = weaponIcon.copy(weaponIcon.getConfig(), true);
                            Canvas canvas = new Canvas(weaponIcon);
                            if (weaponWatermark != null) {
                                canvas.drawBitmap(weaponWatermark, 0, 0, null);
                            }
                        }
                        list.add(new Weapon(weaponSearchResultDefinition, weaponIcon));
                    }
                }
            }
            return list;
        }

        /**
         * onPostExecute is invoked on the UI thread after the background computation finishes. The
         * result of the background computation is then passed to this step as a parameter.
         *
         * @param weapons The result (weaponArrayList of weapons) of the operation computed by doInBackground.
         * @since 22.3
         */
        @Override
        protected void onPostExecute(ArrayList<Weapon> weapons) {
            super.onPostExecute(weapons);
            weaponSearchList(weapons);
        }
    }
}