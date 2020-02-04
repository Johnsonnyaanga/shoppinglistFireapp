package com.example.countyapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.countyapp.model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.text.DateFormat;
import java.util.Date;


public class HomeActivity extends AppCompatActivity {
private Toolbar toolbar;
private FloatingActionButton fab_btn;
private EditText type;
    private EditText amount;
    private EditText note;
    private RecyclerView recyclerView;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar=findViewById(R.id.home_toolbar);
        fab_btn= findViewById(R.id.fab_btn);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Daily shopping list ");
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid=mUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Shopping list").child(uid);
        mDatabase.keepSynced(true);


        recyclerView =findViewById(R.id.recycler_home);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

fab_btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        customDialog();

    }
});
    }

 private void customDialog(){
        AlertDialog.Builder mydialog = new AlertDialog.Builder(HomeActivity.this);
     LayoutInflater inflater  = LayoutInflater.from(HomeActivity.this);
     View myview = inflater.inflate(R.layout.input_data, null);
     final AlertDialog dialog = mydialog.create();
     dialog.setView(myview);

      type =myview.findViewById(R.id.edt_type);
      amount= myview.findViewById(R.id.edt_amount);
      note=myview.findViewById(R.id.edt_note);
     Button btnsave=myview.findViewById(R.id.btn_save);

     btnsave.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             String mType= type.getText().toString().trim();
             String mAmount=amount.getText().toString().trim();
             String mnote=note.getText().toString().trim();
             int ammint=Integer.parseInt(mAmount);


             if(TextUtils.isEmpty(mType)){
                 type.setError("Required field");
                 return;
             }
             if(TextUtils.isEmpty(mAmount)){
                 amount.setError("Required field");
                 return;
             }
             if(TextUtils.isEmpty(mnote)){
                 note.setError("Required field");
                 return;
             }
             String id=mDatabase.push().getKey();
             String date= DateFormat.getDateInstance().format(new Date());
             Data data= new Data(mType,ammint,mnote,date,id);
             mDatabase.child(id).setValue(data);
             Toast.makeText(getApplicationContext(),"Data Add", Toast.LENGTH_SHORT).show();


             dialog.dismiss();

         }
     });
     dialog.show();
 }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Data,MyViewHolder> adapter=new FirebaseRecyclerAdapter<Data, MyViewHolder>
                (
                        Data.class,
                        R.layout.item_data,
                        MyViewHolder.class,
                        mDatabase
                )
        {
            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, final Data model, final int position) {

                viewHolder.setDate(model.getDate());
                viewHolder.setType(model.getType());
                viewHolder.setNote(model.getNote());
                viewHolder.setAmmount(model.getAmount());



            }
        };

        recyclerView.setAdapter(adapter);

    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        View myview;

        public MyViewHolder(View itemView) {
            super(itemView);
            myview=itemView;
        }

        public void setType(String type){
            TextView mType=myview.findViewById(R.id.type);
            mType.setText(type);
        }

        public void setNote(String note){
            TextView mNote=myview.findViewById(R.id.note);
            mNote.setText(note);
        }

        public void setDate(String date){
            TextView mDate=myview.findViewById(R.id.date);
            mDate.setText(date);
        }

        public void setAmmount(int ammount){

            TextView mAmount=myview.findViewById(R.id.amount);
            String stam=String.valueOf(ammount);
            mAmount.setText(stam);

        }



    }






}




