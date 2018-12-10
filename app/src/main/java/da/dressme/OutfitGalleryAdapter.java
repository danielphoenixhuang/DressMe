package da.dressme;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.List;

import static da.dressme.ColorAnalysisClass.colorMatch;

public class OutfitGalleryAdapter extends Adapter<OutfitGalleryAdapter.ViewHolder> {

    private List<String> outfitReferences;
    private List<String> fileNameReferences;
    private Context mContext;
    private String userID;
    private Button analButt;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private Uri picUri;

    public OutfitGalleryAdapter(Context context, List<String> outfitRefs, List<String> imgNameRefs, String uID)
    {
        mContext = context;
        outfitReferences = outfitRefs;
        fileNameReferences = imgNameRefs;
        userID = uID;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout parentLayout;
        ImageView galleryItem;
        TextView testText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.constraintLayout_outfitGall);
            galleryItem = itemView.findViewById(R.id.imageView_gallery_item);
            testText = itemView.findViewById(R.id.testText_textview);
            analButt = itemView.findViewById(R.id.button_viewAnal);
        }
    }


    @NonNull
    @Override
    public OutfitGalleryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_view, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final OutfitGalleryAdapter.ViewHolder holder, int position) {
        final int pos = position;
        final String outfitID = outfitReferences.get(pos);
        final String fileName = fileNameReferences.get(pos);

        analButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent analIntent = new Intent(mContext, AnalysisActivity.class);
                analIntent.putExtra("userID", userID);
                analIntent.putExtra("outfitID", outfitID);
                analIntent.putExtra("fileName", fileName);
                mContext.startActivity(analIntent);
            }
        });

        DatabaseReference outfitDBRef = database.getReference().child("outfits").child(outfitID);
        StorageReference outfitStorageRef = storage.getReference().child(userID).child("outfits").child(outfitID);

        outfitDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url = dataSnapshot.child("downloadURL").getValue().toString();
                Picasso.get().load(url).fit().into(holder.galleryItem);

                String topColor1 = dataSnapshot.child("top_color_1").getValue().toString();
                String topColor2 = dataSnapshot.child("top_color_2").getValue().toString();
                String botColor1 = dataSnapshot.child("bot_color_1").getValue().toString();
                String botColor2 = dataSnapshot.child("bot_color_2").getValue().toString();

                holder.testText.setText(colorMatch(topColor1, topColor2, botColor1, botColor2));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return outfitReferences.size();
    }


}
