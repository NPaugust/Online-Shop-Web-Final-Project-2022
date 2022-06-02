package com.example.byAvgustine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.byAvgustine.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static java.lang.Thread.sleep;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView3;
    cartAdapter cartAdapter;
    FirebaseAuth mAuth;
    FirebaseDatabase root;
    DatabaseReference reff;
    DatabaseReference countreff;
    TextView total;
    FirebaseFirestore fStore;
    Button order;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        try {
            mAuth = FirebaseAuth.getInstance();
            //root = FirebaseDatabase.getInstance();
            fStore = FirebaseFirestore.getInstance();
            total = findViewById(R.id.total);
            order = findViewById(R.id.order);

            //try {
            String id = mAuth.getCurrentUser().getUid();
            //}catch (Exception e){}

            order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DocumentReference doc = fStore.collection("student").document(id);
                    doc.update("count", 0, "total", 0);

                    DatabaseReference cart = FirebaseDatabase.getInstance().getReference("Cart").child(id);
                    cart.removeValue();

                    Intent in = new Intent(Cart.this, HomeActivity.class);
                    startActivity(in);
                    Toast.makeText(getApplicationContext(), "Thank you for ordering!", Toast.LENGTH_SHORT).show();

                }
            });



            mAuth = FirebaseAuth.getInstance();
            total = findViewById(R.id.total);




            recyclerView3 = findViewById(R.id.recycler3);
            recyclerView3.setLayoutManager(new LinearLayoutManager(this));



            Log.d("TAGGG", id);
            root = FirebaseDatabase.getInstance();
            reff = root.getReference("Cart");
            FirebaseRecyclerOptions<cartModel> option =
                    new FirebaseRecyclerOptions.Builder<cartModel>()
                            .setQuery(reff.child(id), cartModel.class).build();


            cartAdapter = new cartAdapter(option);
            recyclerView3.setAdapter(cartAdapter);


            try {

                sleep(1300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            DocumentReference doc = fStore.collection("student").document(id);
            doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    root = FirebaseDatabase.getInstance();

                    User user = documentSnapshot.toObject(User.class);
                    int total1 = user.getTotal();
                    String total2 = String.valueOf(total1);


                }

            });



        } catch (Exception e){
            Toast.makeText(getApplicationContext(),"You're logged in as guest", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        try {

            cartAdapter.startListening();
        }catch (Exception e){}
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            cartAdapter.stopListening();
        }catch (Exception e){}
    }

}