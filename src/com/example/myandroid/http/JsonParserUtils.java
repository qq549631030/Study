package com.example.myandroid.http;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.myandroid.database.model.User;

public class JsonParserUtils {
	public static List<User> parserToUsers(String result) {
		List<User> users = new ArrayList<User>();
		try {
			JSONObject resultObject = new JSONObject(result);
			if (resultObject.has("results")) {
				JSONArray userArray = resultObject.getJSONArray("results");
				if (userArray != null) {
					for (int i = 0; i < userArray.length(); i++) {
						JSONObject userObject = (JSONObject) userArray.get(i);
						User user = jsonToUser(userObject);
						users.add(user);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return users;
	}

	public static User jsonToUser(JSONObject userObject) {
		User user = new User();
		try {
			if (userObject.has("objectId")) {
				user.setObjectId(userObject.getString("objectId"));
			}
			if (userObject.has("username")) {
				user.setUsername(userObject.getString("username"));
			}
			if (userObject.has("password")) {
				user.setPassword(userObject.getString("password"));
			}
			if (userObject.has("email")) {
				user.setEmail(userObject.getString("email"));
			}
			if (userObject.has("emailVerified")) {
				user.setEmailVerified(userObject.getBoolean("emailVerified"));
			}
			if (userObject.has("createdAt")) {
				user.setCreatedAt(userObject.getString("createdAt"));
			}
			if (userObject.has("updateAt")) {
				user.setUpdatedAt(userObject.getString("updateAt"));
			}
			if (userObject.has("nickname")) {
				user.setNickname(userObject.getString("nickname"));
			}
			if (userObject.has("avatarUrl")) {
				user.setAvatarUrl(userObject.getString("avatarUrl"));
			}
			if (userObject.has("age")) {
				user.setAge(userObject.getInt("age"));
			}
			if (userObject.has("gender")) {
				user.setGender(userObject.getBoolean("gender"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return user;
	}
}
