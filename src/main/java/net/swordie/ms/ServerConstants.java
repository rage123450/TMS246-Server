package net.swordie.ms;

import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.util.container.Triple;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ServerConstants {
	public static final String DIR = System.getProperty("user.dir");
	public static final String WZ_DIR = DIR + "/wz";
	public static final String DAT_DIR = DIR + "/dat";
	public static final int MAX_CHARACTERS = JobConstants.LoginJob.values().length * 3;
	public static final String SCRIPT_DIR = DIR + "/scripts";
	public static final String RESOURCES_DIR = DIR + "/resources";
	public static final String HANDLERS_DIR = DIR + "/src/main/java/net/swordie/ms/handlers";
	public static final short VERSION = 248;
//	public static final String MINOR_VERSION = "3";
	public static final String MINOR_VERSION = "1";
	public static final byte LOCALE = 6;
	public static final boolean TEST_MODE = true; // ignore SpamHeader
	public static final boolean PACKET_LOG = true;
	public static final int LOGIN_PORT = 8484;
	public static final int API_PORT = 8483;
	public static final short CHAT_PORT = 0;
	public static final int BCRYPT_ITERATIONS = 10;
	public static final long TOKEN_EXPIRY_TIME = 60 * 24; // minutes
	public static boolean LOCAL_HOST_SERVER = false;
	public static final int RESTART_MINUTES = (int) getTimeTillMidnight();
	public static final boolean DAILY_RESTART = false;

	public static int SgoldappleSuc;
	public static List<Triple<Integer, Integer, Integer>> goldapple = new ArrayList<Triple<Integer, Integer, Integer>>();
	public static List<Triple<Integer, Integer, Integer>> Sgoldapple = new ArrayList<Triple<Integer, Integer, Integer>>();

	public static long getTimeTillMidnight() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return TimeUnit.MILLISECONDS.toMinutes(c.getTimeInMillis() - System.currentTimeMillis());
	}
}
