package com.uplooking.meihaoshiguang.entity;

/**
 * 食材 的实体类
 * @author Li
 *
 */
public class IngredientEntity {

	private String name;
	private String imgName;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImgName() {
		return imgName;
	}
	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
	public IngredientEntity(String name, String imgName) {
		super();
		this.name = name;
		this.imgName = imgName;
	}
	public IngredientEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
