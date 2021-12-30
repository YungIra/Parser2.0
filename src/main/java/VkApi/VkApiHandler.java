package VkApi;

import Entenies.Student;
import Entenies.Table;
import Entenies.VkData;
import com.vk.api.sdk.client.Lang;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.users.Fields;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.MalformedURLException;
import java.net.URL;

public class VkApiHandler {
    private VkApiClient vk;
    private Integer userId;
    private String accessToken;

    public VkApiHandler() {
        TransportClient transportClient = HttpTransportClient.getInstance();
        vk = new VkApiClient(transportClient);
        userId = 8039174;
        accessToken = "e304c8fae304c8fae304c8fa13e37e63fcee304e304c8fa82c878f971c1d474da53c4eb";
    }

    public void loadDataAboutUserInfo(String groupId, Table table) throws ClientException {
        UserActor actor = new UserActor(userId, accessToken);

        JSONParser parser = new JSONParser();
        try {
            int numberOfSubscribers = getNumberOfSubscribers(actor, groupId, parser);
            for (int i = 0; i < numberOfSubscribers; i += 1000) {
                JSONArray subscribers = getSubscribers(actor, groupId, parser);
                for (Object subscribe : subscribers) {
                    JSONObject userInfo = (JSONObject) subscribe;
                    if (table.containStudent(userInfo.get("first_name").toString(), userInfo.get("last_name").toString())) {
                        JSONObject city = (JSONObject) userInfo.get("city");
                        Student student = table.getStudent(userInfo.get("first_name").toString(), userInfo.get("last_name").toString());
                        student.setFirstname(userInfo.get("first_name").toString());
                        student.setLastname(userInfo.get("last_name").toString());
                        VkData vkData = getVkDataAboutSubscribe(userInfo);
                        student.setVkData(vkData);
                    }
                }
                Thread.sleep(100);
            }
        } catch (ParseException | MalformedURLException | ApiException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private VkData getVkDataAboutSubscribe(JSONObject userInfo) throws MalformedURLException {
        JSONObject city = (JSONObject) userInfo.get("city");
        VkData vkData = new VkData();
        if (userInfo.get("bdate") != null){
            vkData.setBirthDay(userInfo.get("bdate").toString());
            vkData.setAge(userInfo.get("bdate").toString());
        }

        vkData.setUserVkId(userInfo.get("id").hashCode());
        vkData.setLinkPhoto(new URL(userInfo.get("photo_max_orig").toString()));
        vkData.setGender(userInfo.get("sex").equals(2l) ? "Муж." : "Жен.");
        if (city != null)
            vkData.setCity(city.get("title").toString());
        return vkData;
    }

    private JSONArray getSubscribers(UserActor actor, String groupId, JSONParser parser)
            throws ClientException, ParseException {
        JSONObject jsonObject = (JSONObject) parser
                .parse(vk.groups()
                        .getMembers(actor)
                        .groupId(groupId)
                        .count(1000).offset(0)
                        .fields(Fields.BDATE, Fields.PHOTO_MAX_ORIG, Fields.CITY, Fields.ABOUT, Fields.SEX)
                        .lang(Lang.RU)
                        .executeAsString());
        JSONObject j = (JSONObject) jsonObject.get("response");
        return (JSONArray) j.get("items");
    }

    private int getNumberOfSubscribers(UserActor actor, String groupId, JSONParser parser)
            throws ClientException, ApiException, ParseException {
        JSONObject vkAnswer = (JSONObject) parser
                .parse(vk.groups()
                        .getMembers(actor)
                        .groupId(groupId)
                        .execute()
                        .toString());
        return Integer.valueOf(vkAnswer.get("count").toString());
    }
}
