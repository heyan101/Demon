package demon.web;

import java.util.List;

import demon.service.http.ApiGateway;
import demon.service.http.protocol.JsonProtocol;
import demon.service.http.protocol.JsonReq;
import demon.service.http.protocol.JsonResp;
import demon.service.http.protocol.RetStat;

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
