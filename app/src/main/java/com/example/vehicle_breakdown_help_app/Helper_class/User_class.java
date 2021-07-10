package com.example.vehicle_breakdown_help_app.Helper_class;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class User_class implements Parcelable {
    private Uri user_profile_image;
    private String user_name;
    private String user_email;
    private String user_phone;
    private String user_password;

    public User_class(Uri user_profile_image, String user_name, String user_email, String user_phone, String user_password) {
        this.user_profile_image = user_profile_image;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_phone = user_phone;
        this.user_password = user_password;
    }

    public Uri getUser_profile_image() {
        return user_profile_image;
    }

    public void setUser_profile_image(Uri user_profile_image) {
        this.user_profile_image = user_profile_image;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    protected User_class(Parcel in) {
        user_profile_image = (Uri) in.readValue(Uri.class.getClassLoader());
        user_name = in.readString();
        user_email = in.readString();
        user_phone = in.readString();
        user_password = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(user_profile_image);
        dest.writeString(user_name);
        dest.writeString(user_email);
        dest.writeString(user_phone);
        dest.writeString(user_password);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User_class> CREATOR = new Parcelable.Creator<User_class>() {
        @Override
        public User_class createFromParcel(Parcel in) {
            return new User_class(in);
        }

        @Override
        public User_class[] newArray(int size) {
            return new User_class[size];
        }
    };
}
