import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Set;

import org.apache.commons.io.FileUtils;

public class Primes {

	final static ArrayList<Double> primes = new ArrayList<Double>();
	final static LinkedHashMap<Double, ArrayList<Double>> composites = new LinkedHashMap<Double, ArrayList<Double>>();
	final static LinkedHashMap<Double, StringBuffer> code = new LinkedHashMap<Double, StringBuffer>();

	public static void main(String[] args) {
		double p = 0;
		StringBuffer sb = new StringBuffer();
		StringBuffer csb = new StringBuffer();
		Date start = new Date();

		int i = 0;
		double prev = -1;
		print: for (double h = 1; h <= 2147483650.0; h++) {
			p = Math.sqrt((24 * h) + 1);

			if (p % 1 == 0) {
				for (double prime : primes) {
					if (p != prime && p % prime == 0) {
						if (!composites.containsKey(prime)) {
							composites.put(prime, new ArrayList<Double>());
							code.put(prime, new StringBuffer());
						}
						composites.get(prime).add(p);
						composites.get(prime).add(new Double(p / prime));
						composites.get(prime).add(new Double(h));
						continue print;
					}
				}
				primes.add(p);
				if (i % 6 == 0) {
					sb.append("\n");
				}
				String str = (i + 1) + ":" + new Double(p).intValue() + ":" + new Double(h).intValue() + ((prev > -1) ? ":" + (new Double(h - prev).intValue() - 1) : "N/A");
				prev = h;
				int len = str.length();
				if (len < 35) {
					for (int k = 0; k < 35 - len; k++) {
						str = " " + str;
					}
				}
				sb.append(str + "\t");
				i++;
			}
		}
		try {
			Date stop = new Date();
			sb.append("\n\nStart time: " + start + "\nEnd time: " + stop);
			sb.append("\n\nFormat\ncounter:prime:hour:diff");
			File f = new File(System.getProperty("user.home") + File.separator + "primes.txt");
			FileUtils.writeStringToFile(f, sb.toString());

			Set<Double> keys = composites.keySet();
			ArrayList<Double> d = composites.get(keys.toArray()[0]);
			row: for (int sz = 0; sz < d.size(); sz += 3) {
				column: for (Double key : keys) {
					ArrayList<Double> data = composites.get(key);

					if (sz >= data.size()) {
						break column;
					}

					csb.append("\t");
					int xz = data.get(sz).intValue();
					int root = data.get(sz + 1).intValue();
					int hour = data.get(sz + 2).intValue();
					int seq = hour / (xz + 1);
					boolean multiple = root % key == 0;

					StringBuffer codeSb = code.get(key);
					// the root of the space isn't part of the code
					if (sz > 0) {
						if (multiple && codeSb.length() > 4 && codeSb.charAt(codeSb.length() - 4) == '-') {
							codeSb.setCharAt(codeSb.length() - 4, '*');
							codeSb.append((hour + "").charAt((hour + "").length() - 1));
							codeSb.append('*');
							codeSb.append('\n');
						}
						else {
							if (multiple) codeSb.append('-');
							codeSb.append((hour + "").charAt((hour + "").length() - 1));
						}
					}

					String str = new Double(key).intValue() + ":" + xz + ":" + root + ":" + hour + ":" + seq + ((multiple) ? " <-- [" + new Double(root / key).intValue() + "] --" : "");
					int len = str.length();
					if (len < 45) {
						for (int k = 0; k < 45 - len; k++) {
							str = " " + str;
						}
					}
					csb.append(str);
				}
				csb.append("\n");
			}

			csb.append("\n\nStart time: " + start + "\nEnd time: " + stop);
			csb.append("\n\nFormat\nxz:root:hour:seq");
			File f2 = new File(System.getProperty("user.home") + File.separator + "composites.txt");
			FileUtils.writeStringToFile(f2, csb.toString());

			Set<Double> codeKeys = code.keySet();
			StringBuffer allCodes = new StringBuffer();
			for (double key : codeKeys) {
				allCodes.append("//////////////////////// [ " + key + " ] //////////////////////////////////\n\n");
				allCodes.append(code.get(key));
				allCodes.append("\n\n");
			}
			File f3 = new File(System.getProperty("user.home") + File.separator + "codes.txt");
			FileUtils.writeStringToFile(f3, allCodes.toString());

			Desktop.getDesktop().open(f);
			Desktop.getDesktop().open(f2);
			Desktop.getDesktop().open(f3);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}
