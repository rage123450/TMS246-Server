package net.swordie.ms.loaders;

import net.swordie.ms.ServerConstants;
import net.swordie.ms.client.Account;
import net.swordie.ms.client.User;
import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.chatemotion.ChatEmoticon;
import net.swordie.ms.client.character.chatemotion.ChatEmoticonSaved;
import net.swordie.ms.connection.db.DatabaseManager;
import net.swordie.ms.util.XMLApi;
import net.swordie.ms.util.container.Triple;
import net.swordie.ms.util.container.Tuple;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.python.modules._sre;
import org.w3c.dom.Node;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatEmoticonData {
    private static Map<Integer,  List<Integer>> emoticons = new ConcurrentHashMap<>();

    public static void LoadEmotionFromWz() {
        String wzDir = String.format("%s/UI.wz/", ServerConstants.WZ_DIR);
        String checkDir = wzDir + "ChatEmoticon.img.xml";
        File file = new File(checkDir);
        Node root = XMLApi.getRoot(file);
        Node mainNode = XMLApi.getAllChildren(root).get(0);
        for (Node emotionNode : XMLApi.getAllChildren(mainNode)) {
            int type = Integer.parseInt(XMLApi.getNamedAttribute(emotionNode, "name"));
            List<Node> emotionNodes = XMLApi.getAllChildren(mainNode);
            List<Integer> e_list = new ArrayList<>();
            for (Node dat : XMLApi.getAllChildren(emotionNode)) {
                String test = XMLApi.getNamedAttribute(dat, "name");
                if (isNumeric(XMLApi.getNamedAttribute(dat, "name"))) {
                    e_list.add(Integer.valueOf(Integer.parseInt(XMLApi.getNamedAttribute(dat, "name"))));
                }
            }
            getEmoticons().put(Integer.valueOf(type), e_list);
            //System.out.println(getEmoticons());
        }
    }
    public static void LoadChatEmoticonTabs(Char chr) {
        Session session = DatabaseManager.getSession();
        Transaction transaction = session.beginTransaction();

        Query loadEmoticonQuery = session.createNativeQuery("SELECT * FROM emoticon");
        loadEmoticonQuery.setParameter("charid", chr.getId());

        //ChatEmoticon em = new ChatEmoticon(chr.getId(), emoticonId, time, bookmarks);
        transaction.commit();
        session.close();
    }

    public static void LoadSavedChatEmoticon(Char chr) {
        Session session = DatabaseManager.getSession();
        Transaction transaction = session.beginTransaction();

        //Query loadEmoticonQuery = session.createNativeQuery("SELECT * FROM emoticon_saved ");
        /*
        ChatEmoticonSaved chatEmoticonSaved = new ChatEmoticonSaved(chr.getId(), ,);
        Query loadEmoticonQuery = session.createNativeQuery("INSERT INTO emoticon_saved (charid,emoticonid,chat) VALUES (:charid,:emoticonid,:chat)");
        loadEmoticonQuery.setParameter("charid", chr.getId());
        loadEmoticonQuery.setParameter("emoticonid", chatEmoticonSaved.getEmoticonid());

        List list = loadEmoticonQuery.list();
        */

        List<ChatEmoticonSaved> list = session.createQuery("FROM ChatEmoticonSaved WHERE charid = :charid").setParameter("charid", chr.getId()).list();
        System.out.println("1:"+list.get(1));
        for (ChatEmoticonSaved ces : list) {
            System.out.println(ces.getCharid());
        }

        int emoticonid = 0;


        transaction.commit();
        session.close();
    }

    public static void LoadChatEmoticons(Char chr, List<ChatEmoticon> ems) {
        for (ChatEmoticon em : ems) {
            List<Integer> e_list = getEmoticons().get(Integer.valueOf(em.getEmoticonid()));
            for (Iterator<Integer> iterator = e_list.iterator(); iterator.hasNext(); ) {
                int e = ((Integer)iterator.next()).intValue();
                short slot = 0;
                for (Tuple<Integer, Short> a : em.getBookmarks()) {
                    if (((Integer)a.getLeft()).intValue() == e && ((Short)a.getRight()).shortValue() > 0)
                        slot = ((Short)a.getRight()).shortValue();
                }
                Triple<Long, Integer, Short> p = new Triple<>(Long.valueOf(em.getTime()), Integer.valueOf(e), Short.valueOf(slot));
                chr.getEmoticons().add(p);
            }
        }
    }

    public static boolean isNumeric(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static Map<Integer,  List<Integer>> getEmoticons() {
        return emoticons;
    }

    public static void main(String[] args) {
        DatabaseManager.init();
        User user = User.getFromDBByName("william");
        Char chr = Char.getFromDBById(user.getId());
        LoadSavedChatEmoticon(chr);
        LoadEmotionFromWz();
    }
}
