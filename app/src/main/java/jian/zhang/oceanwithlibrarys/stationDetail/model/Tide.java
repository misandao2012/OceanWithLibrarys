package jian.zhang.oceanwithlibrarys.stationDetail.model;

public class Tide {
	private String mId;
	private String mTime;
	private String mFeet;
	private String mLowOrHigh;

	public String getId() {
		return mId;
	}

	public void setId(String id) {
		this.mId = id;
	}

	public String getTime() {
		return mTime;
	}

	public void setTime(String time) {
		mTime = time;
	}

	public String getFeet() {
		return mFeet;
	}

	public void setFeet(String feet) {
		mFeet = feet;
	}

	public String getLowOrHigh() {
		return mLowOrHigh;
	}

	public void setLowOrHigh(String lowOrHigh) {
		this.mLowOrHigh = lowOrHigh;
	}
}
