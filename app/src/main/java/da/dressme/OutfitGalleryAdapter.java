package da.dressme;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OutfitGalleryAdapter extends Adapter<OutfitGalleryAdapter.ViewHolder> {

    private List<String> pictureReferences;
    private Context mContext;
    private String userID;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private Uri picUri;

    public OutfitGalleryAdapter(Context context, List<String> picRefs, String uID)
    {
        mContext = context;
        pictureReferences = picRefs;
        userID = uID;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout parentLayout;
        ImageView galleryItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.constraintLayout_outfitGall);
            galleryItem = itemView.findViewById(R.id.imageView_gallery_item);
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
        String picName = pictureReferences.get(pos);

        StorageReference pathReference = storage.getReference().child(userID + "/uploads/" + picName);

        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                picUri = uri;
                Picasso.get().load(picUri).fit().into(holder.galleryItem);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return pictureReferences.size();
    }


}
