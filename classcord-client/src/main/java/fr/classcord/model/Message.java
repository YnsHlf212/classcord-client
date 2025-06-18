package fr.classcord.model;

import org.json.JSONObject;

public class Message {
    public String type;
    public String subtype;
    public String from;
    public String to;
    public String content;
    public String timestamp;

	public Message(String type, String subtype, String from, String to, String content, String timestamp) {
		this.type = type;
		this.subtype = subtype;
		this.from = from;
		this.to = to;
		this.content = content;
		this.timestamp = timestamp;
	}

	public Message() {
		
	}

	public JSONObject toJson() {
        JSONObject message = new JSONObject();
        message.put("type", type);
        message.put("subtype", subtype);
        message.put("to", to);
        message.put("from", from);
        message.put("content", content);
        message.put("timestamp", timestamp);
        return message;
    }

	public static Message fromJson(String jsonString) {
    	JSONObject json = new JSONObject(jsonString);
    
    	Message message = new Message();
    	message.setType(json.optString("type"));
    	message.setSubtype(json.optString("subtype"));
    	message.setTo(json.optString("to"));
    	message.setFrom(json.optString("from"));
    	message.setContent(json.optString("content"));
    	message.setTimestamp(json.optString("timestamp"));
    
    	return message;
	}

	 @Override
    public String toString() {
        return toJson().toString();
    }

    public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSubtype() {
		return this.subtype;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	public String getFrom() {
		return this.from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return this.to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

}