package da.dressme;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class AnalysisActivity extends AppCompatActivity {

    private Intent receivedIntent;
    private String receivedUserID;
    private String receivedOutfitID;
    private String receivedFileName;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    private ImageView outfitImage;
    private ImageView topImage;
    private ImageView bottomImage;
    private TextView topTextColor1;
    private TextView topTextColor2;
    private TextView bottomTextColor1;
    private TextView bottomTextColor2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        outfitImage = findViewById(R.id.imageView_outfitPictureAnalysis);
        topImage = findViewById(R.id.imageView_outfitTopAnalysis);
        bottomImage = findViewById(R.id.imageView_outfitBottomAnalysis);
        topTextColor1 = findViewById(R.id.textView_topColor1);
        topTextColor2 = findViewById(R.id.textView_topColor2);
        bottomTextColor1 = findViewById(R.id.textView_bottomColor1);
        bottomTextColor2 = findViewById(R.id.textView_bottomColor2);

        receivedIntent = getIntent();
        receivedUserID = receivedIntent.getStringExtra("userID");
        receivedOutfitID = receivedIntent.getStringExtra("outfitID");
        receivedFileName = receivedIntent.getStringExtra("fileName");

        DatabaseReference outfitDBRef = database.getReference().child("outfits").child(receivedOutfitID);
        StorageReference outfitStorageRef = storage.getReference().child(receivedUserID).child("outfits").child(receivedOutfitID);
        outfitDBRef.child("downloadURL").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue().toString();
                Picasso.get().load(url).fit().into(outfitImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        outfitStorageRef.child("top.jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().into(topImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        outfitStorageRef.child("bottom.jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().into(bottomImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        outfitDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                topTextColor1.setText(dataSnapshot.child("top_color_1").getValue().toString());
                topTextColor2.setText(dataSnapshot.child("top_color_2").getValue().toString());
                bottomTextColor1.setText(dataSnapshot.child("bot_color_1").getValue().toString());
                bottomTextColor1.setText(dataSnapshot.child("bot_color_1").getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
