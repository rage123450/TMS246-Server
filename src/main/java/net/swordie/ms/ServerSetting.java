package net.swordie.ms;

import net.swordie.ms.util.container.Triple;
import org.python.antlr.ast.Str;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class ServerSetting {

    public static void settingGoldApple(){
        try {
            FileInputStream setting = new FileInputStream("Properties/金蘋果.properties");
            Properties setting_ = new Properties();
            setting_.load(setting);
            setting.close();
            ServerConstants.goldapple.clear();
            ServerConstants.Sgoldapple.clear();
            String[] items = setting_.getProperty(toUni("金蘋果小獎")).split("\\{");
            String[] Sitems = setting_.getProperty(toUni("金蘋果大獎")).split("\\{");
            ServerConstants.SgoldappleSuc = Integer.parseInt(setting_.getProperty(toUni("金蘋果大獎機率")));
            if (items != null)
                for (String s : items) {
                    int itemid = 0, count = 0, suc = 0;
                    for (String s2 : s.replaceAll("},", "").replaceAll("}", "").replaceAll(" ", "").split(",")) {
                        if (s2.length() > 0)
                            if (itemid == 0) {
                                itemid = Integer.parseInt(s2);
                            } else if (count == 0) {
                                count = Integer.parseInt(s2);
                            } else if (suc == 0) {
                                suc = Integer.parseInt(s2);
                            }
                    }
                    if (itemid != 0 && count != 0 && suc != 0)
                        ServerConstants.goldapple.add(
                                new Triple<>(Integer.valueOf(itemid), Integer.valueOf(count), Integer.valueOf(suc)));
                }
            if (Sitems != null)
                for (String s : Sitems) {
                    int itemid = 0, count = 0, suc = 0;
                    for (String s2 : s.replaceAll("},", "").replaceAll("}", "").replaceAll(" ", "").split(",")) {
                        if (s2.length() > 0)
                            if (itemid == 0) {
                                itemid = Integer.parseInt(s2);
                            } else if (count == 0) {
                                count = Integer.parseInt(s2);
                            } else if (suc == 0) {
                                suc = Integer.parseInt(s2);
                            }
                    }
                    if (itemid != 0 && count != 0 && suc != 0)
                        ServerConstants.Sgoldapple.add(
                                new Triple<>(Integer.valueOf(itemid), Integer.valueOf(count), Integer.valueOf(suc)));
                }
            System.out.println("金蘋果普通物品 : " + ServerConstants.goldapple.size() + "個加載完成!");
            System.out.println("金蘋果特殊物品數量 : " + ServerConstants.Sgoldapple.size() + "個加載完成!");
            System.out.println("金蘋果特殊物品概率 : '" + ServerConstants.SgoldappleSuc + "%' 加載完成!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static String toUni(String str) throws UnsupportedEncodingException {
        return new String(str.getBytes("UTF-8"),"8859_1");
    }
}
