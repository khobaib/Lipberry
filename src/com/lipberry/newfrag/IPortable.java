package com.lipberry.newfrag;

import android.os.Bundle;

public interface IPortable {

	public void back();
	public void switchFragment(String tag, Bundle arg, boolean stack);
}
