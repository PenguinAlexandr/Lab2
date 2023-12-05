package stu.cn.ua.lab2;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;

public class AnswerService extends Service {

    private static final String TAG = "AnswerService";

    public class AnswerBinder extends Binder {
        AnswerService getService() {
            return AnswerService.this;
        }
    }

    private final IBinder binder = new AnswerBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public String generateAnswer(String userQuestion) {
        // Перевірка на пусте питання
        if (TextUtils.isEmpty(userQuestion)) {
            return "Введіть питання";
        }

        // Отримання даних користувача з SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_settings", MODE_PRIVATE);
        String firstName = sharedPreferences.getString("first_name", "");
        String lastName = sharedPreferences.getString("last_name", "");
        String birthDate = sharedPreferences.getString("birth_date", "");
        boolean isMale = sharedPreferences.getBoolean("is_male", false);

        // Розрахунок числових значень
        int numericValue = calculateNumericValue(firstName, lastName, birthDate, isMale, userQuestion);

        // Генерація відповіді на базі числового значення
        String[] answers = {"Так", "Ні", "Можливо", "Цілком вірогідно", "Малоймовірно", "Не знаю"};
        int index = numericValue % answers.length;
        return answers[index];
    }

    private int calculateNumericValue(String firstName, String lastName, String birthDate, boolean isMale, String userQuestion) {
        String combinedData = firstName + lastName + birthDate + (isMale ? "male" : "female") + userQuestion;
        int numericValue = 0;
        for (char c : combinedData.toCharArray()) {
            numericValue += c;
        }
        return numericValue;
    }
}
