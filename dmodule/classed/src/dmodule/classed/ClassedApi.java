package dmodule.classed;

import demon.exception.LogicalException;
import demon.exception.UnInitilized;
import dmodule.SDK.SdkCenter;
import dmodule.SDK.inner.IBeans;
import dmodule.SDK.inner.IClassedApi;

public class ClassedApi implements IClassedApi{
	protected IBeans beans;
	protected ClassedModel classedModel;
	
	private static ClassedApi authApi;
	private ClassedApi(IBeans beans, ClassedModel classedModel) throws LogicalException {
		this.beans = beans;
		this.classedModel = classedModel;
		
		SdkCenter.getInst().addInterface(IClassedApi.name, this);
	}
	
	public static void init(IBeans beans, ClassedModel classedModel) throws LogicalException {
		authApi = new ClassedApi(beans, classedModel);
	}
	
	public static ClassedApi getInst() throws UnInitilized {
		if (null == authApi) {
			throw new UnInitilized();
		}
		return authApi;
	}
	
	/******************************************************************************************/

	public IClassedModel getClassedModel() {
		return this.classedModel;
	}
}
