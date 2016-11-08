package dmodule.SDK.inner;

import java.sql.SQLException;
import java.util.List;

public interface IClassedApi {
	
	public static final String name = "IClassedApi";
	
	public IClassedModel getClassedModel();
	public interface IClassedModel {

		/**
		 * 插入分类名，并返回当前分类名的ID
		 * @param classedName 分类名
		 * @param parentId 分类的父分类ID
		 * @return classed_id
		 * @throws SQLException 
		 */
		Long insertClassed(String classedName, Long parentId) throws SQLException;

		/**
		 * 更新多个分类名
		 * @param classedNames 分类名
		 * @param parentId 分类的父分类ID
		 * @return
		 * @throws SQLException 
		 */
		void insertClasseds(List<String> classedNames, Long parentId) throws SQLException;
		
	}

}
