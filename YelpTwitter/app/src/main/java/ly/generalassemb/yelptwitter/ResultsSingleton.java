package ly.generalassemb.yelptwitter;

import java.util.ArrayList;

/**
 * Created by aaronfields on 7/20/16.
 */
public class ResultsSingleton {

    private static ResultsSingleton resultsSingleton;
    private static ArrayList<Food> foodArrayList;

    private ResultsSingleton(){
        foodArrayList = new ArrayList<>();
    }

    public static ResultsSingleton getInstance(){
        if(resultsSingleton == null)
            resultsSingleton = new ResultsSingleton();
        return resultsSingleton;
    }

    public ArrayList<Food> getFoodArrayList() {
        return foodArrayList;
    }

    public void setFoodArrayList(ArrayList<Food> foodArrayList) {
        ResultsSingleton.foodArrayList = foodArrayList;
    }

    public Food getFoodAtPosition(int position){
        return foodArrayList.get(position);
    }
}
