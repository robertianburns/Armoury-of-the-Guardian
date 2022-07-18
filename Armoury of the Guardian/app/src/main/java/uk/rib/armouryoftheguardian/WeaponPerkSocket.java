package uk.rib.armouryoftheguardian;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

/**
 * This creates the end user's selected weapon's perk sockets, which is where the perks go.
 * <p>
 * 'Parcelable' is an interface for classes whose instances can be written to and restored from a
 * Parcel. Classes implementing the Parcelable interface must also have a non-null static field
 * called CREATOR of a type that implements the Parcelable.Creator interface.
 *
 * @version 22.3
 * @since 22.3
 */
public class WeaponPerkSocket implements Parcelable {

    public static final Creator<WeaponPerkSocket> CREATOR = new Creator<WeaponPerkSocket>() {

        /**
         * This creates a new instance of the Parcelable class.
         *
         * @param parcel The Parcel to read the object's data from.
         * @return The end user's selected weapon's perk socket's instance of the Parcelable class.
         * @since 22.3
         */
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public WeaponPerkSocket createFromParcel(Parcel parcel) {
            return new WeaponPerkSocket(parcel);
        }

        /**
         * This creates a new array of the Parcelable class and returns the end user's selected
         * weapon's perk sockets size.
         *
         * @return The end user's selected weapon's perk sockets size.
         * @since 22.3
         */
        @Override
        public WeaponPerkSocket[] newArray(int size) {
            return new WeaponPerkSocket[size];
        }
    };

    ArrayList<WeaponPerk> curatedWeaponPerkList;
    ArrayList<WeaponPerk> randomWeaponPerkList;
    JsonObject weaponPerkSocketTypeDefinition;
    String weaponPerkSocketName;

    /**
     * This sets the end user's selected weapon's perk sockets definition, curated perk list, and
     * random perk list.
     *
     * @param weaponPerkSocketTypeDefinition The end user's selected weapon's perk sockets type.
     * @param randomWeaponPerkList           The end user's selected weapon's random perk list.
     * @param curatedWeaponPerkList          The end user's selected weapon's curated perk list.
     * @since 22.3
     */
    public WeaponPerkSocket(JsonObject weaponPerkSocketTypeDefinition, ArrayList<WeaponPerk> curatedWeaponPerkList, ArrayList<WeaponPerk> randomWeaponPerkList) {
        this.weaponPerkSocketTypeDefinition = weaponPerkSocketTypeDefinition;
        this.curatedWeaponPerkList = curatedWeaponPerkList;
        this.randomWeaponPerkList = randomWeaponPerkList;
        weaponPerkSocketName = weaponPerkSocketTypeDefinition.getAsJsonArray("plugWhitelist").get(0).getAsJsonObject().get("categoryIdentifier").getAsString();
    }

    /**
     * This reads the end user's selected weapon's perk sockets definition, curated perk list, and
     * random perk list from the given parcel.
     *
     * @param parcel The Parcel whose data is being read from.
     * @since 22.3
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected WeaponPerkSocket(Parcel parcel) {
        curatedWeaponPerkList = new ArrayList<>();
        randomWeaponPerkList = new ArrayList<>();

        weaponPerkSocketName = parcel.readString();
        weaponPerkSocketTypeDefinition = JsonParser.parseString(parcel.readString()).getAsJsonObject();

        parcel.readParcelableList(curatedWeaponPerkList, WeaponPerk.class.getClassLoader());
        parcel.readParcelableList(randomWeaponPerkList, WeaponPerk.class.getClassLoader());
    }

    /**
     * This returns the end user's selected weapon's perk socket's getWeaponPerkName as a string.
     *
     * @return A string of the end user's selected weapon's perk socket's getWeaponPerkName.
     * @since 22.3
     */
    @NonNull
    public String toString() {
        return this.weaponPerkSocketName;
    }

    /**
     * This gets all the end user's selected weapon's curated perks.
     *
     * @return A list of the end user's selected weapon's curated perks.
     * @since 22.3
     */
    public ArrayList<WeaponPerk> getCuratedWeaponPerkList() {
        return this.curatedWeaponPerkList;
    }

    /**
     * This gets all the end user's selected weapon's potential random perks that it can use.
     *
     * @return A list of the end user's selected weapon's potential random perks.
     * @since 22.3
     */
    public ArrayList<WeaponPerk> getRandomWeaponPerkList() {
        return this.randomWeaponPerkList;
    }

    /**
     * This describes the kinds of special objects contained in this Parcelable instance's marshaled
     * representation.
     *
     * @return The definition of the end user's selected weapon.
     * @since 22.3
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * This flattens the end user's selected weapon's perk socket's getWeaponPerkName, definition,
     * curated perk list, and random perk list in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written. This value cannot be null.
     * @param flags Additional flags about how the object should be written. May be 0 or
     *              PARCELABLE_WRITE_RETURN_VALUE.
     * @since 22.3
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelableList(curatedWeaponPerkList, flags);
        dest.writeParcelableList(randomWeaponPerkList, flags);
        dest.writeString(weaponPerkSocketName);
        dest.writeString(weaponPerkSocketTypeDefinition.toString());

    }
}
