package uk.rib.armouryoftheguardian;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

/**
 * This is for the weapon screen and gets the end user's selected weapon's name, picture, definition,
 * statistics, and potential perks.
 * <p>
 * Parcelable is used, and is an interface for class instances that can be written to, and restored
 * from, a Parcel. For classes implementing the Parcelable interface, they must also have a non-null
 * static field called CREATOR of a type that implements the Parcelable.Creator interface.
 *
 * @version 22.3
 * @since 22.3
 */
public class Weapon implements Parcelable {

    /**
     * This returns a new weapon.
     * <p>
     * 'createFromParcel' is used to create a new instance of the Parcelable class (which
     * Weapon implements) and then instantiating it from the supplied Parcel whose data had
     * previously been written by Parcelable.writeToParcel.
     *
     * @since 22.3
     */
    public static final Creator<Weapon> CREATOR = new Creator<Weapon>() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override

        public Weapon createFromParcel(Parcel source) {
            return new Weapon(source);
        }

        @Override
        public Weapon[] newArray(int size) {
            return new Weapon[size];
        }
    };

    ArrayList<WeaponPerkSocket> weaponPerkSocketList;
    ArrayList<WeaponStatistics> weaponStatistics;
    Bitmap weaponIcon;
    Bitmap weaponScreenshot;
    boolean hasRandomisedPerks = false;
    JsonObject weaponDefinition;

    /**
     * This defines the end user's selected weapon's image and description from the JsonObject
     * and Bitmap of the weapon.
     *
     * @since 22.3
     */
    public Weapon(JsonObject weaponDefinition, Bitmap weaponIcon) {
        this.weaponDefinition = weaponDefinition;
        this.weaponIcon = weaponIcon;
    }

    /**
     * This gets all weapon info from the parcel, which includes:
     * <ul>
     *      <li>The definition of the weapon</li>
     *      <li>The weapon's weapon_image_icon</li>
     *      <li>The weapon's statistics, like its damage, range, stability, and so forth</li>
     *      <li>The perks the weapon can use</li>
     * </ul>
     *
     * @param parcel The Parcel being read for information on the weapon.
     * @since 22.3
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected Weapon(Parcel parcel) {
        weaponDefinition = JsonParser.parseString(parcel.readString()).getAsJsonObject();
        weaponIcon = parcel.readParcelable(Bitmap.class.getClassLoader());
        weaponStatistics = new ArrayList<>();
        parcel.readParcelableList(weaponStatistics, WeaponStatistics.class.getClassLoader());
        weaponPerkSocketList = new ArrayList<>();
        parcel.readParcelableList(weaponPerkSocketList, WeaponPerkSocket.class.getClassLoader());
    }

    /**
     * This returns the weapon's definition as a string from the JSON object.
     *
     * @return The weapon's definition as a string.
     * @since 22.3
     */
    public String weaponName() {
        return weaponDefinition.getAsJsonObject("displayProperties").get("name").getAsString();
    }

    /**
     * This returns the end user's selected weapon's damage type as a string.
     *
     * @return The end user's selected weapon's damage type as a string.
     * @since 22.3
     */
    public String weaponType() {
        return weaponDefinition.get("itemTypeAndTierDisplayName").getAsString();
    }

    /**
     * This returns the end user's selected weapon's quote/text as a string.
     *
     * @return The end user's selected weapon's quote/text as a string.
     * @since 22.3
     */
    public String weaponFlavourText() {
        return weaponDefinition.get("flavorText").getAsString();
    }

    /**
     * This checks if the end user's selected weapon can have randomised perks via a Boolean..
     *
     * @since 22.3
     */
    public void setHasRandomisedPerks(boolean bool) {
        this.hasRandomisedPerks = bool;
    }

    /**
     * This returns the miniature icon of the end user's selected weapon.
     *
     * @return The miniature icon of the end user's selected weapon.
     * @since 22.3
     */
    public Bitmap getWeaponIcon() {
        return this.weaponIcon;
    }

    /**
     * This returns the screenshot of the end user's selected weapon, showing it in its entirety.
     *
     * @return The screenshot of the end user's selected weapon.
     * @since 22.3
     */
    public Bitmap getWeaponScreenshot() {
        return this.weaponScreenshot;
    }

    /**
     * This sets the screenshot of the end user's selected weapon, showing it in its entirety.
     *
     * @since 22.3
     */
    public void setWeaponScreenshot(Bitmap img) {
        this.weaponScreenshot = img;
    }

    /**
     * This returns the perk sockets/slots of the end user's selected weapon.
     *
     * @return The perk sockets/slots of the end user's selected weapon.
     * @since 22.3
     */
    public ArrayList<WeaponPerkSocket> getPerkSocketList() {
        return this.weaponPerkSocketList;
    }

    /**
     * This sets the perk sockets/slots of the end user's selected weapon.
     *
     * @since 22.3
     */
    public void setPerkSocketList(ArrayList<WeaponPerkSocket> list) {
        this.weaponPerkSocketList = list;
    }

    /**
     * This returns the statistics of the end user's selected weapon.
     *
     * @return The perk sockets/slots of the end user's selected weapon.
     * @since 22.3
     */
    public ArrayList<WeaponStatistics> getWeaponStatistics() {
        return this.weaponStatistics;
    }

    /**
     * This sets the statistics of the end user's selected weapon.
     *
     * @param list The list of statistics of the end user's selected weapon.
     * @since 22.3
     */
    public void setWeaponStatistics(ArrayList<WeaponStatistics> list) {
        this.weaponStatistics = list;
    }

    /**
     * This returns the definition of the end user's selected weapon.
     *
     * @return The definition of the end user's selected weapon.
     * @since 22.3
     */
    public JsonObject getWeaponDefinition() {
        return this.weaponDefinition;
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
     * This flattens the end user's selected weapon's information in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written. This value cannot be null.
     * @param flags Additional flags about how the object should be written. May be 0 or
     *              PARCELABLE_WRITE_RETURN_VALUE.
     * @since 22.3
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.weaponDefinition.toString());
        dest.writeParcelable(this.weaponIcon, flags);
        if (weaponStatistics != null) {
            dest.writeParcelableList(weaponStatistics, flags);
        }
        if (weaponPerkSocketList != null) {
            dest.writeParcelableList(weaponPerkSocketList, flags);
        }
    }
}
