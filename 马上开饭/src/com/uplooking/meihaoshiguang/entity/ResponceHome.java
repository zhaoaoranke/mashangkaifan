package com.uplooking.meihaoshiguang.entity;

import java.util.List;

public class ResponceHome {
	private HeadObject headObject;
	private HotRecipeObject recipe_object;
	
	
	public static class HeadObject{
		
		public List<HeadRecipe> list;
		public static class HeadRecipe{
			public int recipeId;
			public String name;
			public String img;
//			public HeadRecipe(int recipeId, String name,String img) {
//				super();
//				
//				this.recipeId = recipeId;
//				this.name = name;
//				this.img = img;
//			}
			
		}
		
	}
	/**
	 * 人气美食
	 * @author Li
	 *
	 */
	public static class HotRecipeObject{
		public List<HotRecipe> list;
		public static class HotRecipe{
			public int id;
			public String title;
			public String img;
//			public HotRecipe(int id, String title, String img) {
//				super();
//				this.id = id;
//				this.title = title;
//				this.img = img;
//			}
			
			
		}
	}

	public HotRecipeObject getRecipe_object() {
		return recipe_object;
	}

	public void setRecipe_object(HotRecipeObject recipe_object) {
		this.recipe_object = recipe_object;
	}

	public HeadObject getHeadObject() {
		return headObject;
	}

	public void setHeadObject(HeadObject headObject) {
		this.headObject = headObject;
	}
	
	
}



