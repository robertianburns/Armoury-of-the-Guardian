package uk.rib.armouryoftheguardian;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;

/**
 * This is for the end user's selected weapon's statistics.
 * <p>
 * The 'Comparable' interface imposes a total ordering on the objects of each class that implements
 * it. This ordering is referred to as the class's natural ordering, and the class's 'compareTo'
 * method is referred to as its natural comparison method.
 * <p>
 * 'Parcelable' is an interface for classes whose instances can be written to and restored from a
 * Parcel. Classes implementing the Parcelable interface must also have a non-null static field
 * called CREATOR of a type that implements the Parcelable.Creator interface.
 *
 * @version 22.3
 * @since 22.3
 */
public class WeaponStatistics implements Comparable, Parcelable {

    /**
     * Classes implementing the Parcelable interface must also have a non-null static field called
     * CREATOR of a type that implements the Parcelable.Creator interface.
     *
     * @since 22.3
     */
    public static final Creator<WeaponStatistics> CREATOR = new Creator<WeaponStatistics>() {

        /**
         * This creates a new instance of the Parcelable class, instantiating it from the given
         * Parcel whose data had previously been written by Parcelable.writeToParcel().
         *
         * @param parcel The Parcel to read the object's data from.
         * @return The weapon statistic's instance of the Parcelable class.
         * @since 22.3
         */
        @Override
        public WeaponStatistics createFromParcel(Parcel parcel) {
            return new WeaponStatistics(parcel);
        }

        /**
         * This creates a new array of the Parcelable class and returns the end user's selected
         * weapon's statistic's size.
         *
         * @return The end user's selected weapon's statistic's size.
         * @since 22.3
         */
        @Override
        public WeaponStatistics[] newArray(int size) {
            return new WeaponStatistics[size];
        }
    };

    /* Some statistic stuff... */
    boolean weaponStatisticCategories;
    int weaponStatisticIndex;
    int weaponStatisticValue;
    JsonObject weaponStatisticDefinition;
    String weaponStatisticHash;


    /**
     * This sets the end user's selected weapon's statistic's hash, definition, and value.
     *
     * @param weaponStatisticHash       The hash of the end user's selected weapon's statistic.
     * @param weaponStatisticDefinition The definition of the end user's selected weapon's statistic.
     * @param weaponStatisticValue      The value of the end user's selected weapon's statistic.
     * @since 22.3
     */
    public WeaponStatistics(String weaponStatisticHash, JsonObject weaponStatisticDefinition, int weaponStatisticValue) {
        this.weaponStatisticHash = weaponStatisticHash;
        this.weaponStatisticDefinition = weaponStatisticDefinition;
        this.weaponStatisticValue = weaponStatisticValue;
        try {
            this.weaponStatisticIndex = this.weaponStatisticDefinition.get("index").getAsInt();
            this.weaponStatisticCategories = setWeaponStatisticCategories();
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
    }

    /**
     * This reads the end user's selected weapon's statistic hash, definition, value, and index from
     * the Parcel.
     *
     * @param parcel The Parcel being read from.
     * @since 22.3
     */
    protected WeaponStatistics(Parcel parcel) {
        weaponStatisticHash = parcel.readString();
        weaponStatisticDefinition = JsonParser.parseString(parcel.readString()).getAsJsonObject();
        weaponStatisticValue = parcel.readInt();
        weaponStatisticIndex = parcel.readInt();
        weaponStatisticCategories = parcel.readByte() != 0;
    }

    /**
     * This sets the different statistic categories of weapons. For example, an Auto Rifle wouldn't
     * have a 'Charge Time' as it doesn't charge up before firing.
     *
     * @return The different statistics categories of weapons.
     * @since 22.3
     */
    public boolean setWeaponStatisticCategories() throws JSONException {
        return !this.weaponStatisticName().equals("Magazine") && !this.weaponStatisticName().equals("Rounds Per Minute") &&
                !this.weaponStatisticName().equals("Inventory Size") && !this.weaponStatisticName().equals("Recoil Direction") &&
                !this.weaponStatisticName().equals("Ammo Capacity") && !this.weaponStatisticName().equals("Charge Time");
    }

    /**
     * This gets the different statistics categories of weapons.
     *
     * @return The different statistics categories of weapons.
     * @since 22.3
     */
    public boolean getWeaponStatisticCategories() {
        return this.weaponStatisticCategories;
    }

    /**
     * This returns the end user's selected weapon's statistic's names.
     *
     * @return The weapon statistic's getWeaponPerkName.
     * @since 22.3
     */
    public String weaponStatisticName() throws JSONException {
        return this.weaponStatisticDefinition.getAsJsonObject("displayProperties").get("name").getAsString();
    }

    /**
     * This returns a string of the end user's selected weapon's statistic's names along with the
     * accompanying values.
     *
     * @return A string of he weapon statistic's getWeaponPerkName along with the accompanying value.
     * @since 22.3
     */
    public String toString() {
        try {
            return this.weaponStatisticName() + ": " + this.getWeaponStatisticValue();
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
        return null;
    }

    /**
     * This gets the end user's selected weapon's statistic's value.
     *
     * @return The end user's selected weapon's statistic's value.
     * @since 22.3
     */
    public int getWeaponStatisticValue() {
        return this.weaponStatisticValue;
    }

    /**
     * This gets the index of the end user's selected weapon's statistic.
     *
     * @return The index of the end user's selected weapon's statistic.
     * @since 22.3
     */
    public int getWeaponStatisticIndex() {
        return this.weaponStatisticIndex;
    }

    /**
     * Compares this object with the specified object for order. Returns a negative integer, zero,
     * or a positive integer as this object is less than, equal to, or greater than the specified
     * object.
     *
     * @param object The object to be compared.
     * @return An integer after comparing the specified object.
     * @since 22.3
     */
    @Override
    public int compareTo(Object object) {
        WeaponStatistics weaponStatistics = (WeaponStatistics) object;
        if (this.getWeaponStatisticCategories() && !weaponStatistics.getWeaponStatisticCategories()) {
            return -1;
        } else if (!this.getWeaponStatisticCategories() && weaponStatistics.getWeaponStatisticCategories()) {
            return 1;
        } else {
            return Integer.compare(this.getWeaponStatisticIndex(), weaponStatistics.getWeaponStatisticIndex());
        }
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
     * This flattens the end user's selected weapon's statistic information in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written. This value cannot be null.
     * @param flags Additional flags about how the object should be written. May be 0 or
     *              PARCELABLE_WRITE_RETURN_VALUE.
     * @since 22.3
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(weaponStatisticHash);
        dest.writeString(weaponStatisticDefinition.toString());
        dest.writeInt(weaponStatisticValue);
        dest.writeInt(weaponStatisticIndex);
        dest.writeByte((byte) (weaponStatisticCategories ? 1 : 0));
    }
}
