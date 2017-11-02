package com.example.sakibmac.myapplication;

import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConversionHelper {

    private final String KEY_SUCCESS = "success";

    @Nullable
    public static List<Compliment_Bean> getcomment(JSONObject jObject) {

        try {
            JSONArray mallsListArray = jObject.getJSONArray("server_response");

            List<Compliment_Bean> tutorregister = new ArrayList<>();
            for (int i = 0; i < mallsListArray.length(); i++) {
                JSONObject tutorjson = mallsListArray.getJSONObject(i);
                Compliment_Bean tutorListBean = getcomments(tutorjson);
                tutorregister.add(tutorListBean);
            }
            return tutorregister;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Compliment_Bean getcomments(JSONObject jObject) throws JSONException {
        Compliment_Bean tutorData = new Compliment_Bean();

        if (jObject.getString("type").equals("parent")) {
            tutorData.setImage(jObject.getString("image"));
            tutorData.setName(jObject.getString("name"));
            tutorData.setEmail(jObject.getString("email"));
            tutorData.setCode(jObject.getString("code"));
            tutorData.setParentId(jObject.getInt("user_id"));
        } else if (jObject.getString("type").equals("teacher")) {
            tutorData.setImage(jObject.getString("image"));
            tutorData.setName(jObject.getString("teacher_name"));
            tutorData.setEmail(jObject.getString("email"));
            tutorData.setMobile(jObject.getString("mobile"));
            tutorData.setStudentid(jObject.getInt("user_id"));
            tutorData.setCode(jObject.getString("code"));
        } else if (jObject.getString("type").equals("student")) {
            tutorData.setImage(jObject.getString("image"));
            tutorData.setName(jObject.getString("name"));
            tutorData.setEmail(jObject.getString("email"));
            tutorData.setMobile(jObject.getString("mobile"));
            tutorData.setStudentid(jObject.getInt("user_id"));
            tutorData.setCode(jObject.getString("code"));
        } else if (jObject.getString("type").equals("subject")) {
            tutorData.setSubject(jObject.getString("subject"));
            tutorData.setTeacher(jObject.getString("teacher_name"));

        } else if (jObject.getString("type").equals("class_routine")) {
            tutorData.setSubject(jObject.getString("subject"));
            tutorData.setStart(jObject.getString("start_time"));
            tutorData.setEnd(jObject.getString("end_time"));

        } else if (jObject.getString("type").equals("notice")) {
            tutorData.setTitle(jObject.getString("notice_title"));
            tutorData.setNotice(jObject.getString("notice"));

        } else if (jObject.getString("type").equals("attendance")) {
            tutorData.setRoll(jObject.getString("roll_no"));
            tutorData.setName(jObject.getString("name"));
            tutorData.setStudentid(jObject.getInt("student_id"));
            tutorData.setStatus(jObject.getString("status"));
        } else if (jObject.getString("type").equals("exam")) {
            tutorData.setMarks(jObject.getString("mark"));
            tutorData.setMarksTotal(jObject.getString("mark_total"));
            tutorData.setComments(jObject.getString("comment"));
            tutorData.setRoll(jObject.getString("roll_no"));
            tutorData.setName(jObject.getString("student_name"));
            tutorData.setStudentid(jObject.getInt("student_id"));
        }

        return tutorData;
    }
}

