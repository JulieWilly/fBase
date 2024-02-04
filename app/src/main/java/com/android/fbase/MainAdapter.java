package com.android.fbase;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends FirebaseRecyclerAdapter<Model,MainAdapter.myViewHolder> {

    public MainAdapter(@NonNull FirebaseRecyclerOptions<Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder,final int position, @NonNull Model model) {
        holder.name.setText(model.getName());
        holder.course.setText(model.getCourse());
        holder.email.setText(model.getEmail());

        Glide.with(holder.img.getContext())
                .load(model.getSurl())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);

        // bind the views here.
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_pop_up))
                        .setExpanded(true,1300)
                        .create();
                dialogPlus.show();

                View vw = dialogPlus.getHolderView();

                EditText name = vw.findViewById(R.id.userName);
                EditText course = vw.findViewById(R.id.userCourse);
                EditText email = vw.findViewById(R.id.userEmail);
                EditText surl = vw.findViewById(R.id.userImgUrl);
                Button btnUpdate = vw.findViewById(R.id.btnUpdate);

                name.setText(model.getName());
                course.setText(model.getCourse());
                email.setText(model.getSurl());
                surl.setText(model.getSurl());

                dialogPlus.show();

                btnUpdate.setOnClickListener(v ->{
                    Map<String,Object> map = new HashMap<>();
                    map.put("name", name.getText().toString());
                    map.put("course", course.getText().toString());
                    map.put("email",email.getText().toString());
                    map.put("surl", surl.getText().toString());

                    FirebaseDatabase.getInstance().getReference().child("Students")
                            .child(getRef(position).getKey()).updateChildren(map)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(holder.name.getContext(), "Data updated successfully", Toast.LENGTH_SHORT).show();
                                    // to minimize the dialog after updating data in it.
                                    dialogPlus.dismiss();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(holder.name.getContext(), "Error while updating data", Toast.LENGTH_SHORT).show();
                                    dialogPlus.dismiss();
                                }
                            });
                });
                holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.name.getContext());
                        builder.setTitle("Are you sure you want delete your details");
                        builder.setMessage("Deleted data cannot be undone.");
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseDatabase.getInstance().getReference().child("Students")
                                        .child(getRef(position).getKey()).removeValue();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(holder.name.getContext(), "Canceled", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate the layout manager.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_items, parent, false);
        //pass the view to create a new view ata every () -> instance
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        CircleImageView img;
        TextView name, course, email;
        Button btnEdit, btnDelete;
        // constructor
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img = (CircleImageView) itemView.findViewById(R.id.img1);
            name = (TextView) itemView.findViewById(R.id.txtName);
            course = (TextView) itemView.findViewById(R.id.txtCourse);
            email = (TextView) itemView.findViewById(R.id.txtEmail);

            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
            btnEdit = (Button) itemView.findViewById(R.id.btnEdit);
        }
    }
}