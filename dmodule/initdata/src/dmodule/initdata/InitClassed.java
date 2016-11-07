package dmodule.initdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import demon.service.http.Env;
import demon.utils.XProperties;
import dmodule.SDK.inner.IBeans;

/**
 * 初始化商品分类
 * 		a. 读取 "classed_name.json" 文件，生成商品初始的一级和二级分类
 * 
 * @author Demon
 */
public class InitClassed {
	
	public static void initClassed(Env env, XProperties properties, IBeans beans, String moduleDir) {
		String data = readJSONFile(moduleDir, "classed_name.json");
		JSONObject json = JSONObject.parseObject(data);
		Set<String> keys = json.keySet();
		// classed_one:一级分类，classed_two:二级分类
		for (String classed_one : keys) {
			Long parentId = beans.getClassedApi().getClassedModel().updateClassed(classed_one);
			JSONArray name = json.getJSONArray(classed_one);
			List<String> classed_two = new ArrayList<>();
			for (int i = 0; i < name.size(); ++i) {
				classed_two.add(name.getString(i));
			}
		}
	}
	
	/**
	 * 读取  JSON 文件
	 * @param parentDir 文件所在目录
	 * @param childFile 文件名
	 * @return 文件内容(String)
	 */
	public static String readJSONFile(String parentDir, String childFile) {
		File file = new File(parentDir, childFile);
		BufferedReader reader = null;
		String data = "";
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			String temp = null;
			while((temp = reader.readLine()) != null){
				data += temp;
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	public static void main(String[] args) {
		initClassed(null, null, null, "H:\\Demon-Goods\\Demon\\dmodule\\initdata");
	}
}
