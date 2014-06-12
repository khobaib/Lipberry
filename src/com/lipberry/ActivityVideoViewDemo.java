package com.lipberry;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.VideoView;

public class ActivityVideoViewDemo extends Activity implements
OnPreparedListener, OnCompletionListener {

	private final static String START_LOADING_VIDEO = "start";
	private final static String STOP_LOADING_VIDEO = "stop";

	private MyHandler handler;
	private VideoView videoView;
	private EditText editText;
	private ProgressDialog progressBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.playvedio);
		videoView = (VideoView) findViewById(R.id.YoutubeVideoView);
		videoView.setOnPreparedListener(this);
		videoView.setOnCompletionListener(this);
		editText = (EditText) findViewById(R.id.EditTextUrl);
		progressBar = new ProgressDialog(this);
		progressBar.setMessage("Loading...");
		Button buttonPlay = (Button) findViewById(R.id.ButtonGo);
		buttonPlay.setOnClickListener(new ClickGo());
		handler = new MyHandler();
	}

	private class ClickGo implements OnClickListener {

		@Override
		public void onClick(View v) {
			new Thread(new Runnable() {

				@Override
				public void run() {

					Message msg = handler.obtainMessage();
					Bundle b = new Bundle();
					b.putString(START_LOADING_VIDEO, "");
					msg.setData(b);
					handler.sendMessage(msg);

				}
			}).start();
		}

	}

	protected static String extractYoutubeId(String url)
			throws MalformedURLException {
		String query = new URL(url).getQuery();
		String[] param = query.split("&");
		String id = null;
		for (String row : param) {
			String[] param1 = row.split("=");
			if (param1[0].equals("v")) {
				id = param1[1];
			}
		}
		return id;
	}

	public static String getUrlVideoRTSP(String urlYoutube) {
		try {
			String gdy = "http://gdata.youtube.com/feeds/api/videos/";
			DocumentBuilder documentBuilder = DocumentBuilderFactory
					.newInstance().newDocumentBuilder();
			String id = extractYoutubeId(urlYoutube);
			URL url = new URL(gdy + id);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			Document doc = documentBuilder.parse(connection.getInputStream());
			Element el = doc.getDocumentElement();
			NodeList list = el.getElementsByTagName("media:content");
			String cursor = urlYoutube;
			for (int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);
				if (node != null) {
					NamedNodeMap nodeMap = node.getAttributes();
					HashMap maps = new HashMap();
					for (int j = 0; j < nodeMap.getLength(); j++) {
						Attr att = (Attr) nodeMap.item(j);
						maps.put(att.getName(), att.getValue());
					}
					
					if (maps.containsKey("yt:format")) {
						String f = (String) maps.get("yt:format");
						if (maps.containsKey("url")) {
							cursor = (String) maps.get("url");
						}
						if (f.equals("1"))
							return cursor;
					}
				}
			}
			return cursor;
		} catch (Exception ex) {
			return urlYoutube;
		}

	}

	private class MyHandler extends Handler {
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			if (bundle.containsKey(START_LOADING_VIDEO)) {
				playVideo();
			} else if (bundle.containsKey(STOP_LOADING_VIDEO)) {
				progressBar.dismiss();
			}
		}
	}

	private void playVideo() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				progressBar = new ProgressDialog(ActivityVideoViewDemo.this);
				progressBar.setMessage("Loading...");
				progressBar.show();
				String url = editText.getText().toString();
				url = getUrlVideoRTSP(url);
				videoView.setVideoURI(Uri.parse(url));
				videoView.setMediaController(new MediaController(
						ActivityVideoViewDemo.this));
				videoView.requestFocus();
				videoView.start();
			}
		});

	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		Log.i("ON COMPLETION", "" + mp.getCurrentPosition());
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		Log.i("ON PREPARED", "" + mp.getCurrentPosition());

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Message msg = handler.obtainMessage();
				Bundle b = new Bundle();
				b.putString(STOP_LOADING_VIDEO, "");
				msg.setData(b);
				handler.sendMessage(msg);
			}
		});

	}

}