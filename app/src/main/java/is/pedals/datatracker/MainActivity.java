package is.pedals.datatracker;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.TrafficStats;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<AppStats> appStatsList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> applications = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo app : applications) {
            try {
                CharSequence appName = app.loadLabel(pm);
                appStatsList.add(new AppStats(this, appName, app.uid));
            } catch (Resources.NotFoundException e) {
            }
        }
        Collections.sort(appStatsList);
        ArrayAdapter<AppStats> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, appStatsList);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AppStats app = appStatsList.get(i);
                Intent intent = new Intent(MainActivity.this, ApplicationView.class);
                intent.putExtra("appname", app.name);
                intent.putExtra("uid", app.uid);
                startActivity(intent);
            }
        });
    }
}
