package com.example.larisa.leavingpermissionapp.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import com.example.larisa.leavingpermissionapp.Adapters.RecycleViewAdapterLP;
import com.example.larisa.leavingpermissionapp.Model.LP;
import com.example.larisa.leavingpermissionapp.Model.User;
import com.example.larisa.leavingpermissionapp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateLongClickListener;
import com.prolificinteractive.materialcalendarview.format.DayFormatter;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;

public class FinalCalendar extends AppCompatActivity {
    private MaterialCalendarView calendarView;
    private List<LP> sendLP = new ArrayList<>();
    private Button backToTeam;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
//       DatabaseReference dbReference;
//        dbReference = FirebaseDatabase.getInstance().getReference("Users");
//        dbReference.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                finish();
//                Intent intent2 = new Intent(FinalCalendar.this, FinalCalendar.class);
//                startActivity(intent2);
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });




       final List<LP> lps= (List<LP>) intent.getSerializableExtra("Lps");
       setContentView(R.layout.activity_final_calendar);
        calendarView = findViewById(R.id.calendarView);
        final List<CalendarDay> EventDays = new ArrayList<>();
        backToTeam=findViewById(R.id.backTeamButton);



  for (final LP lp : lps)
  {

      if(lp.getStatus().equals("neconfirmat"))
      {  String dateFormat = lp.getData();
          final CalendarDay newDate = dayConverter(dateFormat);
          calendarView.addDecorator(new DayViewDecorator() {
              @Override
              public boolean shouldDecorate(CalendarDay day) {
                  if(newDate.equals(day))
                  {
                      EventDays.add(day);
                  }
                  return newDate.equals(day);
              }

              @Override
              public void decorate(DayViewFacade view) {

                  view.addSpan(new DotSpan(Color.RED));



              }
          });

//
      }
  else {
       String dateFormat = lp.getData();
      final CalendarDay newDate = dayConverter(dateFormat);
       calendarView.addDecorator(new DayViewDecorator() {
           public boolean shouldDecorate(CalendarDay day) {
               if(newDate.equals(day))
                {
                    EventDays.add(day);
                }                return newDate.equals(day);
             }

             public void decorate(DayViewFacade view) {
                 view.setBackgroundDrawable(getResources().getDrawable(R.drawable.greencircle2));

            }
         });
    }






      calendarView.setOnDateLongClickListener(new OnDateLongClickListener() {
          @Override
          public void onDateLongClick(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date) {
              if(EventDays.contains(date))
              {
                  Intent intent = new Intent(FinalCalendar.this, LPCalendarList.class);
                  for(LP lp :lps)
                  {
                      if(dayConverter(lp.getData()).equals(date))
                      {
                          sendLP.add(lp);
                      }
                  }
                  intent.putExtra("TodayLP", (Serializable)sendLP);
//                  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                  startActivity(intent);
//                  finish();
                  sendLP.clear();



              }


          }
      });

  }


backToTeam.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

       Intent intent = new Intent(FinalCalendar.this,ViewTeam.class);
       intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
       startActivity(intent);



    }
});




    }

    CalendarDay dayConverter (String date)
    { SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String[] convertedDate = new String[0];
        try {
            convertedDate = (sdf2.format(sdf.parse(date))).split("-");


        } catch (ParseException e) {
            e.printStackTrace();
        }
        CalendarDay newDate = CalendarDay.from(Integer.valueOf(convertedDate[0]), Integer.valueOf(convertedDate[1]), Integer.valueOf(convertedDate[2]));
        return newDate;

    }
    @Override
    public void onBackPressed()
    {
        finish();
        Intent intent = new Intent(this,ViewTeam.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

}
