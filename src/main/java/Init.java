/**
 * Created by lishiwei on 16/12/24.
 */

import java.util.*;

public class Init {
    static long pervtime = 0;

    public static void main(String[] args) {
        int[][] userReverseSort;
        float[][] userSimilarity;
        //k代表近邻数目L
        int kNeighbor = 3;
        //得到每个用户的最近邻
        HashMap<String, List<String>> id2TopNeighbor;
        //id sex age occipation zipCode
        HashMap<String, Pearson> pearsonList = trainDat.getUsersDat(Constant.USERPATH);
        //item List
        HashMap<String, Item> itemList = trainDat.getItemDat(Constant.MOVIEPATH);
        trainDat.setRatingDat(Constant.TRAINPATH, pearsonList, itemList);
        //建立用户倒排表
        userReverseSort = getReverseSortArr(pearsonList);
        //构建用户相似度矩阵
        userSimilarity = getUserSimilarity(pearsonList, userReverseSort);
        id2TopNeighbor = getTopSimilarity(userSimilarity, kNeighbor, pearsonList);
        for (Map.Entry<String, List<String>> x : id2TopNeighbor.entrySet()) {
            System.out.println(x.getKey() + "的邻居为");
            for (String str : x.getValue())
                System.out.println(str);
        }
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

    private static int[][] getReverseSortArr(HashMap<String, Pearson> pearsonList) {
        long prev = System.currentTimeMillis();
        System.out.println("Start collect ReverseSort");
        int userCnt = pearsonList.size() + 1;
        int[][] userReverseSort = new int[userCnt][userCnt];
        for (int u = 1; u < userCnt; u++) {
            for (int v = u + 1; v < userCnt; v++) {
                Set<String> uVisitedList = pearsonList.get(String.valueOf(u)).getItemIdList();
                Set<String> vVisitedList = pearsonList.get(String.valueOf(v)).getItemIdList();

                int commonSize = Similarity.getCommonSet(uVisitedList, vVisitedList).size();
                if (commonSize > 0) {
                    userReverseSort[u][v] = 1;
                    userReverseSort[v][u] = 1;
                }
            }
        }
        System.out.println("Finish collect ReverseSort");
        System.out.println("耗时" + (System.currentTimeMillis() - prev));
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
                    userSimilarity[u][v] = Similarity.pearsonSimilarity(uP, vP);
                } else {
                    userSimilarity[u][v] = 0;
                }
            }
        }
        System.out.println("Finsh collect user.dat");
        return userSimilarity;
    }

}

