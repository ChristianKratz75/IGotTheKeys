//package IGotTheKeys;

public class MyComboItem {
	private String value;
	private String key;

	public MyComboItem(String key, String value) {
		this.value = value;
		this.key = key;
	}
	@Override
	public String toString() {
        return key;
    }
    public String getKey() {
        return key;
    }
	public String getValue() {
		return value;
	}
}