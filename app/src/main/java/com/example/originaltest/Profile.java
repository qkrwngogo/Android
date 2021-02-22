package com.example.originaltest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import nl.bryanderidder.themedtogglebuttongroup.ThemedToggleButtonGroup;

public class Profile extends Fragment {
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    ImageButton profileImage;
    StorageReference storageReference;


    public static Profile newInstance () {
        return new Profile();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TabLayout profileTab;
        RelativeLayout userContainer, informationContainer, equipmentContainer;
        userContainer = requireView().findViewById(R.id.userContainer);
        informationContainer = requireView().findViewById(R.id.informationContainer);
        equipmentContainer = requireView().findViewById(R.id.equipmentContainer);
        profileTab = requireView().findViewById(R.id.profileTab);
        profileTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                RelativeLayout selected = null;
                switch (position) {
                    case 0:
                        selected = userContainer;
                        informationContainer.setVisibility(View.INVISIBLE);
                        equipmentContainer.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        selected = informationContainer;
                        userContainer.setVisibility(View.INVISIBLE);
                        equipmentContainer.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        selected = equipmentContainer;
                        userContainer.setVisibility(View.INVISIBLE);
                        informationContainer.setVisibility(View.INVISIBLE);
                        break;
                }
                selected.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        profileImage = requireView().findViewById(R.id.profileImage);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        downloadImageToFirebase();
        ThemedToggleButtonGroup themedButtonGroup1 = requireView().findViewById(R.id.buttonGroup1);
        ThemedToggleButtonGroup themedButtonGroup2 = requireView().findViewById(R.id.buttonGroup2);
        ThemedToggleButtonGroup themedButtonGroup3 = requireView().findViewById(R.id.buttonGroup3);
        selectedButtons(themedButtonGroup1);
        selectedButtons(themedButtonGroup2);
        selectedButtons(themedButtonGroup3);
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000) {
            if(resultCode == Activity.RESULT_OK) {
                assert data != null;
                Uri imageUri = data.getData();
                // profileImage.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        //firebase 저장소에 업로드
        StorageReference fileRef = storageReference.child("uers/"+ Objects.requireNonNull(fAuth.getCurrentUser()).getUid()+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(profileImage)));
    }
    public void downloadImageToFirebase() {
        if(fAuth.getCurrentUser() != null) {
            profileImage.setOnClickListener(v -> {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            });

            StorageReference profileRef = storageReference.child("uers/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
            profileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                profileImage.setBackgroundColor(Color.TRANSPARENT);
                Picasso.get().load(uri).into(profileImage);
            });
        }
    }

    public void selectedButtons(ThemedToggleButtonGroup group) {
        group.setOnSelectListener( themedButton -> {
            // 저장 공간 생성
            Bundle bundle = new Bundle();
            // 선택한 버튼의 태그명
            bundle.putString("equipmentString", themedButton.getTag().toString());
            // 선택한 버튼의 On/Off 여부
            bundle.putBoolean("equipmentBoolean", themedButton.isSelected());
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            Routine routine = new Routine();
            routine.setArguments(bundle);
            transaction.replace(R.id.frg_routine, routine);
            transaction.commit();
            return null;
        });
    }

}
