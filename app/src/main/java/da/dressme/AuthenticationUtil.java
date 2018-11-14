package da.dressme;

import android.app.Activity;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationUtil {

    public static String authenticateAndGetUserID(Activity activity) {

        //If user is not logged in, go to login activity

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null)
        {
            activity.finish();
            activity.startActivity(new Intent(activity, EmailLoginActivity.class));
        }

        // Collect userID
        FirebaseUser user = mAuth.getCurrentUser();
        String userID;

        // If userID is null attempt to log them in again, else set the userID field
        if(user==null) {
            userID = "";
            activity.finish();
            activity.startActivity(new Intent(activity, EmailLoginActivity.class));

        }
        else {
            userID = user.getUid();
        }

        return userID;
    }
}
