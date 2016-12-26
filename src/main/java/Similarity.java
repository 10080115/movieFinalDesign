import java.math.BigDecimal;
import java.util.*;

/**
 * Created by lishiwei on 16/12/26.
 */
class Similarity {
    static float cosineSimilarity(Pearson u, Pearson v) {
        return 1;
    }

    static float timeSimilarity(Pearson u, Pearson v) {
        return 1;
    }

    static float pearsonSimilarity(Pearson u, Pearson v) {
        Set<String> uItemIdList = u.getItemIdList();
        Set<String> vItemIdList = v.getItemIdList();
        Set<String> uvCommonItemList = getCommonSet(uItemIdList, vItemIdList);

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

    static Set<String> getCommonSet(Set<String> uItemIdList, Set<String> vItemIdList) {
        Set<String> commonItemList = new HashSet<String>();
        commonItemList.addAll(uItemIdList);
        commonItemList.retainAll(vItemIdList);
        return commonItemList;
    }
}
