package com.example.freezetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import androidx.test.espresso.idling.CountingIdlingResource;

public class MainActivity extends AppCompatActivity {

    //Создание счетчика для вычисления кол-ва ресурсов.
    CountingIdlingResource idlingResource = new CountingIdlingResource("DATA_LOADER");

    //Создаем активити.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Симулируем многопоточность. В 1м потоке симулируется загрузка данных, вместо данных просто ожидаение 5сек.
    //Во втором потоке меняется текст на главной активити. Текст не меняется в течении 5 сек. Хотя в самом тесте
    //MainActivityTest проверка начинается сразу же после запуска.
    @Override
    protected void onResume() {
        super.onResume();
        //Увеличиваем счетчик ресурсов. Если счетчик больше 0, то Эспрессо понимает, что приложение выполняет какую-то работу.
        idlingResource.increment();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView MyText = (TextView)findViewById(R.id.textID);
                        MyText.setText(getString(R.string.data_loaded));
                        //Уменьшаем счетчик. Если счетчик равен 0, Эспрессо понимает, что приложение простаивает и можно продолжать тест.
                        idlingResource.decrement();
                    }
                });
            }
        }).start();
    }

    //Метод возвращающий количество ресурсов, который потом можно будет вызвать в тесте.
    public CountingIdlingResource getIdlingResourceInTest() {
        return idlingResource;
    }
}
