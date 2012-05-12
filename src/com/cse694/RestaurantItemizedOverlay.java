package com.cse694;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.sax.StartElementListener;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class RestaurantItemizedOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> overlays = new ArrayList<OverlayItem>();
	private Context context;

	public RestaurantItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	public RestaurantItemizedOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		this.context = context;
	}

	public void addOverlay(OverlayItem overlay) {
		overlays.add(overlay);
		populate();
	}

	@Override
	protected boolean onTap(int index) {
		OverlayItem item = overlays.get(index);
		//AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		//dialog.setTitle(item.getTitle());
		//dialog.setMessage(item.getSnippet());
		//dialog.show();
		
		Intent i = new Intent(context, CheckinActivity.class);
		i.putExtra("com.cse694.buzzer.RestaurantId", item.getSnippet());
		context.startActivity(i);
		
		return true;
	}

	@Override
	protected OverlayItem createItem(int index) {
		return overlays.get(index);
	}

	@Override
	public int size() {
		return overlays.size();
	}

}
