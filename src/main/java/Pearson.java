import java.util.*;

/**
 * Created by lishiwei on 16/12/24.
 */
class Pearson {

    private String id;
    //平均分
    private String age;
    private String occupation;
    private String zipCode;
    private String sex;

    //PRO
    private List<Item> visitedList;

    private float avgRating;
    private float avgRatingHot;
    private int maxRatingTime;
    private int minRatiLngTime;
    private int participateCnt;

    //0 male 1 famle

    //id Item
    private List<Item> itemList = new ArrayList<Item>();
    //itemId
    private Set<String> itemIdList = new HashSet<String>();
    private List<String> sortedItemIdList = new ArrayList<String>();
    //item rating
    private HashMap<String, Float> itemId2Rating = new HashMap<String, Float>();
    private HashMap<String, String> itemId2TimeStamp = new HashMap<String, String>();

    private Map<Integer, Pearson> simPearson;


    Pearson(String id, String sex, String age, String occupation, String zipCode) {
        this.id = id;
        this.sex = sex;
        this.age = age;
        this.occupation = occupation;
        this.zipCode = zipCode;
    }

    public String getId() {
        return id;
    }


    public String getAge() {
        return age;
    }

    public String getOccupation() {
        return occupation;
    }


    public String getZipCode() {
        return zipCode;
    }

    public String getSex() {
        return sex;
    }

    public List<Item> getVisitedList() {
        return visitedList;
    }

    public void setVisitedList(List<Item> visitedList) {
        this.visitedList = visitedList;
    }

    public int getParticipateCnt() {
        return participateCnt;
    }

    public void setParticipateCnt(int participateCnt) {
        this.participateCnt = participateCnt;
    }

    public float getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(float avgRating) {
        this.avgRating = avgRating;
    }

    public Set<String> getItemIdList() {
        return itemIdList;
    }

    public void setItemIdList(Set<String> itemIdList) {
        this.itemIdList = itemIdList;
    }

    public HashMap<String, Float> getItemId2Rating() {
        return itemId2Rating;
    }

    public HashMap<String, String> getItemId2TimeStamp() {
        return itemId2TimeStamp;
    }

    public List<String> getSortedItemIdList() {
        return sortedItemIdList;
    }

    public void setSortedItemIdList(List<Set> itemIdList) {
        //itemIdList
        this.sortedItemIdList = sortedItemIdList;
    }
}