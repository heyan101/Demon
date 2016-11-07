package dmodule.SDK.inner;

import java.sql.SQLException;

public interface IClassedApi {
	
	public static final String name = "IClassedApi";
	
	public IClassedModel getClassedModel();
	public interface IClassedModel {

		/**
		 * 更新分类名，并返回当前分类名的ID
		 * @param classedName 分类名
		 * @param parentId 分类的父分类ID
		 * @return classed_id
		 * @throws SQLException 
		 */
		Long updateClassed(String classedName, Long parentId) throws SQLException;
		
	}

}
