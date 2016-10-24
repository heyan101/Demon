package demon.web;

import java.util.List;

import dmodule.service.http.ApiGateway;
import dmodule.service.http.protocol.JsonProtocol;
import dmodule.service.http.protocol.JsonReq;
import dmodule.service.http.protocol.JsonResp;
import dmodule.service.http.protocol.RetStat;

public class WebHttpApi{
    
    protected WebApi webApi;
    
    public WebHttpApi(WebApi webApi){
        this.webApi = webApi;
    }
    
    @ApiGateway.ApiMethod(protocol = JsonProtocol.class)
    public JsonResp loadPageScript(JsonReq req) throws Exception {
        
    	String page = req.paramGetString("page", true);
    	List<String> scripts = webApi.loadPageScript(req.env, page);
    	
    	JsonResp resp = new JsonResp(RetStat.OK);
    	resp.resultMap.put("rows", scripts);
        return resp;
    }
    
}
