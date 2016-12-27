package com.soongwei.meow.objects.json.Request;

public class SearchRequest {
int type;
String search;
public int getType() {
	return type;
}
public void setType(int type) {
	this.type = type;
}
public String getSearch() {
	return search;
}
public void setSearch(String search) {
	this.search = search;
}
@Override
public String toString() {
	return "SearchRequest [type=" + type + ", search=" + search + "]";
}



}
