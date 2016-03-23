package jian.zhang.oceanwithlibrarys.stationDetail.model;

import com.google.gson.annotations.Expose;

public class Tide {
	private String mId;
	@Expose
	private String time;

	@Expose
	private String feet;

	@Expose
	private String tide;
	//private String mLowOrHigh;

	public String getTide() {
		return tide;
	}

	public void setTide(String tide) {
		this.tide = tide;
	}

	public String getId() {
		return mId;
	}

	public void setId(String id) {
		mId = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getFeet() {
		return feet;
	}

	public void setFeet(String feet) {
		this.feet = feet;
	}

	/*public String getLowOrHigh() {
		return mLowOrHigh;
	}

	public void setLowOrHigh(String lowOrHigh) {
		mLowOrHigh = lowOrHigh;
	}*/
}
