package domain;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * This class has not been used in this iteration 
 * @author Josefinators team
 * @version first iteration
 *
 */
public class IntegerAdapter extends XmlAdapter<String, Integer> {

	@Override
	public Integer unmarshal(String s) {
		return Integer.parseInt(s);
	}

	@Override
	public String marshal(Integer number) {
		if (number == null) return "";

		return number.toString();
	}
}