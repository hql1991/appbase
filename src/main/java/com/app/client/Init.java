package com.app.client;

import com.app.server.models.Renter;
import com.app.server.services.RenterService;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Init {
    private static RenterService renterService = RenterService.getInstance();

    public static void main(String[] argv) {

        String[] renterIds = RenterInit.init();
        PhotoInit.init(renterIds);
        String[] ownerIds = OwnerInit.init();
        String[] houseIds = HouseInit.init(ownerIds);
        RentalInit.init(renterIds, ownerIds);

        List<String> house_recolist0 = new ArrayList<>();
        house_recolist0.add(houseIds[1]);
        house_recolist0.add(houseIds[2]);
        List<String> house_recolist1 = new ArrayList<>();
        house_recolist1.add(houseIds[0]);
        house_recolist1.add(houseIds[2]);
        List<String>[] houseRecommendationLists = new List[2];
        houseRecommendationLists[0]=house_recolist0;
        houseRecommendationLists[1]=house_recolist1;
        HouseRecInit.init(renterIds, houseRecommendationLists);

    }


}
