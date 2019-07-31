package com.android.quyentraining.ultis;


import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.quyentraining.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class AppUltis {
    private int numberTemp;
    private int numTagDivineTemp;

    public void convertTime(long time, TextView view, boolean trackTimeWay) {
        long now = System.currentTimeMillis() / 1000;
        long timetemp = now - time;
        if (!trackTimeWay) {
            if (timetemp < 3600 && timetemp > 60) {
                SimpleDateFormat format = new SimpleDateFormat("mm");
                String dateTopic = format.format(timetemp * 1000);
                view.setText(dateTopic + AppConstain.MIN_QUESTION_AGOS);
            } else if (timetemp < 60) {
                SimpleDateFormat format = new SimpleDateFormat("ss");
                String dateTopic = format.format(timetemp * 1000);
                view.setText(dateTopic + AppConstain.SEC_QUESTION_AGOS);
            } else if (timetemp < 86400 && timetemp > 3600) {
                long dateTopic = timetemp / 3600;
                view.setText(String.valueOf(dateTopic) + AppConstain.HOURS_QUESTION_AGOS);
            } else if (timetemp < 604800 && timetemp > 86400) {
                long dateTopic = timetemp / 86400;
                view.setText(String.valueOf(dateTopic) + AppConstain.DAY_QUESTION_AGOS);
            } else {
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                String dateTopic = format.format(Long.valueOf(time) * 1000);
                view.setText(dateTopic);
            }
        } else {
            if (timetemp < 86400) {
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                view.setText(AppConstain.TODAY + format.format(time * 1000));
            } else if (timetemp < 604800 && timetemp > 86400) {
                long timetemp2 = timetemp / 86400;
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                switch ((int) timetemp2) {
                    case 0:
                    case 1:
                        DecimalFormat str = new DecimalFormat("##");
                        view.setText(str.format(timetemp2) + AppConstain.DAY_AGOS + format.format(time * 1000));
                        break;
                    default:
                        view.setText(AppConstain.YESTERDAY + format.format(time * 1000));
                }
            } else {
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                view.setText(format.format(time * 1000));
            }
        }
    }

    public void drawNumberVote(int numberVote, TextView view) {
        if (numberVote >= 1000) {
            numberTemp = numberVote % 1000;
            numberTemp = numberTemp / 100;
            numberVote = numberVote / 1000;
            if (numberTemp == 0) {
                view.setTextSize(18);
                view.setText(String.valueOf(numberVote) + "k");
            } else {
                view.setTextSize(12);
                view.setText(String.valueOf(numberVote) + "." + String.valueOf(numberTemp) + "k");
            }
        } else {
            view.setTextSize(18);
            view.setText(String.valueOf(numberVote));
        }
    }

    public void drawNumberAnswere(int numberAnswer, TextView view, ImageView imageView) {
        if (numberAnswer >= 1000) {
            numberTemp = numberAnswer % 1000;
            numberTemp = numberTemp / 100;
            numberAnswer = numberAnswer / 1000;
            view.setTextColor(Color.argb(255, 16, 143, 79));
            if (numberTemp == 0) {
                view.setTextSize(18);
                view.setText(String.valueOf(numberAnswer) + "k");
            } else {
                view.setTextSize(12);
                view.setText(String.valueOf(numberAnswer) + "." + String.valueOf(numberTemp) + "k");
            }
        } else if (numberAnswer > 0) {
            view.setTextColor(Color.argb(255, 16, 143, 79));
            view.setTextSize(18);
            view.setText(String.valueOf(numberAnswer));
            imageView.setImageResource(R.drawable.ic_answers_non_zero);
        } else {
            view.setTextSize(18);
            view.setText(String.valueOf(numberAnswer));
            imageView.setImageResource(R.drawable.answers);
            view.setTextColor(Color.GRAY);
        }
    }

    public void tagNumer(int numberTag, TextView view) {
        if (numberTag > 1000000) {
            numberTemp = numberTag % 1000000;
            numberTemp = numberTemp / 100000;
            numTagDivineTemp = numberTag / 1000000;
            view.setText(String.valueOf(numTagDivineTemp) + "," + String.valueOf(numberTemp) + "m");
        } else if (numberTag > 1000 && numberTag < 1000000) {
            numberTemp = numberTag % 1000;
            numberTemp = numberTemp / 100;
            numTagDivineTemp = numberTag / 1000;
            if (numberTag < 10000) {
                view.setText(String.valueOf(numTagDivineTemp) + "," + String.valueOf(numberTemp) + "k");
            } else {
                view.setText(String.valueOf(numTagDivineTemp) + "k");
            }
        } else {
            view.setText(String.valueOf(numberTag));
        }
    }

    public void topicVote(int numberVote,TextView view){
        if (numberVote >= 1000) {
            view.setTextSize(12);
            view.setText(String.valueOf(numberVote));
        } else {
            view.setText(String.valueOf(numberVote));
        }
    }
}
