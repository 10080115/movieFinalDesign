import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.util.*;

/**
 * Created by lishiwei on 16/12/26.
 */
public class test {
    public static void main(String[] args) {
        List<String> listA = new ArrayList<String>();
        listA.add("1");
        listA.add("3");
        listA.add("4");
        listA.add("5");


        List<String> listB = new ArrayList<String>();
        listB.add("7");
        listB.add("9");
        listB.add("2");
        listB.add("5");

        Collections.sort(listA);
        Collections.sort(listB);

        int sizeA = listA.size();
        int sizeB = listB.size();

        for (int i = 0, j = 0; i < sizeA && j < sizeB; ) {
            int A = Integer.valueOf(listA.get(i));
            int B = Integer.valueOf(listB.get(j));
            System.out.println(A + " " + B);
            if (A == B) {
                System.out.println("相同");
                break;
            } else if (A > B)
                j++;
            else
                i++;
        }

    }
}
