package fr.classcord.model;

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

      public String toString() {
        return "Message{" +
                "type='" + type + '\'' +
                ", subtype='" + subtype + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", content='" + content + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
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