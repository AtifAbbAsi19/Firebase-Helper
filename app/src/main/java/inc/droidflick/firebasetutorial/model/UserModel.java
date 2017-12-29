package inc.droidflick.firebasetutorial.model;

import android.os.Parcel;
import android.os.Parcelable;

import inc.droidflick.firebasetutorial.model.interfaces.User;

/**
 * Created by Atif Arif on 12/18/2017.
 */

public class UserModel implements User, Parcelable {

    String userName;
    String fullName;


    @Override
    public void setUserName(String userName) {

        this.userName = userName;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String getFullName() {
        return fullName;
    }


    protected UserModel(Parcel in) {
        userName = in.readString();
        fullName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(fullName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

}
