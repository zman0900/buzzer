package com.cse694;

public enum PartySizes {
	ONE_TWO, THREE_FOUR, FIVE_SIX, SEVEN_PLUS;

	public String getNum() {
		String ans = "\b";
		switch (this) {
			case ONE_TWO :
				ans = "1-2";
				break;
			case THREE_FOUR :
				ans = "3-4";
				break;
			case FIVE_SIX :
				ans = "5-6";
				break;
			case SEVEN_PLUS :
				ans = "7+";
		}
		return ans;
	}
}
