package com.uplooking.meihaoshiguang.handler;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uplooking.meihaoshiguang.entity.Material;
import com.uplooking.meihaoshiguang.R;

public class MaterialsHandler {
	private LayoutInflater mInflater;

	public MaterialsHandler(Context context) {
		super();
		mInflater = LayoutInflater.from(context);
	}

	private List<Material> getList(String materialStr) {
		List<Material> list = new ArrayList<Material>();
		// ����,250��;
		// �ײˣ���������ʱ���߲ˣ����Ĵ�ͨ��ʹ��ݫ��,200��;
		// ���ٲ�����
		String[] strings = materialStr.split(";");

		for (String string : strings) {

			String[] strings2 = string.split(",");
			if (strings2.length == 1) {
				list.add(new Material(strings2[0], ""));
			} else if (strings2.length == 2) {
				list.add(new Material(strings2[0], strings2[1]));
			}
		}
		return list;
	}

	public void setDatas(LinearLayout container, String mainMaterialsJson,
			String assistMaterialsJson) {
		List<Material> materials = new ArrayList<Material>();
		if (!TextUtils.isEmpty(mainMaterialsJson)) {

			materials.addAll(getList(mainMaterialsJson));
		}
		if (!TextUtils.isEmpty(assistMaterialsJson)) {

			materials.addAll(getList(assistMaterialsJson));

		}
		// û�����ݣ�������ҳ����ʾ�ؼ�
		if (materials.size() == 0) {
			container.setVisibility(View.GONE);
			return;
		}
		
		for (Material material : materials) {
			View view = mInflater.inflate(
					R.layout.v_recipe_detail_food_material_item, null);
			TextView name = (TextView) view
					.findViewById(R.id.recipe_detail_food_material_name);
			TextView amount = (TextView) view
					.findViewById(R.id.recipe_detail_food_material_amount);
			name.setText(material.getName());
			if (!TextUtils.isEmpty(material.getAmount())) {
				amount.setText(material.getAmount());
			}
			container.addView(view);
		}
	}

}
