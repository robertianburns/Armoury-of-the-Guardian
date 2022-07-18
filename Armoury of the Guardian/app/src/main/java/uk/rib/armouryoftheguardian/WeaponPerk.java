package uk.rib.armouryoftheguardian;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * This sets the viewing of the end user's selected weapon's perks.
 * <p>
 * Parcelable is used, and is an interface for class instances that can be written to, and restored
 * from, a Parcel. For classes implementing the Parcelable interface, they must also have a non-null
 * static field called CREATOR of a type that implements the Parcelable.Creator interface.
 *
 * @version 22.3
 * @since 22.3
 */
public class WeaponPerk implements Parcelable {

    public static final Creator<WeaponPerk> CREATOR = new Creator<WeaponPerk>() {

        /**
         * This creates a new instance of the Parcelable class.
         *
         * @param parcel The Parcel to read the end user's selected weapon's data from.
         * @return The end user's selected weapon's perk perks from a Parcel.
         * @since 22.3
         */
        @Override
        public WeaponPerk createFromParcel(Parcel parcel) {
            return new WeaponPerk(parcel);
        }

        /**
         * This creates a new array of the Parcelable class and returns the end user's selected
         * weapon's perk size.
         *
         * @return The end user's selected weapon's perk size.
         * @since 22.3
         */
        @Override
        public WeaponPerk[] newArray(int size) {
            return new WeaponPerk[size];
        }
    };

    JsonObject weaponPerkDefinition;
    Bitmap weaponPerkIcon;

    /**
     * This sets the end user's selected weapon's perk's definition and icon.
     *
     * @param weaponPerkDefinition The end user's selected weapon's perk's definition.
     * @param weaponPerkIcon       The end user's selected weapon's perk's icon.
     * @since 22.3
     */
    public WeaponPerk(JsonObject weaponPerkDefinition, Bitmap weaponPerkIcon) {
        this.weaponPerkDefinition = weaponPerkDefinition;
        this.weaponPerkIcon = weaponPerkIcon;
    }

    /**
     * This reads the end user's selected weapon's perk's definition and icon from the given Parcel.
     *
     * @param parcel The given Parcel to read the end user's selected weapon's perk's definition and
     *               icon from
     * @since 22.3
     */
    protected WeaponPerk(Parcel parcel) {
        weaponPerkDefinition = JsonParser.parseString(parcel.readString()).getAsJsonObject();
        weaponPerkIcon = parcel.readParcelable(Bitmap.class.getClassLoader());
    }

    /**
     * This gets the end user's selected weapon's perk's icon.
     *
     * @since 22.3
     */
    public Bitmap getWeaponPerkIcon() {
        return this.weaponPerkIcon;
    }

    /**
     * This sets the end user's selected weapon's perk's icon as a bitmap.
     *
     * @since 22.3
     */
    public void setWeaponPerkIcon(Bitmap bitmap) {
        this.weaponPerkIcon = bitmap;
    }

    /**
     * This gets the end user's selected weapon's perk's definition.
     *
     * @return The end user's selected weapon's perk's definition.
     * @since 22.3
     */
    public JsonObject getWeaponPerkDefinition() {
        return this.weaponPerkDefinition;
    }

    /**
     * This gets the end user's selected weapon's perk's name.
     *
     * @return The end user's selected weapon's perk's name.
     * @since 22.3
     */
    public String getWeaponPerkName() {
        return this.weaponPerkDefinition.getAsJsonObject("displayProperties").get("name").getAsString();
    }

    /**
     * This gets the end user's selected weapon's perk's description.
     *
     * @return The end user's selected weapon's perk's description.
     * @since 22.3
     */
    public String getWeaponPerkDescription() {
        return this.weaponPerkDefinition.getAsJsonObject("displayProperties").get("description").getAsString();
    }

    /**
     * This gets the end user's selected weapon's perk's hash.
     *
     * @return The end user's selected weapon's perk's hash.
     * @since 22.3
     */
    public String getWeaponPerkHash() {
        return this.weaponPerkDefinition.get("hash").getAsString();
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
     * This flattens the end user's selected weapon's perk's icon and definition in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written. This value cannot be null.
     * @param flags Additional flags about how the object should be written. May be 0 or
     *              PARCELABLE_WRITE_RETURN_VALUE.
     * @since 22.3
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.weaponPerkDefinition.toString());
        dest.writeParcelable(weaponPerkIcon, flags);
    }
}
