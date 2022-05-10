package dataAccess;

import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import domain.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.google.gson.*;

public class Manager {
    private List<Competition> competitions;

    public Manager()
    {
        competitions = setCompetitions();
    }

    public String request(String endpoint)
    {
        String result = "";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.football-data.org/v2/" + endpoint)
                .get()
                .addHeader("X-Auth-Token", System.getenv("TOKEN"))
                .build();
        try{
            Response response = client.newCall(request).execute();
            if(response.code() == 200) {
                result = response.body().string();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        return result;
    }

    private List<Competition> setCompetitions(){
        String body = this.request("competitions");
        System.out.println(body);

        Gson gson = new Gson();
        JsonObject jsonObject;

        jsonObject = gson.fromJson(body, JsonObject.class);
        Type competitionListType = new TypeToken<ArrayList<Competition>>(){}.getType();
        List<Competition> competitions = gson.fromJson((jsonObject.get("competitions")), competitionListType);
        return competitions;
    }

    public List<Competition> getCompetitions(){
        return competitions;
    }

    //Only use once.
    public List<Match> getMatches(int competitionID){
        String body = this.request("competitions/"+ competitionID + "/matches");

        Gson gson = new Gson();
        JsonObject jsonObject;

        jsonObject = gson.fromJson(body, JsonObject.class);
        Type matchListType = new TypeToken<ArrayList<Match>>(){}.getType();
        List<Match> matches = gson.fromJson((jsonObject.get("matches")), matchListType);
        return matches;
    }

    //Only use once. Integer = teamID, Team = team object.
    public HashMap<Integer,Team> getTeams(int competitionID)
    {
        HashMap<Integer, Team> result = new HashMap<Integer, Team>();
        String body = this.request("/competitions/" +competitionID + "/teams");
        System.out.println(body);

        Gson gson = new Gson();
        JsonObject jsonObject;

        jsonObject = gson.fromJson(body, JsonObject.class);
        Type teamListType = new TypeToken<ArrayList<Team>>(){}.getType();
        List<Team> teams = gson.fromJson((jsonObject.get("teams")), teamListType);
        for(Team t : teams)
            result.put(t.getId(), t);

        return result;
    }

    public static void main(String[] args)
    {
        Manager manager = new Manager();
        /*List<Match> matches = manager.getMatches(2014);
        System.out.println(matches.get(0).utcDate);
        System.out.println(matches.get(0).homeTeam.id);*/
        System.out.println(manager.getTeams(2014));
    }
}

