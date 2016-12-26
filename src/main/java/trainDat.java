import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * Created by lishiwei on 16/12/26.
 */
class trainDat {
    //users.dat
    static HashMap<String, Pearson> getUsersDat(String userPath) {
        System.out.println("Start collect user.dat");
        HashMap<String, Pearson> id2Pearson = new HashMap<String, Pearson>();

        try {
            FileReader reader = new FileReader(userPath);
            BufferedReader br = new BufferedReader(reader);
            String str;

            while ((str = br.readLine()) != null) {
                String id = str.split("::")[0];
                String sex = str.split("::")[1];
                String age = str.split("::")[2];
                String occupation = str.split("::")[3];
                String zipCode = str.split("::")[4];
                Pearson pearson = new Pearson(id, sex, age, occupation, zipCode);
                id2Pearson.put(id, pearson);
            }
            br.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("collect user.dat cost");
        return id2Pearson;
    }

    //movies.dat
    static HashMap<String, Item> getItemDat(String moviePath) {
        System.out.println("Start collect item.dat");
        HashMap<String, Item> id2Item = new HashMap<String, Item>();

        try {
            FileReader reader = new FileReader(moviePath);
            BufferedReader br = new BufferedReader(reader);
            String str;
            // id movieName geners
            while ((str = br.readLine()) != null) {
                String id = str.split("::")[0];
                String movieName = str.split("::")[1];
                String[] userid = str.split("::")[2].split("\\|");
                List<String> genersList = new ArrayList<String>();
                Collections.addAll(genersList, userid);
                id2Item.put(id, new Item(id, movieName, genersList));
            }
            br.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Finish collect item.dat");
        return id2Item;
    }

    static void setRatingDat(String ratingsPath, HashMap<String, Pearson> pearsonList, HashMap<String, Item> itemList) {
        System.out.println("Start collect rating.dat");
        HashMap<String, String> item2StampTime = new HashMap<String, String>();
        HashMap<String, List<Item>> id2Item = new HashMap<String, List<Item>>();
        HashMap<String, Float> id2Avg = new HashMap<String, Float>();
        HashMap<String, Integer> uid2ParticipateCnt = new HashMap<String, Integer>();
        HashMap<String, Integer> item2Hot = new HashMap<String, Integer>();
        HashMap<String, Float> item2Rating = new HashMap<String, Float>();

        //668::2424::3::975635036
        try {
            FileReader reader = new FileReader(ratingsPath);
            BufferedReader br = new BufferedReader(reader);
            String str;

            Rating rating;
            Item item;
            while ((str = br.readLine()) != null) {
                String id = str.split("::")[0];
                String movieId = str.split("::")[1];
                Float score = Float.valueOf(str.split("::")[2]);
                String timeStamp = str.split("::")[3];

                if (uid2ParticipateCnt.get(id) == null)
                    uid2ParticipateCnt.put(id, 1);
                else
                    uid2ParticipateCnt.put(id, uid2ParticipateCnt.get(id) + 1);

                if (id2Avg.get(id) == null)
                    id2Avg.put(id, score);
                else
                    id2Avg.put(id, id2Avg.get(id) + score);

                if (item2Hot.get(movieId) == null)
                    item2Hot.put(movieId, 1);
                else
                    item2Hot.put(movieId, item2Hot.get(movieId) + 1);

                List<Item> list;
                if (id2Item.get(id) == null) {
                    list = new ArrayList<Item>();
                    list.add(itemList.get(movieId));
                    id2Item.put(id, list);
                } else {
                    list = id2Item.get(id);
                    list.add(itemList.get(movieId));
                    id2Item.put(id, list);
                }

                item2Rating = pearsonList.get(id).getItemId2Rating();
                item2Rating.put(movieId, score);

                item2StampTime = pearsonList.get(id).getItemId2TimeStamp();
                item2StampTime.put(movieId, timeStamp);
            }
            br.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setUserParticipateCnt(uid2ParticipateCnt, pearsonList);
        setUserAvg(id2Avg, pearsonList);
        setItemHot(item2Hot, itemList);
        setVisitedList(id2Item, pearsonList);
        System.out.println("Finish collect rating.dat");
    }

    //user ParticipateCnt
    private static void setUserParticipateCnt(HashMap<String, Integer> uid2ParticipateCnt, HashMap<String, Pearson> pearsonList) {
        int participateCnt;
        String uid;
        for (Map.Entry<String, Integer> entry : uid2ParticipateCnt.entrySet()) {
            participateCnt = entry.getValue();
            uid = entry.getKey();
            pearsonList.get(uid).setParticipateCnt(participateCnt);
        }
    }

    //user AvgRating
    private static void setUserAvg(HashMap<String, Float> id2Avg, HashMap<String, Pearson> pearsonList) {
        float sumRating;
        int participateCnt;
        String uid;
        for (Map.Entry<String, Float> entry : id2Avg.entrySet()) {
            sumRating = entry.getValue();
            uid = entry.getKey();
            participateCnt = pearsonList.get(uid).getParticipateCnt();
            pearsonList.get(uid).setAvgRating(sumRating / participateCnt);
        }
    }

    //user itemList
    private static void setVisitedList(HashMap<String, List<Item>> id2Item, HashMap<String, Pearson> pearsonList) {
        String id;
        List<Item> visitedItemList;
        Set<String> visitSet;
        for (Map.Entry<String, List<Item>> entry : id2Item.entrySet()) {
            visitedItemList = entry.getValue();
            id = entry.getKey();
            pearsonList.get(id).setVisitedList(visitedItemList);
            visitSet = new HashSet<String>();
            for (Item aVisitedItemList : visitedItemList) {
                String movieId = aVisitedItemList.getId();
                visitSet.add(movieId);
            }
            pearsonList.get(id).setItemIdList(visitSet);
        }
    }

    //item hot
    private static void setItemHot(HashMap<String, Integer> item2Hot, HashMap<String, Item> itemList) {
        int itemHot;
        String itemId;
        for (Map.Entry<String, Integer> entry : item2Hot.entrySet()) {
            itemHot = entry.getValue();
            itemId = entry.getKey();
            itemList.get(itemId).setHot(itemHot);
        }
    }
}
