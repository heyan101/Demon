package dmodule.classed;

import demon.exception.UnInitilized;

public class ClassedHttpApi {

	private ClassedApi classedApi;
	
	private static ClassedHttpApi classedHttpApi;
	private ClassedHttpApi(ClassedApi classedApi) {
		this.classedApi = classedApi;
	}
	
	public static void init(ClassedApi classedApi) {
		classedHttpApi = new ClassedHttpApi(classedApi);
	}
	
	public static ClassedHttpApi getInst() {
		if (null == classedHttpApi) {
			new UnInitilized();
		}
		return classedHttpApi;
	}
}
