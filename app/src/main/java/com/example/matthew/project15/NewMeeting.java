// Description: This page is where a user would create a new meeting. After the user correctly
// fills in the required fields the details entered in those fields are uploaded to the database.

package com.example.matthew.project15;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class NewMeeting extends AppCompatActivity {
    TextInputEditText TextInputMeetingName, TextInputMeetingDescription, TextInputMeetingDate, TextInputMeetingTime, TextInputMeetingDuration;
    int intMeet_id, intRoom_id, intEmp_id;
    Button btnCreate_meeting, btnLog_out;
    RequestQueue requestQueue;
    //String insertMeetUrl = "http://192.168.2.6/androidphp/insertMeeting.php";
    //String getDeptUrl = "http://192.168.2.6/androidphp/getDepartments.php";
    //String getRoomUrl = "http://192.168.2.6/androidphp/getRooms.php";
    //String getEmpUrl = "http://192.168.2.6/androidphp/getEmployees.php";
    //String getMeetUrl = "http://192.168.2.6/androidphp/getCurrentMeeting.php";
    String insertMeetUrl = "http://thewheretwo.site/insertMeeting.php";
    String getDeptUrl = "http://thewheretwo.site/getDepartments.php";
    String getRoomUrl = "http://thewheretwo.site/getRooms.php";
    String getEmpUrl = "http://thewheretwo.site/getEmployees.php";
    String getMeetUrl = "http://thewheretwo.site/getCurrentMeeting.php";
    Spinner deptSpinner, roomSpinner, empSpinner;
    TextView meetTextView;
    SharedPreferences sharedMeetID;
    SharedPreferences.Editor editorMeetID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newmeeting_activity);

        btnLog_out = (Button) findViewById(R.id.btnLogout);
        btnCreate_meeting = (Button) findViewById(R.id.btnCreate_Meeting);

        TextInputMeetingName = (TextInputEditText) findViewById(R.id.txtMeetingName);
        TextInputMeetingDescription = (TextInputEditText) findViewById(R.id.txtMeetingDesc);
        TextInputMeetingDate = (TextInputEditText) findViewById(R.id.txtMeetingDate);
        TextInputMeetingTime = (TextInputEditText) findViewById(R.id.txtMeetingTime);
        TextInputMeetingDuration = (TextInputEditText) findViewById(R.id.txtMeetingDuration);

        sharedMeetID = PreferenceManager.getDefaultSharedPreferences(this);

        final ArrayList<String> ArrDeptNames = new ArrayList<>();
        final ArrayList<String> ArrRoomNum = new ArrayList<>();
        final ArrayList<String> ArrEmpNames = new ArrayList<>();
        final String[] meetNum = {""};
        ArrDeptNames.add("Select a department...");
        ArrRoomNum.add("Select a room...");
        ArrEmpNames.add("Select an organizer...");

        requestQueue = Volley.newRequestQueue(getApplicationContext());


        // Get the departments from the departments table in the database. Copy into a JSON array
        // and use the results to populate a drop down menu of the departments
        JsonObjectRequest jsonDeptObjectRequest = new JsonObjectRequest(Request.Method.POST,
                getDeptUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonDeptArray = response.getJSONArray("departments");

                    for (int i = 0; i < jsonDeptArray.length(); i++) {

                        JSONObject jsonDeptObject = jsonDeptArray.getJSONObject(i);
                        ArrDeptNames.add(jsonDeptObject.optString("Meeting_Dept_Name"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                deptSpinner = (Spinner) findViewById(R.id.deptSpinner);
                deptSpinner.setAdapter(new ArrayAdapter<>(NewMeeting.this, android.R.layout.simple_spinner_dropdown_item, ArrDeptNames));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.append(error.getMessage());
            }

        });

        requestQueue.add(jsonDeptObjectRequest);



        // Get the rooms from the rooms table in the database. Copy into a JSON array
        // and use the results to populate a drop down menu of all the rooms
        JsonObjectRequest jsonRoomObjectRequest = new JsonObjectRequest(Request.Method.POST,
                getRoomUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonRoomArray = response.getJSONArray("rooms");

                    for (int i = 0; i < jsonRoomArray.length(); i++) {

                        JSONObject jsonRoomObject = jsonRoomArray.getJSONObject(i);
                        ArrRoomNum.add("Room " + jsonRoomObject.optString("Room_Num"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                roomSpinner = (Spinner) findViewById(R.id.roomSpinner);
                roomSpinner.setAdapter(new ArrayAdapter<>(NewMeeting.this, android.R.layout.simple_spinner_dropdown_item, ArrRoomNum));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.append(error.getMessage());
            }

        });
        requestQueue.add(jsonRoomObjectRequest);



        // Get the employees from the employees table in the database. Copy into a JSON array
        // and use the results to populate a drop down menu of all the company employees
        JsonObjectRequest jsonEmployeeObjectRequest = new JsonObjectRequest(Request.Method.POST,
                getEmpUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonEmpArray = response.getJSONArray("employee");

                    for (int i = 0; i < jsonEmpArray.length(); i++) {

                        JSONObject jsonEmpObject = jsonEmpArray.getJSONObject(i);
                        ArrEmpNames.add(jsonEmpObject.optString("UsrFirst_Name") + " " + jsonEmpObject.optString("UsrLast_Name"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                empSpinner = (Spinner) findViewById(R.id.empSpinner);
                empSpinner.setAdapter(new ArrayAdapter<>(NewMeeting.this, android.R.layout.simple_spinner_dropdown_item, ArrEmpNames));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.append(error.getMessage());
            }

        });
        requestQueue.add(jsonEmployeeObjectRequest);



        // Get the meetings from the meetings table in the database. Copy into a JSON array
        // and use the results to a shared variable and to show what the meeting number is
        // at the top of the page.
        JsonObjectRequest jsonMeetObjectRequest = new JsonObjectRequest(Request.Method.POST,
                getMeetUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonMeetArray = response.getJSONArray("mtngid");

                    for (int i = 0; i < jsonMeetArray.length(); i++) {

                        JSONObject jsonRoomObject = jsonMeetArray.getJSONObject(i);
                        meetNum[0] = (jsonRoomObject.optString("Meeting_ID"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                int mtId = Integer.valueOf(meetNum[0]) + 1;

                meetNum[0] = Integer.toString(mtId);
                meetTextView = (TextView) findViewById(R.id.meetId);
                meetTextView.append(meetNum[0]);


                editorMeetID = sharedMeetID.edit();
                editorMeetID.putString("sharedmeetid", meetNum[0]);
                editorMeetID.apply();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.append(error.getMessage());
            }

        });

        requestQueue.add(jsonMeetObjectRequest);




        btnCreate_meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // gather all the information the user has entered/selected
                intMeet_id = deptSpinner.getSelectedItemPosition();
                intRoom_id = roomSpinner.getSelectedItemPosition() ;
                intEmp_id = empSpinner.getSelectedItemPosition();

                String checkName = TextInputMeetingName.getText().toString();
                String checkDesc = TextInputMeetingDescription.getText().toString();
                String checkDate = TextInputMeetingDate.getText().toString();
                String checkTime = TextInputMeetingTime.getText().toString();
                String checkDuration = TextInputMeetingDuration.getText().toString();

                // lines 240-272 are used to validate input
                if(TextUtils.isEmpty(checkName)) {
                    TextInputMeetingName.setError("Missing a name");
                    return;
                }
                if(TextUtils.isEmpty(checkDesc)) {
                    TextInputMeetingDescription.setError("Missing a description");
                    return;
                }
                if(TextUtils.isEmpty(checkDate)) {
                    TextInputMeetingDate.setError("Missing a date");
                    return;
                }
                if(TextUtils.isEmpty(checkTime)) {
                    TextInputMeetingTime.setError("Missing a time");
                    return;
                }
                if(TextUtils.isEmpty(checkDuration)) {
                    TextInputMeetingDuration.setError("Missing a duration");
                    return;
                }
                if(deptSpinner.getSelectedItemPosition() == 0){
                    Toast.makeText(getApplicationContext(), "Please choose a department" , Toast.LENGTH_SHORT).show();
                    return;
                }
                if(roomSpinner.getSelectedItemPosition() == 0){
                    Toast.makeText(getApplicationContext(), "Please choose a room" , Toast.LENGTH_SHORT).show();
                    return;
                }
                if(empSpinner.getSelectedItemPosition() == 0){
                    Toast.makeText(getApplicationContext(), "Please choose an organizer" , Toast.LENGTH_SHORT).show();
                    return;
                }

                // Send the results of what the user has selected to the database.
                StringRequest request = new StringRequest(Request.Method.POST, insertMeetUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.names().get(0).equals("success")) {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), InviteEmployees.class));
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Error: " + jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<>();
                        parameters.put("Meeting_Name", TextInputMeetingName.getText().toString());
                        parameters.put("Meeting_Description", TextInputMeetingDescription.getText().toString());
                        parameters.put("Meeting_DateTime", TextInputMeetingDate.getText().toString() + " " + TextInputMeetingTime.getText().toString());
                        parameters.put("Meeting_Duration", TextInputMeetingDuration.getText().toString());
                        parameters.put("Meeting_Dept_ID", Integer.toString(intMeet_id));
                        parameters.put("Meeting_Room_ID", Integer.toString(intRoom_id));
                        parameters.put("Organizer_ID", Integer.toString(intEmp_id));

                        return parameters;
                    }
                };
                requestQueue.add(request);


            }
        });


        btnLog_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }


}
