package com.fusionx.tilal6991.multiboot;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

public class HomeScreen extends Activity {
	private class CleaupAndExtract extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(final Void... arg0) {
			if (!new File("/data/data/com.fusionx.tilal6991.multiboot/files/zip")
					.exists()) {
				writeRawResource(R.raw.raw, "raw.tar");
				CommonFunctions
						.runRootCommand("tar -zxvf /data/data/com.fusionx.tilal6991.multiboot/files/raw.tar -C /data/data/com.fusionx.tilal6991.multiboot/files/");
				CommonFunctions
						.deleteIfExists("/data/data/com.fusionx.tilal6991.multiboot/files/raw.tar");
			}
			CommonFunctions
					.runRootCommand("chmod -R 777 /data/data/com.fusionx.tilal6991.multiboot/files/*");
			return null;
		}
		private void writeRawResource(final int resource, final String name) {
			if (!new File("/data/data/com.fusionx.tilal6991.multiboot/files/"
					+ name).exists())
				try {
					final InputStream in = getResources().openRawResource(resource);
					final byte[] buffer = new byte[4096];
					final OutputStream out = openFileOutput(name,
							Context.MODE_PRIVATE);
					int n = in.read(buffer, 0, buffer.length);
					while (n >= 0) {
						out.write(buffer, 0, n);
						n = in.read(buffer, 0, buffer.length);
					}
					in.close();
					out.close();
				} catch (final IOException e) {
				}
		}
	}

	public void createGapps(final View view) {
		final Intent intent = new Intent(this, Navigation.class);
		intent.putExtra("gapps", true);
		startActivity(intent);
	}

	public void createRom(final View view) {
		final Intent intent = new Intent(this, Navigation.class);
		intent.putExtra("gapps", false);
		startActivity(intent);
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		CommonFunctions
				.runRootCommand("cp /init.rc /sdcard/currentRom.init.rc");

		if (CommonFunctions.findTextInFile("/sdcard/currentRom.init.rc",
				"mount ext2 loop@"))
			findViewById(R.id.button_create_from_nand).setVisibility(4);
		CommonFunctions.deleteIfExists("/sdcard/currentRom.init.rc");
		findViewById(R.id.button_create_from_nand)
				.setVisibility(View.INVISIBLE);
		new CleaupAndExtract().execute(null, null);
	}

	public void openRomBoot(final View view) {
		final Intent intent = new Intent(this, BootRom.class);
		startActivity(intent);
	}
}
