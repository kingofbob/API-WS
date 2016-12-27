package com.soongwei.meow.objects.json.Respond;

public class TPBResults {
String title;
String magnet;
String age;
String seed;
String size;
String path;
String path2;



public TPBResults() {
	super();
}

public TPBResults(String title, String magnet, String age, String seed, String size, String path, String path2) {
	super();
	this.title = title;
	this.magnet = magnet;
	this.age = age;
	this.seed = seed;
	this.size = size;
	this.path = path;
	this.path2 = path2;
}



public String getPath2() {
	return path2;
}

public void setPath2(String path2) {
	this.path2 = path2;
}

public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}

public String getMagnet() {
	return magnet;
}

public void setMagnet(String magnet) {
	this.magnet = magnet;
}

public String getAge() {
	return age;
}
public void setAge(String age) {
	this.age = age;
}
public String getSeed() {
	return seed;
}
public void setSeed(String seed) {
	this.seed = seed;
}
public String getSize() {
	return size;
}
public void setSize(String size) {
	this.size = size;
}
public String getPath() {
	return path;
}
public void setPath(String path) {
	this.path = path;
}

@Override
public String toString() {
	return "TPBResults [title=" + title + ", magnet=" + magnet + ", age=" + age + ", seed=" + seed + ", size=" + size
			+ ", path=" + path + ", path2=" + path2 + "]";
}






}
