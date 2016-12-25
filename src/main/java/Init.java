/**
 * Created by lishiwei on 16/12/24.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;

public class Init {
    private static final String TRAINPATH = "./ml-1m/train.txt";
    private static final String TESTPATH = "./ml-1m/test.txt";
    private static final String SOURCEPATH = "./ml-1m/rating.dat";
    private static final String USERPATH = "./ml-1m/users.dat";
    private static final String MOVIEPATH = "./ml-1m/movies.dat";

    public static void main(String[] args) {
        int[][] userReverseSort;
        float[][] userSimilarity;
        //k代表近邻数目L
        int kNeighbor = 3;
        //得到每个用户的最近邻
        HashMap<String, List<String>> id2TopNeighbor;
        //id sex age occipation zipCode
        HashMap<String, Pearson> pearsonList = getUsersDat(USERPATH);
        //item List
        HashMap<String, Item> itemList = getItemDat(MOVIEPATH);
        getRatingDat(TRAINPATH, pearsonList, itemList);
        //建立用户倒排表
        userReverseSort = getReverseSortArr(pearsonList);
        //构建用户相似度矩阵
        userSimilarity = getUserSimilarity(pearsonList, userReverseSort);
        id2TopNeighbor = getTopSimilarity(userSimilarity, kNeighbor, pearsonList);
    }

    private static HashMap<String, List<String>> getTopSimilarity(float[][] userSimilarity, int k, HashMap<String, Pearson> pearsonList) {
        HashMap<String, List<String>> id2TopNeighbor = new HashMap<String, List<String>>();
        HashMap<Integer, Float> id2Similarity;
        List<String> topSimilarityNeighbor;
        int userCnt = pearsonList.size() + 1;
        for (int i = 1; i < userCnt; i++) {
            id2Similarity = new HashMap<Integer, Float>();
            for (int j = i + 1; j < userCnt; j++)
                id2Similarity.put(j, userSimilarity[i][j]);
            topSimilarityNeighbor = topSimilarityNeighborListByUserId(id2Similarity, k);
            id2TopNeighbor.put(String.valueOf(i), topSimilarityNeighbor);
        }

        return id2TopNeighbor;
    }

    private static List<String> topSimilarityNeighborListByUserId(HashMap<Integer, Float> id2Similarity, int k) {
        int i = 0;
        List<Map.Entry<Integer, Float>> list_Data = new ArrayList<Map.Entry<Integer, Float>>(id2Similarity.entrySet());
        List<String> nearestId = new ArrayList<String>();
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
        Collections.sort(list_Data, new Comparator<Map.Entry<Integer, Float>>() {
            public int compare(Map.Entry<Integer, Float> o1, Map.Entry<Integer, Float> o2) {
                if (o2.getValue().compareTo(o1.getValue()) > 0) {
                    return 1;
                } else {
                    return -1;
                }

            }
        });

        for (Map.Entry<Integer, Float> entry : list_Data) {
            if (i < k)
                nearestId.add(String.valueOf(entry.getKey()));
            else
                break;
            i++;
        }
        return nearestId;
    }

    //movies.dat
    private static HashMap<String, Item> getItemDat(String moviePath) {
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

    //users.dat
    private static HashMap<String, Pearson> getUsersDat(String userPath) {
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
        System.out.println("Finsh collect user.dat");
        return id2Pearson;
    }

    //ratings.dat 80% train data to it

    private static void getRatingDat(String ratingsPath, HashMap<String, Pearson> pearsonList, HashMap<String, Item> itemList) {
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

    private static void setReverSort(int[][] userReverseSort, Pearson u, Pearson v) {
        Set<String> movieIdRes;
        Set<String> uItemIdList = u.getItemIdList();
        Set<String> vItemIdList = v.getItemIdList();

        int uid = Integer.parseInt(u.getId());
        int vid = Integer.parseInt(v.getId());

        movieIdRes = getCommonItemList(uItemIdList, vItemIdList);
        if (movieIdRes.size() != 0) {
            userReverseSort[uid][vid] = 1;
            userReverseSort[vid][uid] = 1;
        }
    }

    private static int[][] getReverseSortArr(HashMap<String, Pearson> pearsonList) {
        int userCnt = pearsonList.size() + 1;
        int[][] userReverseSort = new int[userCnt][userCnt];
        String uId = "";
        String vId = "";
        for (int u = 1; u < userCnt; u++) {
            for (int v = u + 1; v < userCnt; v++) {
                uId = String.valueOf(u);
                vId = String.valueOf(v);
                if (pearsonList.containsKey(uId) && pearsonList.containsKey(vId)) {
                    setReverSort(userReverseSort, pearsonList.get(uId), pearsonList.get(vId));
                }
            }
        }
        return userReverseSort;
    }


    private static float[][] getUserSimilarity(HashMap<String, Pearson> pearsonList, int[][] userReverseSort) {
        System.out.println("Start collect user.dat");
        int userCnt = pearsonList.size() + 1;
        float[][] userSimilarity = new float[userCnt][userCnt];
        Pearson uP;
        Pearson vP;
        for (int u = 1; u < userCnt; u++) {
            for (int v = u + 1; v < userCnt; v++) {
                if (userReverseSort[u][v] == 1) {
                    uP = pearsonList.get(String.valueOf(u));
                    vP = pearsonList.get(String.valueOf(v));
                    userSimilarity[u][v] = pearsonSimilarity(uP, vP);
                } else {
                    userSimilarity[u][v] = 0;
                }
            }
        }
        System.out.println("Finsh collect user.dat");
        return userSimilarity;
    }


    private static Set<String> getCommonItemList(Set<String> uItemIdList, Set<String> vItemIdList) {
        Set<String> commonItemList = new HashSet<String>();
        commonItemList.addAll(uItemIdList);
        commonItemList.retainAll(vItemIdList);
        return commonItemList;
    }

    private static float pearsonSimilarity(Pearson u, Pearson v) {
        Set<String> uItemIdList = u.getItemIdList();
        Set<String> vItemIdList = v.getItemIdList();
        Set<String> uvCommonItemList = getCommonItemList(uItemIdList, vItemIdList);

        float sumU = 0;
        float sumV = 0;
        float sumUSq = 0;
        float sumVSq = 0;
        float sumUV = 0;
        float uScore = 0;
        float vScore = 0;
        int N = uvCommonItemList.size();

        for (String itemId : uvCommonItemList) {
            uScore = u.getItemId2Rating().get(itemId);
            vScore = v.getItemId2Rating().get(itemId);

            sumU += uScore;
            sumV += vScore;
            sumUSq += Math.pow(uScore, 2);
            sumVSq += Math.pow(vScore, 2);
            sumUV += uScore * vScore;
        }

        float numerator = sumUV - sumU * sumV / N;
        float denominator = (float) Math.sqrt((sumUSq - sumU * sumU / N)
                * (sumVSq - sumV * sumV / N));
        if (denominator == 0) {
            return 0;
        }


        float pearson = numerator / denominator;
        BigDecimal b = new BigDecimal(pearson);
        return b.setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    private static float cosineSimilarity(Pearson u, Pearson v) {
        return 1;
    }

    private static float timeSimilarity(Pearson u, Pearson v) {
        return 1;
    }
}

