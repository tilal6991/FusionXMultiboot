package com.fusionx.tilal6991.dualboot;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FilenameFilter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class BootRom extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_romboot);

        LinearLayout ll = (LinearLayout) findViewById(R.id.layout);

        final String multibootdir = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/multiboot/";

        File root = new File(multibootdir);
        if (root.exists())
        {
            final String files[] = root.list(audioFilter);
            
            runRootCommands("chmod 777 " + multibootdir + "*.sh");

            for (final String file : files) {
                Button btn = new Button(this);
                btn.setText(file);
                ll.addView(btn);
                btn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        DisplayToast("Rebooting into specified ROM");
                        runRootCommands("sh " + multibootdir + file);
                    }
                });
            }
        }
    }
    private String runRootCommands(String cmd) {
        Process p = null;
        StringBuilder sb = new StringBuilder();
        try {
            p = Runtime.getRuntime().exec("su");
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            String read = br.readLine();
            while (read != null) {
                sb.append(read + '\n');
                read = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    FilenameFilter audioFilter = new FilenameFilter() {
        public boolean accept(File dir, String name) {
            if (name.endsWith(".sh"))
                return true;
            else
                return false;
        }
    };

    public void DisplayToast(String paramString) {
        Toast.makeText(this, paramString, Toast.LENGTH_SHORT).show();
    }
}