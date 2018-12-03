package com.serveic_provider.service_provider.serviceProvider;

public class User {
	private int is_baned;
	private String phone_number;
	private String type;
    private String profession;
	private int age;
	private int available;
	private String image;
	private String location;
	private String serviceCounter;
	private String name;
	private int rate=0;
    private int id = NO_IMAGE_PROVIDED;
    private static final int NO_IMAGE_PROVIDED =-1;

    public User(int is_baned, String phone_number, String type, String profession, String serviceCounter) {

		this.is_baned = is_baned;
		this.phone_number = phone_number;
		this.type = type;
		this.profession = profession;
		this.serviceCounter = serviceCounter;
	}

	public User(){

    }
	public void login(){

	}
	public void create() {
	}

	public void delete() {
	}

	public void edit() {
	}

	public void getProfile() {
	}

	public void logout() {
	}

	public void setProfession(String profession) {
		if (this.type.equals("provider"))
			this.profession = profession;
	}

	public String getProfession() {
		return profession;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getIs_baned() {
		return is_baned;
	}

	public void setIs_baned(int is_baned) {
		this.is_baned = is_baned;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getAvailable() {
		return available;
	}

	public void setAvailable(int available) {
		this.available = available;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getServiceCounter() {
		return serviceCounter;
	}

	public void setServiceCounter(String serviceCounter) {
		this.serviceCounter = serviceCounter;
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public boolean hasImage(){
        return id != NO_IMAGE_PROVIDED;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}