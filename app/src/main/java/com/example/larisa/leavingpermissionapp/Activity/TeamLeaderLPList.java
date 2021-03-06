/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Activity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.larisa.leavingpermissionapp.Adapters.LeavePermissionForTLAdapter;
import com.example.larisa.leavingpermissionapp.Model.LeavingPermission;
import com.example.larisa.leavingpermissionapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class TeamLeaderLPList extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TeamLeaderLPList";
    private RecyclerView recyclerView;
    private LeavePermissionForTLAdapter leavePermissionForTLAdapter;
    private Button doneConfirming;
    private TextView listDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_team_leader_lp_list);

        listDate = findViewById(R.id.lpListDate);
        recyclerView = findViewById(R.id.recycleViewLP);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        List<LeavingPermission> leavingPermissionList = (List<LeavingPermission>) intent.getSerializableExtra("TodayLP");

        if (leavingPermissionList != null) {
            listDate.setText(leavingPermissionList.get(0).getData());
        }

        leavePermissionForTLAdapter = new LeavePermissionForTLAdapter(TeamLeaderLPList.this, leavingPermissionList);
        recyclerView.setAdapter(leavePermissionForTLAdapter);
        leavePermissionForTLAdapter.notifyDataSetChanged();
        doneConfirming = findViewById(R.id.finishedReviewing);

        doneConfirming.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.finishedReviewing) {
            DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("Users");
            for (String key : leavePermissionForTLAdapter.modifiedLP.keySet()) {
                final LeavingPermission leavingPermission = leavePermissionForTLAdapter.modifiedLP.get(key);

                dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    if (snapshot.child("fullName").getValue(String.class).equals(leavingPermission.getFullName())) {
                                        for (DataSnapshot snapshot1 : snapshot.child("LeavingPermission").getChildren()) {
                                            if (snapshot1.getKey().equals(leavingPermission.getData())) {
                                                for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                    if (snapshot2.child("id").getValue().equals(key)) {
                                                        if (leavingPermission.getStatus().equals("confirmat")) {

                                                            Log.d(TAG, "onDataChange: leavingPermission.getId() = " + leavingPermission.getId());
                                                            AssetManager assetManager = getAssets();
                                                            InputStream myInput;

//                                                            try {
//
//                                                                myInput = assetManager.open("abc.xls");
//                                                                POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
//
//                                                                HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
//
//                                                                HSSFSheet mySheet = myWorkBook.getSheetAt(0);
//
//                                                                String fullNume[] = leavingPermission.getFullName().split(" ");
//
//                                                                HSSFCell cell;
//                                                                //Nume
//                                                                cell = mySheet.getRow(6).getCell(2);
//                                                                cell.setCellValue(fullNume[1].toUpperCase());
//                                                                //Prenume
//                                                                cell = mySheet.getRow(6).getCell(6);
//                                                                cell.setCellValue(fullNume[0]);
//
//
//                                                                //Matricol
//                                                                cell = mySheet.getRow(8).getCell(2);
//                                                                cell.setCellValue(leavingPermission.getUser().getRegistrationNumber());
//                                                                //Absent de la
//                                                                cell = mySheet.getRow(8).getCell(5);
//                                                                cell.setCellValue(leavingPermission.getData());
//                                                                //Absent pana la
//                                                                cell = mySheet.getRow(8).getCell(7);
//                                                                cell.setCellValue(leavingPermission.getData());
//                                                                //De la ora
//                                                                cell = mySheet.getRow(12).getCell(5);
//                                                                cell.setCellValue(leavingPermission.getFrom());
//                                                                //Pana la ora
//                                                                cell = mySheet.getRow(12).getCell(7);
//                                                                cell.setCellValue(leavingPermission.getTo());
//                                                                //Data depunere
//                                                                cell = mySheet.getRow(18).getCell(3);
//                                                                cell.setCellValue(leavingPermission.getData());
//                                                                //Data confirmare
//                                                                cell = mySheet.getRow(18).getCell(8);
//                                                                cell.setCellValue(leavingPermission.getData());
//                                                                //            //Adresa si numar de telefon
//                                                                cell = mySheet.getRow(21).getCell(1);
//                                                                cell.setCellValue(leavingPermission.getUser().getPhoneNumber());
//
//
//                                                                final InputStream stream =
//                                                                        TeamLeaderLPList.this.getAssets().open(fullNume[0] + fullNume[1] + ".png");
//                                                                byte[] imageBytes = IOUtils.toByteArray(stream);
//                                                                final int pictureIndex =
//                                                                        myWorkBook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);
//                                                                stream.close();
//                                                                final CreationHelper helper = myWorkBook.getCreationHelper();
//                                                                final Drawing drawing = mySheet.createDrawingPatriarch();
//
//                                                                final ClientAnchor anchor = helper.createClientAnchor();
//                                                                anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
//                                                                anchor.setCol1(3);
//                                                                anchor.setCol2(5);
//                                                                anchor.setRow1(15); // same row is okay
//                                                                anchor.setRow2(18);
//
//                                                                drawing.createPicture(anchor, pictureIndex);
//                                                                File path = TeamLeaderLPList.this.getFilesDir();
//
//                                                                FileOutputStream outFile =
//                                                                        new FileOutputStream(new File(path, "/" +
//                                                                                "Cerere_Absenta_" + fullNume[1].toUpperCase() + "_" + fullNume[0] + "_"
//                                                                                + leavingPermission.getData() + "_" + leavingPermission.getFrom() + "_991" +
//                                                                                ".xls"));
//                                                                myWorkBook.write(outFile);
//                                                                outFile.close();
//
//                                                            } catch (IOException e) {
//                                                                e.printStackTrace();
//                                                            }
                                                            snapshot2.child("status").getRef().setValue("confirmat");
                                                        } else {
                                                            snapshot2.child("status").getRef().setValue("refuzat");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
